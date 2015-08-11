package org.oosql.tree;

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
	protected Column columnAnno;

	protected CField(Column column, String  name) {
		type = null;
		this.name = name;
		// TODO popravi notacijo če je seveda to potrebno
		columnAnno = column;
	}

	public CField(Field field) throws OosqlException, ClassNotFoundException {
		if ((columnAnno = field.getAnnotation(Column.class)) == null)
			throw new ColumnAnnotationException("Missing Column annotatio on field [" + field.getName() + "] with type of [" + field.getType() + "]");
		name = field.getName();
		type = field.getType();
		// TODO popravi notacijo, če je esveda to porebno
		columnAnno = field.getAnnotation(Column.class);
	}

	protected CField(Field field, String name, Class type, Column column) throws ClassNotFoundException {
		this.name = name;
		this.type = type;
		// TODO popravi notacijo, če je seveda to porebno
		columnAnno = column;
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

	public <T extends Annotation> T getAnnotation(Class<? extends T> annoType) {
		if (annoType == Column.class) {
			return (T) columnAnno;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return name + " " + type.getTypeName();
	}
}
