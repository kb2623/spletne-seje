package org.datastruct;

import org.datastruct.Comparator.CompareInteger;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BinomHeapTest {

	private BinomHeap<Integer> instance;

	private void setUpComparable() {
		this.instance = new BinomHeap<>();
	}
	
	private void setUpComparator() {
		this.instance = new BinomHeap<>(new CompareInteger());
	}
	
	@Test
	public void testAdd() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testAdd_One();
		setUpComparable(); testAdd_More();
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testAdd_One();
		setUpComparator(); testAdd_More();
	}

	private void testAdd_One() {
		instance.add(23);
	}

	private void testAdd_More() {
		instance.add(43);
		instance.add(63);
		instance.add(62);
		instance.add(32);
		instance.add(12);
		instance.add(90);
		instance.add(30);
		instance.add(16);
		instance.add(20);
		instance.add(55);
		instance.add(7);
		instance.add(1);
		instance.add(89);
	}
	
	@Test
	public void testRemoveFirst() {
		//Urejenost z uporabo >> Comparable
		try {
			setUpComparable(); testRemoveFirst_Empty(); assert false; 
		} catch(java.util.NoSuchElementException e) { 
		} catch(Exception e) {
			fail();
		}
		setUpComparable(); testRemoveFirst_One();
		setUpComparable(); testRemoveFirst_MoreFirst();
		setUpComparable(); testRemoveFirst_MoreTwo();
		//Urejenost z uporabo >> Comparator
		try {
			setUpComparator(); testRemoveFirst_Empty(); assert false; 
		} catch(java.util.NoSuchElementException e) { 
		} catch(Exception e) {
			fail();
		}
		setUpComparator(); testRemoveFirst_One();
		setUpComparator(); testRemoveFirst_MoreFirst();
		setUpComparator(); testRemoveFirst_MoreTwo();
	}

	private void testRemoveFirst_Empty() {
		instance.removeFirst();
	}

	private void testRemoveFirst_One() {
		instance.add(34);
		assertEquals(new Integer(34), instance.removeFirst());
	}

	private void testRemoveFirst_MoreFirst() {
		instance.add(43);
		instance.add(63);
		instance.add(62);
		instance.add(32);
		instance.add(12);
		instance.add(90);
		instance.add(30);
		instance.add(16);
		instance.add(20);
		instance.add(55);
		instance.add(7);
		instance.add(1);
		instance.add(89);
		assertEquals(new Integer(90), instance.removeFirst());
		assertEquals(new Integer(89), instance.removeFirst());
		assertEquals(new Integer(63), instance.removeFirst());
		assertEquals(new Integer(62), instance.removeFirst());
		assertEquals(new Integer(55), instance.removeFirst());
		assertEquals(new Integer(43), instance.removeFirst());
		assertEquals(new Integer(32), instance.removeFirst());
		assertEquals(new Integer(30), instance.removeFirst());
		assertEquals(new Integer(20), instance.removeFirst());
		assertEquals(new Integer(16), instance.removeFirst());
		assertEquals(new Integer(12), instance.removeFirst());
		assertEquals(new Integer(7), instance.removeFirst());
		assertEquals(new Integer(1), instance.removeFirst());
	}

	private void testRemoveFirst_MoreTwo() {
		for(int i = 0; i < 20; i++) {
			instance.add(i);
		}
		assertEquals(20, instance.size());
		for(int i = 19; i >= 0; i--) {
			assertEquals(new Integer(i), instance.removeFirst());
		}
	}
	
	@Test
	public void testGetFirst() {
		//Urejenost z uporabo >> Comparable
		try {
			setUpComparable(); testGetFirst_Empty(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparable(); testGetFirst_One();
		setUpComparable(); testGetFirst_More();
		//Urejenost z uporabo >> Comparator
		try {
			setUpComparator(); testGetFirst_Empty(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparator(); testGetFirst_One();
		setUpComparator(); testGetFirst_More();
	}

	private void testGetFirst_Empty() {
		instance.getFirst();
	}

	private void testGetFirst_One() {
		instance.add(100);
		assertEquals(new Integer(100), instance.getFirst());
	}

	private void testGetFirst_More() {
		instance.add(43);
		instance.add(63);
		instance.add(62);
		instance.add(32);
		instance.add(12);
		instance.add(90);
		instance.add(30);
		instance.add(16);
		instance.add(20);
		instance.add(55);
		instance.add(7);
		instance.add(1);
		instance.add(89);
		assertEquals(new Integer(90), instance.getFirst());
	}
	
	@Test
	public void testSize() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testSize_Empty();
		setUpComparable(); testSize_One();
		setUpComparable(); testSize_Random();
		//Urejenost z uporabo >> Comparable
		setUpComparator(); testSize_Empty();
		setUpComparator(); testSize_One();
		setUpComparator(); testSize_Random();
	}

	private void testSize_Empty() {
		assertEquals(0, instance.size());
	}

	private void testSize_One() {
		instance.add(23);
		assertEquals(1, instance.size());
	}

	private void testSize_Random() {
		int numOfEle = (int)(Math.random() * 100);
		for(int i = numOfEle; i > 0; i--) {
			instance.add(i);
		}
		assertEquals(numOfEle, instance.size());
	}
	
	@Test
	public void testDepth() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testDepth_Empty();
		setUpComparable(); testDepth_One();
		setUpComparable(); testDepth_Two();
		setUpComparable(); testDepth_More();
		//Urejenost z uporabo >> Comparable
		setUpComparator(); testDepth_Empty();
		setUpComparator(); testDepth_One();
		setUpComparator(); testDepth_Two();
		setUpComparator(); testDepth_More();
	}

	private void testDepth_Empty() {
		assertEquals(0, instance.depth());
	}

	private void testDepth_One() {
		instance.add(42);
		assertEquals(0, instance.depth());
	}

	private void testDepth_Two() {
		instance.add(32);
		instance.add(43);
		assertEquals(1, instance.depth());
	}

	private void testDepth_More() {
		for(int i = 0; i < 20; i++) {
			instance.add(i);
		}
		assertEquals(4, instance.depth());
	}
	
	@Test
	public void testIsEmpty() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testIsEmpty_True();
		setUpComparable(); testIsEmpty_False();
		//Urejenost z uporabo >> Comparable
		setUpComparator(); testIsEmpty_True();
		setUpComparator(); testIsEmpty_False();
	}

	private void testIsEmpty_True() {
		assertTrue(instance.isEmpty());
	}

	private void testIsEmpty_False() {
		assertTrue(instance.isEmpty());
		instance.add(42);
		assertFalse(instance.isEmpty());
	}
	
	@Test
	public void testExists() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testExists_Empty();
		setUpComparable(); testExists_One();
		setUpComparable(); testExists_More();
		//Urejenost z uporabo >> Comparable
		setUpComparator(); testExists_Empty();
		setUpComparator(); testExists_One();
		setUpComparator(); testExists_More();
	}

	private void testExists_Empty() {
		assertFalse(instance.exists(12));
	}

	private void testExists_One() {
		instance.add(32);
		assertFalse(instance.exists(23));
		assertTrue(instance.exists(32));
	}

	private void testExists_More() {
		instance.add(43);
		instance.add(63);
		instance.add(62);
		instance.add(32);
		instance.add(12);
		instance.add(90);
		instance.add(30);
		instance.add(16);
		instance.add(20);
		instance.add(55);
		instance.add(7);
		instance.add(1);
		instance.add(89);
		assertFalse(instance.exists(100));
		assertTrue(instance.exists(12));
		assertTrue(instance.exists(90));
		assertTrue(instance.exists(1));
		assertFalse(instance.exists(56));
		assertTrue(instance.exists(43));
		assertTrue(instance.exists(32));
		assertTrue(instance.exists(20));
		assertTrue(instance.exists(63));
		assertTrue(instance.exists(89));
		assertTrue(instance.exists(30));
		assertTrue(instance.exists(16));
		assertTrue(instance.exists(55));
		assertTrue(instance.exists(62));
		assertTrue(instance.exists(7));
	}
	
	@Test
	public void testRemove() {
		//Urejenost z uporabo >> Comparable
		try {
			setUpComparable(); testRemove_Empty(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparable(); testRemove_OneBad(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try{
			setUpComparable(); testRemove_MoreTwo(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparable(); testRemove_OneGood();
		setUpComparable(); testRemove_MoreOne();
		//Urejenost z uporabo >> Comparable
		try {
			setUpComparator(); testRemove_Empty(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparator(); testRemove_OneBad(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try{
			setUpComparator(); testRemove_MoreTwo(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparator(); testRemove_OneGood();
		setUpComparator(); testRemove_MoreOne();
	}

	private void testRemove_Empty() {
		assertTrue(instance.isEmpty());
		instance.remove(89);
	}

	private void testRemove_OneBad() {
		instance.add(43);
		instance.remove(34);
	}

	private void testRemove_OneGood() {
		instance.add(34);
		assertEquals(new Integer(34), instance.remove(34));
	}

	private void testRemove_MoreOne() {
		instance.add(43);
		instance.add(63);
		instance.add(62);
		instance.add(32);
		instance.add(12);
		instance.add(90);
		instance.add(30);
		instance.add(16);
		instance.add(20);
		instance.add(55);
		instance.add(7);
		instance.add(1);
		instance.add(89);
		assertEquals(new Integer(55), instance.remove(55));
		assertEquals(new Integer(32), instance.remove(32));
		assertEquals(new Integer(7), instance.remove(7));
		assertEquals(new Integer(89), instance.remove(89));
		assertEquals(new Integer(43), instance.remove(43));
		assertEquals(new Integer(62), instance.remove(62));
		assertEquals(new Integer(30), instance.remove(30));
		assertEquals(new Integer(1), instance.remove(1));
		assertEquals(new Integer(90), instance.remove(90));
		assertEquals(new Integer(12), instance.remove(12));
		assertEquals(new Integer(63), instance.remove(63));
		assertEquals(new Integer(16), instance.remove(16));
		assertEquals(new Integer(20), instance.remove(20));
	}

	private void testRemove_MoreTwo() {
		instance.add(43);
		instance.add(87);
		instance.add(12);
		instance.add(77);
		instance.add(99);
		instance.add(3);
		instance.add(27);
		instance.add(59);
		instance.add(60);
		instance.add(68);
		instance.add(34);
		assertEquals(new Integer(12), instance.remove(12));
		assertEquals(new Integer(59), instance.remove(59));
		assertEquals(new Integer(34), instance.remove(34));
		instance.remove(100);
	}
	
	@Test
	public void testAsList() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testAsList_Empty();
		setUpComparable(); testAsList_One();
		setUpComparable(); testAsList_Two();
		setUpComparable(); testAsList_Multi_One();
		setUpComparable(); testAsList_Multi_Two();
		//Urejenost z uporabo >> Comparable
		setUpComparator(); testAsList_Empty();
		setUpComparator(); testAsList_One();
		setUpComparator(); testAsList_Two();
		setUpComparator(); testAsList_Multi_One();
		setUpComparator(); testAsList_Multi_Two();
	}
	
	private void testAsList_Empty() {
		assertEquals(null, instance.asList());
	}
	
	private void testAsList_One() {
		instance.add(23);
		assertEquals(new Integer(23), instance.asList().get(0));
	}
	
	private void testAsList_Two() {
		StringBuilder builder = new StringBuilder();
		List<Integer> list;
		instance.add(23);
		instance.add(55);
		list = instance.asList();
		for (Integer integer : list) {
			builder.append(integer).append(' ');
		}
		assertEquals("23 55 ", builder.toString());
	}

	private void testAsList_Multi_One() {
		StringBuilder sb = new StringBuilder();
		instance.add(34);
		instance.add(23);
		instance.add(21);
		instance.add(55);
		instance.add(16);
		instance.add(7);
		List<Integer> list = instance.asList();
		for (Integer integer : list) {
			sb.append(integer).append(' ');
		}
		assertEquals("7 16 23 34 21 55 ", sb.toString());
	}
	
	private void testAsList_Multi_Two() {
		StringBuilder builder = new StringBuilder();
		instance.add(9);
		instance.add(5);
		instance.add(17);
		instance.add(21);
		instance.add(99);
		instance.add(12);
		instance.add(23);
		instance.add(12);
		List<Integer> list = instance.asList();
		for (Integer integer : list) {
			builder.append(integer).append(' ');
		}
		assertEquals("5 9 17 21 12 23 12 99 ", builder.toString());
	}
}