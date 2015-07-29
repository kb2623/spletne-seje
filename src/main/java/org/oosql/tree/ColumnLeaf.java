package org.oosql.tree;

import org.oosql.Util;
import org.oosql.annotation.Column;
import org.oosql.annotation.CColumn;
import org.oosql.annotation.Table;
import org.oosql.exception.ColumnAnnotationException;

import java.lang.reflect.Field;

public class ColumnLeaf implements IColumn {

	private Column anno;

	public ColumnLeaf(Column column, String name) {
		anno = new CColumn(column, name);
	}

	public ColumnLeaf(Field field) throws ColumnAnnotationException {
		Column anno = field.getAnnotation(Column.class);
		try {
			if (!Util.hasEmptyNames(anno.name())) this.anno = anno;
			else this.anno = new CColumn(anno, field.getName());
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field \"" + field.getName() + "\" " + e.getMessage());
		}
	}

	public ColumnLeaf(Table table) {
		anno = new CColumn(table);
	}

	public Column getColumn() {
		return anno;
	}

	public String getName() {
		return anno.name()[0];
	}

	public String getType() {
		return anno.dataType().getName() + (anno.lengthType() > 0 ? "(" + anno.lengthType() + ")" : "");
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
				isPrimaryKey() ? getName() : "",
				null
		};
	}
}
