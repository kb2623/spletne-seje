package org.oosql;

import org.oosql.annotation.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

public class EntryClass {

	private Field field;
	private Object value;

	public EntryClass(Field field, Object value) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public Column columnAnno() {
		return field.getAnnotation(Column.class);
	}

	public ArrayName arrayAnno() {
		return field.getAnnotation(ArrayName.class);
	}

	public EnumName enumAnno() {
		return field.getAnnotation(EnumName.class);
	}

	public MapName mapAnno() {
		return field.getAnnotation(MapName.class);
	}

	public String getName(int index) {
		try {
			if (columnAnno().name().length == 0)
				throw new ArrayIndexOutOfBoundsException();
			else
				return columnAnno().name()[index];
		} catch (ArrayIndexOutOfBoundsException ignore) {
			switch (index) {
				case 0:
					return field.getName();
				default:
					return null;
			}
		}
	}

	public String getEnumName() {
		String name = enumAnno().name();
		if (!name.isEmpty()) return field.getName();
		else return name;
	}

	public String getArrayName(int index) {
		switch (index) {
			case 0: {
				String ret = arrayAnno().arrayName();
				if (!ret.isEmpty()) {
					return ret;
				} else if (field.getType().isArray()) {
					return field.getType().getSimpleName().replaceAll("\\[\\]", "") + field.getName();
				} else {
					return field.getType().getSimpleName() + field.getName();
				}
			} case 1: {
				String ret = arrayAnno().valueName();
				if (!ret.isEmpty()) {
					return ret;
				} else if (field.getType().isArray()) {
					return field.getType().getSimpleName().replaceAll("\\[\\]", "") + field.getName() + "value";
				} else {
					return field.getType().getSimpleName() + field.getName() + "value";
				}
			} default:
				return null;
		}
	}

	public Class getType() {
		return field.getType();
	}

	public Object get() {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
				Object out = field.get(value);
				field.setAccessible(false);
				return out;
			} else {
				return field.get(value);
			}
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	public void set(Object in) throws IllegalAccessException {
		if (!field.isAccessible()) {
			field.setAccessible(true);
			field.set(value, in);
			field.setAccessible(false);
		} else {
			field.set(value, in);
		}
	}

	@Override
	public String toString() {
		return field.getName() + " > " + field.getType();
	}
}
