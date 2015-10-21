package org.datastruct;

import java.util.*;

public class ArrayMap<Key, Value> implements Map<Key, Value> {

	private class Entry<Key, Value> implements Map.Entry<Key, Value> {

		private Key key;
		private Value value;

		public Entry(Key key, Value value) {
			if (key == null) throw new NullPointerException("Can't create [" + Entry.class + "] with null key!!!");
			if (value == null) throw new NullPointerException("Can't create [" + Entry.class + "] with null value!!!");
			this.key = key;
			this.value = value;
		}

		@Override
		public Key getKey() {
			return key;
		}

		@Override
		public Value getValue() {
			return value;
		}

		@Override
		public Value setValue(Value v) {
			if (v == null) throw new NullPointerException("Can't set null value in [" + Entry.class + "]!!!");
			Value ret = value;
			value = v;
			return ret;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Entry)) return false;
			Entry<?, ?> entry = (Entry<?, ?>) o;
			if (!getKey().equals(entry.getKey())) return false;
			return true;
		}

		@Override
		public int hashCode() {
			int result = getKey().hashCode();
			result = 31 * result + getValue().hashCode();
			return result;
		}
	}

	private Entry[] store;
	private int size;
	private int maxSize;
	private float loadFactor;

	public ArrayMap() {
		store = new Entry[10];
		loadFactor = .75f;
		size = 0;
		maxSize = 0;
	}

	public ArrayMap(int size, float loadFactor) throws IllegalArgumentException {
		if (size < 0) throw new IllegalArgumentException();
		if (loadFactor <= 0 && loadFactor <= 1f) throw new IllegalArgumentException();
		store = new Entry[size];
		this.loadFactor = loadFactor;
		this.size = 0;
		maxSize = 0;
	}

	public ArrayMap(int maxSize) throws IllegalArgumentException {
		if (maxSize <= 0) throw new IllegalArgumentException();
		store = new Entry[maxSize];
		this.maxSize = maxSize;
		size = 0;
		loadFactor = 0;
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
	public Value get(Object o) {
		return null;
	}

	@Override
	public Value put(Key key, Value value) {
		Entry<Key, Value> entry = new Entry<>(key, value);
		// TODO izracunati moras index v tabeli za vnos, in vrni vresnost ce ze obstaja
		return null;
	}

	@Override
	public Value remove(Object o) {
		return null;
	}

	@Override
	public void putAll(Map<? extends Key, ? extends Value> map) {

	}

	@Override
	public void clear() {
		store = new Entry[0];
		size = 0;
	}

	@Override
	public Set<Key> keySet() {
		Set<Key> set = new HashSet<>(size);
		if (!isEmpty()) {
			for (Entry<Key, Value> e : store) {
				if (e != null) {
					set.add(e.getKey());
				}
			}
		}
		return set;
	}

	@Override
	public Collection<Value> values() {
		Collection<Value> values = new ArrayList<>(size);
		if (!isEmpty()) {
			for (Entry<Key, Value> e : store) {
				if (e != null) {
					values.add(e.getValue());
				}
			}
		}
		return values;
	}

	@Override
	public Set<Map.Entry<Key, Value>> entrySet() {
		Set<Map.Entry<Key, Value>> set = new HashSet<>();
		if (!isEmpty()) {
			for (Entry<Key, Value> e : store) {
				if (e != null) {
					set.add(e);
				}
			}
		}
		return set;
	}
}
