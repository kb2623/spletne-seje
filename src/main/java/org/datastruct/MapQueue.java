package org.datastruct;

public class MapQueue<K, V> extends SkipMap<K, V> {

	private int maxSize;
	private int size;

	public MapQueue(int maxCone, int maxSize) {
		super(maxCone);
		this.maxSize = maxSize;
		this.size = 0;
	}

	@Override
	public V put(K k, V v) throws NullPointerException {
		// TODO
		return super.put(k, v);
	}

	@Override
	public V remove(Object o) throws ClassCastException, NullPointerException {
		// TODO
		return super.remove(o);
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
		}

		Entry(Object key, Object value, int maxConns) {
			super(key, value, maxConns);
		}
	}
}
