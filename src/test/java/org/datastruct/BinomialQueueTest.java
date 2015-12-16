package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.*;

public class BinomialQueueTest {

	private Queue<Integer> queue;
	private Queue<Integer> tqueue;

	private int size = 1000;

	@Before
	public void setUp() throws Exception {
		CmpInt cmp = new CmpInt();
		queue = new BinomialQueue<>(cmp);
		tqueue = new PriorityQueue<>(cmp);
		assertTrue(queue.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		queue.clear();
		assertTrue(queue.isEmpty());
	}

	@Test(timeout = 10000)
	public void testContains() throws Exception {
		assertFalse(queue.contains(23));
		add(true);
		while (!tqueue.isEmpty()) {
			Integer i = tqueue.remove();
			try {
				assertTrue(queue.contains(i));
			} catch (AssertionError e) {
				assertTrue(queue.isEmpty());
				throw new AssertionError(queue.toString() + "\ncontains " + i, e);
			}
		}
		try {
			queue.contains(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testIterator() throws Exception {
	}

	@Test
	public void testToArray() throws Exception {
	}

	@Test
	public void testToArray1() throws Exception {
	}

	@Test(timeout = 10000)
	public void testAdd() throws Exception {
		add(true);
		try {
			queue.add(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	private void add(boolean test) {
		for (int i = 0; i < size; i++) {
			int rand = (int) (Math.random() * size * size);
			if (test) {
				assertEquals(tqueue.add(rand), queue.add(rand));
			} else {
				assertTrue(queue.add(rand));
			}
		}
		if (test) {
			assertFalse(queue.isEmpty());
			assertEquals(tqueue.size(), queue.size());
		}
	}

	@Test(timeout = 10000)
	public void testRemove() throws Exception {
		try {
			queue.remove();
			fail();
		} catch (NoSuchElementException e) {
		}
		add(true);
		while (!tqueue.isEmpty()) {
			assertEquals(tqueue.remove(), queue.remove());
		}
		assertEquals(tqueue.size(), queue.size());
		assertEquals(tqueue.isEmpty(), queue.isEmpty());
	}

	@Test(timeout = 10000)
	public void testContainsAll() throws Exception {

	}

	@Test(timeout = 10000)
	public void testAddAll() throws Exception {

	}

	@Test(timeout = 10000)
	public void testRemoveAll() throws Exception {

	}

	@Test(timeout = 10000)
	public void testRetainAll() throws Exception {

	}

	@Test(timeout = 10000)
	public void testOffer() throws Exception {
		offer(true);
		assertFalse(queue.offer(null));
	}

	private void offer(boolean test) {
		for (int i = 0; i < size; i++) {
			int rand = (int) (Math.random() * size * size);
			if (test) {
				assertEquals(tqueue.offer(rand), queue.offer(rand));
			} else {
				assertTrue(queue.add(rand));
			}
		}
		if (test) {
			assertFalse(queue.isEmpty());
			assertEquals(tqueue.size(), queue.size());
		}
	}

	@Test(timeout = 10000)
	public void testRemove1() throws Exception {
		add(true);
		for (Object o : queue.toArray()) {
			assertEquals(tqueue.remove(o), queue.remove(o));
			assertEquals(tqueue.size(), queue.size());
		}
		assertTrue(queue.isEmpty());
	}

	@Test
	public void testSome() {
		queue.add(23);
		queue.add(32);
		queue.add(20);
		queue.remove(20);
	}

	@Test(timeout = 10000)
	public void testPoll() throws Exception {
		assertNull(queue.poll());
		offer(true);
		while (!tqueue.isEmpty()) {
			assertEquals(tqueue.poll(), queue.poll());
		}
		assertEquals(tqueue.isEmpty(), queue.isEmpty());
	}

	@Test
	public void testElement() throws Exception {

	}

	@Test(timeout = 10000)
	public void testPeek() throws Exception {

	}

	private class CmpInt implements Comparator<Integer> {

		@Override
		public int compare(Integer i1, Integer i2) {
			return i1.intValue() - i2.intValue();
		}
	}
}