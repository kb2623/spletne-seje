package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.CColumn;

public class ColumnRef implements IColumn {
	private String name;
	private Column ref;

	public ColumnRef(Column ref, String name) {
		this.ref = ref;
		this.name = name;
	}

	@Override
	public boolean isPrimaryKey() {
		return false;
	}

	public String[] izpis() {
		return new String[]{
				name + " " + ref.dataType(),
				name,
				ref.name()[0]
		};
	}

	@Override
	public Column getColumn() {
		return new CColumn(ref, name);
	}
}
