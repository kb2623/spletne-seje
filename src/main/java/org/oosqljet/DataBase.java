package org.oosqljet;

import org.oosqljet.exception.*;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

@SuppressWarnings("unused")
public class DataBase implements AutoCloseable {

	private SqlJetDb data;
	private Map<Class, SqlMapping> mappings;

	public DataBase() {
		mappings = new HashMap<>();
	}

	public DataBase(File file) throws FileNotFoundException, SqlJetException {
		if (file.isFile()) data = SqlJetDb.open(file, true);
		else throw new FileNotFoundException("[" + file.getPath() + "] is not a file");
	}

	public DataBase(String path) throws FileNotFoundException, SqlJetException {
		this(new File(path));
	}

	public void open(File file) throws DataBaseFileException, FileNotFoundException, SqlJetException {
		if (data == null || !data.isOpen()) {
			if (file.isFile()) data = SqlJetDb.open(file, true);
			else throw new FileNotFoundException("[" + file.getPath() + "] is not a file");
		} else {
			throw new DataBaseFileException("File is already opend!!!");
		}
	}

	public void open(String path) throws SqlJetException, DataBaseFileException, FileNotFoundException {
		if (data == null || !data.isOpen()) {
			File file = new File(path);
			if (file.isFile()) data = SqlJetDb.open(new File(path), true);
			else throw new FileNotFoundException("[" + "] is not a file!!!");
		} else {
			throw new DataBaseFileException("File is already opend!!!");
		}
	}
	/**
	 *
	 * @param in Objekt za katerega želimo zgraditi tabele v SQL
	 * @param colName seznam nizov ali pa samo en niz, ki predstavlja imena tujih ključev
	 * @return Niz ki predstavlja tuj ključ
	 * @throws TableAnnotationException V razredu majnka notacija za tabelo
	 * @throws IllegalAccessException Napaka pri dostopu do vrenosti polja v objektu
	 */
	private ForeignKey createTableVisitor(Object in) throws TableAnnotationException, IllegalAccessException, SqlJetException, EntryAnnotationException, NoSuchMethodException {
		TableClass table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Missing @Table in [" + in.getClass().getName() + "]");
		Map<String, EntryClass> entrys = Util.getEntryAnnotations(in);
		if (entrys.isEmpty())
			throw new EntryAnnotationException("Missing @Entry in [" + in.getClass().getName() + "]");
		StringBuilder query[] = {
			new StringBuilder(), // Niz za kreiranje tabele, brez referenc
			new StringBuilder(), // Niza ki predstavlja tuje kjuče
			new StringBuilder() // Niz ki predstavlja primarne ključe
		};
		ForeignKey refs = new ForeignKey(table);
		try {
			if (table == null) throw new SqlJetException("");
			ISqlJetTable dbTable = data.getTable(table.getName());
			for (EntryClass e : entrys.values()) if (e.getAnnotation().primaryKey()) {
				// TODO poštudiraj kaj se zgodi če imaš enume, array, collectione in mape
				if (e.type().isPrimitive() || String.class.isAssignableFrom(e.type())
						|| e.type().isEnum()
						|| Collection.class.isAssignableFrom(e.type()) || e.type().isArray()
						|| Map.class.isAssignableFrom(e.type())
						|| mappings.containsKey(e.type())) {
					refs.add(e);
				} else {
					refs.addAll(createTableVisitor(e.get(in)), e, false);
				}
			}
			for (ISqlJetColumnDef cDef : dbTable.getDefinition().getColumns()) {
				if (entrys.get(cDef.getName()) != null) entrys.remove(cDef.getName());
			}
			if (!entrys.isEmpty()) {
				// TODO Če slučajno dobiš primarni ključ potem moreš prepisati vse podatki, izbrisati tabelo in ustvariti novo tabelo
				System.out.println("Aletering table");
				for (EntryClass e : entrys.values()) {
				  System.out.println(e.getName(0));
				}
				System.out.println();
			}
		} catch (SqlJetException ignore) {
			if (table != null) {
				query[0].append("CREATE TABLE ").append(table.getName()).append('(');
				if (table.getAnnotation().autoId()) {
					query[0].append(table.getName() + "_id INTEGER,");
					query[2].append(table.getName() + "_id,");
				}
			}
			for (EntryClass e : entrys.values()) {
				createTableString(in, query, e, refs, 0);
				query[0].append(',');
			}
			for (StringBuilder b : query) if (b.length() > 0) {
				b.deleteCharAt(b.length() - 1);
			}
			createTable(query[0].toString() +
					(query[2].length() > 0 ? ",PRIMARY KEY(" + query[2].toString() + ")" : "") +
					(query[1].length() > 0 ? "," +query[1].toString() : "") +
					(table.getAnnotation().noRowId() ? ") WITHOUT ROWID;" : ");"));
		}
		return refs.shiftAll();
	}

	private void createTableString(Object in, StringBuilder[] query, EntryClass e, ForeignKey refs, int index) throws SqlJetException, NoSuchMethodException, EntryAnnotationException, IllegalAccessException, TableAnnotationException {
		SQLightDataType type = SQLightDataType.makeDataType(index < 3 ? e.type().getSimpleName() : in.getClass().getSimpleName());
		if (type != null) {
			query[0].append(e.getName(index) + " " + type.toString());
			if (e.getAnnotation().primaryKey() && index < 3) {
				query[2].append(e.getName(index) + ",");
				refs.add(e);
			}
			if (e.getAnnotation().unique()) query[0].append(" UNIQUE");
			if (e.getAnnotation().notNull()) query[0].append(" NOT NULL");
		} else if (e.type().isEnum()) {
			TableClass enumtable = Util.getTableAnnotation(e.get(in));
			if (enumtable == null) {
				type = SQLightDataType.TEXT;
				query[0].append(e.getName(index) + " " + type.toString());
				if (e.getAnnotation().primaryKey() && index < 3) {
					query[2].append(e.getName(index) + ",");
					refs.add(e);
				}
				if (e.getAnnotation().unique()) query[0].append(" UNIQUE");
				if (e.getAnnotation().notNull()) query[0].append(" NOT NULL");
			} else {
				type = SQLightDataType.INTEGER;
				createTable("CREATE TABLE " + enumtable.getName() + "(" +
				  enumtable.getName() + "_id INTEGER," +
				  e.getName(index) + " TEXT UNIQUE," +
				  "PRIMARY KEY(" + enumtable.getName() + "_id)" +
				  ");"
				);
				query[0].append(e.getName(index) + " " + type.toString());
				if (e.getAnnotation().primaryKey() && index < 3) {
					query[2].append(e.getName(index) + ",");
					refs.add(e);
				}
				query[1].append("FOREIGN KEY(" + e.getName(index) + ") REFERENCES " + enumtable.getName() + "(" + enumtable.getName() + "_id),");
			}
		} else if (e.type().isArray() && index < 3) {
			createTable("CREATE TABLE " + e.getName(1) + "(id INTEGER, PRIMARY KEY(id));");
			StringBuilder[] arrayVal = {
			 new StringBuilder("CREATE TABLE " + e.getName(2) + "(" + e.getName(1) + " INTEGER REFERENCES " + e.getName(1) + "(id),"),
			 new StringBuilder("PRIMARY KEY(" + e.getName(1) + ",")
			};
			Object value = e.get(in);
			int dim = 0;
			for (Class c = value.getClass(); c.isArray(); c = c.getComponentType()) {
				arrayVal[0].append("dim_" + dim + "_pos INTEGER,");
				arrayVal[1].append("dim_" + dim + "_pos,");
				dim++;
				value = Array.get(value, 0);
			}
			arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).append("),");
			createTableString(value, arrayVal, e, null, 3); 			
			createTable(arrayVal[0].toString() + "," + arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).toString() + ");");
			query[0].append(e.getName(0) + " INTEGER");
			if (e.getAnnotation().primaryKey()) {
				query[2].append(e.getName(0) + ",");
				refs.add(e);
			}
			query[1].append("FOREIGN KEY(" + e.getName(0) + ") REFERENCES " + e.getName(1) + "(id),");
		} else if (Collection.class.isAssignableFrom(e.type()) && index < 3) {
			createTable("CREATE TABLE " + e.getName(1) + "(id INTEGER, PRIMARY KEY(id));");
			StringBuilder[] arrayVal = {
				new StringBuilder("CREATE TABLE " + e.getName(2) + "(" + e.getName(1) + " INTEGER REFERENCES " + e.getName(1) + "(id),"),
				new StringBuilder("PRIMARY KEY(" + e.getName(1) + ",")
			};
			Object value = null;
			int dim = 0;
			Iterator it = ((Collection) e.get(in)).iterator();
			try {
				while (it.hasNext()) {
					arrayVal[0].append("dim_" + dim + "_pos INTEGER,");
					arrayVal[1].append("dim_" + dim + "_pos,");
					dim++;
					value = it.next();
					it = ((Collection) value).iterator();
				}
			} catch (ClassCastException ignoreCast) {}
			arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).append("),");
			createTableString(value, arrayVal, e, null, 3);
			createTable(arrayVal[0].toString() + "," + arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).toString() + ");");
			query[0].append(e.getName(0) + " INTEGER");
			if (e.getAnnotation().primaryKey()) {
				query[2].append(e.getName(0) + ",");
				refs.add(e);
			}
			query[1].append("FOREIGN KEY(" + e.getName(0) + ") REFERENCES " + e.getName(1) + "(id),");
		} else if (Map.class.isAssignableFrom(e.type())) {
			// TODO Naredi podobno kot za Collection in Array
		} else {
			if (mappings.containsKey(index < 3 ? e.type() : in.getClass())) {
				try {
					type = SQLightDataType.makeDataType(mappings.get(e.type()).getReturnType(e.type()).getSimpleName());
				} catch (NoSuchMethodException ignoreGet) {
				}
				query[0].append(e.getName(0) + " " + type.toString());
				if (e.getAnnotation().primaryKey() && index < 3) {
					query[2].append(e.getName(0) + ",");
					refs.add(e);
				}
				if (e.getAnnotation().unique()) query[0].append(" UNIQUE");
				if (e.getAnnotation().notNull()) query[0].append(" NOT NULL");
			} else {
				ForeignKey ret = createTableVisitor(e.get(in));
				String addCols = ret.getEntryQuery(e, mappings);
				if (!addCols.isEmpty()) query[0].append(addCols);
				if (e.getAnnotation().primaryKey() && index < 3) {
					query[2].append(ret.getPrimaryKeys(e) + ",");
					refs.addAll(ret, e, true);
				}
				query[1].append(ret.getRefQuery(e) + ",");
			}
		}
	}

	private void createTable(String query) throws SqlJetException {
		data.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			data.createTable(query);
			data.commit();
		} catch (SqlJetException e) {
			data.rollback();
			throw e;
		}
	}

	public void createTables(Object in) throws TableAnnotationException, IllegalAccessException, SqlJetException, EntryAnnotationException, NoSuchMethodException {
		createTableVisitor(in);
	}

	private Object insertObject(Object in, boolean inArray) throws TableAnnotationException, IllegalAccessException, EntryAnnotationException {
		TableClass table = Util.getTableAnnotation(in);
		if (table == null && !inArray)
			throw new TableAnnotationException("Missing @Table in [" + in.getClass().getName() + "]");
		Map<String, EntryClass> entrys = Util.getEntryAnnotations(in);
		if (entrys.isEmpty())
			throw new EntryAnnotationException("Missing @Entry in [" + in.getClass().getName() + "]");
		for (EntryClass e : entrys.values()) {
			// TODO
		}
		return null;
	}

	public void insert(Object input) throws DataBaseFileException, IllegalAccessException, TableAnnotationException, EntryAnnotationException {
		if (!data.isOpen()) throw new DataBaseFileException("No file opend!!!");
		insertObject(input, false);
	}

	private Object lookupObject(Object in) {
		// TODO
		return null;
	}

	public Object lookup(Object input) {
		// TODO
		return null;
	}

	public void addMappings(SqlMapping mapping, Class c) throws NoSuchMethodException, SqlMappingException {
		SQLightDataType type = SQLightDataType.makeDataType(mapping.getReturnType(c).getSimpleName());
		if (type == null)
			throw new SqlMappingException("Cannot map \"" + c.getName() + "\" to \"" + mapping.getReturnType(c).getName() + "\"");
		else
			mappings.put(c, mapping);
	}

	@Deprecated
	protected SqlJetDb getDataBase() {
		return data;
	}

	@Override
	public void close() throws SqlJetException {
		data.close();
	}
}
