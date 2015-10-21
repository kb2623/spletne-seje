package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ArrayMapTest {

	private Map<Integer, Integer> mapNoLimits;
	private Map<Integer, Integer> mapWithLimits;

	@Before
	public void setUp() {
		mapNoLimits = new ArrayMap<>(10, .5f);
		mapWithLimits = new ArrayMap<>(10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitOneArgZero() {
		new ArrayMap<>(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitOneArgNegative() {
		new ArrayMap<>(-10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsZero() {
		new ArrayMap<>(0, 2f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsNegative() {
		new ArrayMap<>(-12, 2f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsZeroTwo() {
		new ArrayMap<>(100, 0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsNegativeTwo() {
		new ArrayMap<>(200, -.34f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsOverTwo() {
		new ArrayMap<>(20, 1.01f);
	}

	@Test
	public void testSize() throws Exception {
		mapWithLimits.put(1, 1);
		assertEquals(1, mapWithLimits.size());
		mapWithLimits.put(2, 2);
		assertEquals(2, mapWithLimits.size());
		mapWithLimits.put(3, 3);
		assertEquals(3, mapWithLimits.size());
		mapWithLimits.put(4, 4);
		assertEquals(4, mapWithLimits.size());
		mapWithLimits.put(5, 5);
		assertEquals(5, mapWithLimits.size());
		mapWithLimits.put(5, 5);
		assertEquals(5, mapWithLimits.size());
		mapWithLimits.put(6, 6);
		assertEquals(6, mapWithLimits.size());
		mapWithLimits.put(7, 7);
		assertEquals(7, mapWithLimits.size());
		mapWithLimits.put(5, 5);
		assertEquals(7, mapWithLimits.size());
		mapWithLimits.put(8, 8);
		assertEquals(8, mapWithLimits.size());
		mapWithLimits.put(9, 9);
		assertEquals(9, mapWithLimits.size());
		mapWithLimits.put(10, 10);
		assertEquals(10, mapWithLimits.size());
		mapWithLimits.clear();
		assertEquals(0, mapWithLimits.size());
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue(mapWithLimits.isEmpty());
		testSize();
		assertTrue(mapWithLimits.isEmpty());
		mapWithLimits.put(1, 1);
		assertFalse(mapWithLimits.isEmpty());
		mapWithLimits.put(1, 1);
		assertFalse(mapWithLimits.isEmpty());
		mapWithLimits.put(1, 1);
		assertFalse(mapWithLimits.isEmpty());
		mapWithLimits.clear();
		assertTrue(mapWithLimits.isEmpty());
	}

	@Test
	public void testContainsKey() throws Exception {

	}

	@Test
	public void testContainsValue() throws Exception {

	}

	@Test
	public void testGet() throws Exception {

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutLimits() {
		assertNull(mapWithLimits.put(1, 1));
		assertNull(mapWithLimits.put(2, 2));
		assertNull(mapWithLimits.put(3, 3));
		assertNull(mapWithLimits.put(4, 4));
		assertNull(mapWithLimits.put(5, 5));
		assertNull(mapWithLimits.put(6, 6));
		assertNull(mapWithLimits.put(7, 7));
		assertNull(mapWithLimits.put(8, 8));
		assertNull(mapWithLimits.put(9, 9));
		assertNull(mapWithLimits.put(10, 10));
		assertEquals(10, mapWithLimits.size());
		assertEquals(new Integer(1), mapWithLimits.put(1, 3));
		assertEquals(new Integer(5), mapWithLimits.put(5, 20));
		assertEquals(new Integer(10), mapWithLimits.put(10, 100));
		assertEquals(10, mapWithLimits.size());
		assertEquals(new Integer(3), mapWithLimits.put(1, 1));
		assertEquals(new Integer(20), mapWithLimits.put(5, 5));
		assertEquals(new Integer(100), mapWithLimits.put(10, 10));
		mapWithLimits.put(11, 11);
	}

	@Test
	public void testPutNoLimits() {
		assertNull(mapNoLimits.put(1, 1));
		assertNull(mapNoLimits.put(2, 2));
		assertNull(mapNoLimits.put(3, 3));
		assertNull(mapNoLimits.put(4, 4));
		assertNull(mapNoLimits.put(5, 5));
		assertNull(mapNoLimits.put(6, 6));
		assertNull(mapNoLimits.put(7, 7));
		assertNull(mapNoLimits.put(8, 8));
		assertNull(mapNoLimits.put(9, 9));
		assertNull(mapNoLimits.put(10, 10));
		assertEquals(10, mapNoLimits.size());
		assertEquals(new Integer(1), mapNoLimits.put(1, 3));
		assertEquals(new Integer(5), mapNoLimits.put(5, 20));
		assertEquals(new Integer(10), mapNoLimits.put(10, 100));
		assertEquals(10, mapNoLimits.size());
		assertEquals(new Integer(3), mapNoLimits.put(1, 1));
		assertEquals(new Integer(20), mapNoLimits.put(5, 5));
		assertEquals(new Integer(100), mapNoLimits.put(10, 10));
		assertNull(mapNoLimits.put(11, 11));
		assertNull(mapNoLimits.put(12, 12));
		assertNull(mapNoLimits.put(13, 13));
		assertNull(mapNoLimits.put(14, 14));
		assertNull(mapNoLimits.put(15, 15));
		assertEquals(15, mapNoLimits.size());
	}

	@Test
	public void testRemove() throws Exception {

	}

	@Test
	public void testPutAll() throws Exception {

	}

	@Test
	public void testClear() throws Exception {
		assertTrue(mapNoLimits.isEmpty());
		testPutNoLimits();
		mapNoLimits.clear();
		assertTrue(mapNoLimits.isEmpty());
	}

	@Test
	public void testKeySet() throws Exception {

	}

	@Test
	public void testValues() throws Exception {
		testPutNoLimits();
		Collection<Integer> c = mapNoLimits.values();
		assertTrue(c.contains(new Integer(1)));
		assertTrue(c.contains(new Integer(15)));
		assertTrue(c.contains(new Integer(3)));
		assertFalse(c.contains(new Integer(20)));
	}

	@Test
	public void testEntrySet() throws Exception {
		testPutNoLimits();
		Set<Map.Entry<Integer, Integer>> set = mapNoLimits.entrySet();
		// TODO
	}
}