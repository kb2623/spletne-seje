package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings("SuspiciousMethodCalls")
public class RadixTreeTest {

	private RadixTree<Niz, Integer> intRTree;
	private RadixTree<Niz, String> tree;

	@Before
	public void setUp() {
		this.intRTree = new RadixTree<>();
		this.tree = new RadixTree<>();
	}

	@Test
	public void testAdd() {
		testAddOK(); setUp();
		testAddMoreOK(); setUp();
		testAddNullArgumentExceptions(); setUp();
		testAddDuplicateKeyException();
	}

	private void testAddOK() {
		this.intRTree.add(23, new Niz("test"));
		this.intRTree.add(32, new Niz("team"));
		assertEquals(new Integer(23), intRTree.get(new Niz("test")));
		assertEquals(new Integer(32), intRTree.get(new Niz("team")));
	}

	private void testAddMoreOK() {
		this.intRTree.add(1, new Niz("romane"));
		this.intRTree.add(2, new Niz("romanus"));
		this.intRTree.add(3, new Niz("romulus"));
		this.intRTree.add(4, new Niz("rubens"));
		this.intRTree.add(5, new Niz("ruber"));
		this.intRTree.add(6, new Niz("rubicon"));
		this.intRTree.add(7, new Niz("rubicudus"));
		assertEquals(new Integer(4), this.intRTree.get(new Niz("rubens")));
		assertEquals(new Integer(7), this.intRTree.get(new Niz("rubicudus")));
		assertEquals(new Integer(1), this.intRTree.get(new Niz("romane")));
		assertEquals(new Integer(5), this.intRTree.get(new Niz("ruber")));
		assertEquals(new Integer(2), this.intRTree.get(new Niz("romanus")));
		assertEquals(new Integer(6), this.intRTree.get(new Niz("rubicon")));
		assertEquals(new Integer(3), this.intRTree.get(new Niz("romulus")));
	}

	private void testAddNullArgumentExceptions() {
		try{
			this.intRTree.add(null, null);
			assert false;
		} catch(NullPointerException e) {}
		try {
			this.intRTree.add(null, new Niz("hello"));
			assert false;
		} catch(NullPointerException e) {}
		try {
			this.intRTree.add(12, null);
			assert false;
		} catch(NullPointerException e) {}
	}

	private void testAddDuplicateKeyException() {
		testAddMoreOK();
		testAddOK();
		assertNotNull(this.intRTree.add(41, new Niz("team")));
		assertNotNull(this.intRTree.add(42, new Niz("rubicon")));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testPrintTree() {
		testAdd();
		intRTree.printTree();
		setUp();
		testAddMoreOK();
		intRTree.printTree();
		setUp();
		testAddMoreOK();
		testAdd();
		intRTree.printTree();
	}

	@Test
	public void testIsEmpty() {
		assertTrue(intRTree.isEmpty());
		this.intRTree.add(23, new Niz("test"));
		this.intRTree.add(32, new Niz("team"));
		assertFalse(intRTree.isEmpty());
	}

	@Test
	public void testRemove() {
		testRemoveOK(); setUp();
		testRemoveMore();
	}

	private void testRemoveOK() {
		testAddOK();
		assertTrue(this.intRTree.remove(new Niz("test")));
		assertFalse(this.intRTree.remove(new Niz("Hello")));
	}

	private void testRemoveMore() {
		testAddMoreOK();
		testAddOK();
		assertTrue(this.intRTree.remove(new Niz("test")));
		assertTrue(this.intRTree.remove(new Niz("rubicudus")));
		assertFalse(this.intRTree.remove(new Niz("rubers")));
		assertTrue(this.intRTree.remove(new Niz("romanus")));
		assertFalse(this.intRTree.remove(new Niz("test")));
		assertTrue(this.intRTree.remove(new Niz("team")));
		assertFalse(this.intRTree.remove(new Niz("team")));
		assertTrue(this.intRTree.remove(new Niz("romane")));
		assertTrue(this.intRTree.remove(new Niz("romulus")));
		assertTrue(this.intRTree.remove(new Niz("rubicon")));
		assertTrue(this.intRTree.remove(new Niz("ruber")));
		assertTrue(this.intRTree.remove(new Niz("rubens")));
	}

	@Test
	public void testRemoveObject() {
		testRemoveOKObject(); setUp();
		testRemoveMoreObject();
	}

	private void testRemoveOKObject() {
		testAddOK();
		assertEquals(new Integer(23), ((Map) intRTree).remove(new Niz("test")));
		assertNull(((Map) intRTree).remove(new Niz("Hello")));
	}

	private void testRemoveMoreObject() {
		testAddMoreOK();
		testAddOK();
		assertEquals(new Integer(23), ((Map) intRTree).remove("test"));
		assertEquals(new Integer(7), ((Map) intRTree).remove("rubicudus"));
		assertNull(((Map) intRTree).remove("rubers"));
		assertEquals(new Integer(2), ((Map) intRTree).remove("romanus"));
		assertNull(((Map) intRTree).remove("test"));
		assertEquals(new Integer(32), ((Map) intRTree).remove("team"));
		assertNull(((Map) intRTree).remove("team"));
		assertEquals(new Integer(1), ((Map) intRTree).remove("romane"));
		assertEquals(new Integer(3), ((Map) intRTree).remove("romulus"));
		assertEquals(new Integer(6), ((Map) intRTree).remove("rubicon"));
		assertEquals(new Integer(5), ((Map) intRTree).remove("ruber"));
		assertEquals(new Integer(4), ((Map) intRTree).remove("rubens"));
	}

	@Test
	public void testCount() {
		testCountOne(); setUp();
		testCountTwo(); setUp();
		testCountThree();
	}

	private void testCountOne() {
		testAddOK();
		assertEquals(2, this.intRTree.count());
	}

	private void testCountTwo() {
		testAddMoreOK();
		assertEquals(7, this.intRTree.count());
	}

	private void testCountThree() {
		testAddOK();
		testAddMoreOK();
		assertEquals(9, this.intRTree.count());
	}

	@Test
	public void testKeySet() {
		testKeySetOne();
		setUp(); testKeySetTwo();
		setUp(); testKeySetThree();
	}

	private void testKeySetOne() {
		testAddOK();
		assertEquals("[test, team]", intRTree.keySet().toString());
	}

	private void testKeySetTwo() {
		testAddMoreOK();
		assertEquals("[ruber, romulus, romanus, rubicudus, rubicon, rubens, romane]", intRTree.keySet().toString());
	}

	private void testKeySetThree() {
		testAddMoreOK();
		testAddOK();
		assertEquals("[ruber, romulus, test, romanus, rubicudus, team, rubicon, rubens, romane]", intRTree.keySet().toString());
	}

	@Test
	public void testAsList() {
		testAsListOne();
		setUp(); testAsListTwo();
		setUp(); testAsListThree();
	}

	private void testAsListOne() {
		testAddOK();
		assertEquals("[32, 23]", this.intRTree.asList().toString());
	}

	private void testAsListTwo() {
		testAddMoreOK();
		assertEquals("[7, 6, 5, 4, 3, 2, 1]", this.intRTree.asList().toString());
	}

	private void testAsListThree() {
		testAddMoreOK();
		testAddOK();
		assertEquals("[32, 23, 7, 6, 5, 4, 3, 2, 1]", this.intRTree.asList().toString());
	}

	private void addSequenceOne() {
		tree.add("abcd", new Niz("abcd"));
		tree.add("abce", new Niz("abce"));
	}

	@Test
	public void testSearchForPartialParentAndLeafKeyWhenOverlapExists() {
		addSequenceOne();
		assertNull(tree.get(new Niz("abe")));
		assertNull(tree.get(new Niz("abd")));
	}

	@Test
	public void testSearchForLeafNodesWhenOverlapExists() {
		addSequenceOne();
		assertEquals("abcd", tree.get(new Niz("abcd")));
		assertEquals("abce", tree.get(new Niz("abce")));
	}

	@Test
	public void testSearchForStringSmallerThanSharedParentWhenOverlapExists() {
		addSequenceOne();
		assertNull(tree.get(new Niz("abc")));
		assertNull(tree.get(new Niz("ab")));
		assertNull(tree.get(new Niz("a")));
	}

	@Test
	public void testAddWithString() {
		tree.add("apple", new Niz("apple"));
		tree.add("bat", new Niz("bat"));
		tree.add("ape", new Niz("ape"));
		tree.add("bath", new Niz("bath"));
		tree.add("banana", new Niz("banana"));
		assertEquals("apple", tree.get(new Niz("apple")));
		assertEquals("bat", tree.get(new Niz("bat")));
		assertEquals("ape", tree.get(new Niz("ape")));
		assertEquals("bath", tree.get(new Niz("bath")));
		assertEquals("banana", tree.get(new Niz("banana")));
	}

	@Test
	public void testAddExistingUnrealNodeConvertsItToReal() {
		tree.add("applepie", new Niz("applepie"));
		tree.add("applecrisp", new Niz("applecrisp"));
		assertNull(tree.get(new Niz("apple")));
		tree.add("apple", new Niz("apple"));
		assertEquals("apple", tree.get(new Niz("apple")));
	}

	@Test
	public void testDuplicates() {
		assertNull(tree.add("apple", new Niz("apple")));
		assertNotNull(tree.add("appleOne", new Niz("apple")));
	}

	@Test
	public void testAddWithRepeatingPatternsInKey() {
		tree.add("xbox 360", new Niz("xbox 360"));
		tree.add("xbox", new Niz("xbox"));
		tree.add("xbox 360 games", new Niz("xbox 360 games"));
		tree.add("xbox games", new Niz("xbox games"));
		tree.add("xbox xbox 360", new Niz("xbox xbox 360"));
		tree.add("xbox xbox", new Niz("xbox xbox"));
		tree.add("xbox 360 xbox games", new Niz("xbox 360 xbox games"));
		tree.add("xbox games 360", new Niz("xbox games 360"));
		tree.add("xbox 360 360", new Niz("xbox 360 360"));
		tree.add("xbox 360 xbox 360", new Niz("xbox 360 xbox 360"));
		tree.add("360 xbox games 360", new Niz("360 xbox games 360"));
		tree.add("xbox xbox 361", new Niz("xbox xbox 361"));
		assertEquals(12, tree.count());
	}

	@Test
	public void testRemoveNodeWithNoChildren() {
		tree.add("apple", new Niz("apple"));
		assertTrue(tree.remove(new Niz("apple")));
	}

	@Test
	public void testRemoveNodeWithOneChild() {
		tree.add("apple", new Niz("apple"));
		tree.printTree();
		tree.add("applepie", new Niz("applepie"));
		tree.printTree();
		assertTrue(tree.remove(new Niz("apple")));
		tree.printTree();
		assertNull(tree.get(new Niz("apple")));
		assertEquals("applepie", tree.get(new Niz("applepie")));
	}

	@Test
	public void testRemoveNodeWithMultipleChildren() {
		tree.add("apple", new Niz("apple"));
		tree.add("applepie", new Niz("applepie"));
		tree.add("applecrisp", new Niz("applecrisp"));
		assertTrue(tree.remove(new Niz("apple")));
		assertNull(tree.get(new Niz("apple")));
		assertEquals("applepie", tree.get(new Niz("applepie")));
		assertEquals("applecrisp", tree.get(new Niz("applecrisp")));
	}

	@Test
	public void testCantDeleteSomethingThatDoesntExist() {
		assertFalse(tree.remove(new Niz("apple")));
	}

	@Test
	public void testCantDeleteSomethingThatWasAlreadyDeleted() {
		tree.add("apple", new Niz("apple"));
		assertTrue(tree.remove(new Niz("apple")));
		assertFalse(tree.remove(new Niz("apple")));
	}

	@Test
	public void testChildrenNotAffectedWhenOneIsDeleted() {
		tree.add("apple", new Niz("apple"));
		tree.add("appleshack", new Niz("appleshack"));
		tree.add("applepie", new Niz("applepie"));
		tree.add("ape", new Niz("ape"));
		assertTrue(tree.remove(new Niz("apple")));
		assertEquals("appleshack", tree.get(new Niz("appleshack")));
		assertEquals("applepie", tree.get(new Niz("applepie")));
		assertEquals("ape", tree.get(new Niz("ape")));
		assertNull(tree.get(new Niz("apple")));
	}

	@Test
	public void testSiblingsNotAffectedWhenOneIsDeleted() {
		tree.add("apple", new Niz("apple"));
		tree.add("ball", new Niz("ball"));
		assertTrue(tree.remove(new Niz("apple")));
		assertEquals("ball", tree.get(new Niz("ball")));
	}

	@Test
	public void testCantRemoveUnrealNode() {
		tree.add("apple", new Niz("apple"));
		tree.add("ape", new Niz("ape"));
		assertFalse(tree.remove(new Niz("ap")));
	}

	@Test
	public void testCantFindRootNode() {
		assertNull(tree.get(""));
	}

	@Test
	public void testFindSimpleInsert() {
		tree.add("apple", new Niz("apple"));
		assertEquals("apple", tree.get(new Niz("apple")));
	}

	@Test
	public void testCantFindNonexistantNode() {
		assertNull(tree.get("apple"));
	}

	@Test
	public void testCantFindUnrealNode() {
		tree.add("apple", new Niz("apple"));
		tree.add("ape", new Niz("ape"));
		assertNull(tree.get(new Niz("ap")));
	}

	@Test
	public void testGetSize() {
		tree.add("apple", new Niz("apple"));
		tree.add("appleshack", new Niz("appleshack"));
		tree.add("appleshackcream", new Niz("appleshackcream"));
		tree.add("applepie", new Niz("applepie"));
		tree.add("ape", new Niz("ape"));
		assertEquals(5, tree.count());
	}

	@Test
	public void testDeleteReducesSize() {
		tree.add("apple", new Niz("apple"));
		tree.add("appleshack", new Niz("appleshack"));
		tree.remove(new Niz("appleshack"));
		assertEquals(1, tree.count());
	}

	@Test
	public void testAddOne() {
		tree.add("applepie", new Niz("applepie"));
		tree.add("appleshack", new Niz("appleshack"));
		tree.add("apple", new Niz("apple"));
		assertNotNull(tree.get(new Niz("apple")));
		assertNotNull(tree.get(new Niz("applepie")));
	}

	@Test
	public void testIterator() {
		testIteratorOne(); setUp();
		testIteratorTwo(); setUp();
		testIteratorThree();
	}

	private void testIteratorOne() {
		testAddOK();
		Iterator<Integer> it = this.intRTree.iterator();
		assertEquals(new Integer(23), it.next());
		assertEquals(new Integer(32), it.next());
	}

	private void testIteratorTwo() {
		String niz = "";
		testAddMoreOK();
		for (Integer anInsRadixTree : this.intRTree) {
			niz += " " + anInsRadixTree.toString();
		}
		assertEquals(" 1 2 3 4 5 6 7", niz);
	}

	private void testIteratorThree() {
		String niz = "";
		testAddOK();
		testAddMoreOK();
		for (Integer anInsRadixTree : this.intRTree) {
			niz += " " + anInsRadixTree.toString();
		}
		assertEquals(" 23 32 1 2 3 4 5 6 7", niz);
	}

	@Test
	public void testEntrySet() {
		testEntrySetOne();
		setUp(); testEntrySetTwo();
		setUp(); testEntrySetThree();
	}

	private void testEntrySetOne() {
		testAddOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		intRTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals(new Integer(32), intRTree.get(new Niz("team")));
		assertEquals(new Integer(23), intRTree.get(new Niz("test")));
	}

	private void testEntrySetTwo() {
		testAddMoreOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		intRTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals("[[ruber,5][romulus,3][romanus,2][rubicudus,7][rubicon,6][rubens,4][romane,1]]", builder.toString());
	}

	private void testEntrySetThree() {
		testAddMoreOK(); testAddOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		intRTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals("[[ruber,5][romulus,3][test,23][romanus,2][rubicudus,7][team,32][rubicon,6][rubens,4][romane,1]]", builder.toString());
	}

	class Niz implements Sequence<Niz> {

		private String value;

		Niz(String s) {
			value = s;
		}

		@Override
		public int equalDistance(Niz s) {
			int distance = 0;
			for (int i = 0; i < value.length(); i++) {
				if (i < s.value.length()) {
					if (value.charAt(i) == s.value.charAt(i)) {
						distance++;
					} else {
						return distance;
					}
				} else {
					return distance;
				}
			}
			return distance;
		}

		@Override
		public int length() {
			return value.length();
		}

		@Override
		public Niz append(Niz s) {
			return new Niz(value + s);
		}

		@Override
		public Niz subSequence(int start, int end) {
			return new Niz(value.substring(start, end));
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			if (o == this) return true;
			if (o instanceof String) {
				return value.equals(o);
			} else {
				Niz s = (Niz) o;
				if (!value.equals(s.value)) return false;
				return true;
			}
		}

		@Override
		public int hashCode() {
			int hash = 0;
			for (char c : value.toCharArray()) {
				hash += (int) c;
			}
			return hash;
		}

		@Override
		public String toString() {
			return value;
		}
	}
}
