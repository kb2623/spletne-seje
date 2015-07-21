package org.oosqljet;

import org.datastruct.RadixTree;
import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Util {
	/**
	 * Metoda za iskanje <code>@Table</code> notacije. Metoda išče notacijo tudi v nadrazredih, vse dokler ne pride do Object razreda.
	 *
	 * @param in Objekt za katerega želimo najti <code>@Table</code> notacijo
	 * @return Podatke o notacijo če notacija obstaja, v nosprotnem primeru null;
	 * @see TableClass
	 */
	public static TableClass getTableAnnotation(Object in) throws NullPointerException {
		if (in == null) throw new NullPointerException();
		for (Class c = in.getClass(); c.getSuperclass() != null; c = c.getSuperclass()) {
			Annotation anno = c.getDeclaredAnnotation(Table.class);
			if (anno != null) return new TableClass(c);
		}
		return null;
	}
	/**
	 *
	 * @param in
	 * @return
	 */
	public static TableClass getTableAnnotation(Class in) throws NullPointerException {
		if (in == null) throw new NullPointerException();
		for (Class c = in; c.getSuperclass() != null; c = c.getSuperclass()) {
			Annotation[] anno = c.getAnnotationsByType(Table.class);
			if (anno.length > 0) return new TableClass(c);
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
	public static Map<String, EntryClass> getEntryAnnotations(Object in) throws NullPointerException {
		if (in == null) throw new NullPointerException();
		Map<String, EntryClass> tab = new RadixTree<>();
		for (Class c = in.getClass(); c.getSuperclass() != null; c = c.getSuperclass()) {
		  for (Field field : c.getDeclaredFields()) {
			 if (field.isAnnotationPresent(Column.class)) {
				EntryClass tmp = new EntryClass(field);
				tab.put(tmp.getName(0), tmp);
			 }
		  }
		}
		return !tab.isEmpty() ? tab : null;
	}
}
