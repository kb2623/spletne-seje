package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnC;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.TableAnnotationException;
import org.oosql.tree.field.CFieldArray;

import java.util.LinkedList;
import java.util.List;

public class CArray extends CNode {

	protected TTable tabelaVrednost;

	public CArray() {
		super();
		tabelaVrednost = null;
	}

	public CArray(CFieldArray fieldArray, IColumn innerClass) throws ColumnAnnotationException, TableAnnotationException {
		super(fieldArray.getColumnAnnos(), new TTable(fieldArray.getArrayAnno().arrayTable(), new LinkedList<>()));
		List<IColumn> list = new LinkedList<>();
		list.add(new CNode(fieldArray.getArrayAnno().arrayTable().id(), refTable));
		for (int i = 0; i < fieldArray.getDimension(); i++) {
			Column dimColumn = fieldArray.getArrayAnno().valueTable().columns().value()[i];
			list.add(new CLeaf(dimColumn));
		}
		list.add(innerClass);
		tabelaVrednost = new TTable(list, fieldArray.getArrayAnno().valueTable());
	}

	@Override
	public String[] izpis() {
		tabelaVrednost.izpis();
		return izpisRef();
	}
}
