package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;
import org.oosql.exception.ColumnAnnotationException;

import java.sql.JDBCType;

public class CLeaf implements IColumn {

	private Column anno;

	public CLeaf(Column column) {
		anno = column;
	}

	public Column getColumn() {
		return anno;
	}

	public String getName() {
		return anno.name();
	}

	public String getType() {
		return anno.type().getName() + (anno.typeLen() > 0 ? "(" + anno.typeLen() + ")" : "");
	}

	public boolean isPrimaryKey() {
		return anno.pk();
	}

	public boolean isNotNull() {
		return anno.notNull();
	}

	public boolean isUnique() {
		return anno.unique();
	}

	public String[] izpis() {
		return new String[]{
				getName() + " " + getType() + (isPrimaryKey() || isNotNull() ? " NOT NULL" : "") + (!isPrimaryKey() && isUnique() ? " UNIQUE" : ""),
				isPrimaryKey() ? getName() : ""
		};
	}
}
