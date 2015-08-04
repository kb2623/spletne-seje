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

	protected TTable(Table table, String altName, List<IColumn> columns) {
		if (table.name().isEmpty())
			tableName = altName;
		else
			tableName = table.name();
		this.columns = columns;
		if (!DefaultValues.isDefault(table.id()))
			columns.add(new CLeaf(table.id(), true));
	}

	protected TTable(EnumTable table) {
		tableName = table.name();
		this.columns = new LinkedList<>();
		this.columns.add(new CLeaf(table.keyColumn(), true));
		this.columns.add(new CLeaf(table.valueColumn(), false));
	}

	public TTable(Class in) throws OosqlException {
		Table table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Missing in [" + in.getName() + "]!!!");
		else
			tableName = table.name();
		List<Field> entrys = Util.getColumnFields(in);
		if (entrys == null)
			throw new ColumnAnnotationException("Missing in [" + in.getName() + "]!!!");
		else
			columns = new LinkedList<>();
		if (!DefaultValues.isDefault(table.id()))
			columns.add(new CLeaf(table.id(), true));
		for (Field e : entrys) try {
			columns.add(procesField(e));
		} catch (TableAnnotationException error) {
			List<Field> inner = Util.getColumnFields(e.getType());
			if (inner != null) {
				for (Field ec : inner) try {
					columns.add(procesField(ec));
				} catch (ColumnAnnotationException errorIn) {
					throw new ColumnAnnotationException("Class [" + in.getName() + "] > field [" + e.getName() + "] " +
							"> Inner class [" + e.getType().getName() + "]", errorIn);
				} catch (TableAnnotationException errorIn) {
					throw new TableAnnotationException("Class [" + in.getName() + "] > field [" + e.getName() + "] " +
							"> Inner class [" + e.getType().getName() + "]", errorIn);
				}
			} else {
				throw new ColumnAnnotationException("Column annotations missing in [" + e.getType().getName() + "]");
			}
		} catch (ColumnAnnotationException error) {
			throw new ColumnAnnotationException("Class [" + in.getName() + "]", error);
		}
	}

	private IColumn procesField(Field field) throws OosqlException {
		try {
			Class fieldType = field.getType();
			if (fieldType.isPrimitive() || Number.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)
					|| Character.class.isAssignableFrom(fieldType) || String.class.isAssignableFrom(fieldType)) {
				return new CLeaf(field.getAnnotation(Column.class), fieldType, field.getName());
			} else if (fieldType.isEnum()) {
				Table table = Util.getTableAnnotation(fieldType);
				Column column = field.getAnnotation(Column.class);
				if (table == null) {
					return new CLeaf(field.getAnnotation(Column.class), fieldType, field.getName());
				} else {
					EnumTable eName = field.getAnnotation(EnumTable.class);
					return new CEnum(column, table, eName == null ? new EnumTableC(table) : eName, field.getName());
				}
			} else if (fieldType.isArray()) {
				int dim = 0;
				Class c;
				for (c = fieldType; c.isArray(); c = c.getComponentType()) dim++;
				return processArray(field.getAnnotation(Column.class), field.getAnnotation(ArrayTable.class), field.getName(), dim, c);
			} else if (Collection.class.isAssignableFrom(fieldType)) {
				int dim = 0;
				Class c = null;
				for (String s : field.getGenericType().getTypeName().replace(">", "").split("<")) try {
					c = Class.forName(s);
					if (!Collection.class.isAssignableFrom(c)) {
						break;
					} else {
						dim++;
					}
				} catch (ClassNotFoundException e) {
					if (s.endsWith("[]")) {
						throw new ColumnAnnotationException("Nested Array not supported");
					} else {
						throw new ColumnAnnotationException(e.getMessage());
					}
				}
				return processArray(field.getAnnotation(Column.class), field.getAnnotation(ArrayTable.class), field.getName(), dim, c);
			} else if (Map.class.isAssignableFrom(fieldType)) {
				// TODO
				return null;
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

	private IColumn processArray(Column column, ArrayTable tables, String altName, int dim, Class innerType) throws OosqlException {
		if (tables == null) {
			tables = new ArrayTableC(altName);
		} else {
			tables = new ArrayTableC(tables, altName);
		}
		List<IColumn> valColumns = new LinkedList<>();
		try {
			valColumns.add(processClassArray(innerType, tables));
		} catch (TableAnnotationException e) {
			for (Field f : Util.getColumnFields(innerType)) try {
				if (f.getType().isArray()) {
					throw new ColumnAnnotationException("Nested arrays not supported");
				} else if (Collection.class.isAssignableFrom(f.getType())) {
					throw new ColumnAnnotationException("Nested Collections not supported");
				} else if (Map.class.isAssignableFrom(f.getType())) {
					throw new ColumnAnnotationException("Nested Map not supported");
				} else {
					valColumns.add(procesField(f));
				}
			} catch (ColumnAnnotationException eIn) {
				throw new ColumnAnnotationException("Inner class [" + innerType.getName() + "]", eIn);
			}
		}
		return new CArray(column, altName, tables, dim, valColumns);
	}

	private IColumn processClassArray(Class type, ArrayTable arrayTable) throws OosqlException {
		if (Map.class.isAssignableFrom(type)) {
			throw new ColumnAnnotationException("Nested Map not supported");
		} else if (Collection.class.isAssignableFrom(type)) {
			throw new ColumnAnnotationException("Nested Collection not supported");
		} else if (type.isPrimitive() || Number.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)
				|| Character.class.isAssignableFrom(type) || String.class.isAssignableFrom(type)) {
			return new CLeaf(arrayTable.valueColum(), type, type.getSimpleName());
		} else if (type.isEnum()) {
			Table table = Util.getTableAnnotation(type);
			if (table == null) {
				return new CLeaf(arrayTable.valueColum(), type, type.getSimpleName());
			} else {
				return new CEnum(arrayTable.valueColum(), table, arrayTable.enumColumn(), type.getSimpleName());
			}
		} else if (ISqlMapping.class.isAssignableFrom(type)) {
			// TODO imamo razred, ki uporablja preslikavo
			return null;
		} else {
			return new CNode(arrayTable.valueColum(), type.getSimpleName(), new TTable(type));
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
