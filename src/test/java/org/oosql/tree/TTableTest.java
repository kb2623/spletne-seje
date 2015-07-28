package org.oosql.tree;

import org.junit.Test;

import org.oosql.annotation.Table;
import org.oosql.annotation.Column;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.OosqlException;

public class TTableTest {

	@Test
	public void testSimpleClass() throws OosqlException {
		@Table
		class TestOne {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
		}
		TTable table = new TTable(TestOne.class);
		table.izpis();
	}

	@Test
	public void testTwoClassOne() throws OosqlException {
		@Table
		class TestOne {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
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
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
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
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
			@Column
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
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
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
			@Column(pk = true)
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

	@Test
	public void testThreeClassThree() throws OosqlException {
		@Table
		class TestOne {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
		}
		@Table
		class TestTwo {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
		}
		@Table
		class TestThree {
			@Column(pk = true, dataType = "INTEGER")
			private int number;
			@Column(dataType = "TEXT")
			private String text;
			@Column(dataType = "REAL")
			private float realNumber;
			@Column(pk = true)
			private TestOne classOne;
			@Column(name = {"tt_1id","tt_2id"}, pk = true)
			private TestTwo classTwo;
		}
		TTable table = new TTable(TestThree.class);
		table.izpis();
	}

}