package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AVLTreeTest {

	private Map<Integer, Integer> map;
	private Map<Integer, Integer> tmap;

	private int size = 50;

	@Before
	public void setUp() throws Exception {
		Compare cmp = new Compare();
		map = new AVLTree<>(cmp);
		tmap = new TreeMap<>(cmp);
		assertTrue(map.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		assertFalse(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testPut() throws Exception {
		try {
			map.put(null, 23);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
	}

	private void testPut(boolean test) {
		for (int i = 0; i < size; i++) {
			int rNumK = (int) (Math.random() * (size + 1) + 1);
			int rNumV = (int) (Math.random() * (size + 1000) + 1);
			System.out.println(rNumK);
			if (test) {
				assertEquals(tmap.put(rNumK, rNumV), map.put(rNumK, rNumV));
			} else {
				map.put(rNumK, rNumV);
			}
		}
		if (test) {
			assertEquals(tmap.size(), map.size());
			assertFalse(map.isEmpty());
			assertEquals(tmap.toString(), map.toString());
		}
	}

	@Test
	public void testKeySet() throws Exception {
		testKeySetWithIterator();
		testPut(true);
		testKeySetWithIterator();
	}

	private void testKeySetWithIterator() {
		Iterator<Integer> itM = map.keySet().iterator();
		Iterator<Integer> itT = tmap.keySet().iterator();
		while (itM.hasNext() && itT.hasNext()) {
			assertEquals(itT.next(), itM.next());
		}
		if (itM.hasNext() || itT.hasNext()) {
			fail();
		}
	}

	@Test
	public void testValues() throws Exception {
		testValuesWithIterator();
		testPut(true);
		testValuesWithIterator();
	}

	private void testValuesWithIterator() {
		Iterator<Integer> itM = map.values().iterator();
		Iterator<Integer> itT = tmap.values().iterator();
		while (itM.hasNext() && itT.hasNext()) {
			assertEquals(itT.next(), itM.next());
		}
		if (itM.hasNext() || itT.hasNext()) {
			fail();
		}
	}

	@Test
	public void testEntrySet() throws Exception {
		testEntrySetWithIterator();
		testPut(true);
		testEntrySetWithIterator();
	}

	private void testEntrySetWithIterator() {
		Iterator<Map.Entry<Integer, Integer>> itM = map.entrySet().iterator();
		Iterator<Map.Entry<Integer, Integer>> itT = tmap.entrySet().iterator();
		while (itM.hasNext() && itT.hasNext()) {
			Map.Entry<Integer, Integer> eM = itM.next();
			Map.Entry<Integer, Integer> eT = itT.next();
			assertEquals(eT.getKey(), eM.getKey());
			assertEquals(eT.getValue(), eM.getValue());
		}
		if (itM.hasNext() || itT.hasNext()) {
			fail();
		}
	}

	@Test
	public void testContainsKey() throws Exception {
		try {
			map.containsKey(null);
			fail();
		} catch (NullPointerException e) {
		}
		int key = (int) (Math.random() * 51 + 100);
		assertFalse(map.containsKey(key));
		testPut(true);
		for (int i = 0; i < size; i++) {
			key = (int) (Math.random() * (size + 1) + 1);
			try {
				assertEquals(tmap.containsKey(key), map.containsKey(key));
			} catch (AssertionError e) {
				throw new AssertionError("Can not find key = " + key + "\n" + tmap.toString() + "\n" + map.toString(), e);
			}
		}
	}

	@Test
	public void testContainsValue() throws Exception {
		int value = (int) (Math.random() * 11 + 100);
		assertFalse(map.containsValue(value));
		testPut(true);
		for (int i = 0; i < size + 200; i++) {
			value = (int) (Math.random() * (size + 2001) + 1);
			assertEquals(tmap.containsValue(value), map.containsValue(value));
		}
	}

	@Test
	public void testGet() throws Exception {
		try {
			map.get(null);
			fail();
		} catch (NullPointerException e) {
		}
		int key = (int) (Math.random() * (size + 1) + 1);
		assertNull(map.get(key));
		testPut(true);
		for (int i = 0; i < size; i++) {
			key = (int) (Math.random() * (size + 1) + 1);
			assertEquals(tmap.get(key), map.get(key));
		}
	}

	@Test
	public void testRemove() throws Exception {
		try {
			map.remove(null);
			fail();
		} catch (NullPointerException e) {
		}
		int key = (int) (Math.random() * 201 + 1);
		assertNull(map.remove(key));
		testPut(true);
		for (int i = 0; i < size; i++) {
			key = (int) (Math.random() * (size + 1) + 1);
			try {
				assertEquals(tmap.remove(key), map.remove(key));
			} catch (AssertionError e) {
				String s = "Error at ramoveing key = " + key + "\n" +
						tmap.toString() + "\n" +
						map.toString();
				throw new AssertionError(s, e);
			}
		}
		assertEquals(tmap.toString(), map.toString());
	}

	@Test
	public void testPutAll() throws Exception {
		Map<Integer, Integer> nMap = new HashMap<>();
		for (int i = 0; i < 50; i++) {
			int key = (int) (Math.random() * (size + 200 + 1) + 1);
			int value = (int) (Math.random() * 2001 + 1);
			nMap.put(key, value);
		}
		map.putAll(nMap);
		tmap.putAll(nMap);
		assertEquals(tmap.toString(), map.toString());
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(tmap.size(), map.size());
		testPut(true);
		assertEquals(tmap.size(), map.size());
		map.clear();
		tmap.clear();
		assertEquals(tmap.size(), map.size());
		map.put(23, 23);
	}

	@Test
	public void testMixWith() throws NoSuchFieldException, IllegalAccessException {
		int i = 0;
		System.out.println(((AVLTree) map).printTree());

		assertNull(map.put(46, ++i));
		System.out.println(((AVLTree) map).printTree());

		assertNull(map.put(19, ++i));
		System.out.println(((AVLTree) map).printTree());

		assertNull(map.put(37, ++i));
		System.out.println(((AVLTree) map).printTree());

		assertNull(map.put(9, ++i));
		System.out.println(((AVLTree) map).printTree());

		assertNull(map.put(20, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 37, 3);
		// Level 1
		testAVLEntry(root.lower, 19, 2);
		testAVLEntry(root.higher, 46, 1);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 1);
		testAVLEntry(root.lower.higher, 20, 1);
		assertNull(root.higher.lower);
		assertNull(root.higher.higher);

		assertNull(map.put(24, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 20, 3);
		// Level 1
		testAVLEntry(root.lower, 19, 2);
		testAVLEntry(root.higher, 37, 2);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 1);
		assertNull(root.lower.higher);
		testAVLEntry(root.higher.lower, 24, 1);
		testAVLEntry(root.higher.higher, 46, 1);
		// Level 3
		assertNull(root.lower.lower.lower);
		assertNull(root.lower.lower.higher);
		assertNull(root.higher.lower.lower);
		assertNull(root.higher.lower.higher);
		assertNull(root.higher.higher.lower);
		assertNull(root.higher.higher.higher);

		assertNull(map.put(23, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 20, 4);
		// Level 1
		testAVLEntry(root.lower, 19, 2);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 1);
		assertNull(root.lower.higher);
		testAVLEntry(root.higher.lower, 24, 2);
		testAVLEntry(root.higher.higher, 46, 1);
		// Level 3
		assertNull(root.lower.lower.lower);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.higher.lower.lower, 23, 1);
		assertNull(root.higher.lower.higher);
		assertNull(root.higher.higher.lower);
		assertNull(root.higher.higher.higher);

		assertNull(map.put(41, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 20, 4);
		// Level 1
		testAVLEntry(root.lower, 19, 2);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 1);
		assertNull(root.lower.higher);
		testAVLEntry(root.higher.lower, 24, 2);
		testAVLEntry(root.higher.higher, 46, 2);
		// Level 3
		assertNull(root.lower.lower.lower);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.higher.lower.lower, 23, 1);
		assertNull(root.higher.lower.higher);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		assertNull(root.higher.higher.higher);

		assertNull(map.put(21, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 20, 4);
		// Level 1
		testAVLEntry(root.lower, 19, 2);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 1);
		assertNull(root.lower.higher);
		testAVLEntry(root.higher.lower, 23, 2);
		testAVLEntry(root.higher.higher, 46, 2);
		// Level 3
		assertNull(root.lower.lower.lower);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.higher.lower.lower, 21, 1);
		testAVLEntry(root.higher.lower.higher, 24, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		assertNull(root.higher.higher.higher);

		assertNull(map.put(42, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 20, 4);
		// Level 1
		testAVLEntry(root.lower, 19, 2);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 1);
		assertNull(root.lower.higher);
		testAVLEntry(root.higher.lower, 23, 2);
		testAVLEntry(root.higher.higher, 42, 2);
		// Level 3
		assertNull(root.lower.lower.lower);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.higher.lower.lower, 21, 1);
		testAVLEntry(root.higher.lower.higher, 24, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 1);

		assertNull(map.put(13, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 20, 4);
		// Level 1
		testAVLEntry(root.lower, 13, 2);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 1);
		testAVLEntry(root.lower.higher, 19, 1);
		testAVLEntry(root.higher.lower, 23, 2);
		testAVLEntry(root.higher.higher, 42, 2);
		// Level 3
		assertNull(root.lower.lower.lower);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.higher.lower.lower, 21, 1);
		testAVLEntry(root.higher.lower.higher, 24, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 1);

		assertNull(map.put(36, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 23, 4);
		// Level 1
		testAVLEntry(root.lower, 20, 3);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 13, 2);
		testAVLEntry(root.lower.higher, 21, 1);
		testAVLEntry(root.higher.lower, 24, 2);
		testAVLEntry(root.higher.higher, 42, 2);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 9, 1);
		testAVLEntry(root.lower.lower.higher, 19, 1);
		assertNull(root.lower.higher.lower);
		assertNull(root.lower.higher.higher);
		assertNull(root.higher.lower.lower);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 1);

		assertNull(map.put(29, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 23, 4);
		// Level 1
		testAVLEntry(root.lower, 20, 3);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 13, 2);
		testAVLEntry(root.lower.higher, 21, 1);
		testAVLEntry(root.higher.lower, 29, 2);
		testAVLEntry(root.higher.higher, 42, 2);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 9, 1);
		testAVLEntry(root.lower.lower.higher, 19, 1);
		assertNull(root.lower.higher.lower);
		assertNull(root.lower.higher.higher);
		testAVLEntry(root.higher.lower.lower, 24, 1);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 1);

		assertNull(map.put(7, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 23, 4);
		// Level 1
		testAVLEntry(root.lower, 13, 3);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 2);
		testAVLEntry(root.lower.higher, 20, 2);
		testAVLEntry(root.higher.lower, 29, 2);
		testAVLEntry(root.higher.higher, 42, 2);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 7, 1);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower, 19, 1);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 1);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 1);

		assertNull(map.put(15, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Level 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 3);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 2);
		testAVLEntry(root.higher.higher, 42, 2);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 7, 1);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 1);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 1);
		// Level 4
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);

		assertNull(map.put(50, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 4);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 2);
		testAVLEntry(root.higher.higher, 42, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 7, 1);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 1);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		assertNull(root.higher.lower.lower.higher);
		assertNull(root.higher.lower.higher.lower);
		assertNull(root.higher.lower.higher.higher);
		assertNull(root.higher.higher.lower.lower);
		assertNull(root.higher.higher.lower.higher);
		assertNull(root.higher.higher.higher.lower);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);

		assertNull(map.put(44, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 4);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 2);
		testAVLEntry(root.higher.higher, 42, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 7, 1);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 1);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		assertNull(root.higher.lower.lower.higher);
		assertNull(root.higher.lower.higher.lower);
		assertNull(root.higher.lower.higher.higher);
		assertNull(root.higher.higher.lower.lower);
		assertNull(root.higher.higher.lower.higher);
		testAVLEntry(root.higher.higher.higher.lower, 44, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);

		assertNull(map.put(27, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 4);
		// Level 2
		testAVLEntry(root.lower.lower, 9, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 3);
		testAVLEntry(root.higher.higher, 42, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 7, 1);
		assertNull(root.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		assertNull(root.higher.lower.higher.lower);
		assertNull(root.higher.lower.higher.higher);
		assertNull(root.higher.higher.lower.lower);
		assertNull(root.higher.higher.lower.higher);
		testAVLEntry(root.higher.higher.higher.lower, 44, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);

		assertNull(map.put(2, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 4);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 3);
		testAVLEntry(root.higher.higher, 42, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		assertNull(root.higher.lower.higher.lower);
		assertNull(root.higher.lower.higher.higher);
		assertNull(root.higher.higher.lower.lower);
		assertNull(root.higher.higher.lower.higher);
		testAVLEntry(root.higher.higher.higher.lower, 44, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);

		assertNull(map.put(32, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 4);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 3);
		testAVLEntry(root.higher.higher, 42, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower, 41, 1);
		testAVLEntry(root.higher.higher.higher, 46, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		assertNull(root.higher.lower.higher.higher);
		assertNull(root.higher.higher.lower.lower);
		assertNull(root.higher.higher.lower.higher);
		testAVLEntry(root.higher.higher.higher.lower, 44, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);

		assertNull(map.put(47, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 4);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 3);
		testAVLEntry(root.higher.higher, 46, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower, 42, 2);
		testAVLEntry(root.higher.higher.higher, 50, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		assertNull(root.higher.lower.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		assertNull(root.higher.higher.higher.higher);

		assertNull(map.put(34, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 5);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 4);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 3);
		testAVLEntry(root.higher.higher, 46, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 2);
		testAVLEntry(root.higher.higher.lower, 42, 2);
		testAVLEntry(root.higher.higher.higher, 50, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 1);
		testAVLEntry(root.higher.higher.lower.lower, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		assertNull(root.higher.higher.higher.higher);

		assertNull(map.put(35, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 2);
		testAVLEntry(root.higher.higher.higher, 50, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		assertNull(root.higher.higher.higher.higher);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		assertNull(root.higher.higher.lower.lower.lower);
		assertNull(root.higher.higher.lower.lower.higher);
		assertNull(root.higher.higher.lower.higher.lower);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(49, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 3);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 2);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		assertNull(root.higher.higher.lower.lower.lower);
		assertNull(root.higher.higher.lower.lower.higher);
		assertNull(root.higher.higher.lower.higher.lower);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(38, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 2);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		assertNull(root.higher.higher.lower.higher.lower);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(5, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 1);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		assertNull(root.higher.higher.lower.higher.lower);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(8, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		assertNull(root.higher.higher.lower.higher.lower);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(10, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.lower.higher.higher, 10, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 1);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		assertNull(root.higher.higher.lower.higher.lower);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(43, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 19, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.lower.higher.higher, 10, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		assertNull(root.lower.higher.lower.higher);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(18, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 20, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 18, 2);
		testAVLEntry(root.lower.higher.higher, 21, 1);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.lower.higher.higher, 10, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		testAVLEntry(root.lower.higher.lower.higher, 19, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.lower.higher.lower.lower.lower);
		assertNull(root.lower.higher.lower.lower.higher);
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(16, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 18, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 15, 2);
		testAVLEntry(root.lower.higher.higher, 20, 2);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.lower.higher.higher, 10, 1);
		assertNull(root.lower.higher.lower.lower);
		testAVLEntry(root.lower.higher.lower.higher, 16, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(17, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 18, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 16, 2);
		testAVLEntry(root.lower.higher.higher, 20, 2);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.lower.higher.higher, 10, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		testAVLEntry(root.lower.higher.lower.higher, 17, 1);
		testAVLEntry(root.lower.higher.higher.lower, 19, 1);
		testAVLEntry(root.lower.higher.higher.higher, 21, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 41, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		assertNull(root.higher.higher.lower.lower.higher);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(40, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 18, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 16, 2);
		testAVLEntry(root.lower.higher.higher, 20, 2);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.lower.higher.higher, 10, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		testAVLEntry(root.lower.higher.lower.higher, 17, 1);
		testAVLEntry(root.lower.higher.higher.lower, 19, 1);
		testAVLEntry(root.lower.higher.higher.higher, 21, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 40, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		testAVLEntry(root.higher.higher.lower.lower.higher, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);

		assertNull(map.put(3, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 13, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 18, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 3, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 16, 2);
		testAVLEntry(root.lower.higher.higher, 20, 2);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		testAVLEntry(root.lower.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		testAVLEntry(root.lower.lower.higher.higher, 10, 1);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		testAVLEntry(root.lower.higher.lower.higher, 17, 1);
		testAVLEntry(root.lower.higher.higher.lower, 19, 1);
		testAVLEntry(root.lower.higher.higher.higher, 21, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 40, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		testAVLEntry(root.higher.higher.lower.lower.higher, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);
		System.out.println(((AVLTree) map).printTree());
		/** brisanje z iskanjem najmanjsega lista */
		assertEquals(new Integer(11), map.remove(13));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 10, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 18, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 3, 2);
		testAVLEntry(root.lower.lower.higher, 9, 2);
		testAVLEntry(root.lower.higher.lower, 16, 2);
		testAVLEntry(root.lower.higher.higher, 20, 2);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		testAVLEntry(root.lower.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		testAVLEntry(root.lower.lower.higher.lower, 8, 1);
		assertNull(root.lower.lower.higher.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		testAVLEntry(root.lower.higher.lower.higher, 17, 1);
		testAVLEntry(root.lower.higher.higher.lower, 19, 1);
		testAVLEntry(root.lower.higher.higher.higher, 21, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 40, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		testAVLEntry(root.higher.higher.lower.lower.higher, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);
		/** brisanje z iskanjem najmanjsega, ki ni list */
		assertEquals(new Integer(28), map.remove(10));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 9, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 7, 3);
		testAVLEntry(root.lower.higher, 18, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 3, 2);
		testAVLEntry(root.lower.lower.higher, 8, 1);
		testAVLEntry(root.lower.higher.lower, 16, 2);
		testAVLEntry(root.lower.higher.higher, 20, 2);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		testAVLEntry(root.lower.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.lower.higher, 5, 1);
		assertNull(root.lower.lower.higher.lower);
		assertNull(root.lower.lower.higher.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		testAVLEntry(root.lower.higher.lower.higher, 17, 1);
		testAVLEntry(root.lower.higher.higher.lower, 19, 1);
		testAVLEntry(root.lower.higher.higher.higher, 21, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 40, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		testAVLEntry(root.higher.higher.lower.lower.higher, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);
		/** brisanje desnega lista */
		assertEquals(new Integer(27), map.remove(8));
		root = (AVLTree.AVLEntry) rootField.get(map);
		// Lebel 0
		testAVLEntry(root, 23, 6);
		// Level 1
		testAVLEntry(root.lower, 9, 4);
		testAVLEntry(root.higher, 37, 5);
		// Level 2
		testAVLEntry(root.lower.lower, 3, 3);
		testAVLEntry(root.lower.higher, 18, 3);
		testAVLEntry(root.higher.lower, 29, 4);
		testAVLEntry(root.higher.higher, 46, 4);
		// Level 3
		testAVLEntry(root.lower.lower.lower, 2, 1);
		testAVLEntry(root.lower.lower.higher, 7, 2);
		testAVLEntry(root.lower.higher.lower, 16, 2);
		testAVLEntry(root.lower.higher.higher, 20, 2);
		testAVLEntry(root.higher.lower.lower, 24, 2);
		testAVLEntry(root.higher.lower.higher, 34, 3);
		testAVLEntry(root.higher.higher.lower, 42, 3);
		testAVLEntry(root.higher.higher.higher, 49, 2);
		// Level 4
		assertNull(root.lower.lower.lower.lower);
		assertNull(root.lower.lower.lower.higher);
		testAVLEntry(root.lower.lower.higher.lower, 5, 1);
		assertNull(root.lower.lower.higher.higher);
		testAVLEntry(root.lower.higher.lower.lower, 15, 1);
		testAVLEntry(root.lower.higher.lower.higher, 17, 1);
		testAVLEntry(root.lower.higher.higher.lower, 19, 1);
		testAVLEntry(root.lower.higher.higher.higher, 21, 1);
		assertNull(root.higher.lower.lower.lower);
		testAVLEntry(root.higher.lower.lower.higher, 27, 1);
		testAVLEntry(root.higher.lower.higher.lower, 32, 1);
		testAVLEntry(root.higher.lower.higher.higher, 36, 2);
		testAVLEntry(root.higher.higher.lower.lower, 40, 2);
		testAVLEntry(root.higher.higher.lower.higher, 44, 2);
		testAVLEntry(root.higher.higher.higher.lower, 47, 1);
		testAVLEntry(root.higher.higher.higher.higher, 50, 1);
		// Level 5
		assertNull(root.higher.lower.lower.higher.lower);
		assertNull(root.higher.lower.lower.higher.higher);
		assertNull(root.higher.lower.higher.lower.lower);
		assertNull(root.higher.lower.higher.lower.higher);
		testAVLEntry(root.higher.lower.higher.higher.lower, 35, 1);
		assertNull(root.higher.lower.higher.higher.higher);
		testAVLEntry(root.higher.higher.lower.lower.lower, 38, 1);
		testAVLEntry(root.higher.higher.lower.lower.higher, 41, 1);
		testAVLEntry(root.higher.higher.lower.higher.lower, 43, 1);
		assertNull(root.higher.higher.lower.higher.higher);
		assertNull(root.higher.higher.higher.lower.lower);
		assertNull(root.higher.higher.higher.lower.higher);
	}

	private void testAVLEntry(AVLTree.AVLEntry entry, Object key, int height) {
		assertEquals(key, entry.key);
		assertEquals(height, entry.height);
	}

	class Compare implements Comparator<Integer> {
		@Override
		public int compare(Integer i1, Integer i2) {
			return i1.hashCode() - i2.hashCode();
		}
	}
}