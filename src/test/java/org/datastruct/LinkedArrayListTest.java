package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class LinkedArrayListTest {

	private List<Double> myList;
	private List<Double> list;

	@Before
	public void setUp() {
		myList = new LinkedArrayList<>(25);
		list = new ArrayList<>(100);
	}

	@Test
	public void testSize() {
		assertEquals(0, myList.size());
		testAdd(false);
		assertEquals(100, myList.size());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(myList.isEmpty());
		testAdd(false);
		assertFalse(myList.isEmpty());
	}

	@Test
	public void testContains() throws Exception {
		testAdd(true);
		for (int i = 0; i < 100; i++) {
			int randNum = (int) (Math.random() * 100);
			assertEquals(list.get(randNum), myList.get(randNum));
		}
	}

	@Test
	public void testIterator() throws Exception {
		testAdd(true);
		Iterator<Double> itLi = list.iterator();
		Iterator<Double> itMl = myList.iterator();
		while (itLi.hasNext() && itMl.hasNext()) {
			assertEquals(itLi.next(), itMl.next());
		}
		if (itLi.hasNext() || itMl.hasNext()) {
			fail();
		}
		testAdd(true);
		itLi = list.iterator();
		itMl = myList.iterator();
		boolean first = true;
		while (itLi.hasNext() && itMl.hasNext()) {
			if ((int) (Math.random() * 2) == 0 && !first) {
				itLi.remove();
				itMl.remove();
			} else {
				assertEquals(itLi.next(), itMl.next());
				first = false;
			}
		}
		assertEquals(list.size(), myList.size());
		itLi = list.iterator();
		itMl = myList.iterator();
		while (itLi.hasNext() && itMl.hasNext()) {
			assertEquals(itLi.next(), itMl.next());
		}
		if (itLi.hasNext() || itMl.hasNext()) {
			fail();
		}
	}

	@Test
	public void testToArray() throws Exception {

	}

	@Test
	public void testToArray1() throws Exception {

	}

	@Test
	public void testAdd() {
		testAdd(true);
	}

	private void testAdd(boolean test) {
		for (int i = 0; i < 100; i++) {
			double randNum = Math.random() * 100 + 1;
			if (test) {
				list.add(randNum);
			}
			assertTrue(myList.add(randNum));
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
		testAdd(true);
		for (int i = 0; i < 100; i++) {
			assertEquals(list.get(i), myList.get(i));
		}
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