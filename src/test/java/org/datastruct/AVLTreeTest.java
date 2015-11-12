package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AVLTreeTest {

	private AVLTree<Integer, Double> tree;

	@Before
	public void setUp() throws Exception {
		tree = new AVLTree<>();
	}

	@After
	public void tearDown() throws Exception {
		assertFalse(tree.isEmpty());
		tree.clear();
		assertTrue(tree.isEmpty());
	}

	@Test
	public void testSize() throws Exception {

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

	@Test
	public void testPut() throws Exception {
		assertNull(tree.put(55, 54.32));
		assertNull(tree.put(25, 66.32));
		assertNull(tree.put(45, 90.99));
		assertNull(tree.put(34, 54.34));
		assertEquals(4, tree.size());
		assertNull(tree.put(78, 89.43));
		assertNull(tree.put(88, 45.12));
		assertEquals(6, tree.size());
		assertNull(tree.put(46, 65.09));
		assertNull(tree.put(66, 88.33));
		assertNull(tree.put(70, 70.89));
		assertNull(tree.put(99, 99D));
		assertNull(tree.put(85, 86.23));
		assertEquals(11, tree.size());
		assertEquals(new Double(90.99), tree.remove(45));
		assertEquals(10, tree.size());
		assertEquals(new Double(88.33), tree.remove(66));
		assertEquals(9, tree.size());
	}

	@Test
	public void testRemove() throws Exception {

	}

	@Test
	public void testPutAll() throws Exception {

	}

	@Test
	public void testKeySet() throws Exception {

	}

	@Test
	public void testValues() throws Exception {

	}

	@Test
	public void testEntrySet() throws Exception {

	}
}