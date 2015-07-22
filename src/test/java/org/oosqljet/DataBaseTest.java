package org.oosqljet;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.oosqljet.exception.EntryAnnotationException;
import org.oosqljet.exception.SqlMappingException;
import org.oosqljet.exception.TableAnnotationException;
import org.sessionization.fields.FieldType;
import org.sessionization.fields.File;
import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;
import org.sessionization.fields.Referer;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings({"deprecated", "unused"})
public class DataBaseTest {

	DataBase db;

	@Before
	public void setUp() {
		db = new DataBase();
	}

	@Test(expected = TableAnnotationException.class)
	public void testCreateTables() throws EntryAnnotationException, NoSuchMethodException, TableAnnotationException, IllegalAccessException, SQLException {
		db.createTables("hello");
	}

	@Test
	public void testSqlJetException() {
		StringBuilder builder = new StringBuilder().append("From Java");
		builder.insert(0, "Hello ");
		assertEquals("Hello From Java", builder.toString());
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
			if (field.isAnnotationPresent(Column.class)) {
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

	@Test(expected = TableAnnotationException.class)
	public void testCreateTableAnnotaionException() throws NoSuchMethodException, SqlMappingException, SqlJetException, IOException, IllegalAccessException, TableAnnotationException, EntryAnnotationException {
		TestNoAnno test = new TestNoAnno();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		file.delete();
	}

	private class TestNoAnno {

		@Column private int number;
		@Column private String line;

	}

	@Test
	public void testSimpleClassCreateTable() throws NoSuchMethodException, SqlMappingException, SqlJetException,IOException, IllegalAccessException, TableAnnotationException, EntryAnnotationException {
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
		file.delete();
	}

	@Test
	public void testCreateTableReferences() throws NoSuchMethodException, SqlMappingException, SqlJetException, IOException, IllegalAccessException, TableAnnotationException, EntryAnnotationException {
		TClass0 test = new TClass0();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		file.delete();
	}

	@Table
	private class TClass0 {
		@Column(primaryKey = true)
		private int number;
		@Column(primaryKey = true, name = {"pk1", "pk2", "pk3", "pk4"})
		private TClass1 tclass1;
		@Column
		private String text;
		@Column(name = {"pp_id"})
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
		@Column(primaryKey = true)
		private int number;
		@Column(primaryKey = true, name = {"pk1_c1", "pk2_c1"})
		private TClass2 tclass2;

		public TClass1() {
			number = 20;
			tclass2 = new TClass2();
		}
	}

	@Table
	private class TClass2 {
		@Column
		private int number;
		@Column(primaryKey = true)
		private String text;
		@Column(primaryKey = true)
		private float realNum;

		public TClass2() {
			number = 30;
			text = "Berkovic";
			realNum = 2;
		}
	}

	@Table
	private class TClass3 {
		@Column(primaryKey = true)
		private int number;
		@Column
		private double realNum;
		@Column(name = {"pk1", "pk2", "pk3"})
		private TClass1 tclass1;

		public TClass3() {
			number = 25;
			realNum = 2.5;
			tclass1 = new TClass1();
		}
	}

	@Test
	public void testCreateTableArray() throws IOException, SqlJetException, TableAnnotationException, IllegalAccessException, EntryAnnotationException, NoSuchMethodException {
		TClassArray test = new TClassArray();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		file.delete();
	}

	@Table
	private class TClassArray {
		@Column
		private int[] tab1;
		@Column
		private int[][] tab2;
		@Column
		private List<Integer> list1;
		@Column
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

	@Test
	public void testCreateTableExtendClassNewTableName() throws IOException, SqlJetException, TableAnnotationException, IllegalAccessException, EntryAnnotationException, NoSuchMethodException {
		TClassOneE test = new TClassTwoE();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		file.delete();
	}

	@Table(autoId = true)
	private class TClassOneE {
		@Column
		private int num;
		@Column
		private float fNum;

		public TClassOneE() {
			num = 1;
			fNum = 2;
		}
	}

	@Table
	private class TClassTwoE extends TClassOneE {
		@Column
		private double dNum;
		private int num;
		private float fNum;

		public TClassTwoE() {
			super();
			dNum = 1.1;
			num = 2;
			fNum = 3;
		}
	}

	@Test
	public void testCreateTableExtendClass() throws IOException, SqlJetException, TableAnnotationException, IllegalAccessException, EntryAnnotationException, NoSuchMethodException {
		TClassTwoEE test = new TClassTwoEE();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		file.delete();
	}

	private class TClassTwoEE extends TClassOneE {
		@Column
		private double dNum;
		private int num;
		private float fNum;

		public TClassTwoEE() {
			super();
			dNum = 1.1;
			num = 2;
			fNum = 3;
		}
	}

	@Test
	public void testCreateTableExtendClassAlterTable() throws IOException, SqlJetException, TableAnnotationException, IllegalAccessException, EntryAnnotationException, NoSuchMethodException {
		TClassOneE testOne = new TClassOneE();
		TClassTwoEE testTwo = new TClassTwoEE();
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		file.delete();
	}

	@Table
	private class TClassMapTest {
		@Column
		private Map<String, Integer> mapOne;
		@Column
		private Map<Integer[], String> mapTwo;
		@Column
		private Map<Double[], String[]> mapThere;
		@Column
		private Map<TClass3, TClass2> mapFour;

		public TClassMapTest() {
			mapOne = new HashMap<>();
			mapTwo = new HashMap<>();
			mapThere = new HashMap<>();
			mapFour = new HashMap<>();
		}
	}

	@Test
	public void testCreateTableMaps() {
		// TODO
	}
}
