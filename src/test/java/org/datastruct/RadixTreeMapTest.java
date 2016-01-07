package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class RadixTreeMapTest {

	private Map<String, Double> map;
	private Map<String, Double> tmap;

	private int size = 10000;

	@Before
	public void setUp() throws Exception {
		map = new RadixTree<>();
		tmap = new HashMap<>(size);
	}

	private String getRandomKey(int size) {
		int rNum = (int) (Math.random() * size + 1 + 1);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < rNum; i++) {
			int c = (int) (Math.random() * 26 + 97);
			builder.append((char) c);
		}
		return builder.toString();
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(0, map.size());
		testPut(true);
		assertEquals(tmap.size(), map.size());
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue(map.isEmpty());
		testPut(false);
		assertFalse(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testContainsKey() throws Exception {
		try {
			map.containsKey(null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		for (String s : tmap.keySet()) {
			assertTrue(map.containsKey(s));
		}
	}

	@Test
	public void testContainsValue() throws Exception {
		try {
			map.containsValue(null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		for (Double d : tmap.values()) {
			assertTrue(map.containsValue(d));
		}
	}

	@Test
	public void testGet() throws Exception {
		try {
			map.get(null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		for (String s : tmap.keySet()) {
			assertEquals(tmap.get(s), map.get(s));
		}
	}

	@Test
	public void testPut() throws Exception {
		try {
			map.put(null, 2.34d);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.put("asdf", null);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.put(null, null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
	}

	private void testPut(boolean test) {
		for (int i = 0; i < size; i++) {
			double value = Math.random() * 5000 + 1;
			String key = getRandomKey((int) (Math.random() * 26 + 1));
			if (test) {
				assertEquals(tmap.put(key, value), map.put(key, value));
			} else {
				map.put(key, value);
			}
		}
	}

	@Test
	public void testRemove() throws Exception {
		try {
			testPut(true);
			for (String s : tmap.keySet()) {
				if ((int) (Math.random() * 2 + 1) == 0) {
					assertEquals(tmap.remove(s), map.remove(s));
				}
			}
			assertEquals(tmap.size(), map.size());
			Set<Map.Entry<String, Double>> sm = map.entrySet();
			Set<Map.Entry<String, Double>> st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<String, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
		} catch (AssertionError e) {
			((RadixTree) map).printTree();
			throw new AssertionError(e);
		}
	}

	@Test
	public void testPutAll() throws Exception {
		Map<String, Double> addMap = new HashMap<>(50);
		addMap.put("asdf", null);
		try {
			map.putAll(addMap);
			fail();
		} catch (NullPointerException e) {
		}
		addMap.clear();
		addMap.put(null, 23.43);
		try {
			map.putAll(addMap);
			fail();
		} catch (NullPointerException e) {
		}
		addMap.clear();
		for (int i = 0; i < 50; i++) {
			double value = Math.random() * 5000 + 1;
			String key = getRandomKey((int) (Math.random() * 26 + 1));
			addMap.put(key, value);
		}
		map.putAll(addMap);
		tmap.putAll(addMap);
		assertEquals(tmap.size(), map.size());
		for (Map.Entry<String, Double> e : map.entrySet()) {
			assertEquals(tmap.get(e.getKey()), e.getValue());
		}
	}

	@Test
	public void testClear() throws Exception {
		assertTrue(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
		testPut(false);
		assertFalse(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testKeySet() throws Exception {
		try {
			Set<String> sm = map.keySet();
			Set<String> st = tmap.keySet();
			assertEquals(st.size(), sm.size());
			for (String s : sm) {
				assertTrue(tmap.containsKey(s));
			}
			testPut(true);
			sm = map.keySet();
			st = tmap.keySet();
			assertEquals(st.size(), sm.size());
			for (String s : sm) {
				assertTrue(tmap.containsKey(s));
			}
		} catch (AssertionError e) {
			((RadixTree) map).printTree();
			throw new AssertionError(e);
		}
	}

	@Test
	public void testValues() throws Exception {
		try {
			Collection<Double> sm = map.values();
			Collection<Double> st = tmap.values();
			assertEquals(st.size(), sm.size());
			for (Double d : sm) {
				assertTrue(tmap.containsValue(d));
			}
			testPut(true);
			sm = map.values();
			st = tmap.values();
			assertEquals(st.size(), sm.size());
			for (Double d : sm) {
				assertTrue(tmap.containsValue(d));
			}
		} catch (AssertionError e) {
			((RadixTree) map).printTree();
			throw new AssertionError(e);
		}
	}

	@Test
	public void testEntrySet() throws Exception {
		try {
			Set<Map.Entry<String, Double>> sm = map.entrySet();
			Set<Map.Entry<String, Double>> st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<String, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
			testPut(true);
			sm = map.entrySet();
			st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<String, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
		} catch (AssertionError e) {
			((RadixTree) map).printTree();
			throw new AssertionError(e);
		}
	}
}