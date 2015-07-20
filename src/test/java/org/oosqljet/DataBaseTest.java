package org.oosqljet;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.oosqljet.exception.DataBaseFileException;
import org.oosqljet.exception.EntryAnnotationException;
import org.oosqljet.exception.SqlMappingException;
import org.oosqljet.exception.TableAnnotationException;
import org.sessionization.fields.FieldType;
import org.sessionization.fields.File;
import org.oosqljet.annotation.Entry;
import org.oosqljet.annotation.Table;
import org.sessionization.fields.Referer;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings({"deprecated", "unused"})
public class DataBaseTest {

	DataBase db;

	@Before
	public void setUp() {
		db = new DataBase();
	}

	@Test
	public void testCreateTables() throws EntryAnnotationException, NoSuchMethodException, SqlJetException, TableAnnotationException, IllegalAccessException {
		db.createTables("hello");
	}

	@Test(expected = SqlJetException.class)
	public void testSqlJetException() throws SqlJetException {
		StringBuilder builder = new StringBuilder().append("From Java");
		builder.insert(0, "Hello ");
		System.out.println(builder.toString());
		SqlJetDb db = SqlJetDb.open(new java.io.File("."), true);
		db.getTable(null);
	}

	@Test
	public void testReflections() throws NoSuchMethodException {
		TestClass anotations = new TestClass("klemen", "1.1.1992");

		System.out.println("Class with Table Annotation");
		Annotation[] typeA = anotations.getClass().getAnnotationsByType(Table.class);
		if (typeA.length == 1) {
			Table anno = (Table) typeA[0];
			System.out.println(anno.name());
		} else {
			fail();
		}

		System.out.println("\nFields with Field Annotation");
		Field[] fields = anotations.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Entry.class)) {
				System.out.print(field.getName() + " - " + field.getType().getName() + " >> ");
				if (field.getType().isPrimitive()) System.out.print("primitive ");
				if (field.getType().isArray()) System.out.print("array ");
				if (field.getType().isEnum()) System.out.print("enum ");
				System.out.println();
			}
		}

		System.out.println("\nTest super class from class that has extends");
		Class referer = Referer.class;
		System.out.println(referer.getSuperclass().getSimpleName());

		System.out.println("\nTest super class from class that has no extends");
		Class file = File.class;
		System.out.println(file.getSuperclass().getName());

		System.out.println("\nTest Methods return type");
		Method[] methods = anotations.getClass().getDeclaredMethods();
		System.out.println(methods.length);
		for (Method method : methods) {
			System.out.println(method.getName() + " >> " + method.getReturnType().getName());
		}

		System.out.println("\nTest method type");
		SqlMapping<String, Integer> test = new SqlMapping<String, Integer>() {
			@Override
			public Integer inMapping(String in) {
				return null;
			}
			public String outMapping(Integer in) {
				return null;
			}
		};
		System.out.println(test.getReturnType(String.class).getName());
	}

	@Test
	public void testEnumCreation() {
		FieldType d = FieldType.ClientIP;
		assertEquals("ClientIP", Enum.valueOf(FieldType.class, d.name()).name());
	}

	@Test
	public void testArrayDimension() {
		int[][][][] array = new int[1][1][1][1];
		int i = 0;
		Class c = array.getClass();
		while (c.isArray()) {
			i++;
			c = c.getComponentType();
		}
		System.out.println("Num of dims: " + i);
	}

	@Test
	public void testCollectionDimension() {
		List<List<List<Integer>>> lists = new LinkedList<>();
		lists.add(new LinkedList<>());
		lists.get(0).add(new LinkedList<>());
		lists.get(0).get(0).add(23);
		System.out.println("Dimensiion of list: " + getCollectionDimension(lists));
	}

	private <E extends Collection> int getCollectionDimension(E coll) {
		int i = 0;
		try {
			for (Iterator<E> it = (Iterator<E>) coll.iterator(); it.hasNext(); it = it.next().iterator())
				i++;
		} catch (ClassCastException ignore) {}
		return i;
	}

	@Test
	public void testSQLight() throws SqlJetException {
		//Odpri datoteko
		java.io.File dbFile = new java.io.File("dbFile");
		//Izbriši datoteko, če že obstaja
		dbFile.delete();
		//Ustvari novo SQLight bazo
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		//začni s transakcijami
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			//Kreiranje tabele
			jetDb.createTable("CREATE TABLE employees (second_name TEXT NOT NULL PRIMARY KEY , first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)");
			//Če transakcija uspe
			jetDb.commit();
		} catch(Exception e) {
			//Če transakcija ne uspe
			jetDb.rollback();
		}
		//začni z novimi transakcijami
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			//Ustvarjanje nove table v PB
			ISqlJetTable table = jetDb.getTable("employees");
			//Dodajanje zapisov v PB
			System.out.print(table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.println(table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis()));
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		//začni z novimi transakcijami
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			//Ustvarjanje nove table v PB
			ISqlJetTable table = jetDb.getTable("employees1");
			ISqlJetCursor cursor = table.open();
			while(!cursor.eof()) {
				System.out.println(cursor.getRowId()+" : "+cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getInteger(2));
				cursor.next();
			}
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		//Ko končaš z delom
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightMultiRowPrimarykey() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE employees (second_name TEXT NOT NULL , first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL, PRIMARY KEY(second_name, first_name))");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees");
			System.out.print(table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.print(table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis()) + ", ");
			System.out.println(table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis()));
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees1");
			ISqlJetCursor cursor = table.open();
			while(!cursor.eof()) {
				System.out.println(cursor.getRowId()+" : "+cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getInteger(2));
				cursor.next();
			}
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightTebleDefOne() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE album(\n" +
			 "  albumartist TEXT,\n" +
			 "  albumname TEXT,\n" +
			 "  PRIMARY KEY(albumartist, albumname)\n" +
			 ");");
			jetDb.createTable("CREATE TABLE song(\n" +
			 "  songid     INTEGER,\n" +
			 "  songartist TEXT,\n" +
			 "  songalbum TEXT,\n" +
			 "  songname   TEXT,\n" +
			 "  PRIMARY KEY(songid),\n" +
			 "  FOREIGN KEY(songartist, songalbum) REFERENCES album(albumartist, albumname)\n" +
			 ");");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
			System.out.println(e.getMessage());
			fail();
		}
		assertEquals(2, jetDb.getTable("album").getDefinition().getColumns().size());
		assertEquals(1, jetDb.getTable("album").getDefinition().getConstraints().size());
		assertEquals(4, jetDb.getTable("song").getDefinition().getColumns().size());
		assertEquals(2, jetDb.getTable("song").getDefinition().getConstraints().size());
		for (ISqlJetColumnDef e : jetDb.getTable("album").getDefinition().getColumns()) {
			System.out.println(e.getName());
		}
		System.out.println();
		for (ISqlJetColumnDef e : jetDb.getTable("song").getDefinition().getColumns()) {
			System.out.println(e.getName());
		}
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightTebleDefTwo() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE artist(\n" +
			 "  artistid    INTEGER PRIMARY KEY, \n" +
			 "  artistname  TEXT\n" +
			 ");");
			jetDb.createTable("CREATE TABLE singer(\n" +
			 "  singerid    INTEGER PRIMARY KEY, \n" +
			 "  singername  TEXT\n" +
			 ");");
			jetDb.createTable("CREATE TABLE track(\n" +
			 "  trackid     INTEGER,\n" +
			 "  trackname   TEXT, \n" +
			 "  artist		 INTEGER,\n" +
			 "  singer		 INTEGER,\n" +
			 "  PRIMARY KEY(trackid),\n" +
			 "  FOREIGN KEY(artist) REFERENCES artist(artistid),\n" +
			 "  FOREIGN KEY(singer) REFERENCES artist(singerid)\n" +
			 ");");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
			fail();
		}
		assertEquals(2, jetDb.getTable("artist").getDefinition().getColumns().size());
		assertEquals(0, jetDb.getTable("artist").getDefinition().getConstraints().size());
		assertEquals(4, jetDb.getTable("track").getDefinition().getColumns().size());
		assertEquals(3, jetDb.getTable("track").getDefinition().getConstraints().size());
		for (ISqlJetColumnDef e : jetDb.getTable("artist").getDefinition().getColumns()) {
			System.out.println(e.getName());
		}
		System.out.println();
		for (ISqlJetColumnDef e : jetDb.getTable("track").getDefinition().getColumns()) {
			System.out.println(e.getName());
		}
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightTebleDefThree() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE artist(\n" +
			 "  artistid    INTEGER,\n" +
			 "  artistname  TEXT,	\n" +
			 "  PRIMARY KEY(artistid)" +
			 ");");
			jetDb.createTable("CREATE TABLE singer(\n" +
			 "  singerid    INTEGER,\n" +
			 "  singername  TEXT,	\n" +
			 "  artistid 	 INTEGER,\n" +
			 "  PRIMARY KEY(singerid, artistid),\n" +
			 "  FOREIGN KEY(artist) REFERENCES artist(artistid)\n" +
			 ");");
			jetDb.createTable("CREATE TABLE track(\n" +
			 "  trackid     INTEGER,\n" +
			 "  trackname   TEXT,	\n" +
			 "  artist		 INTEGER,\n" +
			 "  singer		 INTEGER,\n" +
			 "  PRIMARY KEY(trackid, singer, artist),\n" +
			 "  FOREIGN KEY(singer, artist) REFERENCES singer(artistid, singerid)\n" +
			 ");");
			jetDb.commit();
		} catch(SqlJetException e) {
			jetDb.rollback();
			System.out.println(e.getMessage());
			fail();
		}
		assertEquals(2, jetDb.getTable("artist").getDefinition().getColumns().size());
		assertEquals(1, jetDb.getTable("artist").getDefinition().getConstraints().size());
		assertEquals(3, jetDb.getTable("singer").getDefinition().getColumns().size());
		assertEquals(2, jetDb.getTable("singer").getDefinition().getConstraints().size());
		assertEquals(4, jetDb.getTable("track").getDefinition().getColumns().size());
		assertEquals(2, jetDb.getTable("track").getDefinition().getConstraints().size());
		for (ISqlJetColumnDef e : jetDb.getTable("artist").getDefinition().getColumns()) {
			System.out.println(e.getName());
		}
		System.out.println();
		for (ISqlJetColumnDef e : jetDb.getTable("singer").getDefinition().getColumns()) {
			System.out.println(e.getName());
		}
		System.out.println();
		for (ISqlJetColumnDef e : jetDb.getTable("track").getDefinition().getColumns()) {
			System.out.println(e.getName());
		}
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightArrayTable() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE array\n" +
			 "(\n" +
			 "    id integer," +
			 "		PRIMARY KEY (id)\n" +
			 ") WITHOUT ROWID;");
			jetDb.createTable("CREATE TABLE array_points\n" +
			 "(\n" +
			 "    array_id integer,\n" +
			 "    position integer,\n" +
			 "		x integer NOT NULL,\n" +
			 "    y integer NOT NULL\n" +
			 "    PRIMARY KEY (array_id, position)\n" +
			 " 	FOREIGN KEY (array_id) REFERENCES array(id)" +
			 ") WITHOUT ROWID;");
			jetDb.createTable("CREATE TABLE foo\n" +
			 "(\n" +
			 "    id integer NOT NULL,\n" +
			 "    array_id integer NOT NULL,\n" +
			 "    PRIMARY KEY (id),\n" +
			 "    FOREIGN KEY (array_id) REFERENCES array(id)\n" +
			 ");");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
			System.out.println(e.getMessage());
			fail();
		}
		try {
			ISqlJetTable table = jetDb.getTable("array");
			System.out.println(table.insert(null));
			System.out.println(table.insert(null));
			jetDb.commit();
			jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
			ISqlJetCursor c = jetDb.getTable("array").open();
			while (!c.eof()) {
				System.out.println(c.getRowId() + " >> " + c.getValue(0));
				c.next();
			}
			jetDb.commit();
		} catch (Exception e) {
			jetDb.rollback();
			System.out.println(e.getMessage());
			fail();
		}
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightInsertNull() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE employees (second_name TEXT NOT NULL PRIMARY KEY , first_name TEXT, date_of_birth INTEGER NOT NULL)");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees");
			table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis());
			table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis());
			table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis());
			table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis());
			table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", null, Calendar.getInstance().getTimeInMillis());
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		ISqlJetTable table = jetDb.getTable("employees");
		ISqlJetCursor cursor = table.open();
		while(!cursor.eof()) {
			System.out.println(cursor.getRowId()+" : "+cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getInteger(2));
			cursor.next();
		}
		jetDb.commit();
		jetDb.close();
		dbFile.delete();
	}

	@Test(expected = SqlJetException.class)
	public void testSQLightInsertNullException() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE employees (second_name TEXT NOT NULL PRIMARY KEY , first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)");
			jetDb.commit();
		} catch(SqlJetException e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		ISqlJetTable table = jetDb.getTable("employees");
		try {
			table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis());
			table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis());
			table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis());
			table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis());
			table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", null, Calendar.getInstance().getTimeInMillis());
			jetDb.commit();
		} catch (SqlJetException e) {
			jetDb.rollback();
			jetDb.close();
			dbFile.delete();
			throw e;
		}
	}

	@Test
	public void testSQLightInsertKeyIncrement() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE employees (id INTEGER PRIMARY KEY ASC, second_name TEXT NOT NULL, first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees");
			table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis());
			table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis());
			table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis());
			table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis());
			table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis());
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		ISqlJetTable table = jetDb.getTable("employees");
		ISqlJetCursor cursor = table.open();
		while(!cursor.eof()) {
			System.out.println(cursor.getRowId()+" : " + cursor.getInteger(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getInteger(3));
			cursor.next();
		}
		jetDb.commit();
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightLookUp() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE employees (id INTEGER PRIMARY KEY ASC, second_name TEXT NOT NULL, first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)");
			jetDb.createIndex("CREATE INDEX sname_index ON employees(second_name)");
			jetDb.createIndex("CREATE INDEX name_index ON employees(first_name)");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees");
			table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis());
			table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis());
			table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis());
			table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis());
			table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Klemen", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Marko", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Simon", Calendar.getInstance().getTimeInMillis());
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		System.out.println("Print test!!!");
		jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		ISqlJetTable table = jetDb.getTable("employees");
		for (ISqlJetCursor cursor = table.open(); !cursor.eof(); cursor.next())
			System.out.println(cursor.getRowId() + " : " + cursor.getInteger(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getInteger(3));
		jetDb.commit();
		System.out.println("Lookup test 1.!!!");
		jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		table = jetDb.getTable("employees");
		for (ISqlJetCursor cursor = table.lookup("sname_index", "Berkovic"); !cursor.eof(); cursor.next())
			System.out.println(cursor.getRowId() + " : " + cursor.getInteger(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getInteger(3));
		jetDb.commit();
		System.out.println("Lookup test 2.!!!");
		jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		table = jetDb.getTable("employees");
		for (ISqlJetCursor cursor = table.lookup("name_index", "Klemen"); !cursor.eof(); cursor.next())
			System.out.println(cursor.getRowId() + " : " + cursor.getValue(0) + " " + cursor.getValue(1) + " " + cursor.getValue(2) + " " + cursor.getValue(3));
		jetDb.commit();
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightInsertByFieldName() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE employees (id INTEGER PRIMARY KEY ASC, second_name TEXT, first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)");
			jetDb.createIndex("CREATE INDEX sname_index ON employees(second_name)");
			jetDb.createIndex("CREATE INDEX name_index ON employees(first_name)");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("second_name", "Berkovic");
		map.put("first_name", "Alojz");
		map.put("date_of_birth", Calendar.getInstance().getTimeInMillis());
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("first_name", "Kristina");
		map1.put("date_of_birth", Calendar.getInstance().getTimeInMillis());
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees");
			table.insertByFieldNames(map);
			table.insertByFieldNames(map1);
			table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis());
			table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis());
			table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis());
			table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis());
			table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Klemen", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Marko", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Simon", Calendar.getInstance().getTimeInMillis());
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		System.out.println("Print test!!!");
		jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		ISqlJetTable table = jetDb.getTable("employees");
		for (ISqlJetCursor cursor = table.open(); !cursor.eof(); cursor.next())
			System.out.println(cursor.getRowId() + " : " + cursor.getInteger(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getInteger(3));
		jetDb.commit();
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testSQLightAlterTable() throws SqlJetException {
		java.io.File dbFile = new java.io.File("dbFile");
		dbFile.delete();
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.createTable("CREATE TABLE employees (id INTEGER PRIMARY KEY ASC, second_name TEXT, first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)");
			jetDb.createIndex("CREATE INDEX sname_index ON employees(second_name)");
			jetDb.createIndex("CREATE INDEX name_index ON employees(first_name)");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("second_name", "Berkovic");
		map.put("first_name", "Alojz");
		map.put("date_of_birth", Calendar.getInstance().getTimeInMillis());
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("first_name", "Kristina");
		map1.put("date_of_birth", Calendar.getInstance().getTimeInMillis());
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees");
			System.out.println(table.insertByFieldNames(map));
			table.insertByFieldNames(map1);
			table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis());
			table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis());
			table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis());
			table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis());
			table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Klemen", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Marko", Calendar.getInstance().getTimeInMillis());
			table.insert("Berkovic", "Simon", Calendar.getInstance().getTimeInMillis());
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			jetDb.alterTable("ALTER TABLE employees ADD phone_number INTEGER");
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			ISqlJetTable table = jetDb.getTable("employees");
			table.insert("Berkovic", "Kristina", Calendar.getInstance().getTimeInMillis(), 41123123);
			table.insert("Berkovic", "Alojz", Calendar.getInstance().getTimeInMillis(), 31012012);
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		System.out.println("Print test!!!");
		jetDb.beginTransaction(SqlJetTransactionMode.READ_ONLY);
		ISqlJetTable table = jetDb.getTable("employees");
		for (ISqlJetCursor cursor = table.open(); !cursor.eof(); cursor.next())
			System.out.println(cursor.getRowId() + " : " + cursor.getInteger(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getInteger(3) + " " + cursor.getInteger(4));
		jetDb.commit();
		jetDb.close();
		dbFile.delete();
	}

	@Test(expected = TableAnnotationException.class)
	public void testCreateTableAnnotaionException() throws NoSuchMethodException, SqlMappingException, SqlJetException, DataBaseFileException, IOException, IllegalAccessException, TableAnnotationException, EntryAnnotationException {
		TestNoAnno test = new TestNoAnno();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		db.open(file);
		try {
			db.createTables(test);
		} catch (Exception e) {
			file.delete();
			throw e;
		}
	}

	private class TestNoAnno {

		@Entry private int number;
		@Entry private String line;

	}

	@Test
	public void testSimpleClassCreateTable() throws NoSuchMethodException, SqlMappingException, SqlJetException, DataBaseFileException, IOException, IllegalAccessException, TableAnnotationException, EntryAnnotationException {
		SimpleClass test = new SimpleClass("Klemen", "Berkovic", LocalDateTime.now());
		SqlMapping<LocalDateTime, Integer> mapDate = new SqlMapping<LocalDateTime, Integer>() {
			@Override
			public Integer inMapping(LocalDateTime in) {

				return 1234;
			}

			@Override
			public LocalDateTime outMapping(Integer in) {
				return LocalDateTime.now();
			}
		};
		db.addMappings(mapDate, LocalDateTime.class);
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		db.open(file);
		db.createTables(test);
		assertEquals(4, db.getDataBase().getTable("SimpleClass").getDefinition().getColumns().size());
		assertEquals(1, db.getDataBase().getTable("SimpleClass").getDefinition().getConstraints().size());
		file.delete();
	}

	@Test
	public void testCreateTableReferences() throws NoSuchMethodException, SqlMappingException, SqlJetException, DataBaseFileException, IOException, IllegalAccessException, TableAnnotationException, EntryAnnotationException {
		TClass0 test = new TClass0();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		db.open(file);
		try {
			db.createTables(test);
			assertEquals(3, db.getDataBase().getTable("TClass2").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("TClass2").getDefinition().getConstraints().size());
			assertEquals(3, db.getDataBase().getTable("TClass1").getDefinition().getColumns().size());
			assertEquals(2, db.getDataBase().getTable("TClass1").getDefinition().getConstraints().size());
			assertEquals(5, db.getDataBase().getTable("TClass3").getDefinition().getColumns().size());
			assertEquals(2, db.getDataBase().getTable("TClass3").getDefinition().getConstraints().size());
			assertEquals(6, db.getDataBase().getTable("TClass0").getDefinition().getColumns().size());
			assertEquals(3, db.getDataBase().getTable("TClass0").getDefinition().getConstraints().size());
			file.delete();
		} catch (Exception e) {
			file.delete();
			throw e;
		}
	}

	@Table
	private class TClass0 {
		@Entry(primaryKey = true)
		private int number;
		@Entry(primaryKey = true, name = {"pk1", "pk2", "pk3", "pk4"})
		private TClass1 tclass1;
		@Entry
		private String text;
		@Entry(name = {"pp_id"})
		private TClass3 tclass3;

		public TClass0() {
			number = 10;
			tclass1 = new TClass1();
			text = "klemen";
			tclass3 = new TClass3();
		}
	}

	@Table
	private class TClass1 {
		@Entry(primaryKey = true)
		private int number;
		@Entry(primaryKey = true, name = {"pk1_c1", "pk2_c1"})
		private TClass2 tclass2;

		public TClass1() {
			number = 20;
			tclass2 = new TClass2();
		}
	}

	@Table
	private class TClass2 {
		@Entry
		private int number;
		@Entry(primaryKey = true)
		private String text;
		@Entry(primaryKey = true)
		private float realNum;

		public TClass2() {
			number = 30;
			text = "Berkovic";
			realNum = 2;
		}
	}

	@Table
	private class TClass3 {
		@Entry(primaryKey = true)
		private int number;
		@Entry
		private double realNum;
		@Entry(name = {"pk1", "pk2", "pk3"})
		private TClass1 tclass1;

		public TClass3() {
			number = 25;
			realNum = 2.5;
			tclass1 = new TClass1();
		}
	}

	@Test
	public void testCreateTableArray() throws IOException, SqlJetException, TableAnnotationException, IllegalAccessException, DataBaseFileException, EntryAnnotationException, NoSuchMethodException {
		TClassArray test = new TClassArray();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		db.open(file);
		try {
			db.createTables(test);
			assertEquals(4, db.getDataBase().getTable("TClassArray").getDefinition().getColumns().size());
			assertEquals(4, db.getDataBase().getTable("TClassArray").getDefinition().getConstraints().size());
			assertEquals(1, db.getDataBase().getTable("inttab1").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("inttab1").getDefinition().getConstraints().size());
			assertEquals(1, db.getDataBase().getTable("inttab2").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("inttab2").getDefinition().getConstraints().size());
			assertEquals(1, db.getDataBase().getTable("listlist1").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("listlist2").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("listlist2").getDefinition().getConstraints().size());
			assertEquals(1, db.getDataBase().getTable("listlist1").getDefinition().getConstraints().size());
			assertEquals(3, db.getDataBase().getTable("inttab1avalue").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("inttab1avalue").getDefinition().getConstraints().size());
			assertEquals(4, db.getDataBase().getTable("inttab2avalue").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("inttab2avalue").getDefinition().getConstraints().size());			
			assertEquals(3, db.getDataBase().getTable("listlist1avalue").getDefinition().getColumns().size());
			assertEquals(4, db.getDataBase().getTable("listlist2avalue").getDefinition().getColumns().size());
			assertEquals(1, db.getDataBase().getTable("listlist1avalue").getDefinition().getConstraints().size());
			assertEquals(1, db.getDataBase().getTable("listlist2avalue").getDefinition().getConstraints().size());
			file.delete();
		} catch (Exception e) {
			file.delete();
			throw e;
		}

	}

	@Table
	private class TClassArray {
		@Entry
		private int[] tab1;
		@Entry
		private int[][] tab2;
		@Entry
		private List<Integer> list1;
		@Entry
		private List<List<Integer>> list2;

		public TClassArray() {
			tab1 = new int[]{1, 2, 3, 4};
			tab2 = new int[][]{{1, 2},{3, 4}};
			list1 = new LinkedList<>();
			list1.add(1);
			list1.add(2);
			list1.add(3);
			list1.add(4);
			list2 = new LinkedList<>();
			list2.add(new LinkedList<>());
			list2.add(new LinkedList<>());
			list2.get(0).add(1);
			list2.get(0).add(2);
			list2.get(1).add(3);
			list2.get(1).add(4);
		}
	}
}
