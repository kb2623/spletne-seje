package org.oosql.tree;

import org.oosql.SqlMapping;
import org.oosql.Util;
import org.oosql.TableClass;
import org.oosql.exception.OosqlException;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.TableAnnotationException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class Table {

	private String tableName;
	private List<IColumn> columns;

	public Table(Class in) throws OosqlException {
		Table table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Table annotation missing in \"" + in.getClass().getName() + "\"!!!");
		else
			tableName = table.getName();
		List<Field> entrys = Util.getEntryAnnotations(in);
		if (entrys == null)
			throw new ColumnAnnotationException("Column annotations missing in \"" + in.getClass().getName() + "\"!!!");
		else
			columns = new LinkedList<>();
		if (table.getAnno().autoId()) columns.add(new ColumnLeaf(new CColumn(table)));
		for (Field e : entrys) {
			try {
				columns.add(procesEntry(e));
			} catch (TableAnnotationException error) {
				if (SqlMapping.class.isAssignableFrom(e.getType())) {
					// TODO Imamo razred, ki ima mapiranje
				} else {
					throw error;
				}
			}
		}
	}

	private IColumn procesEntry(Field field) throws OosqlException {
		Class fieldType = field.getType();
		if (fieldType.isPrimitive() || String.class.isAssignableFrom(fieldType)) {
			return new ColumnLeaf(field);
		} else if (fieldType.isEnum()) {
			TableClass table = Util.getTableAnnotation(fieldType);
			if (table == null) {
				// TODO
			} else {
				// TODO
			}
		} else if (fieldType.isArray()) {
			// TODO
			int dim = 0;
			for (Class c = fieldType; c.isArray(); c = c.getComponentType()) dim++;
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			int dim = 0;
			Class c;
			for (String s : ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName().replace(">", "").split("<")) {
				dim++;
				try {
					c = Class.forName(s);
					if (!Collection.class.isAssignableFrom(c)) break;
				} catch (ClassNotFoundException e) {
					break;
				}
			}
		} else if (Map.class.isAssignableFrom(fieldType)) {
			// Imamo slovar
		} else {
			// Imamo nov objekt
			// nazaj dobimo nov objekt tipa SqlTable, po katerem se lahko sprehodimo da dobimo nove vrstice v tabeli, ter jih povežemo
			return new ColumnNode(field, new Table(fieldType));
		}
	}

	private IColumn getReferences() {
		// TODO
		return null;
	}

	public String getTableName() {
		return tableName;
	}

	public String getReferences() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// TODO
		return builder.toString();
	}
}
