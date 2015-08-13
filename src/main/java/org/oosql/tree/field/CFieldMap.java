package org.oosql.tree.field;

import org.oosql.annotation.Column;
import org.oosql.annotation.ColumnsC;
import org.oosql.annotation.MapTable;
import org.oosql.annotation.MapTableC;
import org.oosql.exception.OosqlException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class CFieldMap extends CField {

	private MapTable mapAnno;
	private CField key;
	private CField value;
	protected static int index = 0;

	public CFieldMap(Field field) throws OosqlException, ClassNotFoundException {
		super(field);
		CFieldArray.index = 0;
		CFieldMap.index = 0;
		if (Map.class.isAssignableFrom(field.getType())) {
			setMap(field, field.getName(), field.getGenericType().getTypeName());
		}
	}

	protected CFieldMap(Field field, String name, String typeName) throws OosqlException, ClassNotFoundException {
		super(field);
		setMap(field, name, typeName);
		index++;
	}

	private void setMap(Field field, String name, String typeName) throws OosqlException, ClassNotFoundException {
		try {
			mapAnno = field.getAnnotationsByType(MapTable.class)[index];
			// TODO popravi notacijo, če je seveda to porebno
		} catch (ArrayIndexOutOfBoundsException e) {
			mapAnno = new MapTableC(name);
		}
		int keyValSep = keyValueSeparator(typeName);
		String keyType = typeName.subSequence(typeName.indexOf("<") + 1, keyValSep - 1).toString();
		String valueType = typeName.subSequence(keyValSep + 1, typeName.lastIndexOf(">")).toString();
		if (keyType.endsWith("[]")) {
			key = new CFieldArray(field, name + "_key_array", keyType);
		} else if (keyType.endsWith(">")) {
			Class c = Class.forName(keyType.substring(0, keyType.indexOf("<")));
			if (Collection.class.isAssignableFrom(c)) {
				key = new CFieldArray(field, name + "_key_list", keyType);
			} else if (Map.class.isAssignableFrom(c)) {
				key = new CFieldMap(field, name + "_key_map", keyType);
			} else {
				key = new CField(field, name + "_key", c, new ColumnsC(new Column[]{mapAnno.keyColumn()}));
			}
		} else {
			key = new CField(field, name + "_key", Class.forName(keyType), new ColumnsC(new Column[]{mapAnno.keyColumn()}));
		}
		if (valueType.endsWith("[]")) {
			value = new CFieldArray(field, name + "_value_array", valueType);
		} else if (valueType.endsWith(">")) {
			Class c = Class.forName(keyType.substring(0, keyType.indexOf("<")));
			if (Collection.class.isAssignableFrom(c)) {
				value = new CFieldArray(field, name + "_value_list", keyType);
			} else if (Map.class.isAssignableFrom(c)) {
				value = new CFieldMap(field, name + "_value_map", keyType);
			} else {
				value = new CField(field, name + "_value", c, new ColumnsC(new Column[]{mapAnno.valueColumn()}));
			}
		} else {
			value = new CField(field, name + "_value", Class.forName(valueType), new ColumnsC(new Column[]{mapAnno.valueColumn()}));
		}
	}

	private int keyValueSeparator(String s) {
		int gen = 0;
		int index = 0;
		for (char c : s.toCharArray()) {
			index++;
			if (gen == 1 && c == ',') {
				return index;
			} else if (c == '<') {
				gen++;
			} else if (c == '>') {
				gen--;
			}
		}
		return -1;
	}

	private int indexOfLArrow(StringBuilder builder) {
		if (builder.charAt(builder.length() - 1) == ']') {
			return -1;
		} else {
			return builder.indexOf("<");
		}
	}

	public MapTable getMapAnno() {
		return mapAnno;
	}

	public CField getKeyClass() {
		return key;
	}

	public CField getValueClass() {
		return value;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<? extends T> annoType) {
		if (annoType == MapTable.class) {
			return (T) mapAnno;
		} else {
			return super.getAnnotation(annoType);
		}
	}

	@Override
	public String toString() {
		return super.toString() + "\n\t[key: " + key.toString() + "]\n\t[value: " + value.toString() + "]";
	}
}
