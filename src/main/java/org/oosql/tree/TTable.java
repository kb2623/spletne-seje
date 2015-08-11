package org.oosql.tree;

import org.oosql.ISqlMapping;
import org.oosql.annotation.*;
import org.oosql.exception.OosqlException;
import org.oosql.exception.TableAnnotationException;
import org.oosql.exception.ColumnAnnotationException;

import java.util.Map;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;
import java.lang.reflect.Field;

public class TTable {

	private String tableName;
	private List<IColumn> columns;

	protected TTable(Table table, String altName, List<IColumn> columns) {
		if (table.name().isEmpty())
			tableName = altName;
		else
			tableName = table.name();
		this.columns = columns;
		if (!DefaultValues.isDefault(table.id()))
			columns.add(new CLeaf(table.id()));
	}

	protected TTable(Table table) {
		tableName = table.name();
		this.columns = new LinkedList<>();
		this.columns.add(new CLeaf(table.id()));
		this.columns.add(new CLeaf(table.enumColumn()));
	}

	public TTable(Class in) throws OosqlException, ClassNotFoundException {
		Table table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Missing in [" + in.getTypeName() + "]!!!");
		else
			tableName = table.name();
		List<CField> entrys = Util.getColumnFields(in);
		if (entrys == null)
			throw new ColumnAnnotationException("Missing in [" + in.getTypeName() + "]!!!");
		else
			columns = new LinkedList<>();
		if (!DefaultValues.isDefault(table.id()))
			columns.add(new CLeaf(table.id()));
		for (CField e : entrys) try {
			columns.add(procesField(e));
		} catch (TableAnnotationException error) {
			List<CField> inner = Util.getColumnFields(e.getType());
			if (inner != null) {
				for (CField ec : inner) try {
					columns.add(procesField(ec));
				} catch (ColumnAnnotationException errorIn) {
					throw new ColumnAnnotationException("Class [" + in.getTypeName() + "] > field [" + e.getName() + "] " +
							"> Inner class [" + e.getType().getName() + "]", errorIn);
				} catch (TableAnnotationException errorIn) {
					throw new TableAnnotationException("Class [" + in.getTypeName() + "] > field [" + e.getName() + "] " +
							"> Inner class [" + e.getType().getName() + "]", errorIn);
				}
			} else {
				throw new TableAnnotationException("Class [" + in.getTypeName() + "]", error);
			}
		} catch (ColumnAnnotationException error) {
			throw new ColumnAnnotationException("Class [" + in.getTypeName() + "]", error);
		}
	}

	private IColumn procesField(CField field) throws OosqlException, ClassNotFoundException {
		try {
			Class fieldType = field.getType();
			if (fieldType.isPrimitive() || Number.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)
					|| Character.class.isAssignableFrom(fieldType) || String.class.isAssignableFrom(fieldType)) {
				return new CLeaf(field.getAnnotation(Column.class), fieldType, field.getName());
			} else if (fieldType.isEnum()) {
				Table table = Util.getTableAnnotation(fieldType);
				Column column = field.getAnnotation(Column.class);
				if (table == null) {
					return new CLeaf(column, fieldType, field.getName());
				} else {
					return new CEnum(column, table, field.getName());
				}
			} else if (fieldType.isArray()) {
				CFieldArray fieldArray = (CFieldArray) field;
				IColumn column = procesField(fieldArray.getInnerClass());
				return new CArray(fieldArray, column);
			} else if (Collection.class.isAssignableFrom(fieldType)) {
				CFieldArray fieldArray = (CFieldArray) field;
				IColumn column = procesField(fieldArray.getInnerClass());
				return new CArray(fieldArray, column);
			} else if (Map.class.isAssignableFrom(fieldType)) {
				CFieldMap fieldMap = (CFieldMap) field;
				IColumn keyColumn = procesField(fieldMap.getKeyClass());
				IColumn valueColumn = procesField(fieldMap.getValueClass());
				return new CMap(fieldMap, keyColumn, valueColumn);
			} else if (ISqlMapping.class.isAssignableFrom(fieldType)) {
				// TODO imamo razred, ki implementira plreslikavo
				return null;
			} else {
				return new CNode(field.getAnnotation(Column.class), field.getName(), new TTable(fieldType));
			}
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field [" + field.getName() + "]", e);
		} catch (TableAnnotationException e) {
			throw new TableAnnotationException("field [" + field.getName() + "]", e);
		}
	}

	protected List<IColumn> getReferences() {
		List<IColumn> list = new LinkedList<>();
		columns.stream().filter(c -> c.isPrimaryKey()).forEach(c -> list.add(c));
		return list;
	}

	public String getTableName() {
		return tableName;
	}

	public void izpis() {
		StringBuilder colunms = new StringBuilder();
		StringBuilder primaryKey = new StringBuilder();
		StringBuilder refs = new StringBuilder();
		for (IColumn c : this.columns) {
			String[] tab = c.izpis();
			colunms.append("\t" + tab[0] + ",\n");
			if (c.isPrimaryKey()) primaryKey.append(tab[1] + ", ");
			if (c instanceof CNode) refs.append("\t" + tab[2] + ",\n");
		}
		if (primaryKey.length() == 0 && refs.length() == 0) {
			colunms.deleteCharAt(colunms.length() - 2);
		} else if (primaryKey.length() > 0) {
			primaryKey.insert(0, "\tPRIMARY KEY(");
			primaryKey.delete(primaryKey.length() - 2, primaryKey.length());
			if (refs.length() == 0) {
				primaryKey.append(")\n");
			} else {
				primaryKey.append("),\n");
				refs.deleteCharAt(refs.length() - 2);
			}
		} else {
			refs.deleteCharAt(refs.length() - 2);
		}
		System.out.println("CREATE TABLE " + tableName + "(");
		System.out.print(colunms.toString());
		System.out.print(primaryKey.toString());
		System.out.print(refs.toString());
		System.out.println(")");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// TODO
		return builder.toString();
	}
}
