package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by klemen on 11/18/15.
 */
public class BTreeTest {

	private Map<Integer, Double> map;

	@Before
	public void setUp() throws Exception {
		map = new BTree<>(3);
		assertTrue(map.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		map.clear();
		assertTrue(map.isEmpty());
	}

	private int binarySearch(Integer[] array, int len, Integer value) {
		if (len == 0) {
			return -1;
		}
		int low = 0;
		int high = len - 1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (array[mid] == value) {
				return mid;
			} else if (array[mid] < value) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		if (high < 0) {
			return high;
		} else {
			return -(high + 2);
		}
	}

	@Test
	public void testSize() throws Exception {
		Integer[] array = {5, 11, 20, 58, 72, 103, 164, 212, 260, 323, 365, 371, 372, 385, 404, 425, 527, 541, 658, 677, 710, 748, 758, 796, 841, 850, 902, 917, 920, 939, 956, 974};
		System.out.println(binarySearch(array, array.length, new Integer(100)));
		System.out.println(binarySearch(array, array.length, new Integer(980)));
		Integer[] tab = {5, 11, 20, 58, 72, 103, 164, 212, 260, 323, 365, 371, 372, 385, 404, 425, 527, 541, 658, 677, 710, 748, 758, 796, 841, 850, 902, 917, 920, 939, 956, null};
		for (int i = 31; i > 5; i--) {
			tab[i] = tab[i - 1];
		}
		System.out.println(tab[4] + " " + tab[5] + " " + tab[6] + " " + tab[31]);
		Integer[] tab1 = new Integer[32];
		System.out.println(binarySearch(tab1, 0, new Integer(55)));
		tab1[0] = new Integer(55);
		System.out.println(binarySearch(tab1, 1, new Integer(55)));
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
		try {
			map.put(null, null);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.put(null, 2.34);
			fail();
		} catch (NullPointerException e) {
		}
		assertNull(map.put(55, 1.23));
		assertEquals(new Double(1.23), map.put(55, 1.01));
		assertNull(map.put(11, 2.01));
		assertNull(map.put(99, 3.01));
		assertNull(map.put(44, 3.01));
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