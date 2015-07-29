package org.oosql.tree;

import org.oosql.exception.ColumnAnnotationException;

import java.lang.reflect.Field;

public class ColumnEnum extends ColumnNode {

	public ColumnEnum(Field field) throws ColumnAnnotationException {
		super(field, null);
	}
}
