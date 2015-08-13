package org.oosql.tree.field;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;
import org.oosql.annotation.Columns;
import org.oosql.annotation.ColumnsC;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.OosqlException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CField {

	protected Class type;
	protected String name;
	protected Columns columnAnno;

	protected CField(Columns column, String  name) throws ColumnAnnotationException {
		type = null;
		this.name = name;
		columnAnno = fixColumn(column.value());
	}

	public CField(Field field) throws OosqlException, ClassNotFoundException {
		if (field.getAnnotationsByType(Column.class) == null)
			throw new ColumnAnnotationException("Missing Column annotatio on field [" + field.getName() + "] with type of [" + field.getType() + "]");
		name = field.getName();
		type = field.getType();
		columnAnno = fixColumn(field.getAnnotationsByType(Column.class));
	}

	protected CField(Field field, String name, Class type, Columns column) throws ClassNotFoundException, ColumnAnnotationException {
		this.name = name;
		this.type = type;
		columnAnno = fixColumn(column.value());
	}

	private ColumnsC fixColumn(Column[] columns) {
		List<Column> list = new ArrayList<>(columns.length);
		for (Column column : columns) {
			if (column.name().isEmpty()) {
				list.add(new ColumnC(column, name));
			} else {
				list.add(column);
			}
		}
		return new ColumnsC(list.toArray(new Column[columns.length]));
	}

	public Class getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Column getColumnAnno() {
		return columnAnno.value()[0];
	}

	public Columns getColumnAnnos() {
		return columnAnno;
	}

	public <T extends Annotation> T getAnnotation(Class<? extends T> annoType) {
		if (annoType == Column.class) {
			return (T) columnAnno.value()[0];
		} else if (annoType == Columns.class) {
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
