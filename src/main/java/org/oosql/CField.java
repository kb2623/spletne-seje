package org.oosql;

import org.oosql.annotation.ArrayTable;
import org.oosql.annotation.Column;
import org.oosql.annotation.MapTable;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.OosqlException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class CField {

	protected Class type;
	protected String name;
	private Column columnAnno;

	public CField(Field field) throws OosqlException, ClassNotFoundException {
		if ((columnAnno = field.getAnnotation(Column.class)) == null)
			throw new ColumnAnnotationException("Missing Column annotatio on field [" + field.getName() + "] with type of [" + field.getType() + "]");
		name = field.getName();
		if (!field.getType().isArray() && !Collection.class.isAssignableFrom(field.getType()) && !Map.class.isAssignableFrom(field.getType())) {
			type = field.getType();
		}
	}

	protected CField(String name, Class type) throws ClassNotFoundException {
		this.name = name;
		this.type = type;
	}

	public Class getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Column getColumnAnno() {
		return columnAnno;
	}

	public Annotation getAnnotaion(Class annoType) {
		if (annoType == Column.class) {
			return columnAnno;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return name + " " + type.getTypeName();
	}
}
