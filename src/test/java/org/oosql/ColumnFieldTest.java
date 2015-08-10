package org.oosql;

import org.junit.Test;
import org.oosql.annotation.ArrayTable;
import org.oosql.annotation.Column;
import org.oosql.annotation.Table;
import org.oosql.exception.OosqlException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ColumnFieldTest {
	@Test
	public void testOne() throws OosqlException, ClassNotFoundException {
		@Table
		class TestOne<E> {
			@Column
			private int some_one;
			@Column(name = {"test_some_one"}, pk = true)
			private double some_two;
			@Column
			private String test_text;
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
//			@Column
//			private Map<Integer[], String[]> map_one;
		}
		for (Field f : Util.getColumnFields(TestOne.class)) {
			CField cf;
			if (f.getType().isArray() || Collection.class.isAssignableFrom(f.getType())) {
				cf = new CFieldArray(f);
			} else if (Map.class.isAssignableFrom(f.getType())) {
				cf = new CFieldMap(f);
			} else {
				cf = new CField(f);
			}
			System.out.println(cf.toString());
		}
	}

	@Test
	public void testTwo() throws ClassNotFoundException {
		int[] dim = {0,0,0};
		System.out.println(int[][][].class.getName());
		System.out.println(Array.newInstance(int.class, dim).getClass().getName());
		Class.forName("java.util.List");
	}
}
