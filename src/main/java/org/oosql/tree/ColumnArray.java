package org.oosql.tree;

import java.util.List;

public class ColumnArray extends ColumnNode {

	private Table tabelaVrednost;
	private List<ColumnLeaf> vresnot;

	public ColumnArray(List<ColumnLeaf> columns, Table table) {
		super(columns, table);
	}
}
