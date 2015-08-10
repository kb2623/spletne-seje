package org.oosql;

import org.oosql.annotation.ArrayTable;
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
		super(field);
		if (typeName.endsWith("[]")) {
			setArray(field, typeName, index);
		} else {
			setCollection(field, typeName, index);
		}
	}

	private void setCollection(Field field, String typeName, int index) throws ClassNotFoundException, OosqlException {
		arrayAnno = field.getAnnotationsByType(ArrayTable.class)[index];
		StringBuilder builder = new StringBuilder(typeName);
		int i = indexOfLArrow(builder);
		type = Class.forName(builder.substring(0, builder.indexOf("<")));
		builder.delete(0, i + 1).deleteCharAt(builder.length() - 1);
		dimension++;
		Class c = type;
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
			innerClass = new CFieldArray(field, builder.toString(), 1);
		} else if (Map.class.isAssignableFrom(c)) {
			innerClass = new CFieldMap(field);
		} else {
			innerClass = new CField("list_" + field.getName(), Class.forName(builder.toString()));
		}
	}

	private int indexOfLArrow(StringBuilder buffer) {
		if (buffer.charAt(buffer.length() - 1) == ']') return -1;
		return buffer.indexOf("<");
	}

	private void setArray(Field field, String typeName, int index) throws ClassNotFoundException, OosqlException {
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
			if (Collection.class.isAssignableFrom(c)) {
				innerClass = new CFieldArray(field, builder.toString(), index + 1);
			} else if (Map.class.isAssignableFrom(type)) {
				innerClass = new CFieldMap(field);
			} else {
				innerClass = new CField(name, c);
			}
		} catch (ClassNotFoundException e) {
			switch (builder.toString()) {
			case "int":
				type = Array.newInstance(int.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), int.class);
				break;
			case "double":
				type = Array.newInstance(double.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), double.class);
				break;
			case "byte":
				type = Array.newInstance(byte.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), byte.class);
				break;
			case "float":
				type = Array.newInstance(float.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), float.class);
				break;
			case "char":
				type = Array.newInstance(char.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), char.class);
				break;
			case "short":
				type = Array.newInstance(short.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), short.class);
				break;
			case "long":
				type = Array.newInstance(long.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), long.class);
				break;
			case "boolena":
				type = Array.newInstance(boolean.class, dim).getClass();
				innerClass = new CField("array_" + field.getName(), boolean.class);
				break;
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
