package org.datastruct;

import java.util.*;

public class SkipMap<K, V> implements Map<K, V> {

	private int maxCone;
	private Entry<K, V> sentinel;

	public SkipMap(int maxCone) {
		this.maxCone = maxCone;
		this.sentinel = new Entry(maxCone);
	}

	@Override
	public V put(K k, V v) throws NullPointerException {
		if (k == null) throw new NullPointerException();
		if (v == null) throw new NullPointerException();
		int hash = k.hashCode();
		Entry<K, V> e = null;
		Stack<Entry> stack = new Stack<>();
		Entry<K, V> curr = sentinel;
		for (int level = maxCone - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = tmp.key.hashCode() - hash;
				if (cmp == 0) {
					if (tmp.key.equals(k)) {
						return tmp.setValue(v);
					} else {
						e = new Entry(k, v, tmp.conns.length);
						for (; level > 0; level--) {
							stack.offer(tmp);
						}
						curr = tmp;
					}
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = tmp.key.hashCode() - hash;
						if (cmp == 0) {
							if (tmp.key.equals(k)) {
								return tmp.setValue(v);
							} else {
								e = new Entry(k, v, tmp.conns.length);
								for (; level > 0; level--) {
									stack.offer(tmp);
								}
								curr = tmp;
								break;
							}
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
			stack.offer(curr);
		}
		if (e == null) {
			e = new Entry(k, v, maxCone);
		}
		for (int level = 0; level < e.conns.length; level++) {
			curr = stack.poll();
			e.conns[level] = curr.conns[level];
			curr.conns[level] = e;
		}
		return null;
	}

	@Override
	public void clear() {
		for (int level = 0; level < maxCone; level++) {
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
		int hash = key.hashCode();
		Entry<K, V> curr = this.sentinel;
		for (int level = maxCone - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = tmp.key.hashCode() - hash;
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
						cmp = tmp.key.hashCode() - hash;
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
	public boolean containsValue(Object o) throws NullPointerException {
		if (o == null) throw new NullPointerException();
		for (Entry<K, V> curr = this.sentinel.conns[0]; curr != null; curr = curr.conns[0]) {
			if (curr.value.equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public V get(Object o) throws ClassCastException, NullPointerException {
		if (o == null) throw new NullPointerException();
		K key = (K) o;
		int hash = key.hashCode();
		Entry<K, V> curr = this.sentinel;
		for (int level = maxCone - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = tmp.key.hashCode() - hash;
				if (cmp == 0) {
					if (tmp.key.equals(key)) {
						return tmp.value;
					} else {
						curr = tmp;
					}
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = tmp.key.hashCode() - hash;
						if (cmp == 0) {
							if (tmp.key.equals(key)) {
								return tmp.value;
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
		return null;
	}

	@Override
	public V remove(Object o) throws ClassCastException, NullPointerException {
		if (o == null) throw new NullPointerException();
		K key = (K) o;
		int hash = key.hashCode();
		Stack<Entry<K, V>> stack = new Stack<>();
		Entry<K, V> curr = this.sentinel;
		Entry<K, V> found = null;
		for (int level = maxCone - 1; level >= 0; level--) {
			Entry<K, V> tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = tmp.key.hashCode() - hash;
				if (cmp == 0) {
					if (tmp.key.equals(key)) {
						found = tmp;
					} else {
						curr = tmp;
					}
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = tmp.key.hashCode() - hash;
						if (cmp == 0) {
							if (tmp.key.equals(key)) {
								found = tmp;
								break;
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
			stack.offer(curr);
		}
		for (int level = 0; level < maxCone && found != null; level++) {
			curr = stack.poll();
			if (found.conns.length > level) {
				curr.conns[level] = found.conns[level];
			} else {
				break;
			}
		}
		if (found == null) {
			return null;
		}
		return found.value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) throws NullPointerException {
		for (Map.Entry e : map.entrySet()) {
			this.put((K) e.getKey(), (V) e.getValue());
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

	class Entry<K, V> implements Map.Entry<K, V> {

		protected Entry<K, V>[] conns;
		protected K key;
		protected V value;

		Entry(int maxConns) {
			this.conns = new Entry[maxConns];
			this.key = null;
			this.value = null;
		}

		Entry(K key, V value, int maxConns) {
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
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Entry)) return false;
			Entry<?, ?> entry = (Entry<?, ?>) o;
			if (getKey() != null ? !getKey().equals(entry.getKey()) : entry.getKey() != null) return false;
			if (getValue() != null ? !getValue().equals(entry.getValue()) : entry.getValue() != null) return false;
			return true;
		}

		@Override
		public int hashCode() {
			int result = getKey() != null ? getKey().hashCode() : 0;
			return result;
		}
	}
}
