package org.oosql.annotation;

import java.sql.JDBCType;
import java.lang.annotation.Annotation;

public class ColumnC implements Column {

	private String name;
	private boolean pk;
	private boolean notNull;
	private boolean unique;
	private JDBCType type;
	private int typeLen;

	public ColumnC() {
		this(
				null,
				false,
				false,
				false,
				JDBCType.INTEGER,
				0
		);
	}

	public ColumnC(String name, JDBCType type, int typeLen) {
		this(
				name,
				true,
				false,
				false,
				type,
				typeLen
		);
	}

	public ColumnC(String name, boolean pk, boolean notNull, boolean unique, JDBCType type, int typeLen) {
		this.name = name;
		this.pk = pk;
		this.notNull = notNull;
		this.unique = unique;
		this.type = type;
		this.typeLen = typeLen;
	}

	public ColumnC(Column column, String name, Boolean pk, Boolean notNull, Boolean unique, JDBCType type, Integer typeLen) {
		this(
				name == null ? column.name() : name,
				pk == null ? column.pk() : pk,
				notNull == null ? column.notNull()	: notNull,
				unique == null ? column.unique() : unique,
				type == null ? column.type()	: type,
				typeLen == null ? column.typeLen() : typeLen
		);
	}

	public ColumnC(Column column, String name) {
		this(
				column,
				name,
				null,
				null,
				null,
				null,
				null
		);
	}

	@Override
	public String name() {
		return name != null ? name : "";
	}

	@Override
	public boolean pk() {
		return pk;
	}

	@Override
	public boolean notNull() {
		return notNull;
	}

	@Override
	public boolean unique() {
		return unique;
	}

	@Override
	public JDBCType type() {
		return type;
	}

	@Override
	public int typeLen() {
		return typeLen;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Column.class;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Column) {
			Column c = (Column) o;
			return name().equals(c.name()) && pk == c.pk() && notNull == c.notNull() && unique == c.unique() && type.equals(c.type()) && typeLen == c.typeLen();
		} else {
			return false;
		}
	}
}
