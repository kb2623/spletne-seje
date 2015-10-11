package org.datastruct;

import org.datastruct.Comparator.CompareInteger;
import org.datastruct.Comparator.CompareString;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HeapTest {

	private Heap<String> pv;
	private Heap<Integer> instance;

	private void setUpComparable() {
		this.pv = new Heap<>(10);
		this.instance = new Heap<>();
	}
	
	private void setUpComparator() {
		this.pv = new Heap<>(10, new CompareString());
		this.instance = new Heap<>(new CompareInteger());
	}
	
	@Test
	public void testPrioritetnaVrsta() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testPrioritetnaVrsta_One();
		setUpComparable(); testPrioritetnaVrsta_Two();
		setUpComparable(); testPrioritetnaVrsta_Three();
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testPrioritetnaVrsta_One();
		setUpComparable(); testPrioritetnaVrsta_Two();
		setUpComparable(); testPrioritetnaVrsta_Three();
	}
	
	private void testPrioritetnaVrsta_One() {
	StringBuilder buff = new StringBuilder();
		for(int i = 0; i < 20; i++) {
			instance.add(i);
		}
		for(int i = 0; i < 20; i++) {
			buff.append(instance.removeFirst()).append(' ');
		}
		assertEquals("19 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0 ", buff.toString());
	}

	private void testPrioritetnaVrsta_Two() {
		StringBuilder buff = new StringBuilder();
		assertTrue(instance.isEmpty());
		instance.add(73);
		instance.add(6);
		instance.add(57);
		assertFalse(instance.isEmpty());
		instance.add(88);
		instance.add(60);
		assertTrue(instance.exists(57));
		instance.add(34);
		instance.add(83);
		instance.add(72);
		instance.add(48);
		instance.add(85);
		assertEquals(4, instance.depth());
		assertEquals(10, instance.size());
		assertEquals(new Integer(88), instance.getFirst());
		buff.append(instance.removeFirst()).append(' ');
		assertFalse(instance.exists(12));
		assertFalse(instance.exists(88));
		assertEquals(new Integer(85), instance.getFirst());
		buff.append(instance.removeFirst()).append(' ');
		assertEquals(new Integer(83), instance.getFirst());
		buff.append(instance.removeFirst()).append(' ');
		assertTrue(instance.exists(48));
		assertEquals(3, instance.depth());
		assertEquals(7, instance.size());
		for(int i = 0; i < 7; i++) {
			buff.append(instance.removeFirst()).append(' ');
		}
		assertEquals("88 85 83 73 72 60 57 48 34 6 ", buff.toString());
		assertFalse(instance.exists(6));
		assertTrue(instance.isEmpty());
	}

	private void testPrioritetnaVrsta_Three() {
		StringBuilder buff = new StringBuilder();
		assertTrue(instance.isEmpty());
		assertFalse(instance.exists(8));
		instance.add(43);
		instance.add(32);
		instance.add(89);
		instance.add(100);
		assertTrue(instance.exists(32));
		instance.add(19);
		instance.add(84);
		instance.add(5);
		assertEquals(3, instance.depth());
		assertFalse(instance.isEmpty());
		instance.add(27);
		instance.add(14);
		instance.add(73);
		assertEquals(10, instance.size());
		assertEquals(4, instance.depth());
		assertEquals(new Integer(100), instance.getFirst());
		buff.append(instance.removeFirst()).append(' ');
		assertEquals(new Integer(32), instance.remove(32));
		assertEquals(8, instance.size());
		assertEquals(4, instance.depth());
		assertEquals(new Integer(43), instance.remove(43));
		assertEquals(7, instance.size());
		assertEquals(3, instance.depth());
		for(int i = 0; i < 7; i++) {
			buff.append(instance.removeFirst()).append(' ');
		}
		assertEquals("100 89 84 73 27 19 14 5 ", buff.toString());
		assertEquals(0, instance.depth());
		assertEquals(0, instance.size());
		assertTrue(instance.isEmpty());
	}
	
	@Test
	public void testAdd() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testAdd_One();
		setUpComparable(); testAdd_Multiple();
		testAdd_Overflow(true);
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testAdd_One();
		setUpComparator(); testAdd_Multiple();
		testAdd_Overflow(false);
	}
	
	private void testAdd_One() {
		instance.add(12);
	}

	private void testAdd_Multiple() {
		pv.add("Test1");
		pv.add("Test2");
	}

	private void testAdd_Overflow(boolean cmp) {
		if(cmp) {
			pv = new Heap<>(2);
		} else  {
			pv = new Heap<>(2, new CompareString());
		}
		pv.add("Test1");
		pv.add("Test2");
		pv.add("Test3");
	}
	
	@Test
	public void testAsList() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testAsList_Empty();
		setUpComparable(); testAsList_One();
		setUpComparable(); testAsList_More();
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testAsList_Empty();
		setUpComparator(); testAsList_One();
		setUpComparator(); testAsList_More();
	}

	private void testAsList_Empty() {
		assertTrue(instance.isEmpty());
		assertEquals(null, instance.asList());
	}

	private void testAsList_One() {
		StringBuilder buff = new StringBuilder();
		List<Integer> list;
		instance.add(32);
		list = instance.asList();
		for(Integer i : list) {
			buff.append(i).append(' ');
		}
		assertEquals("32 ", buff.toString());
	}

	private void testAsList_More() {
		StringBuilder sb = new StringBuilder();
		List<Integer> list;
		for(int i = 0; i < 20; i++) {
			instance.add(i);
		}
		list = instance.asList();
		for(Integer i : list) {
			sb.append(i).append(' ');
		}
		assertEquals("19 18 13 16 17 10 12 9 15 8 7 1 5 4 11 0 6 3 14 2 ", sb.toString());
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
		try {
			setUpComparable(); testRemoveFirst_Exception(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparable(); testRemoveFirst_One();
		setUpComparable(); testRemoveFirst_Multiple();
		//Urejenost z uporabo >> Comparator
		try {
			setUpComparator(); testRemoveFirst_Empty(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparator(); testRemoveFirst_Exception(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparator(); testRemoveFirst_One();
		setUpComparator(); testRemoveFirst_Multiple();
	}
	
	private void testRemoveFirst_Exception() {
		instance.add(32);
		instance.add(23);
		instance.add(15);
		instance.add(63);
		instance.add(86);
		instance.add(9);
		instance.add(3);
		instance.add(99);
		assertEquals(new Integer(99), instance.removeFirst());
		assertEquals(new Integer(86), instance.removeFirst());
		assertEquals(new Integer(63), instance.removeFirst());
		assertEquals(new Integer(32), instance.removeFirst());
		assertEquals(new Integer(23), instance.removeFirst());
		assertEquals(new Integer(15), instance.removeFirst());
		assertEquals(new Integer(9), instance.removeFirst());
		assertEquals(new Integer(3), instance.removeFirst());
		instance.removeFirst();
	}

	private void testRemoveFirst_Empty() {
		instance.removeFirst();
	}
	
	private void testRemoveFirst_One() {
		pv.add("Test");
		assertEquals("Test", pv.removeFirst());
	}

	private void testRemoveFirst_Multiple() {
		pv.add("Test1");
		pv.add("Test5");
		pv.add("Test2");
		pv.add("Test4");
		pv.add("Test3");
		assertEquals("Test5", pv.removeFirst());
		assertEquals("Test4", pv.removeFirst());
		assertEquals("Test3", pv.removeFirst());
		assertEquals("Test2", pv.removeFirst());
		assertEquals("Test1", pv.removeFirst());
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
		try {
			setUpComparable(); testGetFirst_Exception(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparable(); testGetFirst_One();
		setUpComparable(); testGetFirst_Multiple();
		//Urejenost z uporabo >> Comparator
		try {
			setUpComparator(); testGetFirst_Empty(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparator(); testGetFirst_Exception(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparator(); testGetFirst_One();
		setUpComparator(); testGetFirst_Multiple();
	}
	
	private void testGetFirst_Exception() {
		instance.add(41);
		instance.add(13);
		instance.add(89);
		instance.add(32);
		instance.add(18);
		instance.add(9);
		instance.add(100);
		instance.add(48);
		assertEquals(new Integer(100), instance.getFirst());
		instance.removeFirst();
		assertEquals(new Integer(89), instance.getFirst());
		instance.removeFirst();
		assertEquals(new Integer(48), instance.getFirst());
		instance.removeFirst();
		assertEquals(new Integer(41), instance.getFirst());
		instance.removeFirst();
		assertEquals(new Integer(32), instance.getFirst());
		instance.removeFirst();
		assertEquals(new Integer(18), instance.getFirst());
		instance.removeFirst();
		assertEquals(new Integer(13), instance.getFirst());
		instance.removeFirst();
		assertEquals(new Integer(9), instance.getFirst());
		instance.removeFirst();
		instance.getFirst();
	}
	
	private void testGetFirst_Empty() {
		instance.getFirst();
	}
	
	private void testGetFirst_One() {
		pv.add("Test");
		assertEquals("Test", pv.getFirst());
	}

	private void testGetFirst_Multiple() {
		pv.add("Test1");
		assertEquals("Test1", pv.getFirst());
		pv.add("Test3");
		pv.add("Test2");
		assertEquals("Test3", pv.getFirst());
		assertEquals("Test3", pv.getFirst());
	}
	
	@Test
	public void testSize() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testSize_Empty();
		setUpComparable(); testSize_One();
		setUpComparable(); testSize_Basic();
		setUpComparable(); testSize_Multiple();
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testSize_Empty();
		setUpComparator(); testSize_One();
		setUpComparator(); testSize_Basic();
		setUpComparator(); testSize_Multiple();
	}
	
	private void testSize_Basic() {
		int size = (int)(Math.random() * 100) + 1;
		for(int i = 0; i < size; i++) {
			instance.add((int)(Math.random() * 1000) + 1);
		}
		assertEquals(size, instance.size());
	}

	private void testSize_Empty() {
		assertEquals(0, instance.size());
	}
	
	private void testSize_One() {
		pv.add("Test");
		assertEquals(1, pv.size());
	}
	
	private void testSize_Multiple() {
		assertEquals(0, pv.size());
		pv.add("Test");
		assertEquals(1, pv.size());
		pv.add("Test1");
		assertEquals(2, pv.size());
		pv.add("Test2");
		assertEquals(3, pv.size());
	}
	
	@Test
	public void testDepth() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testDepth_Empty();
		setUpComparable(); testDepth_One();
		setUpComparable(); testDepth_Basic();
		setUpComparable(); testDepth_Multiple();
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testDepth_Empty();
		setUpComparator(); testDepth_One();
		setUpComparator(); testDepth_Basic();
		setUpComparator(); testDepth_Multiple();
	}
	
	private void testDepth_Basic() {
		int size = (int)(Math.random() * 7) + 1;
		for(int i = 0; i < size; i++) {
			instance.add((int)(Math.random() * 1000) + 1);
		}
		assertEquals((int)(Math.log(size) / Math.log(2)) + 1, instance.depth());
	}

	private void testDepth_Empty() {
		assertEquals(0, instance.depth());
	}
	
	private void testDepth_One() {
		pv.add("Test1");
		assertEquals(1, pv.depth());
	}

	private void testDepth_Multiple() {
		pv.add("Test1");
		assertEquals(1, pv.depth());
		pv.add("Test5");
		assertEquals(2, pv.depth());
		pv.add("Test2");
		assertEquals(2, pv.depth());
		pv.add("Test4");
		assertEquals(3, pv.depth());
		pv.add("Test3");
		assertEquals(3, pv.depth());
		pv.add("Test6");
		assertEquals(3, pv.depth());
		pv.add("Test8");
		assertEquals(3, pv.depth());
		pv.add("Test7");
		assertEquals(4, pv.depth());
	}
	
	@Test
	public void testIsEmpty() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testIsEmpty_Empty();
		setUpComparable(); testIsEmpty_One();
		setUpComparable(); testIsEmpty_Basic();
		setUpComparable(); testIsEmpty_Multiple();
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testIsEmpty_Empty();
		setUpComparator(); testIsEmpty_One();
		setUpComparator(); testIsEmpty_Basic();
		setUpComparator(); testIsEmpty_Multiple();
	}
	
	private void testIsEmpty_Basic() {
		assertTrue(instance.isEmpty());
		instance.add(32);
		instance.add(89);
		instance.add(43);
		assertFalse(instance.isEmpty());
		instance.removeFirst();
		assertFalse(instance.isEmpty());
		instance.removeFirst();
		instance.removeFirst();
		assertTrue(instance.isEmpty());
	}
	
	private void testIsEmpty_Empty() {
		assertTrue(instance.isEmpty());
	}
	
	private void testIsEmpty_One() {
		pv.add("Test");
		assertFalse(pv.isEmpty());
	}

	private void testIsEmpty_Multiple() {
		pv.add("Test");
		pv.add("Test1");
		pv.add("Test2");
		assertFalse(pv.isEmpty());
	}
	
	@Test
	public void testExists() {
		//Urejenost z uporabo >> Comparable
		setUpComparable(); testExists_Empty();
		setUpComparable(); testExists_One();
		setUpComparable(); testExists_Basic();
		setUpComparable(); testExists_GoodInput();
		setUpComparable(); testExists_BadInput();
		//Urejenost z uporabo >> Comparator
		setUpComparator(); testExists_Empty();
		setUpComparator(); testExists_One();
		setUpComparator(); testExists_Basic();
		setUpComparator(); testExists_GoodInput();
		setUpComparator(); testExists_BadInput();
	}
	
	private void testExists_Basic() {
		instance.add(43);
		instance.add(32);
		instance.add(76);
		instance.add(52);
		instance.add(42);
		instance.add(8);
		instance.add(91);
		instance.add(64);
		assertTrue(instance.exists(8));
		assertTrue(instance.exists(64));
		assertFalse(instance.exists(100));
		assertTrue(instance.exists(52));
		assertFalse(instance.exists(9));
	}

	private void testExists_Empty() {
		assertFalse(instance.exists(12));
	}

	private void testExists_One() {
		instance.add(123);
		assertTrue(instance.exists(123));
		assertFalse(instance.exists(45));
	}

	private void testExists_BadInput() {
		instance.add(32);
		instance.add(23);
		instance.add(89);
		instance.add(40);
		assertFalse(instance.exists(11));
		assertFalse(instance.exists(100));
		assertFalse(instance.exists(36));
	}

	private void testExists_GoodInput() {
		instance.add(13);
		instance.add(42);
		instance.add(56);
		instance.add(43);
		instance.add(52);
		assertTrue(instance.exists(13));
		assertTrue(instance.exists(43));
		assertTrue(instance.exists(56));
		assertTrue(instance.exists(52));
		assertTrue(instance.exists(42));
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
			setUpComparable(); testRemove_Exception(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparable(); testRemove_BadInput_One(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparable(); testRemove_BadInput_Two(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparable(); testRemove_GoodInput();
		setUpComparable(); testRemove_One();
		//Urejenost z uporabo >> Comparator
		try {
			setUpComparator(); testRemove_Empty(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparator(); testRemove_Exception(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparator(); testRemove_BadInput_One(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		try {
			setUpComparator(); testRemove_BadInput_Two(); assert false;
		} catch(java.util.NoSuchElementException e) {
		} catch(Exception e) {
			fail();
		}
		setUpComparator(); testRemove_GoodInput();
		setUpComparator(); testRemove_One();
	}
	
	private void testRemove_Exception() {
		instance.add(34);
		instance.add(18);
		assertEquals(new Integer(18), instance.remove(18));
		instance.add(100);
		instance.add(36);
		instance.add(78);
		assertEquals(new Integer(36), instance.remove(36));
		instance.add(90);
		assertEquals(new Integer(100), instance.remove(100));
		instance.add(10);
		instance.add(18);
		assertEquals(new Integer(78), instance.remove(78));
		assertEquals(new Integer(10), instance.remove(10));
		assertEquals(new Integer(18), instance.remove(18));
		assertEquals(new Integer(90), instance.remove(90));
		assertEquals(new Integer(34), instance.remove(34));
		instance.remove(10);
	}

	private void testRemove_Empty() {
		instance.remove(21);
	}

	private void testRemove_One() {
		instance.add(32);
		instance.remove(32);
	}

	private void testRemove_BadInput_One() {
		instance.add(23);
		instance.remove(33);
	}

	private void testRemove_BadInput_Two() {
		instance.add(23);
		instance.add(73);
		instance.add(10);
		instance.add(53);
		instance.remove(6);
	}

	private void testRemove_GoodInput() {
		instance.add(89);
		instance.add(43);
		instance.add(432);
		instance.add(42);
		instance.add(14);
		instance.add(8);
		assertEquals(new Integer(8), instance.remove(8));
		assertEquals(new Integer(42), instance.remove(42));
		assertEquals(new Integer(432), instance.remove(432));
		assertEquals(new Integer(43), instance.remove(43));
		assertEquals(new Integer(14), instance.remove(14));
		assertEquals(new Integer(89), instance.remove(89));
	}
}