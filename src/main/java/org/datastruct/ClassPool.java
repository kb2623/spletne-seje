package org.datastruct;

import org.datastruct.exception.MapDoesNotExist;
import org.datastruct.exception.ObjectDoesNotExist;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

public class ClassPool {

	private static Map<Class, Map<Integer, Object>> mapObject = null;
	private static Properties properties = null;

	private ClassPool() throws IOException {
		mapObject = new ArrayMap<>();
		properties = new Properties();
		properties.load(getClass().getResourceAsStream("ClassPool.properties"));
	}

	private ClassPool(Properties props) {
		mapObject = new ArrayMap<>();
		properties = props;
	}

	public static void initClassPool(int size, Properties properties) throws IOException {
		if (size == 0) {
			mapObject = new ArrayMap<>();
		} else {
			mapObject = new ArrayMap<>(size);
		}
		// TODO za propretije
	}

	private static int getSize(Class c) {
		if (properties == null) {
			return 50;
		} else {
			try {
				return Integer.valueOf(properties.getProperty("size." + c.getClass().getSimpleName()));
			} catch (NumberFormatException e) {
				return 50;
			}
		}
	}

	private synchronized static String getMapType(Class c) {
		if (properties == null) {
			return "";
		} else {
			return properties.getProperty("map." + c.getClass().getSimpleName());
		}
	}

	public synchronized static <T> T getObject(Class<T> c, Object... args) throws ExceptionInInitializerError, NullPointerException {
		if (mapObject == null || properties == null) {
			throw new NullPointerException("Have to init first!!!");
		}
		T ret = null;
		int hash = 0;
		for (Object o : args) {
			hash += o.hashCode();
		}
		if (ClassPool.mapObject == null) {
			Map<Integer, Object> map = new MapLRU<>(5, getSize(c));
			ret = makeObject(c, args);
			map.put(hash, ret);
		} else {
			try {
				ret = getObject(c, hash);
			} catch (MapDoesNotExist mapDoesNotExist) {
				Map<Integer, Object> map = new MapLRU<>(5, getSize(c));
				ret = makeObject(c, args);
				map.put(hash, ret);
			} catch (ObjectDoesNotExist objectDoesNotExist) {
				Map<Integer, Object> map = ClassPool.mapObject.get(c);
				ret = makeObject(c, args);
				map.put(hash, ret);
			}
		}
		return ret;
	}

	private static <T> T getObject(Class<T> c, int hash) throws MapDoesNotExist, ObjectDoesNotExist {
		Map<Integer, Object> map = ClassPool.mapObject.get(c);
		if (map == null) {
			throw new MapDoesNotExist();
		} else {
			T ret = c.cast(map.get(hash));
			if (ret == null) {
				throw new ObjectDoesNotExist();
			}
			return ret;
		}
	}

	private static <T> T makeObject(Class<T> c, Object... args) throws ExceptionInInitializerError {
		Class[] initArgsType = new Class[args.length];
		for (int i = 0; i < initArgsType.length; i++) {
			if (args[i] instanceof Enum) {
				/** za enume moramo pridobiti super razred, ki pa je razred, ki se nahaja v kostruktorju */
				initArgsType[i] = args[i].getClass().getSuperclass();
			} else {
				initArgsType[i] = args[i].getClass();
			}
		}
		try {
			Constructor init = c.getConstructor(initArgsType);
			Object newObject = init.newInstance(args);
			return c.cast(newObject);
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
