package org.datastruct;

import org.junit.Before;
import org.junit.Test;

public class MapQueueTest {

	private MapQueue<Integer, Integer> map;

	@Before
	public void setUp() {
		map = new MapQueue<>(5, 100);
	}

	@Test
	public void testPut() {

	}
}