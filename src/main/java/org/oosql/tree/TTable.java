package org.oosql.tree;

import org.oosql.Util;
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

	protected TTable(EnumTable columns, Table table) {
		tableName = table.name();
		this.columns = new LinkedList<>();
		this.columns.add(new CLeaf(columns.keyColumn(), true));
		this.columns.add(new CLeaf(columns.valueColumn(), false));
	}

	public TTable(Class in) throws OosqlException {
		Table table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Missing in \"" + in.getClass().getName() + "\"!!!");
		else
			tableName = table.name();
		List<Field> entrys = Util.getColumnFields(in);
		if (entrys == null)
			throw new ColumnAnnotationException("Missing in \"" + in.getClass().getName() + "\"!!!");
		else
			columns = new LinkedList<>();
		if (!table.id().equals(new ColumnC())) columns.add(new CLeaf(table.id(), true));
		for (Field e : entrys) try {
			columns.add(procesField(e));
		} catch (TableAnnotationException error) {
			if (ISqlMapping.class.isAssignableFrom(e.getType())) {
				// TODO Imamo razred, ki ima mapiranje
			} else {
				List<Field> inner = Util.getColumnFields(e.getType());
				if (!inner.isEmpty()) {
					for (Field ec : inner) try {
						columns.add(procesField(ec));
					} catch (ColumnAnnotationException errorIn) {
						throw new ColumnAnnotationException("Inner class [" + e.getType().getName() + "]", errorIn);
					}
				} else {
					throw error;
				}
			}
		} catch (ColumnAnnotationException error) {
			throw new ColumnAnnotationException("Class [" + in.getName() + "]", error);
		}
	}

	private IColumn procesField(Field field) throws OosqlException {
		try {
			Class fieldType = field.getType();
			if (fieldType.isPrimitive() || String.class.isAssignableFrom(fieldType)) {
				return new CLeaf(field.getAnnotation(Column.class), fieldType, field.getName());
			} else if (fieldType.isEnum()) {
				Table table = Util.getTableAnnotation(fieldType);
				Column column = field.getAnnotation(Column.class);
				if (table == null) {
					return new CLeaf(field.getAnnotation(Column.class), fieldType, field.getName());
				} else {
					EnumTable eName = field.getAnnotation(EnumTable.class);
					return new CEnum(field, table, eName == null ? new EnumTableC(table) : eName);
				}
			} else if (fieldType.isArray()) {
				int dim = 0;
				Class c;
				for (c = fieldType; c.isArray(); c = c.getComponentType()) dim++;
				if (Collection.class.isAssignableFrom(c)) {
					throw new ColumnAnnotationException("Nested Collection not supported");
				} else if (Map.class.isAssignableFrom(c)) {
					throw new ColumnAnnotationException("Nested Map not supported");
				} else {
					IColumn valueCol = processClass(c, field.getAnnotation(ArrayTable.class).valueColum());
				}
				return null;
			} else if (Collection.class.isAssignableFrom(fieldType)) {
				int dim = 0;
				Class c = null;
				for (String s : field.getGenericType().getTypeName().replace(">", "").split("<")) try {
					c = Class.forName(s);
					if (!Collection.class.isAssignableFrom(c)) break;
					else dim++;
				} catch (ClassNotFoundException e) {
					throw new ColumnAnnotationException("Nested Array not supported");
				}
				if (Map.class.isAssignableFrom(c)) {
					throw new ColumnAnnotationException("Nested Map not supported");
				} else {
					// TODO
				}
				return null;
			} else if (Map.class.isAssignableFrom(fieldType)) {
				// Imamo slovar
				return null;
			} else {
				return new CNode(field.getAnnotation(Column.class), field.getName(), new TTable(fieldType));
			}
		} catch (ColumnAnnotationException e) {
			throw new ColumnAnnotationException("field [" + field.getName() + "]", e);
		}
	}

	private IColumn processClass(Class type, Column column) throws OosqlException {
		if (type.isPrimitive() || String.class.isAssignableFrom(type)) {
			// TODO imamo primitivni tip oziroma niz
			return null;
		} else if (type.isEnum()) {
			// TODO imamo enum
			return null;
		} else if (ISqlMapping.class.isAssignableFrom(type)) {
			// TODO imamo razred, ki uporablja preslikavo
			return null;
		} else {
			return new CNode(column, type.getSimpleName(), new TTable(type));
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
			if (c.isPrimaryKey()) primaryKey.append("," + tab[1]);
			if (c instanceof CNode) refs.append("\t," + tab[2] + "\n");
		}
		if (primaryKey.length() < 0) colunms.deleteCharAt(colunms.length() - 2);
		if (primaryKey.length() > 0) primaryKey.deleteCharAt(0).insert(0, "\tPRIMARY KEY(").append(")\n");
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
