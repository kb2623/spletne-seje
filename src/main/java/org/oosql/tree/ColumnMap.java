package org.oosql.tree;

import org.oosql.exception.ColumnAnnotationException;

import java.lang.reflect.Field;

public class ColumnMap extends ColumnArray {
	public ColumnMap(Field field, TTable table) throws ColumnAnnotationException {
		super(field, table);
	}
}
