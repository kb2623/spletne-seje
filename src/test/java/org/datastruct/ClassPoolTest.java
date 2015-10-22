package org.datastruct;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassPoolTest {

	private class TestObject {

		private int hash;

		public TestObject(String s, Integer i) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			hash = s.hashCode() + i.hashCode();
		}

		public int getHash() {
			return hash;
		}

		public void setHash(int hash) {
			this.hash = hash;
		}
	}

	@Test
	public void testSetMaxSize() throws Exception {

	}

	@Test
	public void testGetObject() throws Exception {

	}
}