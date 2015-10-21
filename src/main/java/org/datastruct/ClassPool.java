package org.datastruct;

import org.datastruct.exception.MapDoesNotExist;
import org.datastruct.exception.ObjectDoesNotExist;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClassPool {

	private class PoolObject {

		private PoolObject prev;
		protected Object data;
		protected int hash;
		private PoolObject next;

		private PoolObject(PoolObject prev, Object data, int hash, PoolObject next) {
			this.prev = prev;
			this.data = data;
			this.hash = hash;
			this.next = next;
		}

		protected void lowerPriority() {
			if (prev != null) {
				PoolObject pO = prev;
				pO.next = next;
				prev = pO.prev;
				next = pO;
				pO.prev = this;
			}
		}

		protected PoolObject add(Object data, int hash, PoolObject last) {
			if (last != null && last.prev != null) {
				while (last.prev != null) {
					last = last.prev;
				}
			}
			PoolObject nPo = new PoolObject(null, data, hash, last);
			last.prev = nPo;
			return nPo;
		}

		protected PoolObject removeFirst(PoolObject last, PoolObject first) {
			PoolObject ret = null;
			if (first == null && last == null) {
				return ret;
			} else if (first == null && last != null) {
				first = last;
				while (first.next != null) {
					first = first.next;
				}
				ret = first.prev;
				ret.next = null;
				first.prev = null;
				return ret;
			} else if (first.next == null) {
				if (first.prev != null) {
					first.prev.next = null;
					ret = first.prev;
				}
				first.prev = null;
				return ret;
			} else {
				while (first.next != null) {
					first = first.next;
				}
				ret = first.prev;
				ret.next = null;
				first.prev = null;
				return ret;
			}
		}
	}

	private PoolObject last;
	private PoolObject first;
	private static Map<Class, Map<Integer, Object>> map = null;
	private static int maxSize = 50;

	private ClassPool() {
		first = null;
		last = null;
		map = new ArrayMap<>();
	}

	public static void setMaxSize(int newMaxSize) {
		if (map == null) {
			maxSize = newMaxSize;
		}
	}

	public static <T> T getObject(Class<T> c, Object... args) throws ExceptionInInitializerError {
		T ret = null;
		int hash = 0;
		for (Object o : args) {
			hash += o.hashCode();
		}
		if (ClassPool.map == null) {
			new ClassPool();
			ret = makeObject(c, args);
			Map map = new ArrayMap<>(maxSize, 0f);
			map.put(hash, ret);
			ClassPool.map.put(c, map);
		} else {
			try {
				ret = getObject(c, hash);
			} catch (MapDoesNotExist mapDoesNotExist) {
				ret = makeObject(c, args);
				Map map = new ArrayMap<>(maxSize, 0f);
				map.put(hash, ret);
				ClassPool.map.put(c, map);
			} catch (ObjectDoesNotExist objectDoesNotExist) {
				ret = makeObject(c, args);
				Map map = ClassPool.map.get(c);
				map.put(hash, ret);
			}
		}
		return ret;
	}

	private static <T> T getObject(Class<T> c, int hash) throws MapDoesNotExist, ObjectDoesNotExist {
		Map map = ClassPool.map.get(c);
		if (map == null) {
			throw new MapDoesNotExist();
		} else {
			T ret = c.cast(map.get(hash));
			if (ret == null) {
				throw new ObjectDoesNotExist();
			} else {
				return ret;
			}
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
