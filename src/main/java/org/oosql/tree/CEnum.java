package org.oosql.tree;

import org.oosql.annotation.*;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.OosqlException;

public class CEnum extends CNode {
	protected CEnum(Column column, Table table, String altName) throws OosqlException, ClassNotFoundException {
		super();
		try {
			refTable = new TTable(table);
			setUpColumns(new ColumnsC(new Column[]{column}));
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field [" + altName + "]", e);
		}
	}
}
