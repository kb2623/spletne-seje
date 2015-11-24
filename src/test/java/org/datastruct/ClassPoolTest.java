package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class ClassPoolTest {

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

	@Test
	public void testSome() throws IOException {
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("ClassPool.properties"));
		properties.list(System.out);
	}

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
}