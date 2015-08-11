package org.oosql;

import org.oosql.annotation.ArrayTable;
import org.oosql.annotation.Column;
import org.oosql.exception.OosqlException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class CFieldArray extends CField {

	private CField innerClass;
	private int dimension = 0;
	private ArrayTable arrayAnno;

	public CFieldArray(Field field) throws OosqlException, ClassNotFoundException {
		super(field);
		if (field.getType().isArray()) {
			setArray(field, field.getGenericType().getTypeName(), 0);
		} else if (Collection.class.isAssignableFrom(field.getType())) {
			setCollection(field, field.getGenericType().getTypeName(), 0);
		}
	}

	private CFieldArray(Field field, String typeName, int index) throws ClassNotFoundException, OosqlException {
		super();
		if (typeName.endsWith("[]")) {
			type = setArray(field, typeName, index);
		} else {
			type = setCollection(field, typeName, index);
		}
		name = field.getName();
		columnAnno = field.getAnnotation(Column.class);
	}

	private Class setCollection(Field field, String typeName, int index) throws ClassNotFoundException, OosqlException {
		arrayAnno = field.getAnnotationsByType(ArrayTable.class)[index];
		StringBuilder builder = new StringBuilder(typeName);
		int i = indexOfLArrow(builder);
		Class c = Class.forName(builder.substring(0, builder.indexOf("<")));
		builder.delete(0, i + 1).deleteCharAt(builder.length() - 1);
		dimension++;
		do {
			i = indexOfLArrow(builder);
			if (i == -1) {
				break;
			} else {
				c = Class.forName(builder.substring(0, i));
				builder.delete(0, i + 1).deleteCharAt(builder.length() - 1);
				dimension++;
			}
		} while (Collection.class.isAssignableFrom(c));
		if (builder.charAt(builder.length() - 1) == ']') {
			innerClass = new CFieldArray(field, builder.toString(), index + 1);
		} else if (Map.class.isAssignableFrom(c)) {
			innerClass = new CFieldMap(field);
		} else {
			innerClass = new CField(field, Class.forName(builder.toString()));
		}
		return c;
	}

	private int indexOfLArrow(StringBuilder buffer) {
		if (buffer.charAt(buffer.length() - 1) == ']') return -1;
		return buffer.indexOf("<");
	}

	private Class setArray(Field field, String typeName, int index) throws ClassNotFoundException, OosqlException {
		arrayAnno = field.getAnnotationsByType(ArrayTable.class)[index];
		StringBuilder builder = new StringBuilder(typeName);
		do {
			dimension++;
			builder.delete(builder.length() - 2, builder.length());
		} while (builder.charAt(builder.length() - 1) == ']');
		int[] dim = new int[dimension];
		try {
			Class c;
			String classURL;
			if (builder.indexOf("<") != -1) {
				classURL = builder.substring(0, builder.indexOf("<"));
			} else {
				classURL = builder.toString();
			}
			for (int i : dim) i = 0;
			c = Class.forName(classURL);
			if (Collection.class.isAssignableFrom(c)) {
				innerClass = new CFieldArray(field, builder.toString(), index + 1);
			} else if (Map.class.isAssignableFrom(c)) {
				innerClass = new CFieldMap(field);
			} else {
				innerClass = new CField(field, c);
			}
			return Array.newInstance(c, dim).getClass();
		} catch (ClassNotFoundException e) {
			switch (builder.toString()) {
			case "int":
				innerClass = new CField(field, int.class);
				return Array.newInstance(int.class, dim).getClass();
			case "double":
				innerClass = new CField(field, double.class);
				return Array.newInstance(double.class, dim).getClass();
			case "byte":
				innerClass = new CField(field, byte.class);
				return Array.newInstance(byte.class, dim).getClass();
			case "float":
				innerClass = new CField(field, float.class);
				return Array.newInstance(float.class, dim).getClass();
			case "char":
				innerClass = new CField(field, char.class);
				return Array.newInstance(char.class, dim).getClass();
			case "short":
				innerClass = new CField(field, short.class);
				return Array.newInstance(short.class, dim).getClass();
			case "long":
				innerClass = new CField(field, long.class);
				return Array.newInstance(long.class, dim).getClass();
			case "boolena":
				innerClass = new CField(field, boolean.class);
				return Array.newInstance(boolean.class, dim).getClass();
			default:
				throw e;
			}
		}
	}

	public CField getInnerClass() {
		return innerClass;
	}

	public ArrayTable getArrayAnno() {
		return arrayAnno;
	}

	public int getDimension() {
		return dimension;
	}

	@Override
	public Annotation getAnnotaion(Class annoType) {
		if (annoType == ArrayTable.class) {
			return arrayAnno;
		} else {
			return super.getAnnotaion(annoType);
		}
	}

	@Override
	public String toString() {
		return super.toString() + " >> " + dimension + "\n\t" + innerClass.toString();
	}
}
