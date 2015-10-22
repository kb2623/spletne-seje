package org.datastruct;

import java.util.*;

public class MapLRU<K, V> implements Map<K, V> {

	class Entry<K, V> implements Map.Entry<K, V> {

		protected Entry<K, V> prev;
		protected Entry<K, V> next;
		protected Entry<K, V>[] conns;
		protected K key;
		protected V value;

		protected Entry(int maxConns) {
			this.conns = new Entry[maxConns];
			this.prev = this.next = null;
			this.key = null;
			this.value = null;
		}

		protected Entry(K key, V value, int maxConns) {
			this.key = key;
			this.value = value;
			int size;
			for (size = 1; size < maxConns; size++) {
				if ((int) (Math.random() * 2) != 1) {
					break;
				}
			}
			this.conns = new Entry[size];
			this.prev = this.next = null;
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
	}

	private int maxCone;
	private int maxSize;
	private Entry<K, V> sentinel;

	public MapLRU(int maxCone, int maxSize) {
		this.maxCone = maxCone;
		this.maxSize = maxSize;
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
				if (hash == 0) {
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
		/**
		 * Dodaj na konec prioritetne vrste, ter poprvi povezave v sentinelu
		 */
		if (this.sentinel.next == null) {
			this.sentinel.prev = this.sentinel.next = e;
		} else {
			e.next = this.sentinel.prev;
			this.sentinel.prev.prev = e;
			this.sentinel.prev = e;
		}
		return null;
	}

	@Override
	public void clear() {
		for (int level = 0; level < maxCone; level++) {
			sentinel.conns[level] = null;
		}
		this.sentinel.prev = this.sentinel.next = null;
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
						} else if (cmp < 0) {
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
						lowerPriority(tmp);
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
								lowerPriority(tmp);
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

	/**
	 * Metoda ki zmanjsa prioriteto za brisanje elementa
	 *
	 * @param e elemnt ki mu zmanjsujemp prioriteto
	 */
	private void lowerPriority(Entry e) {
		if (e.prev != null) {
			Entry tmp = e.prev;
			tmp.next = e.next;
			e.next = tmp;
			e.prev = tmp.prev;
			tmp.prev = e;
			if (e == this.sentinel.next && e != this.sentinel.prev) {
				this.sentinel.next = e;
			} else if (tmp == this.sentinel.prev) {
				this.sentinel.prev = e;
			}
		}
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
		/**
		 * Popravljanje strukture vrste
		 */
		if (found.next != null) {
			found.next.prev = found.prev;
			if (found == this.sentinel.next) {
				this.sentinel.next = found.prev;
			}
		}
		if (found.prev != null) {
			found.prev.next = found.next;
			if (found == this.sentinel.prev) {
				this.sentinel.prev = found.next;
			}
		}
		return found.value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {

	}

	@Override
	public int size() {
		int i = 0;
		for (Entry<K, V> node = sentinel.conns[0]; node != null; node = node.conns[0]) {
			i++;
		}
		return i;
	}
}
