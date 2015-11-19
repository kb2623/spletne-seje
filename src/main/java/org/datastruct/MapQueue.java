package org.datastruct;

import java.util.Comparator;

public class MapQueue<K, V> extends SkipMap<K, V> {

	private int maxSize;
	private int size;

	public MapQueue(int maxCone, int maxSize, Comparator<K> cmp) {
		super(null, cmp);
		super.sentinel = new Entry<>(maxCone);
		this.maxSize = maxSize;
		this.size = 0;
	}

	public MapQueue(int maxCone, int maxSize) {
		this(maxCone, maxSize, (k1, k2) -> k1.hashCode() - k2.hashCode());
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		sentinel = new Entry<>(sentinel.conns.length);
		size = 0;
	}

	@Override
	public V put(K k, V v) throws NullPointerException, IllegalArgumentException {
		if (size == maxSize) {
			throw new IllegalArgumentException();
		}
		Entry<K, V> e = new Entry<>(k, v, super.sentinel.conns.length, ((Entry<K, V>) sentinel).prev);
		if (((Entry<K, V>) sentinel).next == null) {
			((Entry<K, V>) sentinel).next = e;
		}
		size++;
		return super.insertEntry(e);
	}

	@Override
	public V remove(Object o) throws ClassCastException, NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		K key = (K) o;
		Entry<K, V> e = (Entry<K, V>) super.removeEntry(key);
		if (e == null) {
			return null;
		} else {
			// todo izbrisi povezave
			return (V) e.value;
		}
	}

	@Override
	public V get(Object o) throws ClassCastException, NullPointerException {
		// TODO
		return super.get(o);
	}

	class Entry<K, V> extends SkipMap.Entry {

		protected Entry<K, V> prev;
		protected Entry<K, V> next;

		Entry(int maxConns) {
			super(maxConns);
			prev = next = null;
		}

		Entry(K key, V value, int maxCone, Entry<K, V> last) throws NullPointerException {
			super(key, value, maxCone);
			prev = null;
			next = last;
		}

		Entry(Object key, Object value, int maxConns) {
			super(key, value, maxConns);
		}
	}
}
