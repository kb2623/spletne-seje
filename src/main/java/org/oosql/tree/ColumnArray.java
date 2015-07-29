package org.oosql.tree;

import org.oosql.exception.ColumnAnnotationException;

import java.util.List;
import java.lang.reflect.Field;

public class ColumnArray extends ColumnNode {

	protected TTable tabelaVrednost;
	protected List<ColumnRef> vresnot;

	public ColumnArray(Field field, TTable table) throws ColumnAnnotationException {
		super(field, table);
	}
}
