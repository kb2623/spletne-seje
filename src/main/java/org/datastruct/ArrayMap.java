package org.datastruct;

import java.util.*;

public class ArrayMap<Key, Value> implements Map<Key, Value> {

	private class Entry<Key, Value> implements Map.Entry<Key, Value> {

		private Key key;
		private Value value;

		public Entry(Key key, Value value) throws NullPointerException {
			if (key == null)
				throw new NullPointerException("Can't create [" + Entry.class.getName() + "] with null key!!!");
			if (value == null)
				throw new NullPointerException("Can't create [" + Entry.class.getName() + "] with null value!!!");
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
			if (v == null) throw new NullPointerException("Can't set null value in [" + Entry.class.getName() + "]!!!");
			Value ret = value;
			value = v;
			return ret;
		}
	}

	private Entry<Key, Value>[] store;
	private int size = 0;
	private int maxSize = 50;
	private float loadFactor = .75f;

	public ArrayMap() {
		store = new Entry[maxSize];
	}

	public ArrayMap(int size, float loadFactor) throws IllegalArgumentException {
		if (size <= 0) throw new IllegalArgumentException();
		if (loadFactor <= 0f || loadFactor > 1f) throw new IllegalArgumentException();
		store = new Entry[size];
		this.loadFactor = loadFactor;
	}

	public ArrayMap(int maxSize) throws IllegalArgumentException {
		if (maxSize <= 0) throw new IllegalArgumentException();
		store = new Entry[maxSize];
		this.maxSize = maxSize;
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
	public boolean containsKey(Object o) throws ClassCastException, NullPointerException {
		if (o == null) throw new NullPointerException();
		if (isEmpty()) return false;
		Key key = (Key) o;
		int index = indexOf(key);
		if (store[index] == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean containsValue(Object o) throws ClassCastException, NullPointerException {
		if (o == null) throw new NullPointerException();
		if (isEmpty()) return false;
		Value value = (Value) o;
		for (Entry<Key, Value> e : store) {
			if (e != null && e.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Value get(Object o) throws ClassCastException, NullPointerException {
		if (o == null) throw new NullPointerException();
		if (isEmpty()) return null;
		Key key = (Key) o;
		int index = indexOf(key);
		if (store[index] != null) {
			return store[index].getValue();
		} else {
			return null;
		}
	}

	@Override
	public Value put(Key key, Value value) throws NullPointerException, UnsupportedOperationException {
		if (key == null) throw new NullPointerException("Null keys not allowed in [" + ArrayMap.class.getName() + "]!!!");
		if (size + 1 > store.length) {
			if (loadFactor == 0) {
				throw new UnsupportedOperationException("Map is full, befor continuing remove some elements!!!");
			} else {
				resizeAndCopy(entrySet());
			}
		}
		int index = indexOf(key);
		Value ret = null;
		if (store[index] != null) {
			ret = store[index].setValue(value);
		} else {
			store[index] = new Entry(key, value);
			size++;
		}
		return ret;
	}

	private void resizeAndCopy(Set<Map.Entry<Key, Value>> set) {
		size = 0;
		store = new Entry[store.length + (int) (store.length * loadFactor)];
		for (Map.Entry<Key, Value> e : set) {
			put(e.getKey(), e.getValue());
		}
	}

	private int indexOf(Key key) {
		int index = key.hashCode() % store.length;
		for (int i = 0; i < store.length; i++) {
			index += i;
			if (index >= store.length) {
				index -= store.length;
			}
			if (store[index] == null) {
				break;
			} else if (store[index].getKey().equals(key)) {
				break;
			}
		}
		return index;
	}

	@Override
	public Value remove(Object o) throws ClassCastException, NullPointerException {
		if (o == null) throw new NullPointerException();
		if (isEmpty()) return null;
		Key key = (Key) o;
		int index = indexOf(key);
		if (store[index] != null) {
			Value ret = store[index].getValue();
			store[index] = null;
			size--;
			return ret;
		} else {
			return null;
		}
	}

	@Override
	public void putAll(Map<? extends Key, ? extends Value> map) {
		for (Map.Entry<? extends Key, ? extends Value> e : map.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		store = new Entry[maxSize];
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
