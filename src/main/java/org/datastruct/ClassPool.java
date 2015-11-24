package org.datastruct;

import org.datastruct.exception.MapDoesNotExist;
import org.datastruct.exception.ObjectDoesNotExist;

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
	private static ClassLoader loader = null;

	private ClassPool(Map<Class, Map<Integer, Object>> map, Properties props, ClassLoader loader) {
		mapObject = map;
		properties = props;
		this.loader = loader;
	}

	public static void initClassPool(Integer size, String pathProp, ClassLoader loader) throws IOException {
		Map<Class, Map<Integer, Object>> map;
		Properties props = new Properties();
		if (size == null || size < +0) {
			map = new HashMap<>();
		} else {
			map = new HashMap<>(size);
		}
		if (pathProp == null) {
			props.load(ClassLoader.getSystemResourceAsStream("ClassPool.properties"));
		} else {
			props.load(new FileInputStream(pathProp));
		}
		if (loader == null) {
			new ClassPool(map, props, ClassLoader.getSystemClassLoader());
		} else {
			new ClassPool(map, props, loader);
		}
	}

	private static Map<Integer, Object> initMap(Class type) {
		int size = 0;
		if (properties.getProperty(type.getSimpleName() + ".size") != null) {
			size = Integer.valueOf(properties.getProperty(type.getSimpleName() + ".size"));
			if (size <= 0) {
				size = 0;
			}
		}
		Class cMap = null;
		try {
			cMap = loader.loadClass(properties.getProperty(type.getSimpleName() + ".map"));
		} catch (ClassNotFoundException e) {
			if (size <= 0) {
				cMap = SkipMap.class;
			} else {
				cMap = MapQueue.class;
			}
		}
		if (size > 0) {
			try {
				Constructor init = cMap.getConstructor(int.class);
				return (Map<Integer, Object>) init.newInstance(size);
			} catch (NoSuchMethodException e) {
				throw new ExceptionInInitializerError("Missing construktor for map " + cMap.getSimpleName() + "!!!");
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new ExceptionInInitializerError("Error making instance for " + cMap.getSimpleName() + "!!!");
			}
		} else {
			try {
				return (Map<Integer, Object>) cMap.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ExceptionInInitializerError("Error making instance for " + cMap.getSimpleName() + "!!!");
			}
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
			Map<Integer, Object> map = new SkipMap<>(5);
			ret = makeObject(c, args);
			map.put(hash, ret);
		} else {
			try {
				ret = getObject(c, hash);
			} catch (MapDoesNotExist mapDoesNotExist) {
				Map<Integer, Object> map = initMap(c);
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
			initArgsType[i] = args[i].getClass();
		}
		try {
			Constructor init = c.getConstructor(initArgsType);
			Object newObject = init.newInstance(args);
			return c.cast(newObject);
		} catch (NoSuchMethodException e) {
			throw new ExceptionInInitializerError(e);
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
