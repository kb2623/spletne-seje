package org.oosql.tree;

import org.junit.Test;

import org.oosql.annotation.EnumTable;
import org.oosql.annotation.Table;
import org.oosql.annotation.Column;
import org.oosql.exception.OosqlException;
import org.oosql.exception.TableAnnotationException;
import org.oosql.exception.ColumnAnnotationException;

import java.sql.JDBCType;
import java.util.List;

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
	public void testEnumNewTableNewName() throws OosqlException {
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
	public void testArray() throws OosqlException {
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
			@Column
			private EnumTableTest enumt;
			@Column
			private int[][][] int_table;
			@Column
			private List<List<List<Integer>>> list;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

}