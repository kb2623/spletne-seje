package org.oosql.tree;

import java.util.Map;
import java.util.HashMap;

class Tables {

	private static Map<Class, TTable> tables = null;

	public static Map<Class, TTable> getInstance() {
		if (tables == null) tables = new HashMap<>();
		return tables;
	}

	public static TTable put(Class c, TTable t) {
		return getInstance().put(c, t);
	}

	public static boolean contains(Class c) {
		return getInstance().containsKey(c);
	}

	public static TTable get(Class c) {
		return getInstance().get(c);
	}

}
