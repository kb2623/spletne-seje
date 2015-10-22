package org.datastruct;

import org.datastruct.exception.MapDoesNotExist;
import org.datastruct.exception.MapFullException;
import org.datastruct.exception.ObjectDoesNotExist;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClassPool {

	// TODO motranja mapa bo MapLRU

	private static Map<Class, Map<Integer, Object>> mapObject = null;
	private static int maxSize = 50;

	private ClassPool() {
		mapObject = new ArrayMap<>();
	}

	public static void setMaxSize(int newMaxSize) {
		if (mapObject == null) {
			maxSize = newMaxSize;
		}
	}

	public static <T> T getObject(Class<T> c, Object... args) throws ExceptionInInitializerError {
		T ret = null;
		int hash = 0;
		for (Object o : args) {
			hash += o.hashCode();
		}
		if (ClassPool.mapObject == null) {
			new ClassPool();
			ret = makeObject(c, args);
		} else {
			try {
				ret = getObject(c, hash);
			} catch (MapDoesNotExist mapDoesNotExist) {
				ret = makeObject(c, args);
			} catch (ObjectDoesNotExist objectDoesNotExist) {
				ret = makeObject(c, args);
			}
		}
		return ret;
	}

	private static <T> T getObject(Class<T> c, int hash) throws MapDoesNotExist, ObjectDoesNotExist {
		Map<Integer, Object> map = ClassPool.mapObject.get(c);
		if (map == null) {
			throw new MapDoesNotExist();
		} else {
			throw new ObjectDoesNotExist();
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
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
