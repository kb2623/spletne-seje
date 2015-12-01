package org.datastruct;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClassPool {

	private static Map<Class, Map<Integer, Object>> mapObject = null;
	private static Properties properties = null;

	private ClassPool(Map<Class, Map<Integer, Object>> map, Properties props) {
		mapObject = map;
		properties = props;
	}

	public static void initClassPool(Integer size, String pathProp) throws IOException {
		Map<Class, Map<Integer, Object>> map;
		Properties props = new Properties();
		if (size == null || size < 0) {
			map = new HashMap<>();
		} else {
			map = new HashMap<>(size);
		}
		if (pathProp == null) {
			props.load(ClassLoader.getSystemResourceAsStream("ClassPool.properties"));
		} else {
			props.load(new FileInputStream(pathProp));
		}
		new ClassPool(map, props);
	}

	private static Map<Integer, Object> initMap(Class type) {
		int size = 0;
		if (properties.getProperty(type.getSimpleName() + ".size") != null) {
			size = Integer.valueOf(properties.getProperty(type.getSimpleName() + ".size"));
			if (size <= 0) {
				System.err.println("Ignoring " + type.getSimpleName() + ".size");
				size = 0;
			}
		}
		if (size == 0) {
			return new AvlTree<>();
		} else {
			int maxCone = 0;
			if (properties.getProperty(type.getSimpleName() + ".cons") != null) {
				maxCone = Integer.valueOf(properties.getProperty(type.getSimpleName() + ".cons"));
				if (maxCone < 2) {
					return new MapQueue<>(5, size);
				} else {
					return new MapQueue<>(maxCone, size);
				}
			} else {
				return new MapQueue<>(5, size);
			}
		}
	}

	public synchronized static <T> T getObject(Class<T> c, Object... args) throws NullPointerException {
		if (mapObject == null || properties == null) {
			throw new NullPointerException("Have to init first!!!");
		}
		int hash = 0;
		for (Object o : args) {
			hash += o.hashCode();
		}
		return getObject(c, hash, args);
	}

	private static <T> T getObject(Class<T> c, int hash, Object... args) {
		Map<Integer, Object> map = ClassPool.mapObject.get(c);
		T ret = null;
		if (map == null) {
			map = initMap(c);
			ret = makeObject(c, args);
			map.put(hash, ret);
			mapObject.put(c, map);
		} else {
			ret = c.cast(map.get(hash));
			if (ret == null) {
				ret = makeObject(c, args);
				map.put(hash, ret);
			}
		}
		return ret;
	}

	private static <T> T makeObject(Class<T> c, Object... args) throws ExceptionInInitializerError {
		Class[] initArgsType = new Class[args.length];
		for (int i = 0; i < initArgsType.length; i++) {
			Class ca = null;
			if (args[i] instanceof Enum<?>) {
				ca = args[i].getClass().getSuperclass();
			} else {
				ca = args[i].getClass();
			}
			initArgsType[i] = ca;
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
