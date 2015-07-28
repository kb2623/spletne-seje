package org.oosql;

import static org.junit.Assert.*;
import org.junit.Test;

import org.oosql.annotation.DataType;
import org.oosql.exception.MappingException;
import org.oosql.exception.OosqlException;
import org.oosql.exception.TableAnnotationException;
import org.sessionization.fields.FieldType;
import org.sessionization.fields.File;
import org.oosql.annotation.Column;
import org.oosql.annotation.Table;
import org.sessionization.fields.Referer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.time.LocalDateTime;

@SuppressWarnings({"deprecated", "unused"})
public class DataBaseTest {

	DataBase db;

	private enum SqlDataType implements DataType {
		Text {
			@Override
			public String getDateType() {
				return null;
			}
		},
		Int {
			@Override
			public String getDateType() {
				return null;
			}
		},
		Real {
			@Override
			public String getDateType() {
				return null;
			}
		};

		@Override
		public Class<? extends Annotation> annotationType() {
			return DataType.class;
		}
	}

	@Test(expected = TableAnnotationException.class)
	public void testCreateTables() throws OosqlException, IllegalAccessException, SQLException {
		db.createTables("hello");
	}

	@Test
	public void testSqlJetException() {
		StringBuilder builder = new StringBuilder().append("From Java");
		builder.insert(0, "Hello ");
		assertEquals("Hello From Java", builder.toString());
	}

	@Test
	public void testReflections() throws MappingException {
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
		class test implements SqlMapping<String, Integer> {
			@Override
			public Integer inMapping(String in) {
				return null;
			}
			@Override
			public String outMapping(Integer in) {
				return null;
			}
		}
		System.out.println(Util.getReturnType(test.class, String.class).getName());
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
	public void testSimpleClassCreateTable() throws IOException, IllegalAccessException, OosqlException, NoSuchMethodException {
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
		java.io.File file = new java.io.File("test.test");
		file.createNewFile();
		file.delete();
	}
}
