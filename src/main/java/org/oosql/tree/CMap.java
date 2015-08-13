package org.oosql.tree;

import org.oosql.exception.ColumnAnnotationException;
import org.oosql.tree.field.CFieldMap;

import java.lang.reflect.Field;

public class CMap extends CArray {
	public CMap(Field field) throws ColumnAnnotationException {
		super();
	}

	public CMap(CFieldMap fieldMap, IColumn key, IColumn value) {
		super();
		// TODO
	}
}
