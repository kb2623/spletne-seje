package org.datastruct;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class BTree<V, K> implements Map<K, V> {

	private Node root;
	private Comparator<K> keyCmp;

	public BTree(int size, Comparator<K> cmp) throws IllegalArgumentException {
		if (size < 2) {
			throw new IllegalArgumentException();
		}
		root = new Node(size);
		keyCmp = new CompareKey<>(cmp);
	}

	public BTree(int size) {
		this(size, (k1, k2) -> k1.hashCode() - k2.hashCode());
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
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

	@Override
	public String toString() {
		// TODO
		return super.toString();
	}

	class CompareKey<K> implements Comparator<K> {

		Comparator<K> cmp;

		CompareKey(Comparator<K> cmp) {
			this.cmp = cmp;
		}

		CompareKey() {
			this((k1, k2) -> k1.hashCode() - k2.hashCode());
		}

		@Override
		public int compare(K k1, K k2) {
			int cmp = this.cmp.compare(k1, k2);
			if (cmp == 0) {
				if (k1.equals(k2)) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return cmp;
			}
		}
	}

	class Entry implements Map.Entry<K, V> {

		private K key;
		private V value;
		private Entry lower;

		Entry(K key, V value) {
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
			return value;
		}
	}

	class Node {

		private Object[] array;
		private Entry higher;
		private int size;

		Node(int size) {
			array = new Object[size];
			higher = null;
			this.size = 0;
		}

		boolean isFull() {
			return size == array.length;
		}

		int getIndex(K key) {
			int low = 0;
			int high = size - 1;
			while (low < +high) {
				int mid = (low + high) / 2;
				int cmp = keyCmp.compare(((Entry) array[mid]).key, key);
				if (cmp == 0) {
					return mid;
				} else if (cmp < 0) {
					low = mid + 1;
				} else {
					high = mid - 1;
				}
			}
			return -(high) - 1;
		}
	}
}
