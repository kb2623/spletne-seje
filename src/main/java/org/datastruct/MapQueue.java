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
		Entry<K, V> sentinel = (Entry<K, V>) super.sentinel;
		Entry<K, V> e = (Entry<K, V>) getEnrty(k);
		if (e == null) {
			if (size == maxSize) {
				throw new IllegalArgumentException();
			} else {
				e = new Entry<>(k, v, sentinel.conns.length, sentinel.prev);
				if (sentinel.prev == null) {
					sentinel.next = sentinel.prev = e;
				} else {
					sentinel.prev.prev = e;
					sentinel.prev = e;
				}
				return insertEntry(e);
			}
		} else {
			if (sentinel.prev != sentinel.next) {
				if (e.next == null) {
					sentinel.next = e.prev;
				}
				if (e.prev != null) {
					e.remove();
					e.next = sentinel.prev;
					sentinel.prev.prev = e;
					sentinel.prev = e;
				}
			}
			return (V) e.setValue(v);
		}
	}

	@Override
	public V remove(Object o) throws ClassCastException, NullPointerException {
		Entry<K, V> sentinel = (Entry<K, V>) super.sentinel;
		K key = (K) o;
		Entry<K, V> e = (Entry<K, V>) getEnrty(key);
		if (e != null) {
			if (sentinel.next != sentinel.prev) {
				if (e.next == null) {
					sentinel.next = e.prev;
				}
			}
			if (e.lowerPriority()) {
				sentinel.prev = e;
			}
			return (V) e.value;
		} else {
			return null;
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

		boolean lowerPriority() {
			if (prev != null) {
				Entry<K, V> e = prev;
				prev = e.prev;
				e.next = next;
				next = e;
				e.prev = this;
				if (prev == null) {
					return true;
				}
			}
			return false;
		}

		void remove() {
			if (prev != null && next != null) {
				prev.next = next;
				next.prev = prev;
			} else if (prev == null && next != null) {
				next.prev = null;
			} else if (prev != null && next == null) {
				prev.next = null;
			}
		}
	}
}
