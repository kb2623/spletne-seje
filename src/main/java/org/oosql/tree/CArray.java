package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;
import org.oosql.annotation.ArrayTable;
import org.oosql.exception.ColumnAnnotationException;

import java.util.LinkedList;
import java.util.List;

public class CArray extends CNode {

	protected TTable tabelaVrednost;

	public CArray() {
		super();
		tabelaVrednost = null;
	}

	public CArray(CFieldArray fieldArray, IColumn innerClass) throws ColumnAnnotationException {
		super(fieldArray.getColumnAnno(), fieldArray.getName(), new TTable(fieldArray.getArrayAnno().arrayTable(), fieldArray.getName(), new LinkedList<>()));
		List<IColumn> list = new LinkedList<>();
		list.add(new CNode(fieldArray.getArrayAnno().arrayid(), fieldArray.getName(), refTable));
		for (int i = 0; i < fieldArray.getDimension(); i++)
			list.add(new CLeaf(new ColumnC(fieldArray.getArrayAnno().dimPrefix() + i, fieldArray.getArrayAnno().dimType(), fieldArray.getArrayAnno().dimLen()), Integer.class, null));
		list.add(innerClass);
		tabelaVrednost = new TTable(fieldArray.getArrayAnno().valueTable(), fieldArray.getName(), list);
	}

	@Override
	public String[] izpis() {
		tabelaVrednost.izpis();
		return izpisRef();
	}
}
