package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
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
	public void testMixWithReflections() throws NoSuchFieldException, IllegalAccessException {
		Field rootField = AVLTree.class.getDeclaredField("root");
		rootField.setAccessible(true);
		AVLTree.AVLEntry root = (AVLTree.AVLEntry) rootField.get(map);
		assertNull(root);
		int i = 1;

		assertNull(map.put(46, i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		assertNotNull(root);
		assertEquals(1, root.height);
		assertNull(root.lower);
		assertNull(root.higher);

		assertNull(map.put(19, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		assertEquals(46, root.key);
		assertEquals(2, root.height);
		assertEquals(19, root.lower.key);
		assertEquals(1, root.lower.height);

		assertNull(map.put(37, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		assertEquals(37, root.key);
		assertEquals(2, root.height);
		assertEquals(19, root.lower.key);
		assertEquals(1, root.lower.height);
		assertEquals(46, root.higher.key);
		assertEquals(1, root.higher.height);

		assertNull(map.put(9, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		assertEquals(37, root.key);
		assertEquals(3, root.height);
		assertEquals(19, root.lower.key);
		assertEquals(2, root.lower.height);
		assertEquals(9, root.lower.lower.key);
		assertEquals(1, root.lower.lower.height);
		assertEquals(46, root.higher.key);
		assertEquals(1, root.higher.height);

		assertNull(map.put(20, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		assertEquals(37, root.key);
		assertEquals(3, root.height);
		assertEquals(19, root.lower.key);
		assertEquals(2, root.lower.height);
		assertEquals(9, root.lower.lower.key);
		assertEquals(1, root.lower.lower.height);
		assertEquals(20, root.lower.higher.key);
		assertEquals(1, root.lower.higher.height);
		assertEquals(46, root.higher.key);
		assertEquals(1, root.higher.height);

		assertNull(map.put(24, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		assertEquals(20, root.key);
		assertEquals(3, root.height);
		assertEquals(19, root.lower.key);
		assertEquals(2, root.lower.height);
		assertEquals(9, root.lower.lower.key);
		assertEquals(1, root.lower.lower.height);
		assertNull(root.lower.higher);
		assertEquals(37, root.higher.key);
		assertEquals(2, root.higher.height);
		assertEquals(24, root.higher.lower.key);
		assertEquals(1, root.higher.lower.height);
		assertEquals(46, root.higher.higher.key);
		assertEquals(1, root.higher.higher.height);

		assertNull(map.put(23, ++i));
		root = (AVLTree.AVLEntry) rootField.get(map);
		assertEquals(20, root.key);
		assertEquals(4, root.height);
		assertEquals(19, root.lower.key);
		assertEquals(2, root.lower.height);
		assertEquals(9, root.lower.lower.key);
		assertEquals(1, root.lower.lower.height);
		assertNull(root.lower.higher);
		assertEquals(37, root.higher.key);
		assertEquals(3, root.higher.height);
		assertEquals(24, root.higher.lower.key);
		assertEquals(2, root.higher.lower.height);
		assertEquals(23, root.higher.lower.lower.key);
		assertEquals(1, root.higher.lower.lower.height);
		assertNull(root.higher.lower.higher);
		assertEquals(46, root.higher.higher.key);
		assertEquals(1, root.higher.higher.height);

		assertNull(map.put(41, ++i));

		assertNull(map.put(21, ++i));

		assertNull(map.put(42, ++i));

		assertNull(map.put(13, ++i));

		assertNull(map.put(36, ++i));

		assertNull(map.put(29, ++i));

		assertNull(map.put(7, ++i));

		assertNull(map.put(15, ++i));

		assertNull(map.put(50, ++i));

		assertNull(map.put(44, ++i));

		assertNull(map.put(27, ++i));

		assertNull(map.put(2, ++i));

		assertNull(map.put(32, ++i));

		assertNull(map.put(47, ++i));

		assertNull(map.put(34, ++i));

		assertNull(map.put(35, ++i));

		assertNull(map.put(49, ++i));

		assertNull(map.put(38, ++i));

		assertNull(map.put(5, ++i));

		assertNull(map.put(8, ++i));

		assertNull(map.put(10, ++i));

		assertNull(map.put(43, ++i));

		assertNull(map.put(18, ++i));

		assertNull(map.put(16, ++i));

		assertNull(map.put(17, ++i));

		assertNull(map.put(40, ++i));

		assertNull(map.put(3, ++i));
	}

	class Compare implements Comparator<Integer> {
		@Override
		public int compare(Integer i1, Integer i2) {
			return i1.hashCode() - i2.hashCode();
		}
	}
}