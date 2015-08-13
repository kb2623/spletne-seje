package org.oosql.tree;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.oosql.ISqlMapping;
import org.oosql.annotation.*;
import org.oosql.exception.OosqlException;
import org.oosql.tree.field.CField;
import org.oosql.tree.field.CFieldArray;
import org.oosql.tree.field.CFieldMap;

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
			Table anno = (Table) c.getAnnotation(Table.class);
			if (anno != null) {
				if (in.isEnum()) {
					String tName = null, idCName = null;
					Boolean pk = null;
					Columns columns = null;
					if (anno.name().isEmpty()) tName = in.getSimpleName();
					if (anno.id().name().isEmpty()) idCName = in.getSimpleName() + "_id";
					if (!anno.id().pk()) pk = true;
					if (anno.columns().value()[0].name().isEmpty()) {
						Column[] array = new Column[anno.columns().value().length];
						array[0] = new ColumnC(anno.columns().value()[0], in.getSimpleName() + "_value", null, null, null, null, null);
						for (int i = 1; i < anno.columns().value().length; i++) {
							array[i] = anno.columns().value()[i];
						}
						columns = new ColumnsC(array);
					}
					return new TableC(anno, tName, null, new ColumnC(anno.id(), idCName, pk, null, null, null, null), columns);
				} else {
					return anno.name().isEmpty() ? new TableC(anno, c.getSimpleName(), null, null, null) : anno;
				}
			}
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
			 if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(Columns.class)) {
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
}
