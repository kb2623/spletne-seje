package org.oosql;

import org.oosql.annotation.ArrayTables;
import org.oosql.annotation.Column;
import org.oosql.annotation.MapTable;
import org.oosql.annotation.ArrayTable;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.OosqlException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.AnnotatedArrayType;
import java.util.Map;

public class ColumnField {

	private Class type;
	private String name;
	private Column columnAnno;
	private MapTable mapAnno = null;
	private int dimension = 0;
	private ArrayTable arrayAnno = null;
	private ColumnField innerClass;

	public ColumnField(Field field) throws OosqlException, ClassNotFoundException {
		if ((columnAnno = field.getAnnotation(Column.class)) == null)
			throw new ColumnAnnotationException("Missing Column annotatio on field [" + field.getName() + "] with type of [" + field.getType() + "]");
		name = field.getName();
		if (field.getType().isArray()) {
			setArray(field, field.getGenericType().getTypeName(), 0);
		} else if (Collection.class.isAssignableFrom(field.getType())) {
			setCollection(field, field.getGenericType().getTypeName(), 0);
		} else if (Map.class.isAssignableFrom(field.getType())) {
			System.out.println("M " + field.getName() + " >> " + field.getGenericType().getTypeName());
			// TODO
		} else {
			type = field.getType();
		}
	}

	private ColumnField(Field field, String typeName, int index) throws ClassNotFoundException {
		this.name = field.getName();
		if (typeName.endsWith("[]")) {
			setArray(field, typeName, index);
		} else {
			setCollection(field, typeName, index);
		}
	}

	private void setCollection(Field field, String typeName, int index) throws ClassNotFoundException {
		arrayAnno = field.getAnnotationsByType(ArrayTable.class)[index];
		StringBuilder builder = new StringBuilder(typeName);
		int i = builder.indexOf("<");
		type = Class.forName(builder.substring(0, builder.indexOf("<")));
		builder.delete(0, i + 1).deleteCharAt(builder.length() - 1);
		dimension++;
		Class c = type;
		do {
			i = builder.indexOf("<");
			if (i == -1) {
				System.out.println(builder.toString());
				break;
			} else {
				c = Class.forName(builder.substring(0, i));
				builder.delete(0, i + 1).deleteCharAt(builder.length() - 1);
				dimension++;
			}
		} while (Collection.class.isAssignableFrom(c));
		if (builder.charAt(builder.length() - 1) == ']') {
			innerClass = new ColumnField(field, builder.toString(), 1);
		} else if (Map.class.isAssignableFrom(c)) {
			// TODO imamo slovar
			innerClass = null;
		} else {
			innerClass = new ColumnField("list_" + field.getName(), c);
		}
	}

	private void setArray(Field field, String typeName, int index) throws ClassNotFoundException {
		arrayAnno = field.getAnnotationsByType(ArrayTable.class)[index];
		StringBuilder builder = new StringBuilder(typeName);
		do {
			dimension++;
			builder.delete(builder.length() - 2, builder.length());
		} while (builder.charAt(builder.length() - 1) == ']');
		int[] dim = null;
		Class c;
		try {
			String classURL;
			if (builder.indexOf("<") != -1) {
				classURL = builder.substring(0, builder.indexOf("<"));
			} else {
				classURL = builder.toString();
			}
			dim = new int[dimension];
			for (int i = 0; i < dimension; i++) dim[i] = 0;
			c = Class.forName(classURL);
			type = Array.newInstance(c, dim).getClass();
			if (Collection.class.isAssignableFrom(type)) {
				innerClass = new ColumnField(field, builder.toString(), index + 1);
			} else if (Map.class.isAssignableFrom(type)) {
				// TODO imamo slovar
				innerClass = null;
			} else {
				innerClass = new ColumnField(name, c);
			}
		} catch (ClassNotFoundException e) {
			switch (builder.toString()) {
			case "int":
				type = Array.newInstance(int.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), int.class);
				break;
			case "double":
				type = Array.newInstance(double.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), double.class);
				break;
			case "byte":
				type = Array.newInstance(byte.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), byte.class);
				break;
			case "float":
				type = Array.newInstance(float.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), float.class);
				break;
			case "char":
				type = Array.newInstance(char.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), char.class);
				break;
			case "short":
				type = Array.newInstance(short.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), short.class);
				break;
			case "long":
				type = Array.newInstance(long.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), long.class);
				break;
			case "boolena":
				type = Array.newInstance(boolean.class, dim).getClass();
				innerClass = new ColumnField("array_" + field.getName(), boolean.class);
				break;
			default:
				throw e;
			}
		}
	}

	private ColumnField(String name, Class in) {
		this.name = name;
		this.type = in;
		// TODO
	}

	public Class getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ColumnField getInnerClass() {
		return innerClass;
	}

	public Column getColumnAnno() {
		return columnAnno;
	}

	public ArrayTable getArrayAnno() {
		return arrayAnno;
	}

	public MapTable getMapAnno() {
		return mapAnno;
	}

	public Annotation getAnnotaion(Class annoType) {
		if (annoType == Column.class) {
			return columnAnno;
		} else if (annoType == ArrayTable.class) {
			return arrayAnno;
		} else if (annoType == MapTable.class) {
			return mapAnno;
		} else {
			return null;
		}
	}

	public int getDimension() {
		return dimension;
	}

	@Override
	public String toString() {
		return name + " " + type.getTypeName() + " >> " + dimension + (innerClass != null ? "\n\t" + innerClass.toString() : "");
	}
}
