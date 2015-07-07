package org.oosqljet;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.sessionization.fields.File;
import org.oosqljet.annotation.Entry;
import org.oosqljet.annotation.Table;
import org.sessionization.fields.Referer;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;

public class DataBaseTest {

    DataBase db;

    @Before
    public void setUp() {
        //TODO
    }

    @Test
    public void testInsert() {

    }

    @Test
    public void testReflections() {
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

		assertTrue(SqlMapping.class.isAssignableFrom(anotations.getClass()));

        System.out.println("\nTest super class from class that has extends");
        Class referer = Referer.class;
        System.out.println(referer.getSuperclass().getSimpleName());

        System.out.println("\nTest super class from class that has no extends");
        Class file = File.class;
        System.out.println(file.getSuperclass().getName());
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
}