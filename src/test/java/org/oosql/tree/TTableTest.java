package org.oosql.tree;

import org.junit.Test;

import org.oosql.annotation.ArrayTable;
import org.oosql.annotation.EnumTable;
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
	public void testBadClass() throws OosqlException {
		TTable table = new TTable(Object.class);
	}

	@Test
	public void testClassNoTable() throws OosqlException {
		class TestOne {
			private int some_one;
			@Column(name = {"test_some_one"}, pk = true)
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
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test(expected = TableAnnotationException.class)
	public void testClassNoTableLevelException() throws OosqlException {
		class TestOne {
			private int some_one;
			@Column(name = {"test_some_one"}, pk = true)
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
			@Column(pk = true, name = {"test_three_pk"})
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
	public void testClassNoTableMultiUse() throws OosqlException {
		// FIXME
		class TestOne {
			private int some_one;
			@Column(name = {"test_some_one"}, pk = true)
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
			@Column(pk = true, name = {"test_three_pk"})
			private int test_pk;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String data;
			@Column
			private TestOne this_is_ok;
			@Column(pk = true, name = {"pk_test_two", "pk_test_one"})
			private TestTwo this_is_ok_sec;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test
	public void testSimpleClass() throws OosqlException {
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
	public void testSimpleClassAutoId() throws OosqlException {
		@Table(id = @Column(name = {"id_testOne"}, pk = true))
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
	public void testSimpleClassAutoIdNoPk() throws OosqlException {
		@Table(id = @Column(name = {"id_testOne"}))
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
	public void testClassExtend() throws OosqlException {
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
			@Column(name = {"two_number"})
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = {"two_text"})
			private String text;
			@Column(type = JDBCType.REAL, name = {"two_realNumber"})
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testClassExtendOne() throws OosqlException {
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
			@Column(name = {"two_number"})
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = {"two_text"})
			private String text;
			@Column(type = JDBCType.REAL, name = {"two_realNumber"})
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testClassExtendName() throws OosqlException {
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
			@Column(name = {"two_number"})
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = {"two_text"})
			private String text;
			@Column(type = JDBCType.REAL, name = {"two_realNumber"})
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testClassExtendOneName() throws OosqlException {
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
			@Column(name = {"two_number"})
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25, name = {"two_text"})
			private String text;
			@Column(type = JDBCType.REAL, name = {"two_realNumber"})
			private float realNumber;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testTwoClassOne() throws OosqlException {
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
	public void testTwoClassTwo() throws OosqlException {
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
	public void testThreeClassOne() throws OosqlException {
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
	public void testThreeClassTwo() throws OosqlException {
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
		table.izpis();
	}

	@Test(expected = ColumnAnnotationException.class)
	public void testThreeClassThreeEmptyName() throws OosqlException {
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
			@Column(name = {"tt_1id", "tt_2id", ""}, pk = true)
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test
	public void testThreeClassThree() throws OosqlException {
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
			@Column(name = {"tt_1id", "tt_2id"}, pk = true)
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test
	public void testEnumNewTable() throws OosqlException {
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
	public void testEnumNewTableSetName() throws OosqlException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			@EnumTable(keyColumn = @Column(name = {"id"}), valueColumn = @Column(name = {"value"}))
			private EnumTableTest enumt;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testEnumNewTableSetAllNames() throws OosqlException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			@EnumTable(name = "test_dela", keyColumn = @Column(name = {"id"}), valueColumn = @Column(name = {"value"}))
			private EnumTableTest enumt;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testEnumNewTableNewNamePk() throws OosqlException {
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
			@EnumTable(keyColumn = @Column(name = {"id"}), valueColumn = @Column(name = {"value"}))
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
			@Column(pk = true, name = {"pk_to_number", "pk_enum"})
			private TestOne test_one;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testEnum() throws OosqlException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(type = JDBCType.REAL)
			private float realNumber;
			@Column
			private EnumClassTest enumc;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testClassWithNoTable() throws OosqlException {
		class TestOne {
			@Column(name = {"number_1"}, pk = true)
			private int number;
			@Column(name = {"text_1"}, type = JDBCType.VARCHAR, typeLen = 25)
			private String text;
			@Column(name = {"realNumber_1"}, type = JDBCType.REAL)
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
	public void testArray() throws OosqlException {
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
	public void testArrayClass() throws OosqlException {
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

	@Test
	public void testArrayClassNoTable() throws OosqlException {
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

	@Test
	public void testArrayClassNoTablePk() throws OosqlException {
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
	public void testArrayClassPk() throws OosqlException {
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
	public void testArrayClassCompundKey() throws OosqlException {
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
			@ArrayTable(valueColum = @Column(name = {"pk1", "pk2"}))
			private List<List<List<TestOne>>> list;
			@Column
			@ArrayTable(valueColum = @Column(name = {"pk1", "pk2"}))
			private TestOne[][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test
	public void testArrayClassCompundKeyTwo() throws OosqlException {
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
			@ArrayTable(valueColum = @Column(name = {"pk1", "pk2"}, pk = true))
			private List<List<List<TestOne>>> list;
			@Column(pk = true)
			@ArrayTable(valueColum = @Column(name = {"pk1", "pk2"}, pk = true), dimPrefix = "cord-")
			private TestOne[][][] array;
		}
		TTable table = new TTable(TestTwo.class);
		table.izpis();
	}

	@Test(expected = ColumnAnnotationException.class)
	public void testArrayExceptionOne() throws OosqlException {
		@Table
		class TestOne {
			@Column
			private List<List<List<Integer[][][]>>> special_list;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test(expected = ColumnAnnotationException.class)
	public void testArrayExceptionTwo() throws OosqlException {
		@Table
		class TestOne {
			@Column
			private List<List<List<Integer>>>[][][] special_list;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test(expected = ColumnAnnotationException.class)
	public void testArrayExceptionThree() throws OosqlException {
		@Table
		class TestOne {
			@Column
			private List<List<List<Map<Integer, String>>>> list_map;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test(expected = ColumnAnnotationException.class)
	public void testArrayExceptionFour() throws OosqlException {
		@Table
		class TestOne {
			@Column
			private Map<Integer, String>[][][] array_map;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testMap() throws OosqlException {
		@Table
		class TestOne {
			@Column
			private Map<Integer, String> test_map;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

}