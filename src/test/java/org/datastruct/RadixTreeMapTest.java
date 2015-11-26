package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class RadixTreeMapTest {

	private Map<MyString, Double> map;
	private Map<MyString, Double> tmap;
	private int size = 500;

	@Before
	public void setUp() throws Exception {
		map = new RadixTree<>();
		tmap = new HashMap<>(size);
	}

	private MyString getRandomKey(int size) {
		int rNum = (int) (Math.random() * size + 1 + 1);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < rNum; i++) {
			int c = (int) (Math.random() * 26 + 97);
			builder.append((char) c);
		}
		return new MyString(builder.toString());
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
		for (MyString s : tmap.keySet()) {
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
		for (MyString s : tmap.keySet()) {
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
			map.put(new MyString("asdf"), null);
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
			MyString key = getRandomKey((int) (Math.random() * 26 + 1));
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
			for (MyString s : tmap.keySet()) {
				if ((int) (Math.random() * 2 + 1) == 0) {
					assertEquals(tmap.remove(s), map.remove(s));
				}
			}
			assertEquals(tmap.size(), map.size());
			Set<Map.Entry<MyString, Double>> sm = map.entrySet();
			Set<Map.Entry<MyString, Double>> st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<MyString, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
		} catch (AssertionError e) {
			((RadixTree) map).printTree();
			throw new AssertionError(e);
		}
	}

	@Test
	public void testPutAll() throws Exception {
		Map<MyString, Double> addMap = new HashMap<>(50);
		addMap.put(new MyString("asdf"), null);
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
			MyString key = getRandomKey((int) (Math.random() * 26 + 1));
			addMap.put(key, value);
		}
		map.putAll(addMap);
		tmap.putAll(addMap);
		assertEquals(tmap.size(), map.size());
		for (Map.Entry<MyString, Double> e : map.entrySet()) {
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
			Set<MyString> sm = map.keySet();
			Set<MyString> st = tmap.keySet();
			assertEquals(st.size(), sm.size());
			for (MyString s : sm) {
				assertTrue(tmap.containsKey(s));
			}
			testPut(true);
			sm = map.keySet();
			st = tmap.keySet();
			assertEquals(st.size(), sm.size());
			for (MyString s : sm) {
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
			Set<Map.Entry<MyString, Double>> sm = map.entrySet();
			Set<Map.Entry<MyString, Double>> st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<MyString, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
			testPut(true);
			sm = map.entrySet();
			st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<MyString, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
		} catch (AssertionError e) {
			((RadixTree) map).printTree();
			throw new AssertionError(e);
		}
	}

	class MyString implements Sequence<MyString> {

		private String value;

		MyString(String s) {
			value = s;
		}

		@Override
		public int equalDistance(MyString s) {
			int distance = 0;
			for (int i = 0; i < value.length(); i++) {
				if (i < s.value.length()) {
					if (value.charAt(i) == s.value.charAt(i)) {
						distance++;
					} else {
						return distance;
					}
				} else {
					return distance;
				}
			}
			return distance;
		}

		@Override
		public int length() {
			return value.length();
		}

		@Override
		public MyString append(MyString s) {
			return new MyString(value + s);
		}

		@Override
		public MyString subSequence(int start, int end) {
			return new MyString(value.substring(start, end));
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			if (o == this) return true;
			if (o instanceof String) {
				return value.equals(o);
			} else {
				MyString s = (MyString) o;
				if (!value.equals(s.value)) return false;
				return true;
			}
		}

		@Override
		public int hashCode() {
			int hash = 0;
			for (char c : value.toCharArray()) {
				hash += ((int) c);
			}
			return hash;
		}

		@Override
		public String toString() {
			return value;
		}
	}
}