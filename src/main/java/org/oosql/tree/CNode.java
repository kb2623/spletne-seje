package org.oosql.tree;

import org.oosql.annotation.Column;
import org.oosql.annotation.Columns;
import org.oosql.annotation.ColumnsC;
import org.oosql.exception.ColumnAnnotationException;

import java.util.LinkedList;
import java.util.List;

public class CNode implements IColumn {

	protected List<CRef> columns;
	protected TTable refTable;

	public CNode() {
		columns = new LinkedList<>();
		refTable = null;
	}

	protected CNode(Column column, TTable table) throws ColumnAnnotationException {
		this();
		refTable = table;
		setUpColumns(new ColumnsC(new Column[]{column}));
	}

	public CNode(Columns columns, TTable table) throws ColumnAnnotationException {
		this();
		refTable = table;
		setUpColumns(columns);
	}

	protected void setUpColumns(Columns columns) throws ColumnAnnotationException {
		int index = 0;
		for (IColumn c : refTable.getReferences()) if (c instanceof CLeaf) {
			this.columns.add(new CRef(((CLeaf) c).getColumn(), getColumn(columns, index)));
			index++;
		} else {
			for (CRef r : ((CNode) c).getAdditionalColumns()) {
				this.columns.add(new CRef(r.getColumn(), getColumn(columns, index)));
				index++;
			}
		}
	}

	protected List<CRef> getAdditionalColumns() {
		return columns;
	}

	public boolean isPrimaryKey() {
		for (CRef cRef : columns) if (cRef.isPrimaryKey()) {
			return true;
		}
		return false;
	}

	public Column getColumn(Columns columns, int index) throws ColumnAnnotationException {
		try {
			return columns.value()[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ColumnAnnotationException("missing columns for Compound key");
		}
	}

	public TTable getRefTable() {
		return refTable;
	}

	public String[] izpis() {
		refTable.izpis();
		return izpisRef();
	}

	protected String[] izpisRef() {
		StringBuilder columns = new StringBuilder();
		StringBuilder primaryKey = new StringBuilder();
		StringBuilder[] reference = new StringBuilder[]{
				new StringBuilder(),
				new StringBuilder()
		};
		for (IColumn c : this.columns) {
			String tab[] = c.izpis();
			columns.append(tab[0] + (c.isPrimaryKey() ? " NOT NULL" : "") + ",\n\t");
			if (isPrimaryKey()) primaryKey.append(tab[1] + ", ");
			reference[0].append(tab[1] + ", ");
			reference[1].append(tab[2] + ", ");
		}
		if (primaryKey.length() > 0) {
			primaryKey.delete(primaryKey.length() - 2, primaryKey.length());
		}
		if (reference[0].length() > 0) {
			reference[0].delete(reference[0].length() - 2, reference[0].length());
			reference[1].delete(reference[1].length() - 2, reference[1].length());
		}
		return new String[]{
				columns.delete(columns.length() - 3, columns.length()).toString(),
				primaryKey.length() > 0 ? primaryKey.toString() : "",
				"FOREIGN KEY(" + reference[0].toString() + ") REFERNCES " + refTable.getTableName() + "(" + reference[1].toString() + ")"
		};
	}
}
