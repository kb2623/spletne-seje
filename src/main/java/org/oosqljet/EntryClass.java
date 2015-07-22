package org.oosqljet;

import org.oosqljet.annotation.Column;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

public class EntryClass {

	private Field field;

	public EntryClass(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public Column getAnnotation() {
		return field.getAnnotation(Column.class);
	}

	private String getNameFromAnno(int index) throws ArrayIndexOutOfBoundsException {
		if (getAnnotation().name()[0].isEmpty()) throw new ArrayIndexOutOfBoundsException();
		else return getAnnotation().name()[index];
	}

	public String getName(int index) {
		try {
			if (field.getType().isArray()) {
				return getArrayName(index);
			} else if (Collection.class.isAssignableFrom(field.getType())) {
				return getCollectionName(index);
			} else if (Map.class.isAssignableFrom(field.getType())) {
				return getMapName(index);
			} else {
				return getNameFromAnno(index);
			}
		} catch (ArrayIndexOutOfBoundsException ignore) {
			switch (index) {
				case 0: return field.getName();
				default: return null;
			}
		}
	}

	public String getMapName(int index) throws ArrayIndexOutOfBoundsException {
		if (index < 2) {
			try {
				if (getAnnotation().mapName()[0].isEmpty())
					throw new ArrayIndexOutOfBoundsException();
				else 
					return getAnnotation().mapName()[index];
			} catch (ArrayIndexOutOfBoundsException e) {
				switch (index) {
					case 0: return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName() + "key";
					case 1: return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1].getTypeName() + "mvalue";
					default: return null;
				}
			}
		} else {
			return getCollectionName(index - 2);
		}
	}

	private String getCollectionName(int index) throws ArrayIndexOutOfBoundsException {
		if (index < 3) {
			try {
				if (getAnnotation().arrayName()[0].isEmpty())
					throw new ArrayIndexOutOfBoundsException();
				else 
					return getAnnotation().arrayName()[index];
			} catch (ArrayIndexOutOfBoundsException e) {
				switch (index) {
					case 0: return field.getName();
					case 1: return field.getType().getSimpleName() + field.getName();
					case 2: return field.getType().getSimpleName() + field.getName() + "avalue";
					default: return null;
				}
			}
		} else {
			return getNameFromAnno(index - 3);
		}
	}

	private String getArrayName(int index) throws ArrayIndexOutOfBoundsException {
		if (index < 3) {
			try {
				if (getAnnotation().arrayName()[0].isEmpty())
					throw new ArrayIndexOutOfBoundsException();
				else 
					return getAnnotation().arrayName()[index];
			} catch (ArrayIndexOutOfBoundsException e) {
				switch (index) {
					case 0: return field.getName();
					case 1: return field.getType().getSimpleName().replace("[]", "") + field.getName();
					case 2: return field.getType().getSimpleName().replace("[]", "") + field.getName() + "avalue";
					default: return null;
				}
			}
		} else {
			return getNameFromAnno(index - 3);
		}
	}

	public Class type() {
		return field.getType();
	}

	public Object get(Object o) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
				Object out = field.get(o);
				field.setAccessible(false);
				return out;
			} else {
				return field.get(o);
			}
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	public void set(Object o, Object in) throws IllegalAccessException {
		if (!field.isAccessible()) {
			field.setAccessible(true);
			field.set(o, in);
			field.setAccessible(false);
		} else {
			field.set(o, in);
		}
	}

	@Override
	public String toString() {
		return field.getName() + " > " + field.getType();
	}
}
