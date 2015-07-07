package org.oosqljet;

import org.oosqljet.annotation.Entry;
import org.oosqljet.annotation.Table;
import org.oosqljet.exception.*;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataBase implements AutoCloseable {

	private SqlJetDb data;

	public DataBase(File file) throws SqlJetException, FileNotFoundException {
		if (file.isFile()) data = SqlJetDb.open(file, true);
		else throw new FileNotFoundException("[" + file.getPath() + "] is not a file");
	}

	public DataBase(String path) throws SqlJetException, FileNotFoundException {
		this(new File(path));
	}
	/**
	 * Metoda za iskanje <code>@Table</code> notacije. Metoda išče notacijo tudi v nadrazredih, vse dokler ne pride do Object razreda.
	 *
	 * @param in Objekt za katerega želimo najti <code>@Table</code> notacijo
	 * @return Podatke o notacijo če notacija obstaja, v nosprotnem primeru null;
	 * @see TableClass
	 */
	private TableClass getTableAnnotation(Object in) {
		for (Class c = in.getClass(); !c.getName().equals("java.lang.Object"); c = c.getSuperclass()) {
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
	private List<EntryClass> getEntryAnnotations(Object in) {
		List<EntryClass> list = new LinkedList<>();
		for (Field field : in.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Entry.class)) list.add(new EntryClass(field));
		}
		return list.size() > 0 ? list : null;
	}

	private String createTableObject(Object in, boolean inArray) throws TableAnnotationException, IllegalAccessException {
		TableClass table = getTableAnnotation(in);
		if (table == null && !inArray)
			throw new TableAnnotationException("Missing @Table in [" + in.getClass().getName() + "]");

		StringBuilder query = new StringBuilder();
		try {
			ISqlJetTable dbTable = data.getTable(table.getName());
			// TODO Tukaj tabela obstaja preveri ali obstajajo vse vrstice
		} catch (SqlJetException ignore) {
			query.append("CREATE TABLE ").append(table.getName()).append('(');
			for (EntryClass e : getEntryAnnotations(in)) {
				switch (e.getType().getSimpleName()) {
				case "int":
				case "long":
				case "byte":
				case "short":
					query.append(' ').append(createColumne(e.getName()[0], "INTEGER", e.getAnnotation()));
					break;
				case "float":
				case "double":
					query.append(' ').append(createColumne(e.getName()[0], "REAL", e.getAnnotation()));
					break;
				case "char":
				case "String":
					query.append(' ').append(createColumne(e.getName()[0], "TEXT", e.getAnnotation()));
					break;
				default:
					if (e.getType().isArray()) {
						// TODO
					} else if (e.getType().isEnum()) {
						// TODO
					} else if (Collection.class.isAssignableFrom(e.getType())) {
						// TODO
					} else if (Map.class.isAssignableFrom(e.getType())) {
						// TODO
					} else if (SqlMapping.class.isAssignableFrom(e.getType())) {
						// TODO
					} else {
						query.append(" FOREIGN KEY ").append(e.getName()[0]).append(" REFERENCES ");
						query.append(createTableObject(e.get(in), false));
					}
				}
				query.append(',');
			}
			query.deleteCharAt(query.length() - 1);
			query.append(')');
			if (table.getAnnotation().noRowId()) query.append("WITHOUT ROWID");
			query.append(';');
		}


		return null;
	}

	private String createColumne(String cName, String type, Entry anno) {
		StringBuilder builder = new StringBuilder();
		builder.append(cName).append(' ').append(type);
		if (anno.primaryKey()) {
			builder.append(" PRIMARY KEY");
			if (anno.autoincrement()) builder.append(' ').append(anno.increment().toString());
		}
		if (anno.unique()) builder.append(" UNIQUE");
		if (anno.notNull()) builder.append(" NOT NULL");
		return builder.toString();
	}

	public void createTables(Object in) throws TableAnnotationException, IllegalAccessException {
		createTableObject(in, false);
	}

	private Object processObject(Object in, boolean inArray) throws TableAnnotationException, IllegalAccessException {
		TableClass table = getTableAnnotation(in);
		if (table == null && !inArray)
			throw new TableAnnotationException("Missing @Table in [" + in.getClass().getName() + "]");

		// TODO

		for (Field field : in.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Entry.class)) {
				if (field.getType().isPrimitive()) {
					// TODO
				} else if (field.getType().isEnum()) {
					// TODO Tukaj moreš uporabiti inMaping za Enume
				} else if (field.getType().isArray()) {
					// TODO kliči metodo processArray in shrani vrednost
				} else {
					if (!field.isAccessible()) field.setAccessible(true);
					Object value = field.get(in);
					if (value instanceof Collection) {
						// TODO kliči metodo processCollection in shrani vrednost
					} else if (value instanceof Map) {
						// TODO kliči metodo processMap in shrani vrednost
					} else {
						// TODO kliči metodo processObject ali pa uporabi inMaping za posebne objekte
					}
				}
			}
		}

		// TODO vrni nek id ali podobno

		return null;
	}

	private Object processArray(Object[] in) {
		// TODO
		return null;
	}

	private Object processCollection(Collection in) {
		// TODO
		return null;
	}

	private Object processMap(Map in) {
		// TODO
		return null;
	}

	private Object processEnum(Enum in) {
		// TODO
		return null;
	}

	public void open(File file) throws SqlJetException, DataBaseFileException, FileNotFoundException {
		if (!data.isOpen()) {
			if (file.isFile()) data = SqlJetDb.open(file, true);
			else throw new FileNotFoundException("[" + file.getPath() + "] is not a file");
		} else {
			throw new DataBaseFileException("File is already opend!!!");
		}
	}

	public void open(String path) throws SqlJetException, DataBaseFileException, FileNotFoundException {
		if (!data.isOpen()) {
			File file = new File(path);
			if (file.isFile()) data = SqlJetDb.open(new File(path), true);
			else throw new FileNotFoundException("[" + "] is not a file!!!");
		} else {
			throw new DataBaseFileException("File is already opend!!!");
		}
	}

	public void insert(Object input) throws DataBaseFileException, IllegalAccessException, TableAnnotationException {
		if (!data.isOpen()) throw new DataBaseFileException("No file opend!!!");
		processObject(input, false);
	}

	public Object lookup(Object input) {
		// TODO
		return null;
	}

	@Override
	public void close() throws SqlJetException {
		data.close();
	}
}
