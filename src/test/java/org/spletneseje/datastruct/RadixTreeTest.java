package org.spletneseje.datastruct;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

@SuppressWarnings("SuspiciousMethodCalls")
public class RadixTreeTest {

	private RadixTree<Integer> insRadixTree;
	private RadixTree<String> tree;

	@Before
	public void setUp() {
		this.insRadixTree = new RadixTree<>();
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
		this.insRadixTree.add(23, "test");
		this.insRadixTree.add(32, "team");
		assertEquals(new Integer(23), insRadixTree.get("test"));
		assertEquals(new Integer(32), insRadixTree.get("team"));
	}

	private void testAddMoreOK() {
		this.insRadixTree.add(1, "romane");
		this.insRadixTree.add(2, "romanus");
		this.insRadixTree.add(3, "romulus");
		this.insRadixTree.add(4, "rubens");
		this.insRadixTree.add(5, "ruber");
		this.insRadixTree.add(6, "rubicon");
		this.insRadixTree.add(7, "rubicudus");
		assertEquals(new Integer(4), this.insRadixTree.get("rubens"));
		assertEquals(new Integer(7), this.insRadixTree.get("rubicudus"));
		assertEquals(new Integer(1), this.insRadixTree.get("romane"));
		assertEquals(new Integer(5), this.insRadixTree.get("ruber"));
		assertEquals(new Integer(2), this.insRadixTree.get("romanus"));
		assertEquals(new Integer(6), this.insRadixTree.get("rubicon"));
		assertEquals(new Integer(3), this.insRadixTree.get("romulus"));
	}

	private void testAddNullArgumentExceptions() {
		try{
			this.insRadixTree.add(null, null);
			assert false;
		} catch(NullPointerException e) {}
		try {
			this.insRadixTree.add(null, "hello");
			assert false;
		} catch(NullPointerException e) {}
		try {
			this.insRadixTree.add(12, null);
			assert false;
		} catch(NullPointerException e) {}
	}

	private void testAddDuplicateKeyException() {
		testAddMoreOK();
		testAddOK();
		try {
			this.insRadixTree.add(41, "team");
			assert false;
		} catch(DuplicateKeyException e) {}
		try {
			this.insRadixTree.add(42, "rubicon");
			assert false;
		} catch(DuplicateKeyException e) {}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testPrintTree() {
		testAdd();
		insRadixTree.printTree();
		setUp(); testAddMoreOK();
		insRadixTree.printTree();
		setUp(); testAddMoreOK(); testAdd();
		insRadixTree.printTree();
	}

	@Test
	public void testIsEmpty() {
		assertTrue(insRadixTree.isEmpty());
		this.insRadixTree.add(23, "test");
		this.insRadixTree.add(32, "team");
		assertFalse(insRadixTree.isEmpty());
	}

	@Test
	public void testRemove() {
		testRemoveOK(); setUp();
		testRemoveMore();
	}

	private void testRemoveOK() {
		testAddOK();
		assertTrue(this.insRadixTree.remove("test"));
		assertFalse(this.insRadixTree.remove("Hello"));
	}

	private void testRemoveMore() {
		testAddMoreOK();
		testAddOK();
		assertTrue(this.insRadixTree.remove("test"));
		assertTrue(this.insRadixTree.remove("rubicudus"));
		assertFalse(this.insRadixTree.remove("rubers"));
		assertTrue(this.insRadixTree.remove("romanus"));
		assertFalse(this.insRadixTree.remove("test"));
		assertTrue(this.insRadixTree.remove("team"));
		assertFalse(this.insRadixTree.remove("team"));
		assertTrue(this.insRadixTree.remove("romane"));
		assertTrue(this.insRadixTree.remove("romulus"));
		assertTrue(this.insRadixTree.remove("rubicon"));
		assertTrue(this.insRadixTree.remove("ruber"));
		assertTrue(this.insRadixTree.remove("rubens"));
	}

	@Test
	public void testRemoveObject() {
		testRemoveOKObject(); setUp();
		testRemoveMoreObject();
	}

	private void testRemoveOKObject() {
		testAddOK();
		assertEquals(new Integer(23), insRadixTree.remove((Object) "test"));
		assertNull(insRadixTree.remove((Object) "Hello"));
	}

	private void testRemoveMoreObject() {
		testAddMoreOK();
		testAddOK();
		assertEquals(new Integer(23), insRadixTree.remove((Object) "test"));
		assertEquals(new Integer(7), insRadixTree.remove((Object) "rubicudus"));
		assertNull(insRadixTree.remove((Object) "rubers"));
		assertEquals(new Integer(2), insRadixTree.remove((Object) "romanus"));
		assertNull(insRadixTree.remove((Object) "test"));
		assertEquals(new Integer(32), insRadixTree.remove((Object) "team"));
		assertNull(insRadixTree.remove((Object) "team"));
		assertEquals(new Integer(1), insRadixTree.remove((Object) "romane"));
		assertEquals(new Integer(3), insRadixTree.remove((Object) "romulus"));
		assertEquals(new Integer(6), insRadixTree.remove((Object) "rubicon"));
		assertEquals(new Integer(5), insRadixTree.remove((Object) "ruber"));
		assertEquals(new Integer(4), insRadixTree.remove((Object) "rubens"));
	}

	@Test
	public void testCount() {
		testCountOne(); setUp();
		testCountTwo(); setUp();
		testCountThree();
	}

	private void testCountOne() {
		testAddOK();
		assertEquals(2, this.insRadixTree.count());
	}

	private void testCountTwo() {
		testAddMoreOK();
		assertEquals(7, this.insRadixTree.count());
	}

	private void testCountThree() {
		testAddOK();
		testAddMoreOK();
		assertEquals(9, this.insRadixTree.count());
	}

	@Test
	public void testKeySet() {
		testKeySetOne();
		setUp(); testKeySetTwo();
		setUp(); testKeySetThree();
	}

	private void testKeySetOne() {
		testAddOK();
		assertEquals("[team, test]", insRadixTree.keySet().toString());
	}

	private void testKeySetTwo() {
		testAddMoreOK();
		assertEquals("[romane, romanus, romulus, rubens, ruber, rubicon, rubicudus]", insRadixTree.keySet().toString());
	}

	private void testKeySetThree() {
		testAddMoreOK();
		testAddOK();
		assertEquals("[romane, romanus, romulus, rubens, ruber, rubicon, rubicudus, team, test]", insRadixTree.keySet().toString());
	}

	@Test
	public void testAsList() {
		testAsListOne();
		setUp(); testAsListTwo();
		setUp(); testAsListThree();
	}

	private void testAsListOne() {
		testAddOK();
		assertEquals("[23, 32]", this.insRadixTree.asList().toString());
	}

	private void testAsListTwo() {
		testAddMoreOK();
		assertEquals("[1, 2, 3, 4, 5, 6, 7]", this.insRadixTree.asList().toString());
	}

	private void testAsListThree() {
		testAddMoreOK();
		testAddOK();
		assertEquals("[1, 2, 3, 4, 5, 6, 7, 23, 32]", this.insRadixTree.asList().toString());
	}

	private void addSequenceOne() {
		tree.add("abcd", "abcd");
		tree.add("abce", "abce");
	}

	@Test
	public void testSearchForPartialParentAndLeafKeyWhenOverlapExists() {
		addSequenceOne();
		assertNull(tree.get("abe"));
		assertNull(tree.get("abd"));
	}

	@Test
	public void testSearchForLeafNodesWhenOverlapExists() {
		addSequenceOne();
		assertEquals("abcd", tree.get("abcd"));
		assertEquals("abce", tree.get("abce"));
	}

	@Test
	public void testSearchForStringSmallerThanSharedParentWhenOverlapExists() {
		addSequenceOne();
		assertNull(tree.get("abc"));
		assertNull(tree.get("ab"));
		assertNull(tree.get("a"));
	}

	@Test
	public void testAddWithString() {
		tree.add("apple", "apple");
		tree.add("bat", "bat");
		tree.add("ape", "ape");
		tree.add("bath", "bath");
		tree.add("banana", "banana");
		assertEquals("apple", tree.get("apple"));
		assertEquals("bat", tree.get("bat"));
		assertEquals("ape", tree.get("ape"));
		assertEquals("bath", tree.get("bath"));
		assertEquals("banana", tree.get("banana"));
	}

	@Test
	public void testAddExistingUnrealNodeConvertsItToReal() {
		tree.add("applepie", "applepie");
		tree.add("applecrisp", "applecrisp");
		assertNull(tree.get("apple"));
		tree.add("apple", "apple");
		assertEquals("apple", tree.get("apple"));
	}

	@Test(expected = DuplicateKeyException.class)
	public void testDuplicatesNotAllowed() {
		tree.add("apple", "apple");
		tree.add("appleOne", "apple");
	}

	@Test
	public void testAddWithRepeatingPatternsInKey() {
		tree.add("xbox 360", "xbox 360");
		tree.add("xbox", "xbox");
		tree.add("xbox 360 games", "xbox 360 games");
		tree.add("xbox games", "xbox games");
		tree.add("xbox xbox 360", "xbox xbox 360");
		tree.add("xbox xbox", "xbox xbox");
		tree.add("xbox 360 xbox games", "xbox 360 xbox games");
		tree.add("xbox games 360", "xbox games 360");
		tree.add("xbox 360 360", "xbox 360 360");
		tree.add("xbox 360 xbox 360", "xbox 360 xbox 360");
		tree.add("360 xbox games 360", "360 xbox games 360");
		tree.add("xbox xbox 361", "xbox xbox 361");
		assertEquals(12, tree.count());
	}

	@Test
	public void testRemoveNodeWithNoChildren() {
		tree.add("apple", "apple");
		assertTrue(tree.remove("apple"));
	}

	@Test
	public void testRemoveNodeWithOneChild() {
		tree.add("apple", "apple");
		tree.add("applepie", "applepie");
		assertTrue(tree.remove("apple"));
		assertNull(tree.get("apple"));
		assertEquals("applepie", tree.get("applepie"));
	}

	@Test
	public void testRemoveNodeWithOneChildObject() {
		tree.add("apple", "apple");
		tree.add("applepie", "applepie");
		assertEquals("apple", tree.remove((Object) "apple"));
		assertNull(tree.get("apple"));
		assertEquals("applepie", tree.get("applepie"));
	}

	@Test
	public void testRemoveNodeWithMultipleChildren() {
		tree.add("apple", "apple");
		tree.add("applepie", "applepie");
		tree.add("applecrisp", "applecrisp");
		assertTrue(tree.remove("apple"));
		assertNull(tree.get("apple"));
		assertEquals("applepie", tree.get("applepie"));
		assertEquals("applecrisp", tree.get("applecrisp"));
	}

	@Test
	public void testRemoveNodeWithMultipleChildrenObject() {
		tree.add("apple", "apple");
		tree.add("applepie", "applepie");
		tree.add("applecrisp", "applecrisp");
		assertEquals("apple", tree.remove((Object) "apple"));
		assertNull(tree.get("apple"));
		assertEquals("applepie", tree.get("applepie"));
		assertEquals("applecrisp", tree.get("applecrisp"));
	}

	@Test
	public void testCantDeleteSomethingThatDoesntExist() {
		assertFalse(tree.remove("apple"));
	}

	@Test
	public void testCantDeleteSomethingThatDoesntExistObject() {
		assertNull(tree.remove((Object) "apple"));
	}

	@Test
	public void testCantDeleteSomethingThatWasAlreadyDeleted() {
		tree.add("apple", "apple");
		assertTrue(tree.remove("apple"));
		assertFalse(tree.remove("apple"));
	}

	@Test
	public void testCantDeleteSomethingThatWasAlreadyDeletedObject() {
		tree.add("apple", "apple");
		assertEquals("apple", tree.remove((Object) "apple"));
		assertNull(tree.remove((Object) "apple"));
	}

	@Test
	public void testChildrenNotAffectedWhenOneIsDeleted() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.add("applepie", "applepie");
		tree.add("ape", "ape");
		assertTrue(tree.remove("apple"));
		assertEquals("appleshack", tree.get("appleshack"));
		assertEquals("applepie", tree.get("applepie"));
		assertEquals("ape", tree.get("ape"));
		assertNull(tree.get("apple"));
	}

	@Test
	public void testChildrenNotAffectedWhenOneIsDeletedObject() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.add("applepie", "applepie");
		tree.add("ape", "ape");
		assertEquals("apple", tree.remove((Object) "apple"));
		assertEquals("appleshack", tree.get("appleshack"));
		assertEquals("applepie", tree.get("applepie"));
		assertEquals("ape", tree.get("ape"));
		assertNull(tree.get("apple"));
	}

	@Test
	public void testSiblingsNotAffectedWhenOneIsDeleted() {
		tree.add("apple", "apple");
		tree.add("ball", "ball");
		assertTrue(tree.remove("apple"));
		assertEquals("ball", tree.get("ball"));
	}

	@Test
	public void testSiblingsNotAffectedWhenOneIsDeletedObject() {
		tree.add("apple", "apple");
		tree.add("ball", "ball");
		assertEquals("apple", tree.remove((Object) "apple"));
		assertEquals("ball", tree.get("ball"));
	}

	@Test
	public void testCantRemoveUnrealNode() {
		tree.add("apple", "apple");
		tree.add("ape", "ape");
		assertFalse(tree.remove("ap"));
	}

	@Test
	public void testCantRemoveUnrealNodeObject() {
		tree.add("apple", "apple");
		tree.add("ape", "ape");
		assertNull(tree.remove((Object) "ap"));
	}

	@Test
	public void testCantFindRootNode() {
		assertNull(tree.get(""));
	}

	@Test
	public void testFindSimpleInsert()  {
		tree.add("apple", "apple");
		assertEquals("apple", tree.get("apple"));
	}

	@Test
	public void testCantFindNonexistantNode() {
		assertNull(tree.get("apple"));
	}

	@Test
	public void testCantFindUnrealNode() {
		tree.add("apple", "apple");
		tree.add("ape", "ape");
		assertNull(tree.get("ap"));
	}

	@Test
	public void testGetSize() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.add("appleshackcream", "appleshackcream");
		tree.add("applepie", "applepie");
		tree.add("ape", "ape");
		assertEquals(5, tree.count());
	}

	@Test
	public void testDeleteReducesSize() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.remove("appleshack");
		assertEquals(1, tree.count());
	}

	@Test
	public void testDeleteReducesSizeObject() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.remove((Object) "appleshack");
		assertEquals(1, tree.count());
	}

	@Test
	public void testAddOne() {
		tree.add("applepie", "applepie");
		tree.add("appleshack", "appleshack");
		tree.add("apple", "apple");
		assertNotNull(tree.get("apple"));
		assertNotNull(tree.get("applepie"));
	}

	@Test
	public void testIterator() {
		testIteratorOne(); setUp();
		testIteratorTwo(); setUp();
		testIteratorThree();
	}

	private void testIteratorOne() {
		testAddOK();
		Iterator<Integer> it = this.insRadixTree.iterator();
		assertEquals(new Integer(23), it.next());
		assertEquals(new Integer(32), it.next());
	}

	private void testIteratorTwo() {
		String niz = "";
		testAddMoreOK();
		for (Integer anInsRadixTree : this.insRadixTree) {
			niz += " " + anInsRadixTree.toString();
		}
		assertEquals(" 1 2 3 4 5 6 7", niz);
	}

	private void testIteratorThree() {
		String niz = "";
		testAddOK();
		testAddMoreOK();
		for (Integer anInsRadixTree : this.insRadixTree) {
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
		insRadixTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals("[[team,32][test,23]]", builder.toString());
	}

	private void testEntrySetTwo() {
		testAddMoreOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		insRadixTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals("[[romane,1][romanus,2][romulus,3][rubens,4][ruber,5][rubicon,6][rubicudus,7]]", builder.toString());
	}

	private void testEntrySetThree() {
		testAddMoreOK(); testAddOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		insRadixTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals("[[romane,1][romanus,2][romulus,3][rubens,4][ruber,5][rubicon,6][rubicudus,7][team,32][test,23]]", builder.toString());
	}
}
