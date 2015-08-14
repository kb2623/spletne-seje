package org.oosql.tree.field;

import org.oosql.annotation.*;
import org.oosql.exception.OosqlException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CFieldArray extends CField {

	private CField innerClass;
	private int dimension = 0;
	private ArrayTable arrayAnno;
	protected static int index = 0;

	public CFieldArray(Field field) throws OosqlException, ClassNotFoundException {
		super(field);
		CFieldMap.index = 0;
		CFieldArray.index = 0;
		if (field.getType().isArray()) {
			setArray(field, field.getName(), field.getGenericType().getTypeName());
		} else if (Collection.class.isAssignableFrom(field.getType())) {
			setCollection(field, field.getName(), field.getGenericType().getTypeName());
		}
	}

	protected CFieldArray(Field field, String name, String typeName) throws ClassNotFoundException, OosqlException {
		super(field.getAnnotation(Columns.class), name);
		if (typeName.endsWith("[]")) {
			type = setArray(field, name, typeName);
		} else {
			type = setCollection(field, name, typeName);
		}
		index++;
	}

	private Class setCollection(Field field, String name, String typeName) throws ClassNotFoundException, OosqlException {

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
				if (Collection.class.isAssignableFrom(c)) {
					builder.delete(0, i + 1).deleteCharAt(builder.length() - 1);
					dimension++;
				} else {
					builder.delete(i, builder.length());
					break;
				}
			}
		} while (true);
		try {
			arrayAnno = fixAnno(field.getAnnotationsByType(ArrayTable.class)[index], name);
		} catch (ArrayIndexOutOfBoundsException e) {
			arrayAnno = new ArrayTableC(name, dimension);
		}
		if (builder.charAt(builder.length() - 1) == ']') {
			innerClass = new CFieldArray(field, name + "_array", builder.toString());
		} else if (Map.class.isAssignableFrom(c)) {
			innerClass = new CFieldMap(field, name + "_map", builder.toString());
		} else {
			innerClass = new CField(field, name + "_field", Class.forName(builder.toString()), getValueColumn());
		}
		return c;
	}

	private int indexOfLArrow(StringBuilder buffer) {
		if (buffer.charAt(buffer.length() - 1) == ']') return -1;
		return buffer.indexOf("<");
	}

	private Class setArray(Field field, String name, String typeName) throws ClassNotFoundException, OosqlException {
		StringBuilder builder = new StringBuilder(typeName);
		do {
			dimension++;
			builder.delete(builder.length() - 2, builder.length());
		} while (builder.charAt(builder.length() - 1) == ']');
		try {
			arrayAnno = fixAnno(field.getAnnotationsByType(ArrayTable.class)[index], name);
		} catch (ArrayIndexOutOfBoundsException e) {
			arrayAnno = new ArrayTableC(name, dimension);
		}
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
				innerClass = new CFieldArray(field, name + "_list", builder.toString());
			} else if (Map.class.isAssignableFrom(c)) {
				innerClass = new CFieldMap(field, name + "_map", builder.toString());
			} else {
				innerClass = new CField(field, name + "_field", c, getValueColumn());
			}
			return Array.newInstance(c, dim).getClass();
		} catch (ClassNotFoundException e) {
			switch (builder.toString()) {
			case "int":
				innerClass = new CField(field, name + "_field", int.class, getValueColumn());
				return Array.newInstance(int.class, dim).getClass();
			case "double":
				innerClass = new CField(field, name + "_field", double.class, getValueColumn());
				return Array.newInstance(double.class, dim).getClass();
			case "byte":
				innerClass = new CField(field, name + "_field", byte.class, getValueColumn());
				return Array.newInstance(byte.class, dim).getClass();
			case "float":
				innerClass = new CField(field, name + "_field", float.class, getValueColumn());
				return Array.newInstance(float.class, dim).getClass();
			case "char":
				innerClass = new CField(field, name + "_field", char.class, getValueColumn());
				return Array.newInstance(char.class, dim).getClass();
			case "short":
				innerClass = new CField(field, name + "_field", short.class, getValueColumn());
				return Array.newInstance(short.class, dim).getClass();
			case "long":
				innerClass = new CField(field, name + "_field", long.class, getValueColumn());
				return Array.newInstance(long.class, dim).getClass();
			case "boolena":
				innerClass = new CField(field, name + "_field", boolean.class, getValueColumn());
				return Array.newInstance(boolean.class, dim).getClass();
			default:
				throw e;
			}
		}
	}

	private Columns getValueColumn() {
		List<Column> list = new LinkedList<>();
		Column[] columns = arrayAnno.valueTable().columns().value();
		for (int i = dimension; i < columns.length; i++) list.add(columns[i]);
		return new ColumnsC(list.toArray(new Column[list.size()]));
	}

	private ArrayTable fixAnno(ArrayTable table, String name) {
		ArrayTable tableRet = table;
		// TODO Popravi anotacijo če je seveda to porebno
		return tableRet;
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
	public <T extends Annotation> T getAnnotation(Class<? extends T> annoType) {
		if (annoType == ArrayTable.class) {
			return (T) arrayAnno;
		} else {
			return super.getAnnotation(annoType);
		}
	}

	@Override
	public String toString() {
		return super.toString() + " >> " + dimension + "\n\t" + innerClass.toString();
	}
}
