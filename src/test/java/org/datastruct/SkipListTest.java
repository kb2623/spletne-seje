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
		assertTrue(list.contains(new Integer(1)));
		assertTrue(list.contains(new Integer(23)));
		assertTrue(list.add(100));
		assertFalse(list.add(100));
		assertTrue(list.add(35));
		assertTrue(list.contains(new Integer(100)));
		assertTrue(list.add(66));
		assertTrue(list.add(33));
		assertTrue(list.add(111));
	}

	@Test
	public void testContainsAll() {
	}

	@Test
	public void testAddAll() {
	}

	@Test
	public void testRemove() {
		testAddOne();
		assertFalse(list.remove(35));
		assertEquals(6, list.size());
		assertTrue(list.remove(34));
		assertEquals(5, list.size());
		assertFalse(list.remove(34));
		assertEquals(5, list.size());
		assertTrue(list.add(34));
		assertEquals(6, list.size());
		assertTrue(list.remove(34));
		assertEquals(5, list.size());
		assertFalse(list.remove(34));
		assertEquals(5, list.size());
	}
}
