package org.oosql.tree;

import org.oosql.annotation.*;
import org.oosql.exception.ColumnAnnotationException;

public class CEnum extends CNode {
	protected CEnum(Column column, Table table, String altName) throws ColumnAnnotationException {
		super();
		try {
			Table enumTable;
			Column kColumn = null, vColumn = null;
			if (Util.hasEmptyNames(table.id()) && Util.hasEmptyNames(table.enumColumn())) {
				enumTable = new TableC(table, altName, null, new ColumnC(table.id(), new String[]{altName + "_id"}, true, null, null, null, null, null), new ColumnC(table.enumColumn(), new String[]{altName + "_value"}, false, null, null, null, null, null), null);
			} else if (Util.hasEmptyNames(table.id())) {
				enumTable = new TableC(table, altName, null, new ColumnC(table.id(), new String[]{altName + "_id"}, true, null, null, null, null, null), null, null);
			} else if (Util.hasEmptyNames(table.enumColumn())) {
				enumTable = new TableC(table, altName, null, null, new ColumnC(table.enumColumn(), new String[]{altName + "_value"}, false, null, null, null, null, null), null);
			} else {
				enumTable = table;
			}
			if (Util.hasEmptyNames(column)) {
				anno = new ColumnC(column, altName);
			} else {
				anno =	column;
			}
			refTable = new TTable(enumTable);
			setUpColumns();
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field [" + altName + "]", e);
		}
	}
}
