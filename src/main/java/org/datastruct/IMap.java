package org.datastruct;

import java.util.Map;

public interface IMap<K, V> extends Map<K, V> {

	@Override
	default void putAll(Map<? extends K, ? extends V> map) throws NullPointerException, ClassCastException {
		for (Entry<? extends K, ? extends V> e : map.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}
}
