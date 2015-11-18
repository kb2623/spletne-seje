package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by klemen on 11/18/15.
 */
public class BTreeTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	private int binarySearch(Integer[] array, int len, Integer value) {
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
		return -(high) - 1;
	}

	@Test
	public void testSize() throws Exception {
		Integer[] array = {5, 11, 20, 58, 72, 103, 164, 212, 260, 323, 365, 371, 372, 385, 404, 425, 527, 541, 658, 677, 710, 748, 758, 796, 841, 850, 902, 917, 920, 939, 956, 974};
		System.out.println(binarySearch(array, array.length, new Integer(500)));
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
	public void testKeySet() throws Exception {

	}

	@Test
	public void testValues() throws Exception {

	}

	@Test
	public void testEntrySet() throws Exception {

	}
}