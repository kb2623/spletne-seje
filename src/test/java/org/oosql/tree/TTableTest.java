package org.oosql.tree;

import org.junit.Test;

import org.oosql.annotation.ArrayTable;
import org.oosql.annotation.Columns;
import org.oosql.annotation.Table;
import org.oosql.annotation.Column;
import org.oosql.exception.OosqlException;
import org.oosql.exception.TableAnnotationException;
import org.oosql.exception.ColumnAnnotationException;

import java.sql.JDBCType;
import java.util.List;
import java.util.Map;

public class TTableTest {

	@Table
	private enum EnumTableTest {
		TT1, TT2, TT3
	}

	private enum EnumClassTest {
		TC1, TC2, TC3
	}

	@Test(expected = TableAnnotationException.class)
	public void testBadClass() throws OosqlException, ClassNotFoundException {
		TTable table = new TTable(Object.class);
	}

	@Test
	public void testClassNoTable() throws OosqlException, ClassNotFoundException {
		class TestOne {
			private int some_one;
			@Column(name = "test_some_one", pk = true)
			private double some_two;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String test_text;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			private TestOne only_test;
			@Column
			private TestOne test_for_real;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test(expected = TableAnnotationException.class)
	public void testClassNoTableLevelException() throws OosqlException, ClassNotFoundException {
		class TestOne {
			private int some_one;
			@Column(name = "test_some_one", pk = true)
			private double some_two;
			@Column
			private String test_text;
		}
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			private TestOne only_test;
			@Column
			private TestOne test_for_real;
		}
		@Table
		class TestThree {
			private int test_no_go;
			@Column(pk = true, name = "test_three_pk")
			private int test_pk;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String data;
			@Column
			private TestOne this_is_ok;
			@Column
			private TestTwo this_is_not_ok;
		}
		TTable table = new TTable(TestThree.class);
	}

	@Test
	public void testClassNoTableMultiUse() throws OosqlException, ClassNotFoundException {
		// FIXME
		class TestOne {
			private int some_one;
			@Column(name = "test_some_one", pk = true)
			private double some_two;
			@Column
			private String test_text;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			private TestOne only_test;
			@Column
			private TestOne test_for_real;
		}
		@Table
		class TestThree {
			private int test_no_go;
			@Column(pk = true, name = "test_three_pk")
			private int test_pk;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String data;
			@Column
			private TestOne this_is_ok;
			@Column(pk = true, name = "pk_test_one")
			@Column(pk = true, name = "pk_test_two")
			private TestTwo this_is_ok_sec;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test
	public void testSimpleClass() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testSimpleClassAutoId() throws OosqlException, ClassNotFoundException {
		@Table(id = @Column(name = "id_testOne", pk = true))
		class TestOne {
			@Column
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testSimpleClassAutoIdNoPk() throws OosqlException, ClassNotFoundException {
		// FIXME Če nimamo pk atributa v tabeli nastavljenega na true potem, se vrstica ne doda v primarni ključ
		@Table(id = @Column(name = "id_testOne"))
		class TestOne {
			@Column
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testClassExtend() throws OosqlException, ClassNotFoundException {
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo extends TestOne {
			@Column(name = "two_number")
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = "two_text")
			private String text;
			@Column(type = JDBCType.REAL, name = "two_realNumber")
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testClassExtendOne() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		class TestTwo extends TestOne {
			@Column(name = "two_number")
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = "two_text")
			private String text;
			@Column(type = JDBCType.REAL, name = "two_realNumber")
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testClassExtendName() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table(name = "test")
		class TestTwo extends TestOne {
			@Column(name = "two_number")
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = "two_text")
			private String text;
			@Column(type = JDBCType.REAL, name = "two_realNumber")
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testClassExtendOneName() throws OosqlException, ClassNotFoundException {
		@Table(name = "test")
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		class TestTwo extends TestOne {
			@Column(name = "two_number")
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = "two_text")
			private String text;
			@Column(type = JDBCType.REAL, name = "two_realNumber")
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testTwoClassOne() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			private TestOne classOne;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testTwoClassTwo() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testThreeClassOne() throws OosqlException, ClassNotFoundException {
		// FIXME duplikati tabel
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			private TestOne classOne;
			@Column
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test(expected = ColumnAnnotationException.class)
	public void testThreeClassTwo() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
			@Column(pk = true)
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
	}

	@Test
	public void testThreeClassThreeEmptyName() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
			@Column(name = "tt_1id", pk = true)
			@Column(name = "tt_2id", pk = true)
			@Column(name = "", pk = true)
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test
	public void testThreeClassThree() throws OosqlException, ClassNotFoundException {
		// FIXME ponovitev tabele za razred TestOne
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
			@Column(name = "tt_1id", pk = true)
			@Column(name = "tt_2id", pk = true)
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test
	public void testEnumNewTable() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			private EnumTableTest enumt;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testEnumNewTableSetName() throws OosqlException, ClassNotFoundException {
		// FIXME Izdelja nov enum, ki ima spremenjen zapis tabel
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			private EnumTableTest enumt;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testEnumNewTableSetAllNames() throws OosqlException, ClassNotFoundException {
		// FIXME Izdelaj nov enum, ki ima spremenjen zapis tabel
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			private EnumTableTest enumt;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testEnumNewTableNewNamePk() throws OosqlException, ClassNotFoundException {
		// FIXME V obeh razredih imamo isto tabelo za enume, a z različnimi imeni, katero uporabiti???
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private EnumTableTest enumt;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private EnumTableTest enumt;
			@Column
			private EnumClassTest enumc;
			@Column(pk = true, name = "pk_to_number")
			@Column(pk = true, name = "pk_enum")
			private TestOne test_one;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testEnum() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private EnumClassTest enumc;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testClassWithNoTable() throws OosqlException, ClassNotFoundException {
		class TestOne {
			@Column(name = "number_1", pk = true)
			private int number;
			@Column(name = "text_1", type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(name = "realNumber_1", type = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column
			private TestOne testC;
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testArray() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column
			private List<List<Integer>> list;
			@Column
			private Integer[][][] array;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testArrayClass() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String name;
		}
		@Table
		class TestTwo {
			@Column
			private List<List<List<TestOne>>> list;
			@Column
			private TestOne[][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test(expected = TableAnnotationException.class)
	public void testArrayClassNoTable() throws OosqlException, ClassNotFoundException {
		class TestOne {
			@Column
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String name;
		}
		@Table
		class TestTwo {
			@Column
			private List<List<List<TestOne>>> list;
			@Column
			private TestOne[][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test(expected = TableAnnotationException.class)
	public void testArrayClassNoTablePk() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String name;
		}
		@Table
		class TestTwo {
			@Column
			@ArrayTable(valueTable = @Table(columns = @Columns({
					@Column(name = "l1")
			})))
			private List<List<List<TestOne>>> list;
			@Column
			@ArrayTable(valueTable = @Table(columns = @Columns(
					@Column(name = "test")
			)))
			private TestOne[][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testArrayClassPk() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String name;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private List<List<List<TestOne>>> list;
			@Column(pk = true)
			private TestOne[][][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testArrayClassCompundKey() throws OosqlException, ClassNotFoundException {
		// FIXME pozabimo dodati prefix vrsticam z dimenzijo in nastavi imena tabelam, ki shranjujejo programske tabele
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(pk = true, type = JDBCType.VARCHAR, typeLen = 25)
			private String name;
		}
		@Table
		class TestTwo {
			@Column
			private List<List<List<TestOne>>> list;
			@Column
			private TestOne[][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testArrayClassCompundKeyTwo() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(pk = true, type = JDBCType.VARCHAR, typeLen = 25)
			private String name;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private List<List<List<TestOne>>> list;
			@Column(pk = true)
			private TestOne[][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testArrayAndList() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column
			private List<List<List<Integer[][][]>>> special_list;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testListAndArray() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column
			private List<List<List<Integer>>>[][][] special_list;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testArrayExceptionThree() throws OosqlException, ClassNotFoundException {
		// TODO implementiraj izdelavo tabel za Slovarje
		@Table
		class TestOne {
			@Column
			private List<List<List<Map<Integer, String>>>> list_map;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testArrayExceptionFour() throws OosqlException, ClassNotFoundException {
		// TODO implementiraj izdelavo tabel za slovarje
		@Table
		class TestOne {
			@Column
			private Map<Integer, String>[][][] array_map;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testMap() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne {
			@Column
			private Map<Integer, String> test_map;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}
}