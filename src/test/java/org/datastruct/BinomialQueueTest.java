package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		queue = new BinomialQueue<>((i1, i2) -> i1.intValue() - i2.intValue());
		tqueue = new PriorityQueue<>((i1, i2) -> i1.intValue() - i2.intValue());
		assertTrue(queue.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		queue.clear();
		assertTrue(queue.isEmpty());
	}

	@Test
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

	@Test
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

	@Test
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

	@Test
	public void testContainsAll() throws Exception {

	}

	@Test
	public void testAddAll() throws Exception {

	}

	@Test
	public void testRemoveAll() throws Exception {

	}

	@Test
	public void testRetainAll() throws Exception {

	}

	@Test
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

	@Test
	public void testRemove1() throws Exception {

	}

	@Test
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

	@Test
	public void testPeek() throws Exception {

	}
}