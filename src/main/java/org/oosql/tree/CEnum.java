package org.oosql.tree;

import org.oosql.Util;
import org.oosql.annotation.*;
import org.oosql.exception.ColumnAnnotationException;

import java.lang.reflect.Field;

public class CEnum extends CNode {
	protected CEnum(Column column, Table table, EnumTable enumTable, String altName) throws ColumnAnnotationException {
		super();
		try {
			Column kColumn = null, vColumn = null;
			if (!enumTable.keyColumn().pk())
				kColumn = new ColumnC(enumTable.keyColumn(), null, true, null, null, null, null, null);
			if (Util.hasEmptyNames(enumTable.keyColumn()))
				kColumn = new ColumnC(enumTable.keyColumn(), table.name() + "_id");
			if (Util.hasEmptyNames(enumTable.valueColumn()))
				vColumn = new ColumnC(enumTable.valueColumn(), table.name() + "_value");
			if (enumTable.name().isEmpty())
				enumTable = new EnumTableC(enumTable, table.name(), kColumn, vColumn);
			else
				enumTable = new EnumTableC(enumTable, null, kColumn, vColumn);
			if (Util.hasEmptyNames(column)) anno = new ColumnC(column, altName);
			else anno = column;
			refTable = new TTable(enumTable);
			setUpColumns();
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field [" + altName + "]", e);
		}
	}
}
