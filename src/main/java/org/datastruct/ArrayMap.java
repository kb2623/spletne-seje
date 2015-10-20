package org.datastruct;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V> implements Map<K, V> {

	private class Entry<K, V> implements Map.Entry<K, V> {

		private K key;
		private V value;

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V v) {
			V ret = value;
			value = v;
			return ret;
		}
	}

	private Entry[] store;
	private float loadFactor;

	public ArrayMap() {
		store = new Entry[10];
		loadFactor = .75f;
	}

	@Override
	public int size() {
		return store.length;
	}

	@Override
	public boolean isEmpty() {
		return store.length <= 0;
	}

	@Override
	public boolean containsKey(Object o) {
		return false;
	}

	@Override
	public boolean containsValue(Object o) {
		return false;
	}

	@Override
	public V get(Object o) {
		return null;
	}

	@Override
	public V put(K k, V v) {
		return null;
	}

	@Override
	public V remove(Object o) {
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Set<K> keySet() {
		return null;
	}

	@Override
	public Collection<V> values() {
		return null;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return null;
	}
}
