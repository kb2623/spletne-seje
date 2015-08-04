package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;

public class CRef implements IColumn {

	private String name;
	private Column ref;

	public CRef(Column ref, String name) {
		this.ref  = ref;
		this.name = name;
	}

	@Override
	public boolean isPrimaryKey() {
		return false;
	}

	public String[] izpis() {
		return new String[]{
				name + " " + ref.type() + (ref.typeLen() > 0 ? "(" + ref.typeLen() + ")" : ""),
				name,
				ref.name()[0]
		};
	}

	@Override
	public Column getColumn() {
		return new ColumnC(ref, name);
	}
}
