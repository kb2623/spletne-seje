package org.spletneseje.fields;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.Test;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import org.spletneseje.fields.ncsa.RequestLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("deprecation")
public class TestFields {

	@Test
	public void testUserAgent() {
		String testString = "Mozilla/5.0+(compatible;+Yahoo!+Slurp/3.0;+http://help.yahoo.com/help/us/ysearch/slurp)";
		UserAgent userAgent = new UserAgent(testString, LogType.W3C);
		String testString2 = "Mozilla/5.0+(Windows;+U;+Windows+NT+5.1;+en-US;+rv:1.9.0.16)+Gecko/2010010414+Firefox/3.0.16+Flock/2.5.6";
		UserAgent userAgent2 = new UserAgent(testString2, LogType.W3C);
		String testString3 = "Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 520)";
		UserAgent userAgent3 = new UserAgent(testString3, LogType.NCSA);
		String testString4 = "facebookexternalhit/1.1 (+http://www.facebook.com/externalhit_uatext.php)";
		UserAgent userAgent4 = new UserAgent(testString4, LogType.NCSA);
		String testString5 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36";
		UserAgent userAgent5 = new UserAgent(testString5, LogType.NCSA);
		System.out.printf("%s%n%s%n%s%n%s%n%s%n", userAgent.izpis(), userAgent2.izpis(), userAgent3.izpis(), userAgent4.izpis(), userAgent5.izpis());
	}

	@Test
	public void testCookie() {
		String testString = "USERID=CustomerA;IMPID=01234";
		Cookie cookie = new Cookie(testString, LogType.NCSA);
		String testString2 = "USERID=CustomerA;+IMPID=01234";
		Cookie cookie2 = new Cookie(testString2, LogType.W3C);
		String testString3 = "-";
		Cookie cookie3 = new Cookie(testString3, LogType.W3C);
		Cookie cookie4 = new Cookie(testString3, LogType.NCSA);
		String testString4 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;+__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092";
		Cookie cookie5 = new Cookie(testString4, LogType.W3C);
		assertEquals("[[USERID = CustomerA][IMPID = 01234]]", cookie.izpis());
		assertEquals("[[USERID = CustomerA][IMPID = 01234]]", cookie2.izpis());
		assertEquals("[-]", cookie3.izpis());
		assertEquals("[-]", cookie4.izpis());
		assertEquals("[[referencna = -][__utmz = 237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa][GAMBIT_ID = 89.142.126.214-4231287712.29944903][ASPSESSIONIDASARQBAB = MDLBNDABLMEFLDGCFIFOJIGM][__utmb = 237691092.48.10.1238543004][__utmc = 237691092][__utma = 237691092.2338317182835442000.1214229487.1238435165.1238543004.8][productsForComparison = 712150%21701041%21610119%21403083]]", cookie5.izpis());
	}

	@Test
	public void testCookieEquals() {
		String testString = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092";
		String testString2 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;GAMBIT_ID=89.142.126.214-4231287712.29944903;referencna=;__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;productsForComparison=712150%21701041%21610119%21403083;ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;__utmb=237691092.48.10.1238543004;__utmc=237691092";
		String testString3 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;GAMBIT_ID=89.142.126.214-4231287712.29944903;referencna=;productsForComparison=712150%21701041%21610119%21403083;ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;__utmb=237691092.48.10.1238543004;__utmc=237691092";
		Cookie cookie = new Cookie(testString, LogType.W3C);
		Cookie cookie2 = new Cookie(testString2, LogType.NCSA);
		Cookie cookie3 = new Cookie(testString3, LogType.NCSA);
		assertTrue(cookie.equals(cookie2));
		assertFalse(cookie.equals(cookie3));
		assertFalse(cookie.equals(new UserAgent("-", LogType.NCSA)));
		assertFalse(cookie.equals(new UserAgent("-", LogType.W3C)));
	}

	@Test
	public void testSQLight() throws SqlJetException {
		//Odpri datoteko
		File dbFile = new File("dbFile");
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
			ISqlJetTable table = jetDb.getTable("employees");
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
	public void testSQLightInsertNull() throws SqlJetException {
		File dbFile = new File("dbFile");
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
		File dbFile = new File("dbFile");
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
		File dbFile = new File("dbFile");
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
		File dbFile = new File("dbFile");
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
		File dbFile = new File("dbFile");
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
		File dbFile = new File("dbFile");
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

	@Test
	public void testDateParsing() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.US);
		LocalDateTime dateTime = LocalDateTime.parse("26/Jul/2002:12:11:52 +0000", formatter);
		assertEquals(26, dateTime.getDayOfMonth());
	}

	@Test
	public void testRequestLine() throws MalformedURLException {
        RequestLine line = new RequestLine("GET", "/jope-puloverji/moski-pulover-b74-red?limit=18&test=hello", "HTTP/1.1");
        assertEquals("GET", line.getMethod().toString());
        assertEquals(1.1, line.getProtocol().getVersion(), 0.01);
        assertEquals("HTTP/1.1", line.getProtocol().toString());
        assertEquals("[[test = hello][limit = 18]]", line.getQuery().toString());
        assertEquals("/jope-puloverji/moski-pulover-b74-red?limit=18&test=hello", line.getUri().getFile());
        assertEquals("/jope-puloverji/moski-pulover-b74-red", line.getUri().getPath());
        line = new RequestLine("POST", "/catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css?limit=10", "HTTP/1.1");
        assertEquals("POST", line.getMethod().toString());
        assertEquals(1.1, line.getProtocol().getVersion(), 0.01);
        assertEquals("HTTP/1.1", line.getProtocol().toString());
        assertEquals("[[limit = 10]]", line.getQuery().toString());
        assertEquals("/catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css?limit=10", line.getUri().getFile());
        assertEquals("/catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css", line.getUri().getPath());
        line = new RequestLine("GET", "/image/cache/data/bigbananaPACK%20copy-cr-214x293.png", "HTTP");
        assertEquals("GET", line.getMethod().toString());
        assertEquals(0, line.getProtocol().getVersion(), 0.01);
        assertEquals("HTTP", line.getProtocol().toString());
        assertEquals("[]", line.getQuery().toString());
        assertEquals("/image/cache/data/bigbananaPACK%20copy-cr-214x293.png", line.getUri().getFile());
        assertEquals("/image/cache/data/bigbananaPACK%20copy-cr-214x293.png", line.getUri().getPath());
    }

}
