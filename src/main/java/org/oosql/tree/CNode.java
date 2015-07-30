package org.oosql.tree;

import org.oosql.Util;
import org.oosql.annotation.*;
import org.oosql.exception.ColumnAnnotationException;

import java.util.List;
import java.util.LinkedList;
import java.lang.reflect.Field;

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
		for (IColumn c : refTable.getReferences()) {
			if (c instanceof CLeaf) {
				columns.add(new CRef(c.getColumn(), getName(index)));
				index++;
			} else {
				for (CRef r : ((CNode) c).getAdditionalColumns()) {
					columns.add(new CRef(r.getColumn(), getName(index)));
					index++;
				}
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
		StringBuilder columns = new StringBuilder();
		StringBuilder primaryKey = new StringBuilder();
		StringBuilder[] reference = new StringBuilder[]{
				new StringBuilder(),
				new StringBuilder()
		};
		for (int i = 0; i < this.columns.size(); i++) {
			String tab[] = this.columns.get(i).izpis();
			columns.append("," + tab[0] + (isPrimaryKey() ? " NOT NULL" : ""));
			if (isPrimaryKey()) primaryKey.append("," + tab[1]);
			reference[0].append("," + tab[1]);
			reference[1].append("," + tab[2]);
		}
		return new String[]{
				columns.deleteCharAt(0).deleteCharAt(columns.length() - 1).toString(),
				primaryKey.length() > 0 ? primaryKey.deleteCharAt(0).toString() : "",
				"FOREFIN KEY(" + reference[0].deleteCharAt(0).toString() + ") REFERNCES "
						+ refTable.getTableName() + "(" + reference[1].deleteCharAt(0).toString() + ")"
		};
	}

	@Override
	public Column getColumn() {
		return anno;
	}
}
