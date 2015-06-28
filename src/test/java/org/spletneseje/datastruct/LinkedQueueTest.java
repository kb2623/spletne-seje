package org.spletneseje.datastruct;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedQueueTest {
    
    private LinkedQueue<Integer> quene;
    
    @Before
    public void setUp() {
        quene = new LinkedQueue<>();
    }
    
    @Test
    public void testAdd() {
        assertTrue(quene.add(1));
        assertTrue(quene.add(2));
        assertTrue(quene.add(3));
        assertTrue(quene.add(4));
        assertTrue(quene.add(5));
        assertTrue(quene.add(6));
        assertTrue(quene.add(7));
        assertTrue(quene.add(8));
        assertTrue(quene.add(9));
        assertTrue(quene.add(10));
        assertTrue(quene.add(11));
        assertTrue(quene.add(12));
    }
    
    @Test
    public void testOffer() {
        assertTrue(quene.offer(12));
        assertTrue(quene.offer(11));
        assertTrue(quene.offer(10));
        assertTrue(quene.offer(9));
        assertTrue(quene.offer(8));
        assertTrue(quene.offer(7));
        assertTrue(quene.offer(6));
        assertTrue(quene.offer(5));
        assertTrue(quene.offer(4));
        assertTrue(quene.offer(3));
        assertTrue(quene.offer(2));
        assertTrue(quene.offer(1));
    }

    @Test
    public void testPeekEmpty() {
        assertNull(quene.peek());
    }

    @Test
    public void testPeekOne() {
        assertTrue(quene.add(2));
        assertEquals(new Integer(2), quene.peek());
        assertTrue(quene.add(3));
        assertEquals(new Integer(2), quene.peek());
    }

    @Test
    public void testPoll() {
        assertNull(quene.poll());
        testAdd();
        assertEquals(new Integer(1), quene.poll());
        assertEquals(new Integer(2), quene.poll());
        assertEquals(new Integer(3), quene.poll());
        assertEquals(new Integer(4), quene.poll());
        assertTrue(quene.offer(13));
        assertEquals(new Integer(5), quene.poll());
        assertEquals(new Integer(6), quene.poll());
        assertEquals(new Integer(7), quene.poll());
        assertEquals(new Integer(8), quene.poll());
        assertTrue(quene.offer(14));
        assertEquals(new Integer(9), quene.poll());
        assertEquals(new Integer(10), quene.poll());
        assertTrue(quene.offer(15));
        assertEquals(new Integer(11), quene.poll());
        assertEquals(new Integer(12), quene.poll());
        assertEquals(new Integer(13), quene.poll());
        assertEquals(new Integer(14), quene.poll());
        assertEquals(new Integer(15), quene.poll());
        assertNull(quene.poll());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(quene.isEmpty());
        assertTrue(quene.offer(1));
        assertFalse(quene.isEmpty());
        assertTrue(quene.offer(1));
        assertFalse(quene.isEmpty());
        assertTrue(quene.add(1));
        assertFalse(quene.isEmpty());
        assertEquals(new Integer(1), quene.poll());
        assertEquals(new Integer(1), quene.poll());
        assertEquals(new Integer(1), quene.poll());
        assertTrue(quene.isEmpty());
    }

    @Test
    public void testPushBack() {
        assertTrue(quene.add(1));
        assertTrue(quene.add(2));
        assertEquals(new Integer(1), quene.peek());
        quene.pushBack();
        assertEquals(new Integer(2), quene.peek());
        assertTrue(quene.add(3));
        quene.pushBack();
        quene.pushBack();
        assertEquals(new Integer(3), quene.peek());
    }

}