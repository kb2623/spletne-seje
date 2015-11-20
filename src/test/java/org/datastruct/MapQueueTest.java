package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNull;

public class MapQueueTest {

	private Map<Integer, Integer> map;

	@Before
	public void setUp() {
		map = new MapQueue<>(5, 5);
	}

	@Test
	public void testPut() {
		assertNull(map.put(1, 1));
		assertNull(map.put(2, 1));
		assertNull(map.put(3, 1));
		assertNull(map.put(4, 1));
		assertNull(map.put(5, 1));
		assertNull(map.put(6, 1));
		System.out.println(((MapQueue) map).printTree());
		System.out.println(map.toString());
		assertNull(map.put(7, 1));
		System.out.println(((MapQueue) map).printTree());
		System.out.println(map.toString());
		assertNull(map.put(8, 1));
		System.out.println(((MapQueue) map).printTree());
		System.out.println(map.toString());
		assertNull(map.put(9, 1));
		System.out.println(((MapQueue) map).printTree());
		System.out.println(map.toString());
		assertNull(map.put(10, 1));
		System.out.println(((MapQueue) map).printTree());
		System.out.println(map.toString());
		assertNull(map.put(11, 1));
		System.out.println(((MapQueue) map).printTree());
		System.out.println(map.toString());
	}
}