package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ArrayMapTest {

	private Map<Integer, Integer> mapNoLimits;
	private Map<Integer, Integer> mapWithLimits;

	@Before
	public void setUp() throws Exception {
		mapNoLimits = new ArrayMap<>(10, .5f);
		mapWithLimits = new ArrayMap<>(10);
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