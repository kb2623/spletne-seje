package org.datastruct;

import java.util.*;

public class AvlTreeMap<K, V> implements NavigableMap<K, V> {

	private AVLEntry<K, V> root;
	private CompareKey<K> keyCmp;

	public AvlTreeMap() {
		this((k1, k2) -> k1.hashCode() - k2.hashCode());
	}

	public AvlTreeMap(Comparator<K> keyCmp) {
		root = null;
		this.keyCmp = new CompareKey<>(keyCmp);
	}

	@Override
	public int size() {
		if (!isEmpty()) {
			int size = 0;
			Stack<AVLEntry<K, V>> stack = new Stack<>();
			AVLEntry<K, V> curr = root;
			while (!stack.isEmpty() || curr != null) {
				if (curr != null) {
					size++;
					if (curr.higher != null) {
						stack.push(curr.higher);
					}
					curr = curr.lower;
				} else {
					curr = stack.pop();
				}
			}
			return size;
		} else {
			return 0;
		}
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public boolean containsKey(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return false;
		} else {
			K key = (K) o;
			AVLEntry<K, V> curr = root;
			while (true) {
				int cmp = keyCmp.compare(key, curr.key);
				if (cmp == 0) {
					return true;
				} else if (cmp > 0) {
					if (curr.higher != null) {
						curr = curr.higher;
					} else {
						return false;
					}
				} else {
					if (curr.lower != null) {
						curr = curr.lower;
					} else {
						return false;
					}
				}
			}
		}
	}

	@Override
	public boolean containsValue(Object o) {
		V value = (V) o;
		Stack<AVLEntry<K, V>> stack = new Stack<>();
		AVLEntry<K, V> curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr != null) {
				if (curr.value.equals(value)) {
					return true;
				} else if (curr.higher != null) {
					stack.push(curr.higher);
				}
				curr = curr.lower;
			} else {
				curr = stack.pop();
			}
		}
		return false;
	}

	@Override
	public V get(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		} else {
			K key = (K) o;
			AVLEntry<K, V> curr = root;
			while (true) {
				int cmp = keyCmp.compare(key, curr.key);
				if (cmp == 0) {
					return curr.value;
				} else if (cmp > 0) {
					if (curr.higher != null) {
						curr = curr.higher;
					} else {
						return null;
					}
				} else {
					if (curr.lower != null) {
						curr = curr.lower;
					} else {
						return null;
					}
				}
			}
		}
	}

	@Override
	public V put(K key, V value) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			root = new AVLEntry<>(key, value);
			return null;
		} else {
			Stack<AVLEntry<K, V>> stack = new Stack<>();
			AVLEntry<K, V> curr = root;
			int cmp;
			while (true) {
				cmp = keyCmp.compare(key, curr.key);
				stack.push(curr);
				if (cmp == 0) {
					return curr.setValue(value);
				} else if (cmp > 0) {
					if (curr.higher != null) {
						curr = curr.higher;
					} else {
						break;
					}
				} else {
					if (curr.lower != null) {
						curr = curr.lower;
					} else {
						break;
					}
				}
			}
			if (cmp > 0) {
				curr.higher = new AVLEntry<>(key, value);
			} else {
				curr.lower = new AVLEntry<>(key, value);
			}
			updateTree(stack);
		}
		return null;
	}

	private void updateTree(Stack<AVLEntry<K, V>> stack) {
		int cmp;
		AVLEntry<K, V> curr;
		while (!stack.isEmpty()) {
			curr = stack.pop();
			cmp = curr.getBalance();
			if (cmp < -1 || cmp > 1) {
				if (stack.isEmpty()) {
					root = rotate(curr, cmp);
				} else if (stack.peek().lower == curr) {
					stack.peek().lower = rotate(curr, cmp);
				} else {
					stack.peek().higher = rotate(curr, cmp);
				}
				return;
			} else {
				curr.updataHeight();
			}
		}
	}

	private AVLEntry<K, V> rotate(AVLEntry<K, V> node, int cmp) {
		if (cmp < 0) {
			AVLEntry<K, V> cNode = node.higher;
			if (cNode.getBalance() > 0) {
				node.higher = rotateRight(cNode);
			}
			return rotateLeft(node);
		} else {
			AVLEntry<K, V> cNode = node.lower;
			if (cNode.getBalance() < 0) {
				node.lower = rotateLeft(cNode);
			}
			return rotateRight(node);
		}
	}

	private AVLEntry<K, V> rotateLeft(AVLEntry<K, V> node) {
		AVLEntry<K, V> nRoot = node.higher;
		node.higher = nRoot.lower;
		nRoot.lower = node;
		node.updataHeight();
		nRoot.updataHeight();
		return nRoot;
	}

	private AVLEntry<K, V> rotateRight(AVLEntry<K, V> node) {
		AVLEntry<K, V> nRoot = node.lower;
		node.lower = nRoot.higher;
		nRoot.higher = node;
		node.updataHeight();
		nRoot.updataHeight();
		return nRoot;
	}

	@Override
	public V remove(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		} else {
			K key = (K) o;
			Stack<AVLEntry<K, V>> stack = new Stack<>();
			AVLEntry<K, V> found = root;
			int cmp;
			while (true) {
				stack.push(found);
				cmp = keyCmp.compare(key, found.key);
				if (cmp == 0) {
					break;
				} else if (cmp > 0) {
					if (found.higher != null) {
						found = found.higher;
					} else {
						return null;
					}
				} else {
					if (found.lower != null) {
						found = found.lower;
					} else {
						return null;
					}
				}
			}
			return deleteFoundNode(found, stack).getValue();
		}
	}

	private Map.Entry<K, V> deleteFoundNode(final AVLEntry<K, V> node, final Stack<AVLEntry<K, V>> stack) {
		AVLEntry<K, V> retNode = node;
		AVLEntry<K, V> minNode = node;
		if (node.lower != null) {
			minNode = node.lower;
			if (minNode.higher != null) {
				while (minNode.higher != null) {
					stack.push(minNode);
					minNode = minNode.higher;
				}
				stack.peek().higher = minNode.lower;
				retNode = replaceValues(node, minNode);
			} else {
				if (stack.pop() == root) {
					stack.push(node);
					node.lower = minNode.lower;
					retNode = replaceValues(node, minNode);
				} else {
					if (stack.peek().lower == node) {
						stack.peek().lower = minNode;
					} else {
						stack.peek().higher = minNode;
					}
					minNode.higher = node.higher;
					stack.push(minNode);
				}
			}
		} else if (node.higher != null) {
			minNode = minNode.higher;
			node.lower = minNode.lower;
			node.higher = minNode.higher;
			retNode = replaceValues(node, minNode);
		} else {
			if (node == root) {
				root = null;
			} else {
				stack.pop();
				if (stack.peek().lower == node) {
					stack.peek().lower = null;
				} else {
					stack.peek().higher = null;
				}
			}
		}
		updateTree(stack);
		return retNode;
	}

	private AVLEntry<K, V> replaceValues(AVLEntry<K, V> n1, AVLEntry<K, V> n2) {
		K key = n1.key;
		V value = n1.value;
		n1.setKeyValue(n2.key, n2.value);
		return n2.setKeyValue(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		root = null;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>(size());
		Stack<AVLEntry<K, V>> stack = new Stack<>();
		AVLEntry<K, V> curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr != null) {
				stack.push(curr);
				curr = curr.lower;
			} else {
				curr = stack.pop();
				set.add(curr.key);
				curr = curr.higher;
			}
		}
		return set;
	}

	@Override
	public Collection<V> values() {
		Collection<V> c = new ArrayList<>(size());
		Stack<AVLEntry<K, V>> stack = new Stack<>();
		AVLEntry<K, V> curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr != null) {
				stack.push(curr);
				curr = curr.lower;
			} else {
				curr = stack.pop();
				c.add(curr.value);
				curr = curr.higher;
			}
		}
		return c;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = new HashSet<>(size());
		Stack<AVLEntry<K, V>> stack = new Stack<>();
		AVLEntry<K, V> curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr != null) {
				stack.push(curr);
				curr = curr.lower;
			} else {
				curr = stack.pop();
				set.add(curr);
				curr = curr.higher;
			}
		}
		return set;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		Stack<AVLEntry<K, V>> stack = new Stack<>();
		AVLEntry<K, V> curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr != null) {
				stack.push(curr);
				curr = curr.lower;
			} else {
				curr = stack.pop();
				builder.append(curr.key).append('=').append(curr.value);
				builder.append(", ");
				curr = curr.higher;
			}
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	@Deprecated
	public String printTree() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		LinkQueue<AVLEntry<K, V>> queue = new LinkQueue<>();
		AVLEntry<K, V> curr = root;
		while (!queue.isEmpty() || curr != null) {
			builder.append(curr.key).append(':').append(curr.height).append(", ");
			if (curr.lower != null) {
				queue.offer(curr.lower);
			}
			if (curr.higher != null) {
				queue.offer(curr.higher);
			}
			curr = queue.poll();
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	@Override
	public Entry<K, V> lowerEntry(K key) {
		// TODO: 1/25/16
		return null;
	}

	@Override
	public K lowerKey(K key) {
		// TODO: 1/25/16  
		return null;
	}

	@Override
	public Entry<K, V> floorEntry(K key) {
		// TODO: 1/25/16  
		return null;
	}

	@Override
	public K floorKey(K key) {
		// TODO: 1/25/16  
		return null;
	}

	@Override
	public Entry<K, V> ceilingEntry(K key) {
		if (key == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		} else {
			AVLEntry<K, V> curr = root;
			AVLEntry<K, V> prev = null;
			while (true) {
				int cmp = keyCmp.compare(key, curr.key);
				if (cmp == 0) {
					return curr;
				} else if (cmp > 0) {
					if (curr.higher != null) {
						prev = curr;
						curr = curr.higher;
					} else {
						break;
					}
				} else {
					if (curr.lower != null) {
						prev = curr;
						curr = curr.lower;
					} else {
						break;
					}
				}
			}
			if (prev == null) {
				prev = root;
			}
			// TODO: 1/23/16 Tukaj najdi naslednji vecji kljuc
		}
		return null;
	}

	@Override
	public K ceilingKey(K key) {
		if (key == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		} else {
			AVLEntry<K, V> curr = root;
			AVLEntry<K, V> prev = null;
			while (true) {
				int cmp = keyCmp.compare(key, curr.key);
				if (cmp == 0) {
					return curr.key;
				} else if (cmp > 0) {
					if (curr.higher != null) {
						prev = curr;
						curr = curr.higher;
					} else {
						break;
					}
				} else {
					if (curr.lower != null) {
						prev = curr;
						curr = curr.lower;
					} else {
						break;
					}
				}
			}
			if (prev == null) {
				prev = root;
			}
			// TODO: 1/23/16 Tukaj najdi naslednji vecji kljuc
		}
		return null;
	}

	@Override
	public Entry<K, V> higherEntry(K key) {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public K higherKey(K key) {
		return null;
	}

	@Override
	public Entry<K, V> firstEntry() {
		if (!isEmpty()) {
			AVLEntry<K, V> curr = root;
			while (true) {
				if (curr.lower != null) {
					curr = curr.lower;
				} else {
					return curr;
				}
			}
		}
		return null;
	}

	@Override
	public Entry<K, V> lastEntry() {
		if (!isEmpty()) {
			AVLEntry<K, V> curr = root;
			while (true) {
				if (curr.higher != null) {
					curr = curr.higher;
				} else {
					return curr;
				}
			}
		}
		return null;
	}

	@Override
	public Entry<K, V> pollFirstEntry() {
		if (isEmpty()) {
			return null;
		} else {
			Stack<AVLEntry<K, V>> stack = new Stack<>();
			AVLEntry<K, V> found = root;
			while (true) {
				stack.push(found);
				if (found.lower != null) {
					found = found.lower;
				} else {
					break;
				}
			}
			return deleteFoundNode(found, stack);
		}
	}

	@Override
	public Entry<K, V> pollLastEntry() {
		if (isEmpty()) {
			return null;
		} else {
			Stack<AVLEntry<K, V>> stack = new Stack<>();
			AVLEntry<K, V> found = root;
			while (true) {
				stack.push(found);
				if (found.higher != null) {
					found = found.higher;
				} else {
					break;
				}
			}
			return deleteFoundNode(found, stack);
		}
	}

	@Override
	public NavigableMap<K, V> descendingMap() {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public NavigableSet<K> navigableKeySet() {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public NavigableSet<K> descendingKeySet() {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public Comparator<? super K> comparator() {
		return keyCmp;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		// TODO: 1/23/16
		return null;
	}

	@Override
	public K firstKey() {
		if (!isEmpty()) {
			return firstEntry().getKey();
		}
		return null;
	}

	@Override
	public K lastKey() {
		if (!isEmpty()) {
			return lastEntry().getKey();
		}
		return null;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		AvlTreeMap clone = new AvlTreeMap(keyCmp.cmp);
		clone.root = root != null ? (AVLEntry) root.clone() : null;
		return super.clone();
	}

	private class CompareKey<K> implements Comparator<K> {

		private Comparator<K> cmp;

		private CompareKey(Comparator<K> cmp) {
			this.cmp = cmp;
		}

		private CompareKey() {
			this((k1, k2) -> k1.hashCode() - k2.hashCode());
		}

		@Override
		public int compare(K k1, K k2) {
			int res = this.cmp.compare(k1, k2);
			if (res == 0) {
				if (k1.equals(k2)) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return res;
			}
		}
	}

	class AVLEntry<K, V> implements Map.Entry<K, V> {

		K key;
		V value;
		AVLEntry<K, V> lower;
		AVLEntry<K, V> higher;
		int height;

		private AVLEntry(K key, V value, AVLEntry<K, V> lower, AVLEntry<K, V> higher, int depth) {
			this.key = key;
			this.value = value;
			this.lower = lower;
			this.higher = higher;
			this.height = depth;
		}

		private AVLEntry(K key, V value) {
			this(key, value, null, null, 1);
		}

		private int getBalance() {
			return (lower != null ? lower.height : 0) - (higher != null ? higher.height : 0);
		}

		private void updataHeight() {
			if ((lower != null ? lower.height : 0) >= (higher != null ? higher.height : 0)) {
				height = (lower != null ? lower.height : 0) + 1;
			} else {
				height = (higher != null ? higher.height : 0) + 1;
			}
		}

		private AVLEntry<K, V> setKeyValue(K key, V value) {
			this.key = key;
			this.value = value;
			return this;
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

		@Override
		protected Object clone() throws CloneNotSupportedException {
			AVLEntry<K, V> lower = this.lower != null ? (AVLEntry<K, V>) this.lower.clone() : null;
			AVLEntry<K, V> higher = this.higher != null ? (AVLEntry<K, V>) this.higher.clone() : null;
			return new AVLEntry<>(key, value, lower, higher, height);
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof AVLEntry)) return false;
			if (this == o) return true;
			AVLEntry<?, ?> that = (AVLEntry<?, ?>) o;
			return getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null &&
					getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;
		}

		@Override
		public int hashCode() {
			return getKey() != null ? getKey().hashCode() : 0;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}
	}
}
