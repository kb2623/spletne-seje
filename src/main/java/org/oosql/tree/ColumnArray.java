package org.oosql.tree;

import org.oosql.exception.ColumnAnnotationException;

import java.util.List;
import java.lang.reflect.Field;

public class ColumnArray extends ColumnNode {

	private TTable tabelaVrednost;
	private List<ColumnLeaf> vresnot;

	public ColumnArray(Field field, TTable table) throws ColumnAnnotationException {
		super(field, table);
	}
}
