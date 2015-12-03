package org.datastruct;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

public class ObjectPool {

	private Map<Class, Map<Integer, Object>> mapObject = null;
	private Map<Class, ObjectCreator> creators = null;
	private Properties properties = null;
	private ClassLoader loader = null;

	public ObjectPool(Map<Class, Map<Integer, Object>> mapObject, Map<Class, ObjectCreator> creators, Properties properties, ClassLoader loader) {
		this.mapObject = mapObject;
		this.creators = creators;
		this.properties = properties;
		this.loader = loader;
	}

	public ObjectPool() {
		this(new AvlTree<>(), new AvlTree<>(), new Properties(), ClassLoader.getSystemClassLoader());
	}

	public static <T> T makeObject(Class<T> c, Object... args) throws ExceptionInInitializerError {
		Class[] initArgsType = new Class[args.length];
		for (int i = 0; i < initArgsType.length; i++) {
			Class ca = null;
			if (args[i] instanceof Enum<?>) {
				if (args[i].getClass().getSuperclass().getSimpleName().equals("Enum")) {
					ca = args[i].getClass();
				} else {
					ca = args[i].getClass().getSuperclass();
				}
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

	public void setCreators(Map<Class, ObjectCreator> map) {
		creators.putAll(map);
	}

	public void setProperties(Properties properties) {
		this.properties.putAll(properties);
	}

	private Map<Integer, Object> initMap(Class type) {
		String className = type.getSimpleName();
		String tmp = properties.getProperty(className + ".map");
		if (tmp != null) {
			try {
				Class c = loadClass(tmp);
				tmp = properties.getProperty(className + ".argstype");
				if (tmp == null) {
					return (Map<Integer, Object>) c.newInstance();
				}
				String[] typesNames = tmp.split(" ");
				Class[] classes = new Class[typesNames.length];
				for (int i = 0; i < classes.length; i++) {
					classes[i] = loadClass(typesNames[i]);
				}
				Constructor init = c.getConstructor(classes);
				tmp = properties.getProperty(className + ".args");
				typesNames = tmp.split(" ");
				if (typesNames.length != classes.length) {
					throw new InstantiationException("Bad args");
				}
				Object[] oTab = new Object[classes.length];
				for (int i = 0; i < oTab.length; i++) {
					oTab[i] = valueOfString(typesNames[i], classes[i]);
				}
				return (Map<Integer, Object>) init.newInstance(oTab);
			} catch (ClassNotFoundException e) {
				System.err.println("Can not load [" + tmp + "]!!!");
			} catch (InstantiationException e) {
				System.err.println(e.getMessage());
			} catch (IllegalAccessException e) {
				System.err.println(e.getMessage());
			} catch (NoSuchMethodException e) {
				System.err.println(e.getMessage());
			} catch (InvocationTargetException e) {
				System.err.println(e.getMessage());
			}
			return new AvlTree<>();
		} else {
			return new AvlTree<>();
		}
	}

	public <T> T getObject(Class<T> c, Object... args) throws NullPointerException {
		if (mapObject == null || properties == null) {
			throw new NullPointerException("Have to init first!!!");
		}
		int hash = 0;
		for (Object o : args) {
			hash += o.hashCode();
		}
		return getObject(c, hash, args);
	}

	private <T> T getObject(Class<T> c, int hash, Object... args) {
		Map<Integer, Object> map = mapObject.get(c);
		T ret = null;
		if (map == null) {
			map = initMap(c);
			ret = makeObjectForPool(c, args);
			map.put(hash, ret);
			mapObject.put(c, map);
		} else {
			ret = c.cast(map.get(hash));
			if (ret == null) {
				ret = makeObjectForPool(c, args);
				map.put(hash, ret);
			}
		}
		return ret;
	}

	private <T> T makeObjectForPool(Class<T> c, Object... args) throws ExceptionInInitializerError {
		ObjectCreator creator = null;
		if (creators != null) {
			creator = creators.get(c);
		}
		if (creator == null) {
			return makeObject(c, args);
		} else {
			return c.cast(creator.create(this, c, args));
		}
	}

	private Class loadClass(String name) throws ClassNotFoundException {
		switch (name) {
			case "byte":
				return byte.class;
			case "short":
				return short.class;
			case "int":
				return int.class;
			case "long":
				return long.class;
			case "float":
				return float.class;
			case "double":
				return double.class;
			case "boolean":
				return boolean.class;
			case "char":
				return char.class;
			default:
				return loader.loadClass(name);
		}
	}

	private <T> T valueOfString(String valuse, Class<T> type) {
		Object ret = null;
		switch (type.getName()) {
			case "byte":
				ret = Byte.valueOf(valuse).byteValue();
				break;
			case "short":
				ret = Short.valueOf(valuse).shortValue();
				break;
			case "int":
				ret = Integer.valueOf(valuse).intValue();
				break;
			case "long":
				ret = Long.valueOf(valuse).longValue();
				break;
			case "float":
				ret = Float.valueOf(valuse).floatValue();
				break;
			case "double":
				ret = Double.valueOf(valuse).doubleValue();
				break;
			case "boolean":
				ret = Boolean.valueOf(valuse).booleanValue();
			case "char":
				ret = valuse.charAt(0);
				break;
			default:
				return null;
		}
		return (T) ret;
	}

	@FunctionalInterface
	public interface ObjectCreator<E> {
		/**
		 * @param pool
		 * @param c
		 * @param args
		 * @return
		 */
		E create(ObjectPool pool, Class<E> c, Object... args);
	}
}
