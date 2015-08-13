package org.oosql.annotation;

import java.lang.annotation.Annotation;

public class TableC implements Table {

	private String name;
	private boolean noRowId;
	private Column id;
	private Columns columns;

	public TableC(String name, boolean noRowId, Column id, Columns columns) {
		this.name = name;
		this.noRowId = noRowId;
		this.id = id;
		this.columns = columns;
	}

	public TableC(Table tab, String name, Boolean noRowId, Column id, Columns column) {
		this(
				name == null ? tab.name() : name,
				noRowId == null ? tab.noRowId() : noRowId,
				id  == null ? tab.id() : id,
				column == null ? tab.columns() : column
		);
	}

	@Override
	public String name() {
		return name != null ? name : "";
	}

	@Override
	public boolean noRowId() {
		return noRowId;
	}

	@Override
	public Column id() {
		return id;
	}

	@Override
	public Columns columns() {
		return columns;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Table.class;
	}
}
