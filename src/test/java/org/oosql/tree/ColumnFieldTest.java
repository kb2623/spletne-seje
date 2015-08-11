package org.oosql.tree;

import org.junit.Test;
import org.oosql.annotation.ArrayTable;
import org.oosql.annotation.Column;
import org.oosql.annotation.MapTable;
import org.oosql.annotation.Table;
import org.oosql.exception.OosqlException;

import java.util.List;
import java.util.Map;

public class ColumnFieldTest {

	@Test
	public void testSimpleClass() throws OosqlException, ClassNotFoundException {
		class myGen<T> {
			@Column
			T type;
		}
		@Table
		class TestOne {
			@Column
			private int some_one;
			@Column(name = {"test_some_one"}, pk = true)
			private double some_two;
			@Column
			private String test_text;
			@Column
			private myGen<Integer> generic;
		}
		for (CField cField : Util.getColumnFields(TestOne.class)) System.out.println(cField.toString() + "\n");
	}

	@Test
	public void testArray() throws OosqlException, ClassNotFoundException {
		class myGen<T> {
			@Column
			T type;
		}
		@Table
		class TestOne {
			@Column
			@ArrayTable
			private myGen<Integer>[][] generic_array;
			@Column
			@MapTable
			@ArrayTable
			@ArrayTable
			private Map<myGen<String>[][], myGen<Integer>[]> generic_map;
			@Column
			@ArrayTable
			private int[][][] array_one;
			@Column
			@ArrayTable
			private Integer[][][] array_one_object;
			@Column
			@ArrayTable
			private List<List<List<Integer>>> array_two;
			@Column
			@ArrayTable
			@ArrayTable
			private List<List<List<Integer[][][]>>> array_three;
			@Column
			@ArrayTable
			@ArrayTable
			@ArrayTable
			@ArrayTable
			private List<List<List<List<List<Integer>>[][]>>>[][][] array_four;
			@Column
			@ArrayTable
			@ArrayTable
			private List<List<List<Integer>>>[][][] array_five;
		}
		for (CField cField : Util.getColumnFields(TestOne.class)) System.out.println(cField.toString() + "\n");
	}

	@Test
	public void testMap() throws ClassNotFoundException, OosqlException {
		class myGen<T> {
			@Column
			T type;
		}
		@Table
		class TestOne {
			@Column
			@MapTable
			@ArrayTable
			@ArrayTable
			private Map<Integer[], String[]> map_one;
			@Column
			@MapTable
			@ArrayTable
			@ArrayTable
			@ArrayTable
			@ArrayTable
			private Map<List<Integer[]>, List<String[]>> map_list;
			@Column
			@MapTable
			private Map<Integer, String> simple_map;
			@Column
			@MapTable
			@MapTable
			private Map<Map<Integer, String>, String> map_two;
			@Column
			@MapTable
			@MapTable
			@MapTable
			private Map<Map<myGen<Integer>, myGen<Integer>>, Map<Integer, Integer>> map_three;
		}
		for (CField cField : Util.getColumnFields(TestOne.class)) System.out.println(cField.toString() + "\n");
	}
}
