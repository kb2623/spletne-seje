package org.datastruct;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.Comparator;

public class SkipListTest {

	private class IntCmp implements Comparator<Integer> {
		@Override
		public int compare(Integer i1, Integer i2) {
			return i1.intValue() - i2.intValue();
		}
	}

	private SkipList<Integer> list;

	@Before
	public void setUp() {
		list = new SkipList(4, new IntCmp());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(list.isEmpty());
		testAddOne();
		assertFalse(list.isEmpty());
	}

	@Test
	public void testAddOne() {
		assertTrue(list.add(23));
		assertTrue(list.add(90));
		assertTrue(list.add(14));
		assertFalse(list.add(90));
		assertTrue(list.add(34));
		assertTrue(list.add(52));
		assertTrue(list.add(1));
		assertFalse(list.add(23));
	}

	@Test
	public void testSize() {
		testAddOne();
		assertEquals(6, list.size());
	}

	@Test(expected = NullPointerException.class)
	public void testContainsNullPointerException() {
		list.contains(null);
	}

	public void testContaisnFalse() {
		testAddOne();
		assertFalse(list.contains("Dela"));
	}

	@Test
	public void testContains() {
		assertFalse(list.contains(new Integer(1)));
		testAddOne();
		System.out.println(list.izpis() + "\n");
		assertTrue(list.contains(new Integer(1)));
		assertTrue(list.contains(new Integer(23)));
		assertTrue(list.add(100));
		System.out.println(list.izpis() + "\n");
		assertFalse(list.add(100));
		System.out.println(list.izpis() + "\n");
		assertTrue(list.add(35));
		System.out.println(list.izpis() + "\n");
		assertTrue(list.contains(new Integer(100)));
		assertTrue(list.add(66));
		System.out.println(list.izpis() + "\n");
		assertTrue(list.add(33));
		assertTrue(list.add(111));
		System.out.println(list.izpis() + "\n");
	}

	@Test
	public void testContainsAll() {
	}

	@Test
	public void testAddAll() {
	}
}
