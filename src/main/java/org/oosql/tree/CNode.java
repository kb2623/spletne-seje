package org.oosql.tree;

import org.oosql.annotation.*;
import org.oosql.exception.ColumnAnnotationException;

import java.util.List;
import java.util.LinkedList;

public class CNode implements IColumn {

	protected Column anno;
	protected List<CRef> columns;
	protected TTable refTable;

	public CNode() {
		anno 		= null;
		columns 	= new LinkedList<>();
		refTable = null;
	}

	public CNode(Column anno, String altName, TTable table) throws ColumnAnnotationException {
		this();
		if (!Util.hasEmptyNames(anno)) this.anno = anno;
		else this.anno = new ColumnC(anno, altName);
		refTable = table;
		setUpColumns();
	}

	protected void setUpColumns() throws ColumnAnnotationException {
		int index = 0;
		for (IColumn c : refTable.getReferences()) if (c instanceof CLeaf) {
			columns.add(new CRef(c.getColumn(), getName(index)));
			index++;
		} else {
			for (CRef r : ((CNode) c).getAdditionalColumns()) {
				columns.add(new CRef(r.getColumn(), getName(index)));
				index++;
			}
		}
	}

	protected List<CRef> getAdditionalColumns() {
		return columns;
	}

	public boolean isPrimaryKey() {
		return anno.pk();
	}

	public String getName(int index) throws ColumnAnnotationException {
		try {
			return anno.name()[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ColumnAnnotationException("missing names for Compound key");
		}
	}

	public boolean isNotNull() {
		return anno.notNull();
	}

	public boolean isUnique() {
		return anno.unique();
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
			columns.append(tab[0] + (isPrimaryKey() ? " NOT NULL" : "") + ",\n\t");
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

	@Override
	public Column getColumn() {
		return anno;
	}
}
