package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LinkedArrayListTest {

	private List<Double> myList;
	private List<Double> list;

	@Before
	public void setUp() {
		myList = new LinkedArrayList<>(25);
		list = new ArrayList<>();
	}

	@Test
	public void testSize() {
		assertEquals(0, myList.size());
		testAdd();
		assertEquals(100, myList.size());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(myList.isEmpty());
		testAdd();
		assertFalse(myList.isEmpty());
	}

	@Test
	public void testContains() throws Exception {

	}

	@Test
	public void testIterator() throws Exception {

	}

	@Test
	public void testToArray() throws Exception {

	}

	@Test
	public void testToArray1() throws Exception {

	}

	@Test
	public void testAdd() {
		for (int i = 0; i < 100; i++) {
			double randNum = Math.random() * 100 + 1;
			list.add(randNum);
			assertTrue(myList.add(randNum));
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(list.get(i), myList.get(i));
		}
	}

	@Test
	public void testRemove() throws Exception {

	}

	@Test
	public void testContainsAll() throws Exception {

	}

	@Test
	public void testAddAll() throws Exception {

	}

	@Test
	public void testAddAll1() throws Exception {

	}

	@Test
	public void testRemoveAll() throws Exception {

	}

	@Test
	public void testRetainAll() throws Exception {

	}

	@Test
	public void testClear() throws Exception {

	}

	@Test
	public void testGet() throws Exception {

	}

	@Test
	public void testSet() throws Exception {

	}

	@Test
	public void testAdd1() throws Exception {

	}

	@Test
	public void testRemove1() throws Exception {

	}

	@Test
	public void testIndexOf() throws Exception {

	}

	@Test
	public void testLastIndexOf() throws Exception {

	}

	@Test
	public void testListIterator() throws Exception {

	}

	@Test
	public void testListIterator1() throws Exception {

	}

	@Test
	public void testSubList() throws Exception {

	}
}