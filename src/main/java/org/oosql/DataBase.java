package org.oosql;

import org.oosql.exception.OosqlException;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.MappingException;
import org.oosql.exception.TableAnnotationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("unused")
public class DataBase implements AutoCloseable {

	private SqlDriver driver;
	private Map<Class, SqlMapping> mappings;

	public DataBase() {
		mappings = new HashMap<>();
	}

	public DataBase(String protocol, File file, Properties properties) throws FileNotFoundException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		if (file.isFile()) driver = new SqlDriver(protocol, file, properties);
		else throw new FileNotFoundException("[" + file.getPath() + "] is not a file");
	}

	public DataBase(String protocol, String path, Properties properties) throws FileNotFoundException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		this(protocol, new File(path), properties);
	}
	/**
	 *
	 * @param in Objekt za katerega želimo zgraditi tabele v SQL
	 * @param colName seznam nizov ali pa samo en niz, ki predstavlja imena tujih ključev
	 * @return Niz ki predstavlja tuj ključ
	 * @throws TableAnnotationException V razredu majnka notacija za tabelo
	 * @throws IllegalAccessException Napaka pri dostopu do vrenosti polja v objektu
	 */
	private ForeignKey createTableVisitor(Object in) throws OosqlException, IllegalAccessException, SQLException {
		TableClass table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Missing @Table in [" + in.getClass().getName() + "]");
		Map<String, EntryClass> entrys = Util.getEntryAnnotations(in);
		if (entrys.isEmpty())
			throw new ColumnAnnotationException("Missing @Entry in [" + in.getClass().getName() + "]");
		StringBuilder query[] = {
				new StringBuilder(), // Niz za kreiranje tabele, brez referenc
				new StringBuilder(), // Niza ki predstavlja tuje kjuče
				new StringBuilder() // Niz ki predstavlja primarne ključe
		};
		ForeignKey refs = new ForeignKey(table);
		try {
			ResultSet res = driver.getTables(table.getName());
			if (!res.next()) {
				res.close();
				throw new AssertionError();
			} else {
				res.close();
			}
			for (EntryClass e : entrys.values()) if (e.columnAnno().pk()) {
				// TODO poštudiraj kaj se zgodi če imaš enume, array, collectione in mape
				if (e.getType().isPrimitive() || String.class.isAssignableFrom(e.getType())
						|| e.getType().isEnum()
						|| Collection.class.isAssignableFrom(e.getType()) || e.getType().isArray()
						|| Map.class.isAssignableFrom(e.getType())
						|| mappings.containsKey(e.getType())) {
					refs.add(e);
				} else {
					refs.addAll(createTableVisitor(e.get(in)), e, false);
				}
			}
			res = driver.getColumns(table.getName());
			while (res.next()) entrys.remove(res.getString("COLUMN_NAME"));
			res.close();
			driver.commit();
			if (!entrys.isEmpty()) {
				for (EntryClass e : entrys.values())
					createTableString(in, query, e, refs, 0);
				try (SqlStatement statement = driver.getStatement()) {
					for (String s : query[0].toString().split(","))
						statement.execUpdate("ALTER TABLE " + table.getName() + " ADD " + s);
					for (String s : query[1].toString().split(","))
						statement.execUpdate("ALTER TABLE " + table.getName() + " ADD " + s);
					driver.commit();
				} catch (SQLException e) {
					driver.rollback();
					throw e;
				}
			}
		} catch (AssertionError ignore) {
			if (table != null) {
				query[0].append("CREATE TABLE ").append(table.getName()).append('(');
				if (table.getAnno().autoId()) {
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
			driver.execUpdate(query[0].toString() +
					(query[2].length() > 0 ? ",PRIMARY KEY(" + query[2].toString() + ")" : "") +
					(query[1].length() > 0 ? "," + query[1].toString() : "") +
					(table.getAnno().noRowId() ? ") WITHOUT ROWID;" : ")"));
		}
		return refs.shiftAll();
	}

	private void createTableString(Object in, StringBuilder[] query, EntryClass e, ForeignKey refs, int index) throws IllegalAccessException, SQLException, OosqlException {
		String type = SQLightDataType.makeDataType(index < 3 ? e.getType().getSimpleName() : in.getClass().getSimpleName());
		if (type != null) {
			query[0].append(e.getName(index) + " " + type);
			if (e.columnAnno().pk() && index < 3) {
				query[2].append(e.getName(index) + ",");
				refs.add(e);
			}
			if (e.columnAnno().unique()) query[0].append(" UNIQUE");
			if (e.columnAnno().notNull()) query[0].append(" NOT NULL");
		} else if (e.getType().isEnum()) {
			TableClass enumtable = Util.getTableAnnotation(e.get(in));
			if (enumtable == null) {
				type = SQLightDataType.TEXT;
				query[0].append(e.getName(index) + " " + type);
				if (e.columnAnno().pk() && index < 3) {
					query[2].append(e.getName(index) + ",");
					refs.add(e);
				}
				if (e.columnAnno().unique()) query[0].append(" UNIQUE");
				if (e.columnAnno().notNull()) query[0].append(" NOT NULL");
			} else {
				type = SQLightDataType.INTEGER;
				driver.execUpdate(
						"CREATE TABLE " + enumtable.getName() + "(" +
								enumtable.getName() + "_id INTEGER," +
								e.getName(index) + " TEXT UNIQUE," +
								"PRIMARY KEY(" + enumtable.getName() + "_id))"
				);
				query[0].append(e.getName(index) + " " + type);
				if (e.columnAnno().pk() && index < 3) {
					query[2].append(e.getName(index) + ",");
					refs.add(e);
				}
				query[1].append("FOREIGN KEY(" + e.getName(index) + ") REFERENCES " + enumtable.getName() + "(" + enumtable.getName() + "_id),");
			}
		} else if (e.getType().isArray() && index < 3) {
			driver.execUpdate("CREATE TABLE " + e.getName(1) + "(id INTEGER, PRIMARY KEY(id))");
			StringBuilder[] arrayVal = {
					new StringBuilder("CREATE TABLE " + e.getName(2) + "(" + e.getName(1) + " INTEGER REFERENCES " + e.getName(1) + "(id),"),
					new StringBuilder("PRIMARY KEY(" + e.getName(1) + ",")
			};
			Object value = e.get(in);
			int dim = 0;
			for (Class c = value.getClass(); c.isArray(); c = c.getComponentType()) {
				arrayVal[0].append("dim_" + dim + "_pos INTEGER,");
				arrayVal[2].append("dim_" + dim + "_pos,");
				dim++;
				value = Array.get(value, 0);
			}
			arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).append("),");
			createTableString(value, arrayVal, e, null, 3); 			
			driver.execUpdate(arrayVal[0].toString() + ","
					+ arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).toString() + ")");
			query[0].append(e.getName(0) + " INTEGER");
			if (e.columnAnno().pk()) {
				query[2].append(e.getName(0) + ",");
				refs.add(e);
			}
			query[1].append("FOREIGN KEY(" + e.getName(0) + ") REFERENCES " + e.getName(1) + "(id),");
		} else if (Collection.class.isAssignableFrom(e.getType()) && index < 3) {
			driver.execUpdate("CREATE TABLE " + e.getName(1) + "(id INTEGER, PRIMARY KEY(id))");
			StringBuilder[] arrayVal = {
					new StringBuilder("CREATE TABLE " + e.getName(2) + "("
							+ e.getName(1) + " INTEGER REFERENCES " + e.getName(1) + "(id),"),
					new StringBuilder("PRIMARY KEY(" + e.getName(1) + ",")
			};
			Object value = null;
			int dim = 0;
			Iterator it = ((Collection) e.get(in)).iterator();
			try {
				while (it.hasNext()) {
					arrayVal[0].append("dim_" + dim + "_pos INTEGER,");
					arrayVal[2].append("dim_" + dim + "_pos,");
					dim++;
					value = it.next();
					it = ((Collection) value).iterator();
				}
			} catch (ClassCastException ignoreCast) {}
			arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).append("),");
			createTableString(value, arrayVal, e, null, 3);
			driver.execUpdate(arrayVal[0].toString() + ","
					+ arrayVal[1].deleteCharAt(arrayVal[1].length() - 1).toString() + ")");
			query[0].append(e.getName(0) + " INTEGER");
			if (e.columnAnno().pk()) {
				query[2].append(e.getName(0) + ",");
				refs.add(e);
			}
			query[1].append("FOREIGN KEY(" + e.getName(0) + ") REFERENCES " + e.getName(1) + "(id),");
		} else if (Map.class.isAssignableFrom(e.getType())) {
			StringBuilder builder = new StringBuilder();
			builder.append("CREATE TABLE " + e.getName(0) + "(");
			// TODO
			builder.append("CREATE TABLE " + e.getName(1) + "(");
			// TODO
		} else {
			if (mappings.containsKey(index < 3 ? e.getType() : in.getClass())) {
				type = SQLightDataType.makeDataType(mappings.get(e.getType()).getReturnType(e.getType()).getSimpleName());
				query[0].append(e.getName(0) + " " + type);
				if (e.columnAnno().pk() && index < 3) {
					query[2].append(e.getName(0) + ",");
					refs.add(e);
				}
				if (e.columnAnno().unique()) query[0].append(" UNIQUE");
				if (e.columnAnno().notNull()) query[0].append(" NOT NULL");
			} else {
				ForeignKey ret = createTableVisitor(e.get(in));
				String addCols = ret.getEntryQuery(e, mappings);
				if (!addCols.isEmpty()) query[0].append(addCols);
				if (e.columnAnno().pk() && index < 3) {
					query[2].append(ret.getPrimaryKeys(e) + ",");
					refs.addAll(ret, e, true);
				}
				query[1].append(ret.getRefQuery(e) + ",");
			}
		}
	}

	public void createTables(Object in) throws OosqlException, IllegalAccessException, SQLException {
		createTableVisitor(in);
	}

	private Object insertObject(Object in, boolean inArray) throws OosqlException, IllegalAccessException {
		TableClass table = Util.getTableAnnotation(in);
		if (table == null && !inArray)
			throw new TableAnnotationException("Missing @Table in [" + in.getClass().getName() + "]");
		Map<String, EntryClass> entrys = Util.getEntryAnnotations(in);
		if (entrys.isEmpty())
			throw new ColumnAnnotationException("Missing @Entry in [" + in.getClass().getName() + "]");
		for (EntryClass e : entrys.values()) {
			// TODO
		}
		return null;
	}

	public void insert(Object input) throws IllegalAccessException, OosqlException {
		insertObject(input, false);
	}

	private Object lookupObject(Object in) {
		// TODO
		return null;
	}

	public Object lookup(Object input, Class... conditions) {
		// TODO
		return null;
	}

	public void addMappings(SqlMapping mapping, Class c) throws NoSuchMethodException, MappingException {
		SQLightDataType type = SQLightDataType.makeDataType(mapping.getReturnType(c).getSimpleName());
		if (type == null)
			throw new MappingException("Cannot map \"" + c.getName() + "\" to \"" + mapping.getReturnType(c).getName() + "\"");
		else
			mappings.put(c, mapping);
	}

	@Deprecated
	protected SqlDriver getDataBase() {
		return driver;
	}

	@Override
	public void close() throws SQLException {
		driver.close();
	}
}
