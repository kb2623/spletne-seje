package org.datastruct;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClassPool {

	private static Map<Long, Object> map = null;

	private ClassPool() {
		map = new HashMap<>();
	}

	public static <T> T getObject(Class<T> c, Object... args) throws ExceptionInInitializerError {
		T ret = null;
		long hash = 0;
		for (Object o : args) {
			hash += o.hashCode();
		}
		if (map == null) {
			new ClassPool();
			ret = makeObject(c, args);
			map.put(hash, ret);
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
			initArgsType[i] = args[i].getClass();
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
