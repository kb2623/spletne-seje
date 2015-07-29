package org.oosql.tree;

import org.oosql.exception.ColumnAnnotationException;

import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Field;

public class CArray extends CNode {

	protected TTable tabelaVrednost;
	protected List<CRef> vresdnot;

	public CArray() {
		super();
		tabelaVrednost = null;
		vresdnot = new LinkedList<>();
	}

	public CArray(Field field, IColumn column) throws ColumnAnnotationException {
		// TODO
	}

	@Override
	public String[] izpis() {
		// TODO
		return super.izpis();
	}
}
