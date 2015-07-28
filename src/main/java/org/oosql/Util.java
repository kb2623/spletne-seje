package org.oosql;

import org.oosql.annotation.CTable;
import org.oosql.annotation.Column;
import org.oosql.annotation.Table;
import org.oosql.exception.ColumnAnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class Util {
	/**
	 * Metoda za iskanje <code>@Table</code> notacije. Metoda išče notacijo tudi v nadrazredih, vse dokler ne pride do Object razreda.
	 *
	 * @param in Objekt za katerega želimo najti <code>@Table</code> notacijo
	 * @return Podatke o notacijo če notacija obstaja, v nosprotnem primeru null;
	 * @see TableClass
	 */
	public static Table getTableAnnotation(Class in) throws NullPointerException {
		if (in == null) throw new NullPointerException();
		for (Class c = in; c.getSuperclass() != null; c = c.getSuperclass()) {
			Annotation anno = c.getDeclaredAnnotation(Table.class);
			if (anno != null) return ((Table) anno).name().isEmpty() ? new CTable((Table) anno, in.getSimpleName()) : (Table) anno;
		}
		return null;
	}
	/**
	 * Metoda, ki preišče razred in iz njega izlišči vse <code>@Entry</code> notacije
	 *
	 * @param in Objekt v katerem želimo najti <code>@Entry</code> notacije
	 * @return
	 * @see List
	 */
	public static List<Field> getEntryAnnotations(Class in) throws NullPointerException {
		if (in == null) throw new NullPointerException();
		List<Field> tab = new LinkedList<>();
		for (Class c = in; c.getSuperclass() != null; c = c.getSuperclass()) {
		  for (Field field : c.getDeclaredFields()) {
			 if (field.isAnnotationPresent(Column.class)) tab.add(field);
		  }
		}
		return !tab.isEmpty() ? tab : null;
	}

	public static Class getReturnType(Class<? extends SqlMapping> in, Class parameter) {
		try {
			return in.getMethod("inMapping", parameter).getReturnType();
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	public static boolean hasEmptyNames(String[] array) throws ColumnAnnotationException {
		if (array.length > 1) {
			for (int i = 0; i < array.length; i++) if (array[i].isEmpty()) {
				throw new ColumnAnnotationException("on index [" + i + "] is empty String for name");
			}
			return false;
		} else {
			return true;
		}
	}

}
