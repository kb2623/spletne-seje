package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;

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

	private Map<Integer, TestObject> map;

	@Before
	public void setUp() {
	}

	@Test
	public void testSetMaxSize() throws Exception {
		Properties properties = null;
		Integer.valueOf(properties.getProperty("size"));
	}

	@Test
	public void testGetObject() throws Exception {

	}
}