package org.oosql.tree;

import org.oosql.Util;
import org.oosql.annotation.Column;
import org.oosql.annotation.CColumn;
import org.oosql.exception.ColumnAnnotationException;

import java.util.List;
import java.util.LinkedList;
import java.lang.reflect.Field;

public class ColumnNode implements IColumn {

	protected Column anno;
	protected List<ColumnRef> columns;
	protected TTable refTable;

	public ColumnNode(Field field, TTable table) throws ColumnAnnotationException {
		Column anno = field.getAnnotation(Column.class);
		try {
			if (!Util.hasEmptyNames(anno.name())) this.anno = anno;
			else this.anno = new CColumn(anno, field.getName());
			columns = new LinkedList<>();
			setUpColumns(table);
			refTable = table;
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field \"" + field.getName() + "\" " + e.getMessage());
		}
	}

	private void setUpColumns(TTable table) throws ColumnAnnotationException {
		try {
			int index = 0;
			for (IColumn c : table.getReferences()) {
				if (c instanceof ColumnLeaf) {
					columns.add(new ColumnRef(c.getColumn(), getName(index)));
					index++;
				} else {
					int j;
					for (ColumnRef r : ((ColumnNode) c).getAdditionalColumns()) {
						columns.add(new ColumnRef(r.getColumn(), getName(index)));
						index++;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ColumnAnnotationException("missing column names");
		}
	}

	protected List<ColumnRef> getAdditionalColumns() {
		return columns;
	}

	public boolean isPrimaryKey() {
		return anno.pk();
	}

	public String getName(int index) throws ArrayIndexOutOfBoundsException {
		return anno.name()[index];
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
			columns.append("," + tab[0] + (isPrimaryKey() ? " NOT NULL" : "") + "\n");
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
