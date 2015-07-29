package org.oosql.tree;

import org.junit.Test;

import org.oosql.annotation.Table;
import org.oosql.annotation.Column;
import org.oosql.exception.OosqlException;
import org.oosql.exception.TableAnnotationException;
import org.oosql.exception.ColumnAnnotationException;

import java.sql.JDBCType;

public class TTableTest {

	@Table
	private enum EnumTable {
		TT1, TT2, TT3
	}

	private enum EnumClass {
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo extends TestOne {
			@Column(name = {"two_number"})
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25, name = {"two_text"})
			private String text;
			@Column(dataType = JDBCType.REAL, name = {"two_realNumber"})
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		class TestTwo extends TestOne {
			@Column(name = {"two_number"})
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25, name = {"two_text"})
			private String text;
			@Column(dataType = JDBCType.REAL, name = {"two_realNumber"})
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table(name = "test")
		class TestTwo extends TestOne {
			@Column(name = {"two_number"})
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25, name = {"two_text"})
			private String text;
			@Column(dataType = JDBCType.REAL, name = {"two_realNumber"})
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		class TestTwo extends TestOne {
			@Column(name = {"two_number"})
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25, name = {"two_text"})
			private String text;
			@Column(dataType = JDBCType.REAL, name = {"two_realNumber"})
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
			@Column
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
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
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
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
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
			@Column
			private EnumTable enumt;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testEnum() throws OosqlException {
		@Table
		class TestOne {
			@Column(pk = true)
			private int number;
			@Column(dataType = JDBCType.VARCHAR, lengthType = 25)
			private String text;
			@Column(dataType = JDBCType.REAL)
			private float realNumber;
			@Column
			private EnumClass enumc;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

}