package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.CColumn;

import java.lang.reflect.Field;

public class ColumnLeaf implements IColumn {

	private Column anno;

	public ColumnLeaf(Field field) {
		Column anno = field.getAnnotation(Column.class);
		if (!anno.name()[0].isEmpty()) this.anno = anno;
		else this.anno = new CColumn(anno, field.getName());
	}

	public String getName() {
		return anno.name()[0];
	}

	public String getType() {
		return anno.dataType().getDateType();
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
}
