package org.oosql.tree;

import java.util.Map;
import java.util.HashMap;

class Tables {

	private static Map<String, TTable> tables = null;

	public static Map<String, TTable> getInstance() {
		if (tables == null) tables = new HashMap<>();
		return tables;
	}

	public static TTable put(String tableName, TTable table) {
		return getInstance().put(tableName, table);
	}

	public static boolean contains(String tableName) {
		return getInstance().containsKey(tableName);
	}

	public static TTable get(String tableName) {
		return getInstance().get(tableName);
	}

}
