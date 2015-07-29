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
import java.lang.reflect.ParameterizedType;

public class TTable {

	private String tableName;
	private List<IColumn> columns;

	protected TTable(EnumTable columns, Table table) {
		tableName = table.name();
		this.columns = new LinkedList<>();
		this.columns.add(new CLeaf(columns.keyColumn()));
		this.columns.add(new CLeaf(columns.valueColumn()));
	}

	public TTable(Class in) throws OosqlException {
		Table table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Table annotation missing in \"" + in.getClass().getName() + "\"!!!");
		else
			tableName = table.name();
		List<Field> entrys = Util.getEntryAnnotations(in);
		if (entrys == null)
			throw new ColumnAnnotationException("Column annotations missing in \"" + in.getClass().getName() + "\"!!!");
		else
			columns = new LinkedList<>();
		if (!table.id().equals(new ColumnC())) columns.add(new CLeaf(table.id()));
		for (Field e : entrys) {
			try {
				columns.add(procesEntry(e));
			} catch (TableAnnotationException error) {
				if (ISqlMapping.class.isAssignableFrom(e.getType())) {
					// TODO Imamo razred, ki ima mapiranje
				} else {
					// TODO imamo razred, ki nima Table annotacije, lahko pa vsebuje Column anotacijo, če pa tudi tega nima potem vrži napako
					throw error;
				}
			} catch (ColumnAnnotationException error) {
				throw new ColumnAnnotationException("Class [" + in.getName() + "]", error);
			}
		}
	}

	private IColumn procesEntry(Field field) throws OosqlException {
		Class fieldType = field.getType();
		if (fieldType.isPrimitive() || String.class.isAssignableFrom(fieldType)) {
			return new CLeaf(field);
		} else if (fieldType.isEnum()) {
			Table table = Util.getTableAnnotation(fieldType);
			Column column = field.getAnnotation(Column.class);
			if (table == null) {
				return new CLeaf(field);
			} else {
				EnumTable eName = field.getAnnotation(EnumTable.class);
				return new CEnum(field, table, eName == null ? new EnumTableC(table) : eName);
			}
		} else if (fieldType.isArray()) {
			// TODO
			int dim = 0;
			Class c;
			for (c = fieldType; c.isArray(); c = c.getComponentType()) dim++;
			// TODO
//			IColumn value = procesEntry(procesEntry(c));
			return null;
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			// TODO
			int dim = 1;
			Class c;
			for (String s : ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName().replace(">", "").split("<")) {
				try {
					c = Class.forName(s);
					if (!Collection.class.isAssignableFrom(c)) break;
					else dim++;
				} catch (ClassNotFoundException e) {
					break;
				}
			}
			return null;
		} else if (Map.class.isAssignableFrom(fieldType)) {
			// Imamo slovar
			return null;
		} else {
			return new CNode(field, new TTable(fieldType));
		}
	}

	protected List<IColumn> getReferences() {
		List<IColumn> list = new LinkedList<>();
		for (IColumn c : columns) if (c.isPrimaryKey()) list.add(c);
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
