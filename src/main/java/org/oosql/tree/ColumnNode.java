package org.oosql.tree;

import java.util.List;
import java.lang.reflect.Field;

public class ColumnNode extends ColumnLeaf {

	private List<IColumn> columns;
	private Table refTable;

	public ColumnNode(Field field, Table table) {
		super(field);
		// TODO pridobi vse primarne ključe in jih dodaj v tabelo columns
	}

	public List<ColumnLeaf> getColumns() {
		return columns;
	}

	public Table getRefTable() {
		return refTable;
	}
}
