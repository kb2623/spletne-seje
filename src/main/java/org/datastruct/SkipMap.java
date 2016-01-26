package org.datastruct;

import java.util.*;

public class SkipMap<K, V> implements NavigableMap<K, V> {

	protected Entry<K, V> sentinel;
	private CompareKey<K> keyCmp;

	protected SkipMap(Entry<K, V> sentinel, Comparator<K> cmp) {
		this.sentinel = sentinel;
		this.keyCmp = new CompareKey<>(cmp);
	}

	public SkipMap(int maxCone, Comparator<K> keyCmp) {
		sentinel = new Entry<>(maxCone);
		this.keyCmp = new CompareKey<>(keyCmp);
	}

	public SkipMap(int maxCone) {
		this(maxCone, (e1, e2) -> e1.hashCode() - e2.hashCode());
	}

	@Override
	public V put(K k, V v) throws NullPointerException {
		Entry<K, V> e = new Entry<>(k, v, sentinel.conns.length);
		return insertEntry(e);
	}

	protected V insertEntry(Entry<K, V> e) {
		Stack<Entry> stack = new Stack<>();
		Entry<K, V> curr = sentinel;
		for (int level = sentinel.conns.length - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = keyCmp.compare(tmp.key, e.key);
				if (cmp == 0) {
					return tmp.setValue(e.value);
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = keyCmp.compare(tmp.key, e.key);
						if (cmp == 0) {
							return tmp.setValue(e.value);
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
			stack.push(curr);
		}
		for (int level = 0; level < e.conns.length; level++) {
			curr = stack.pop();
			e.conns[level] = curr.conns[level];
			curr.conns[level] = e;
		}
		return null;
	}

	@Override
	public void clear() {
		for (int level = 0; level < sentinel.conns.length; level++) {
			sentinel.conns[level] = null;
		}
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>();
		for (Entry<K, V> curr = this.sentinel.conns[0]; curr != null; curr = curr.conns[0]) {
			set.add(curr.key);
		}
		return set;
	}

	@Override
	public Collection<V> values() {
		Collection<V> col = new LinkedList<>();
		for (Entry<K, V> curr = this.sentinel.conns[0]; curr != null; curr = curr.conns[0]) {
			col.add(curr.value);
		}
		return col;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = new HashSet<>();
		for (Entry<K, V> curr = sentinel.conns[0]; curr != null; curr = curr.conns[0]) {
			set.add(curr);
		}
		return set;
	}

	@Override
	public boolean isEmpty() {
		if (sentinel.conns[0] == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean containsKey(Object o) throws NullPointerException {
		if (o == null) throw new NullPointerException();
		K key = (K) o;
		Entry<K, V> curr = this.sentinel;
		for (int level = sentinel.conns.length - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = keyCmp.compare(tmp.key, key);
				if (cmp == 0) {
					if (tmp.key.equals(key)) {
						return true;
					} else {
						curr = tmp;
					}
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = keyCmp.compare(tmp.key, key);
						if (cmp == 0) {
							if (tmp.key.equals(key)) {
								return true;
							} else {
								curr = tmp;
							}
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object o) {
		for (Entry<K, V> curr = this.sentinel.conns[0]; curr != null; curr = curr.conns[0]) {
			if (curr.value.equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public V get(Object o) throws ClassCastException, NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		K key = (K) o;
		Entry<K, V> ret = getEnrty(key);
		if (ret == null) {
			return null;
		} else {
			return ret.value;
		}
	}

	protected Entry<K, V> getEnrty(K key) {
		Entry<K, V> curr = this.sentinel;
		for (int level = sentinel.conns.length - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = keyCmp.compare(tmp.key, key);
				if (cmp == 0) {
					return tmp;
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = keyCmp.compare(tmp.key, key);
						if (cmp == 0) {
							return tmp;
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public V remove(Object o) throws ClassCastException, NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		K key = (K) o;
		Entry<K, V> e = removeEntry(key);
		if (e == null) {
			return null;
		} else {
			return e.value;
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	protected Entry<K, V> removeEntry(K key) {
		Stack<Entry<K, V>> stack = new Stack<>();
		Entry<K, V> curr = this.sentinel;
		Entry<K, V> found = null;
		for (int level = sentinel.conns.length - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = keyCmp.compare(tmp.key, key);
				if (cmp == 0) {
					found = tmp;
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = keyCmp.compare(tmp.key, key);
						if (cmp == 0) {
							found = tmp;
							break;
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
			stack.push(curr);
		}
		for (int level = 0; level < sentinel.conns.length && found != null; level++) {
			curr = stack.pop();
			if (found.conns.length > level) {
				curr.conns[level] = found.conns[level];
			} else {
				break;
			}
		}
		if (found == null) {
			return null;
		} else {
			return found;
		}
	}

	@Override
	public int size() {
		int i = 0;
		for (Entry<K, V> node = this.sentinel.conns[0]; node != null; node = node.conns[0]) {
			i++;
		}
		return i;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		for (Entry<K, V> e = this.sentinel.conns[0]; e != null; e = e.conns[0]) {
			builder.append(e.getKey().toString()).append('=').append(e.getValue().toString());
			builder.append(", ");
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	@Deprecated
	public String printTree() {
		StringBuilder[] builders = new StringBuilder[sentinel.conns.length];
		for (int i = 0; i < builders.length; i++) {
			builders[i] = new StringBuilder();
		}
		Entry<K, V> curr = sentinel.conns[0];
		while (curr != null) {
			for (int i = 0; i < builders.length; i++) {
				if (i < curr.conns.length) {
					builders[i].append('x').append('\t');
				} else {
					builders[i].append(' ').append('\t');
				}
			}
			curr = curr.conns[0];
		}
		StringBuilder builder = new StringBuilder();
		for (int i = builders.length - 1; i >= 0; i--) {
			builder.append(builders[i].toString()).append('\n');
		}
		return builder.toString();
	}

	protected V getAt(int index) {
		if (index < 0) {
			return null;
		}
		int size = size();
		if (index >= size) {
			return null;
		}
		V ret = null;
		Entry<K, V> curr = sentinel.conns[0];
		for (int i = 0; i < size; i++) {
			if (i == index) {
				ret = curr.value;
				break;
			} else {
				curr = curr.conns[0];
			}
		}
		return ret;
	}

	@Override
	public Map.Entry<K, V> lowerEntry(K key) {
		return null;
	}

	@Override
	public K lowerKey(K key) {
		return null;
	}

	@Override
	public Map.Entry<K, V> floorEntry(K key) {
		return null;
	}

	@Override
	public K floorKey(K key) {
		return null;
	}

	@Override
	public Map.Entry<K, V> ceilingEntry(K key) {
		return null;
	}

	@Override
	public K ceilingKey(K key) {
		return null;
	}

	@Override
	public Map.Entry<K, V> higherEntry(K key) {
		return null;
	}

	@Override
	public K higherKey(K key) {
		return null;
	}

	@Override
	public Map.Entry<K, V> firstEntry() {
		return null;
	}

	@Override
	public Map.Entry<K, V> lastEntry() {
		return null;
	}

	@Override
	public Map.Entry<K, V> pollFirstEntry() {
		return null;
	}

	@Override
	public Map.Entry<K, V> pollLastEntry() {
		return null;
	}

	@Override
	public NavigableMap<K, V> descendingMap() {
		return null;
	}

	@Override
	public NavigableSet<K> navigableKeySet() {
		return null;
	}

	@Override
	public NavigableSet<K> descendingKeySet() {
		return null;
	}

	@Override
	public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
		return null;
	}

	@Override
	public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
		return null;
	}

	@Override
	public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
		return null;
	}

	@Override
	public Comparator<? super K> comparator() {
		return keyCmp.cmp;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return null;
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		return null;
	}

	@Override
	public K firstKey() {
		return null;
	}

	@Override
	public K lastKey() {
		return null;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		SkipMap<K, V> map = new SkipMap<>(sentinel.conns.length, keyCmp.cmp);
		map.sentinel = (Entry<K, V>) sentinel.clone();
		return map;
	}

	protected class Entry<K, V> implements Map.Entry<K, V> {

		protected Entry<K, V>[] conns;
		protected K key;
		protected V value;

		Entry(int maxConns) {
			this.conns = new Entry[maxConns];
			this.key = null;
			this.value = null;
		}

		Entry(K key, V value, int maxConns) throws NullPointerException {
			if (key == null) {
				throw new NullPointerException();
			}
			this.key = key;
			this.value = value;
			int size;
			for (size = 1; size < maxConns; size++) {
				if ((int) (Math.random() * 2) != 1) {
					break;
				}
			}
			this.conns = new Entry[size];
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V value) {
			V ret = this.value;
			this.value = value;
			return ret;
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			Entry<K, V> e = new Entry<>(conns.length);
			e.key = key;
			e.value = value;
			// TODO: 1/26/16 preveri ali ze klon obstaja
			/*
			for (int i = 0; i < conns.length; i++) {
				e.conns[i] = (Entry<K, V>) conns[i].clone();
			}
			*/
			return e;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof Entry)) return false;
			if (this == o) return true;
			Entry<?, ?> that = (Entry<?, ?>) o;
			return getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null &&
					getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;
		}

		@Override
		public int hashCode() {
			int result = getKey() != null ? getKey().hashCode() : 0;
			return result;
		}
	}

	class CompareKey<K> implements Comparator<K> {

		Comparator<K> cmp;

		CompareKey(Comparator<K> cmp) {
			this.cmp = cmp;
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
}
