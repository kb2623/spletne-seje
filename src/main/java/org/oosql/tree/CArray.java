package org.oosql.tree;

import org.oosql.Util;
import org.oosql.ISqlMapping;
import org.oosql.annotation.Table;
import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;
import org.oosql.annotation.ArrayTable;
import org.oosql.exception.OosqlException;
import org.oosql.exception.ColumnAnnotationException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Field;
import java.util.Map;

public class CArray extends CNode {

	protected TTable tabelaVrednost;

	public CArray() {
		super();
		tabelaVrednost = null;
	}

	public CArray(Column column, String altName, ArrayTable tables, int dim, IColumn valColumn) throws ColumnAnnotationException {
		super(column, altName, new TTable(tables.arrayTable(), altName, new LinkedList<>()));
		List<IColumn> list = new LinkedList<>();
		list.add(new CNode(tables.arrayid(), altName, refTable));
		for (int i = 0; i < dim; i++) list.add(new CLeaf(new ColumnC(tables.dimPrefix() + i, tables.dimType(), tables.dimLen()), Integer.class, null));
		list.add(valColumn);
		tabelaVrednost = new TTable(tables.valueTable(), altName, list);
	}

	@Override
	public String[] izpis() {
		tabelaVrednost.izpis();
		return izpisRef();
	}
}
