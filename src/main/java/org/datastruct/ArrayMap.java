package org.datastruct;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V> implements Map<K, V> {

	private class Entry<K, V> implements Map.Entry<K, V> {

		private K key;
		private V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

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
	private int size;
	private float loadFactor;

	public ArrayMap() {
		store = new Entry[10];
		loadFactor = .75f;
		size = 0;
	}

	public ArrayMap(int size, float loadFactor) throws IllegalArgumentException {
		if (size < 0) throw new IllegalArgumentException();
		if (loadFactor < 0 && loadFactor <= 1f) throw new IllegalArgumentException();
		store = new Entry[size];
		this.loadFactor = loadFactor;
		this.size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
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
		Entry<K, V> entry = new Entry<>(k, v);
		// TODO izracunati moras index v tabeli za vnos, in vrni vresnost ce ze obstaja
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
		store = new Entry[0];
		size = 0;
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
