package org.oosql.annotation;

import java.lang.annotation.Annotation;
import java.sql.JDBCType;

public class CTable implements Table {

	private String name;
	private boolean noRowId;
	private boolean id;
	private JDBCType idType;
	private String pkConstraintName;

	public CTable(Table tab) {
		name = tab.name();
		noRowId = tab.noRowId();
		id = tab.id();
		idType = tab.idType();
		pkConstraintName = tab.pkConstraintName();
	}

	public CTable(Table tab, String name) {
		this(tab);
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean noRowId() {
		return noRowId;
	}

	@Override
	public boolean id() {
		return id;
	}

	@Override
	public JDBCType idType() {
		return idType;
	}

	@Override
	public String pkConstraintName() {
		return pkConstraintName;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Table.class;
	}
}
