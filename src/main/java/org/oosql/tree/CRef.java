package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;

public class CRef implements IColumn {

	private Column column;
	private String refColumn;

	public CRef(Column refColumn, Column column) {
		this.refColumn = refColumn.name();
		if (column.type().equals(refColumn.type()) && column.typeLen() == refColumn.typeLen()) {
			this.column = column;
		} else if (column.type().equals(refColumn.type()) && column.typeLen() != refColumn.typeLen()) {
			this.column = new ColumnC(column, column.name(), null, null, null, null, refColumn.typeLen());
		} else if (!column.type().equals(refColumn.type()) && column.typeLen() == refColumn.typeLen() ) {
			this.column = new ColumnC(column, column.name(), null, null, null, refColumn.type(), refColumn.typeLen());
		} else {
			this.column = new ColumnC(column, column.name(), null, null, null, refColumn.type(), refColumn.typeLen());
		}
	}

	@Override
	public boolean isPrimaryKey() {
		return column.pk();
	}

	public String[] izpis() {
		return new String[]{
				column.name()+ " " + column.type() + (column.typeLen() > 0 ? "(" + column.typeLen() + ")" : ""),
				column.name(),
				refColumn
		};
	}

	public Column getColumn() {
		return column;
	}
}
