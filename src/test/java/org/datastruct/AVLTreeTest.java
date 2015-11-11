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