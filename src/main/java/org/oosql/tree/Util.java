package org.oosql.tree;

import org.oosql.ISqlMapping;
import org.oosql.annotation.TableC;
import org.oosql.annotation.Column;
import org.oosql.annotation.Table;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.OosqlException;
import org.oosql.tree.CField;
import org.oosql.tree.CFieldArray;
import org.oosql.tree.CFieldMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
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
	public static Table getTableAnnotation(Class in) throws NullPointerException {
		if (in == null) throw new NullPointerException();
		for (Class c = in; c.getSuperclass() != null; c = c.getSuperclass()) {
			Annotation anno = c.getDeclaredAnnotation(Table.class);
			if (anno != null)
				return ((Table) anno).name().isEmpty() ? new TableC((Table) anno, c.getSimpleName(), null, null, null, null) : (Table) anno;
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
	public static List<CField> getColumnFields(Class in) throws NullPointerException, OosqlException, ClassNotFoundException {
		if (in == null) throw new NullPointerException();
		List<CField> tab = new LinkedList<>();
		for (Class c = in; c.getSuperclass() != null; c = c.getSuperclass()) {
		  for (Field field : c.getDeclaredFields()) {
			 if (field.isAnnotationPresent(Column.class)) {
				 if (field.getType().isArray()) {
					 tab.add(new CFieldArray(field));
				 } else if (Collection.class.isAssignableFrom(field.getType())) {
					 tab.add(new CFieldArray(field));
				 } else if (Map.class.isAssignableFrom(field.getType())) {
					 tab.add(new CFieldMap(field));
				 } else {
					 tab.add(new CField(field));
				 }
			 }
		  }
		}
		return !tab.isEmpty() ? tab : null;
	}

	public static Class getReturnType(Class<? extends ISqlMapping> in, Class parameter) {
		try {
			return in.getMethod("inMapping", parameter).getReturnType();
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	public static boolean hasEmptyNames(Column column) throws ColumnAnnotationException {
		String[] array = column.name();
		if (array.length > 0) {
			for (int i = 0; i < array.length; i++) if (array[i].isEmpty()) {
				throw new ColumnAnnotationException("has empty name on index [" + i + "]");
			}
			return false;
		} else {
			return true;
		}
	}

}
