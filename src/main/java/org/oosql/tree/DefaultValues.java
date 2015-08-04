package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;

public class DefaultValues {

	private static Column defaultValue;

	public static Column getInstance() {
		if (defaultValue == null) {
			defaultValue = new ColumnC();
		}
		return defaultValue;
	}

	public static boolean isDefault(Column column) {
		return getInstance().equals(column);
	}
}
