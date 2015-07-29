package org.oosql.tree;

import org.oosql.Util;
import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;
import org.oosql.exception.ColumnAnnotationException;

import java.sql.JDBCType;
import java.lang.reflect.Field;

public class CLeaf implements IColumn {

	private Column anno;

	public CLeaf(Field field) throws ColumnAnnotationException {
		try {
			Column anno = field.getAnnotation(Column.class);
			if (!Util.hasEmptyNames(anno)) {
				if (field.getType().isEnum() && anno.type().equals(JDBCType.INTEGER)) {
					if (anno.typeLen() <= 0) {
						this.anno = new ColumnC(anno, null, null, null, null, JDBCType.VARCHAR, 25, null);
					} else {
						this.anno = new ColumnC(anno, null, null, null, null, JDBCType.VARCHAR, anno.typeLen(), null);
					}
				} else {
					this.anno = anno;
				}
			} else {
				if (field.getType().isEnum() && anno.type().equals(JDBCType.INTEGER)) {
					if (anno.typeLen() <= 0) {
						this.anno = new ColumnC(anno, new String[]{field.getName()}, null, null, null, JDBCType.VARCHAR, 25, null);
					} else {
						this.anno = new ColumnC(anno, new String[]{field.getName()}, null, null, null, JDBCType.VARCHAR, anno.typeLen(), null);
					}
				} else {
					this.anno = new ColumnC(anno, field.getName());
				}
			}
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field [" + field.getName() + "]", e);
		}
	}

	public CLeaf(Column column) {
		if (column.pk())	anno = column;
		else anno = new ColumnC(column, null, true, null, null, null, null, null);
	}

	public Column getColumn() {
		return anno;
	}

	public String getName() {
		return anno.name()[0];
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
