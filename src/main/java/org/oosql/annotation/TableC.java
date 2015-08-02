package org.oosql.annotation;

import java.lang.annotation.Annotation;

public class TableC implements Table {

	private String name;
	private boolean noRowId;
	private Column id;
	private String pkConstraintName;

	public TableC(String name, boolean noRowId, Column id, String pkConstraintName) {
		this.name 				 = name;
		this.noRowId			 = noRowId;
		this.id					 = id;
		this.pkConstraintName = pkConstraintName;
	}

	public TableC(Table tab, String name, Boolean noRowId, Column id, String pkConstraintName) {
		this(
				name 				  == null ? tab.name() 				  : name,
				noRowId 			  == null ? tab.noRowId() 			  : noRowId,
				id 				  == null ? tab.id() 				  : id,
				pkConstraintName == null ? tab.pkConstraintName() : pkConstraintName
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
	public String pkConstraintName() {
		return pkConstraintName;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Table.class;
	}
}
