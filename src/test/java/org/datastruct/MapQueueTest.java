package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MapQueueTest {

	private Map<Integer, Integer> map;
	private List<Integer> listV;
	private List<Integer> listK;

	private int size = 100;
	private int capacity = 25;

	@Before
	public void setUp() {
		map = new MapQueue<>(5, capacity);
		listV = new ArrayList<>(capacity);
		listK = new ArrayList<>(capacity);
	}

	@Test
	public void testPut() {
		put(false);
		Iterator<Integer> itM = ((MapQueue<Integer, Integer>) map).iterator();
		Iterator<Integer> itL = listV.iterator();
		while (itL.hasNext()) {
			try {
				assertEquals(itL.next(), itM.next());
			} catch (NoSuchElementException e) {
				fail();
			}
		}
		if (itM.hasNext()) {
			fail();
		}
	}

	private void put(boolean testGet) {
		for (int i = 0; i < size; i++) {
			int rNumK = (int) (Math.random() * (size + 1) + 1);
			int rNumV = (int) (Math.random() * (size + 1000) + 1);
			map.put(rNumK, rNumV);
			int indexOf = listV.indexOf(new Integer(rNumV));
			if (listV.size() < capacity) {
				if (indexOf > -1) {
					if (testGet) {
						listK.remove(indexOf);
						listK.add(0, rNumK);
					}
					listV.remove(indexOf);
					listV.add(0, rNumV);
				} else {
					if (testGet) {
						listK.add(0, rNumK);
					}
					listV.add(0, rNumV);
				}
			} else {
				if (indexOf > -1) {
					if (testGet) {
						listK.remove(indexOf);
						listK.add(0, rNumK);
					}
					listV.remove(indexOf);
					listV.add(0, rNumV);
				} else {
					if (testGet) {
						assertNotNull(listK.remove(capacity - 1));
						listK.add(0, rNumK);
					}
					listV.remove(capacity - 1);
					listV.add(0, rNumV);
				}
			}
		}
		if (testGet) {
			assertEquals(listK.size(), map.size());
		}
		assertEquals(listV.size(), map.size());
	}

	@Test
	public void testGet() {
		put(true);
		for (int i = 0; i < size; i++) {
			int rIndex = (int) (Math.random() * capacity);
			System.out.println(listV.get(rIndex));
			map.get(listK.get(rIndex));
			lowerPririty(rIndex);
			assertEquals(listV.toString(), map.toString());
		}
	}

	private void lowerPririty(int index) {
		if (index != 0) {
			Integer tmp = listK.set(index - 1, listK.get(index));
			listK.set(index, tmp);
			tmp = listV.set(index - 1, listV.get(index));
			listV.set(index, tmp);
		}
	}
}