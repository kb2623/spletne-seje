package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkQueueTest {

	private LinkQueue<Integer> queue;

	@Before
	public void setUp() throws Exception {
		queue = new LinkQueue<>();
		assertTrue(queue.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		queue.clear();
		assertTrue(queue.isEmpty());
	}

	@Test
	public void testSize() throws Exception {

	}

	@Test
	public void testContains() throws Exception {

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
		queue.add(1);
		queue.add(2);
		queue.add(3);
		assertEquals(new Integer(1), queue.poll());
		assertEquals(new Integer(2), queue.poll());
		assertEquals(new Integer(3), queue.poll());
	}

	@Test
	public void testRemove() throws Exception {

	}

	@Test
	public void testAddAll() throws Exception {

	}

	@Test
	public void testRetainAll() throws Exception {

	}

	@Test
	public void testRemoveAll() throws Exception {

	}

	@Test
	public void testContainsAll() throws Exception {

	}

	@Test
	public void testOffer() throws Exception {

	}

	@Test
	public void testRemove1() throws Exception {

	}

	@Test
	public void testPoll() throws Exception {

	}

	@Test
	public void testElement() throws Exception {

	}

	@Test
	public void testPeek() throws Exception {

	}
}