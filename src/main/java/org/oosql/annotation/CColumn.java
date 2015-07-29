package org.oosql.annotation;

import java.lang.annotation.Annotation;
import java.sql.JDBCType;

public class CColumn implements Column {

	private String[] name;
	private boolean pk;
	private boolean notNull;
	private boolean unique;
	private JDBCType type;
	private int typeLength;

	public CColumn(Column col) {
		name = col.name();
		pk = col.pk();
		notNull = col.notNull();
		unique = col.unique();
		type = col.dataType();
		typeLength = col.lengthType();
	}

	public CColumn(Column col, String name) {
		this(col);
		this.name = new String[]{name};
	}

	public CColumn(Table tab) {
		name = new String[]{tab.name()};
		pk = true;
		notNull = true;
		unique = true;
		type = tab.idType();
		typeLength = 0;
	}

	@Override
	public String[] name() {
		return name;
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
	public JDBCType dataType() {
		return type;
	}

	@Override
	public int lengthType() {
		return typeLength;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Column.class;
	}
}
