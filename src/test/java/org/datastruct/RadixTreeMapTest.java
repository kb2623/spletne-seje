package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RadixTreeMapTest {

	private Map<String, Integer> map;
	private Map<String, Integer> tmap;

	@Before
	public void setUp() throws Exception {
		map = new RadixTree<>();
		tmap = new HashMap<>();
	}

	private String getRandomKey() {
		int rNum = (int) (Math.random() * 101 + 1);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < rNum; i++) {
			int c = (int) (Math.random() * 26 + 97);
			builder.append((char) c);
		}
		return builder.toString();
	}

	@Test
	public void testSize() throws Exception {

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
	public void testPut() throws Exception {

	}

	@Test
	public void testRemove() throws Exception {

	}

	@Test
	public void testPutAll() throws Exception {

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
}