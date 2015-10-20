package org.datastruct;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ClassPoolTest {

	@Test
	public void testSome() {
		Map<Integer, Class> map = new HashMap<>(3, 0f);
		map.put(1, Object.class);
		map.put(2, Class.class);
		map.put(3, String.class);
		map.put(4, Map.class);
	}
}