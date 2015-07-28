package org.oosql.annotation;

import java.lang.annotation.Annotation;

public class CTable implements Table {

	private String name;
	private boolean noRowId;
	private boolean autoId;
	private String autoIdType;

	public CTable(Table tab) {
		name = tab.name();
		noRowId = tab.noRowId();
		autoId = tab.autoId();
		autoIdType = tab.autoIdType();
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
	public boolean autoId() {
		return autoId;
	}

	@Override
	public String autoIdType() {
		return autoIdType;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Table.class;
	}
}
