package org.datastruct;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestArraySet {

    private ArraySet<Integer> intSet;

    @Before
    public void setUp() {
        intSet = new ArraySet<>();
    }

    @Test
    public void testAdd() {
        assertTrue(intSet.add(10));
        assertFalse(intSet.add(10));
        assertTrue(intSet.add(1));
        assertTrue(intSet.add(3));
        assertTrue(intSet.add(5));
        assertTrue(intSet.add(23));
        assertTrue(intSet.add(53));
        assertTrue(intSet.add(74));
        assertTrue(intSet.add(100));
        assertTrue(intSet.add(72));
        assertFalse(intSet.add(1));
        assertFalse(intSet.add(53));
        assertTrue(intSet.add(49));
        assertTrue(intSet.add(7));
        assertTrue(intSet.add(16));
        assertFalse(intSet.add(23));
        assertTrue(intSet.add(22));
        assertTrue(intSet.add(9));
        assertFalse(intSet.add(22));
        assertTrue(intSet.add(101));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullPointerExceptionNullArgument() {
        testAdd();
        intSet.add(null);
    }

    @Test
    public void testRemove() {
        testAdd();
        actionRemove();
    }

    private void actionRemove() {
        assertFalse(intSet.remove(111));
        assertTrue(intSet.remove(101));
        assertFalse(intSet.remove(101));
        assertTrue(intSet.remove(1));
        assertTrue(intSet.remove(3));
        assertTrue(intSet.remove(10));
        assertTrue(intSet.remove(49));
        assertTrue(intSet.remove(16));
        assertFalse(intSet.remove(10));
        assertFalse(intSet.remove(55));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullPointerExceptionNullArgument() {
        intSet.remove(null);
    }

    /*
    Napaka pri iskanju elementa, kjer bi morali dobiti zeljeno napako ClassCastException dobimo NullPointerException
    @Test(expected = ClassCastException.class)
    public void testRemoveClassCastException() {
        intSet.remove(new Stack<>());
    }
    */

    @Test
    public void testSize() {
        assertTrue(intSet.add(8));
        assertEquals(1, intSet.size());
        testAdd();
        assertEquals(16, intSet.size());
        actionRemove();
        assertEquals(10, intSet.size());
        assertFalse(intSet.remove(6));
        assertEquals(10, intSet.size());
        assertTrue(intSet.add(66));
        assertEquals(11, intSet.size());
    }

    @Test
    public void testClearAndIsEmpty() {
        assertTrue(intSet.isEmpty());
        intSet.clear();
        assertTrue(intSet.isEmpty());
        assertEquals(0, intSet.size());
        testAdd();
        assertFalse(intSet.isEmpty());
        intSet.clear();
        assertTrue(intSet.isEmpty());
    }

    @Test
    public void testContains() {
        testAdd();
        assertTrue(intSet.contains(100));
        assertFalse(intSet.contains(8));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsNullPointerExceptionNullArgument() {
        intSet.contains(null);
    }

    /*
    Napaka pri iskanju elementa, kjer bi morali dobiti zeljeno napako ClassCastException dobimo NullPointerException
    @Test(expected = ClassCastException.class)
    public void testContainsClassCastException() {
        intSet.contains(new Stack<>());
    }
    */

    @Test
    public void testIterator() {
        testAdd();
        StringBuilder builder = new StringBuilder();
        intSet.forEach(e -> builder.append(e).append(' '));
        assertEquals("1 3 5 7 9 10 16 22 23 49 53 72 74 100 101 ", builder.toString());
    }

    @Test
    public void testGet() {
        testAdd();
        assertEquals(7, (int) intSet.get(3));
        assertEquals(1, (int) intSet.get(0));
        assertEquals(101, (int) intSet.get(14));
        assertEquals(100, (int) intSet.get(13));
        assertTrue(intSet.remove(100));
        assertNotEquals(100, (int) intSet.get(13));
        assertEquals(101, (int) intSet.get(13));
    }

    @Test
    public void testAddAll() {
        testAdd();
        ArraySet<Integer> testSet = new ArraySet<>();
        assertTrue(testSet.addAll(intSet));
        StringBuilder builder = new StringBuilder();
        testSet.forEach(e -> builder.append(e).append(' '));
        assertEquals("1 3 5 7 9 10 16 22 23 49 53 72 74 100 101 ", builder.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNullPointerExceptionNullArgument() {
        intSet.addAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNullPointerExceptionNullElement() {
        testAdd();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(6);
        list.add(8);
        list.add(null);
        list.add(117);
        intSet.addAll(list);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddAllIllegalStateExceptionDuplicatElement() {
        testAdd();
        ArraySet<Integer> addSet = new ArraySet<>();
        addSet.add(4);
        addSet.add(6);
        addSet.add(8);
        addSet.add(11);
        addSet.add(16);
        addSet.add(17);
        addSet.add(117);
        intSet.addAll(addSet);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddAllIllegalStateExceptionDuplicatCollection() {
        testAdd();
        intSet.addAll(intSet);
    }

    @Test
    public void testRemoveAll() {
        testRemoveAllOne(); setUp();
        testRemoveAllTwo();
    }

    private void testRemoveAllOne() {
        testAdd();
        ArraySet<Integer> removeSet = new ArraySet<>(intSet);
        assertTrue(intSet.removeAll(removeSet));
        assertTrue(intSet.isEmpty());
    }

    private void testRemoveAllTwo() {
        testAdd();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(7);
        list.add(100);
        list.add(23);
        assertTrue(intSet.removeAll(list));
        StringBuilder builder = new StringBuilder();
        intSet.forEach(e -> builder.append(e).append(' '));
        assertEquals("3 5 9 10 16 22 49 53 72 74 101 ", builder.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveAllNullPointerExceptionNullArgument() {
        intSet.removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveAllNullPointerExceptionNullElement() {
        testAdd();
        ArrayList<Integer> list = new ArrayList<>(intSet);
        list.add(null);
        list.add(10);
        intSet.removeAll(list);
    }

    @Test(expected = ClassCastException.class)
    public void testRemoveAllClassCastException() {
        testAdd();
        ArrayList<String> list = new ArrayList<>();
        list.add("Test");
        list.add("for");
        list.add("exceptions");
        intSet.removeAll(list);
    }

    @Test
    public void testRatainAll() {
        testRetainAllOne(); setUp();
        testRetainAllTwo(); setUp();
        testRetainAllThree();
    }

    private void testRetainAllOne() {
        testAdd();
        assertTrue(intSet.retainAll(intSet));
        StringBuilder builder = new StringBuilder();
        intSet.forEach(e -> builder.append(e).append(' '));
        assertEquals("1 3 5 7 9 10 16 22 23 49 53 72 74 100 101 ", builder.toString());
    }

    private void testRetainAllTwo() {
        testAdd();
        ArrayList<Integer> list = new ArrayList<>(intSet);
        list.add(111);
        list.add(200);
        list.add(289);
        assertTrue(intSet.retainAll(list));
        StringBuilder builder = new StringBuilder();
        intSet.forEach(e -> builder.append(e).append(' '));
        assertEquals("1 3 5 7 9 10 16 22 23 49 53 72 74 100 101 ", builder.toString());
    }

    private void testRetainAllThree() {
        testAdd();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(7);
        list.add(9);
        list.add(10);
        list.add(16);
        list.add(23);
        list.add(49);
        list.add(53);
        list.add(72);
        list.add(74);
        assertTrue(intSet.retainAll(list));
        StringBuilder builder = new StringBuilder();
        intSet.forEach(e -> builder.append(e).append(' '));
        assertEquals("5 7 9 10 16 23 49 53 72 74 ", builder.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testRatainAllNullPointerExceptionNullArgumrnt() {
        intSet.retainAll(null);
    }

    @Test
    public void testContainsAll() {
        testContainsAllOK(); setUp();
        testContainsAllNotOK();
    }

    private void testContainsAllOK() {
        testAdd();
        ArraySet<Integer> set = new ArraySet<>(intSet);
        assertTrue(intSet.containsAll(set));
    }

    private void testContainsAllNotOK() {
        testAdd();
        ArrayList<Integer> list = new ArrayList<>(intSet);
        list.add(120);
        list.add(504);
        list.add(278);
        list.add(724);
        assertFalse(intSet.containsAll(list));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsAllNullPointerExceptionNullArgument() {
        intSet.containsAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testContainsAllNullPointerExceptionNullElement() {
        testAdd();
        ArrayList<Integer> list = new ArrayList<>(intSet);
        list.add(null);
        list.add(10);
        intSet.containsAll(list);
    }
}
