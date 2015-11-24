package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

enum TestE {
	Da, Ne, Yes, No;
}

public class ClassPoolTest {

	@Before
	public void setUp() throws IOException {
		ClassPool.initClassPool(10, ClassLoader.getSystemResource("ClassPool.properties").getPath(), ClassLoader.getSystemClassLoader());
	}

	@Test
	public void testGetObject() throws Exception {
		assertEquals(new TestObject1("a", 97, false), ClassPool.getObject(TestObject1.class, "a", 97, true));
		assertEquals(new TestObject1("a", 97, false), ClassPool.getObject(TestObject1.class, "a", 97, true));
		assertEquals(new TestObject1("ba", 89, false), ClassPool.getObject(TestObject1.class, "ba", 89, true));
		assertEquals(new TestObject1("ac", 97, false), ClassPool.getObject(TestObject1.class, "ac", 97, true));
		assertEquals(new TestObject1("a", 97, false), ClassPool.getObject(TestObject1.class, "a", 97, true));
		/*
		assertEquals(new TestObject2(TestE.Ne, false), ClassPool.getObject(TestObject2.class, TestE.Ne, true));
		assertEquals(new TestObject2(TestE.Yes, false), ClassPool.getObject(TestObject2.class, TestE.Yes, true));
		assertEquals(new TestObject2(TestE.Yes, false), ClassPool.getObject(TestObject2.class, TestE.Yes, true));
		assertEquals(new TestObject2(TestE.Da, false), ClassPool.getObject(TestObject2.class, TestE.Da, true));
		assertEquals(new TestObject2(TestE.Ne, false), ClassPool.getObject(TestObject2.class, TestE.Ne, true));
		assertEquals(new TestObject2(TestE.Yes, false), ClassPool.getObject(TestObject2.class, TestE.Yes, true));
		*/
	}

}

class TestObject1 {

	private int hash;

	public TestObject1(String s, Integer i, Boolean test) {
		if (test) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		hash = s.hashCode() + i.hashCode();
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TestObject1)) return false;
		TestObject1 that = (TestObject1) o;
		if (getHash() != that.getHash()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getHash();
	}
}

class TestObject2 {

	private TestE e;

	public TestObject2(TestE e, Boolean test) {
		if (test) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		this.e = e;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TestObject2)) return false;
		TestObject2 that = (TestObject2) o;
		if (e != that.e) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return e != null ? e.hashCode() : 0;
	}
}

