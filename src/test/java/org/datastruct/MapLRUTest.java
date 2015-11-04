package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MapLRUTest {

	private Map<Integer, Integer> map;
	private Map<Integer, Integer> tmap;

	@Before
	public void setUp() throws Exception {
		map = new MapLRU<>(5, 150);
		tmap = new TreeMap<>(new Compare());
	}

	@Test
	public void testPut() throws Exception {
		try {
			map.put(34, null);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.put(null, 23);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
	}

	private void testPut(boolean test) {
		for (int i = 0; i < 150; i++) {
			int rNumK = (int) (Math.random() * 101 + 1);
			int rNumV = (int) (Math.random() * 101 + 1);
			if (test) {
				assertEquals(tmap.put(rNumK, rNumV), map.put(rNumK, rNumV));
			} else {
				map.put(rNumK, rNumV);
			}
		}
	}

	@Test
	public void testClear() throws Exception {

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

	@Test
	public void testIsEmpty() throws Exception {

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
	public void testRemove() throws Exception {

	}

	@Test
	public void testPutAll() throws Exception {

	}

	@Test
	public void testSize() throws Exception {

	}

	class Compare implements Comparator<Integer> {
		@Override
		public int compare(Integer i1, Integer i2) {
			return i1.hashCode() - i2.hashCode();
		}
	}
}