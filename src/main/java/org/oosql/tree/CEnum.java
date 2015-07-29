package org.oosql.tree;

import org.oosql.Util;
import org.oosql.annotation.*;
import org.oosql.exception.ColumnAnnotationException;

import java.lang.reflect.Field;

public class CEnum extends CNode {
	protected CEnum(Field field, Table table, EnumTable enumTable) throws ColumnAnnotationException {
		super();
		try {
			Column kc = null, vc = null;
			if (!enumTable.keyColumn().pk())
				kc = new ColumnC(enumTable.keyColumn(), null, true, null, null, null, null, null);
			if (Util.hasEmptyNames(enumTable.keyColumn()))
				kc = new ColumnC(enumTable.keyColumn(), table.name() + "_id");
			if (Util.hasEmptyNames(enumTable.valueColumn()))
				vc = new ColumnC(enumTable.valueColumn(), table.name() + "_value");
			enumTable = new EnumTableC(enumTable, kc, vc);
			kc = field.getAnnotation(Column.class);
			if (Util.hasEmptyNames(kc)) anno = new ColumnC(kc, field.getName());
			else anno = kc;
			refTable = new TTable(enumTable, table);
			setUpColumns();
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field [" + field.getName() + "]", e);
		}
	}
}
