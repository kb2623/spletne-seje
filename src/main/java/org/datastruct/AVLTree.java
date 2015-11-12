package org.datastruct;

import java.util.*;

public class AVLTree<K, V> implements Map<K, V> {

	private AVLEntry<K, V> root;
	private CompareKey<K> keyCmp;
	public AVLTree() {
		this((k1, k2) -> k1.hashCode() - k2.hashCode());
	}

	public AVLTree(Comparator<K> keyCmp) {
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
		}
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
		}
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

	@Override
	public V put(K key, V value) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		if (isEmpty()) {
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
					return null;
				} else {
					curr.updataHeight();
				}
			}
			return null;
		}
	}

	private AVLEntry<K, V> rotate(AVLEntry<K, V> node, int cmp) {
		if (cmp < 0) {
			AVLEntry<K, V> cNode = node.higher;
			if (cNode.getBalance() > 0) {
				node.higher = rotateRight(cNode, true);
			}
			return rotateLeft(node, false);
		} else {
			AVLEntry<K, V> cNode = node.lower;
			if (cNode.getBalance() < 0) {
				node.lower = rotateLeft(cNode, true);
			}
			return rotateRight(node, false);
		}
	}

	private AVLEntry<K, V> rotateLeft(AVLEntry<K, V> node, boolean drot) {
		AVLEntry<K, V> nRoot = node.higher;
		node.higher = nRoot.lower;
		nRoot.lower = node;
		nRoot.updataHeight();
		node.updataHeight();
		return nRoot;
	}

	private AVLEntry<K, V> rotateRight(AVLEntry<K, V> node, boolean drot) {
		AVLEntry<K, V> nRoot = node.lower;
		node.lower = nRoot.higher;
		nRoot.higher = node;
		nRoot.updataHeight();
		node.updataHeight();
		return nRoot;
	}

	@Override
	public V remove(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
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
		if (found == null) {
			return null;
		} else {
			AVLEntry<K, V> minNode = found;
			if (found.lower != null) {
				minNode = found.lower;
				while (minNode.higher != null) {
					stack.push(minNode);
					minNode = minNode.higher;
				}
				stack.peek().higher = minNode.lower;
				stack.peek().updataHeight();
				;
			} else if (found.higher != null) {
				minNode = minNode.higher;
				found.lower = minNode.lower;
				found.higher = minNode.higher;
				found.updataHeight();
			} else {
				if (found == root) {
					root = null;
				} else {
					if (stack.peek().lower == found) {
						stack.peek().lower = null;
					} else {
						stack.peek().higher = null;
					}
					stack.peek().updataHeight();
				}
			}
			AVLEntry<K, V> node;
			while (!stack.isEmpty()) {
				node = stack.pop();
				cmp = node.getBalance();
				if (cmp < -1 || cmp > 1) {
					if (stack.isEmpty()) {
						root = rotate(node, cmp);
					} else if (stack.peek().lower == node) {
						stack.peek().lower = rotate(node, cmp);
					} else {
						stack.peek().higher = rotate(node, cmp);
					}
					break;
				} else {
					node.updataHeight();
				}
			}
			found.key = minNode.key;
			return found.setValue(minNode.value);
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) throws NullPointerException {
		for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
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

		AVLEntry(K key, V value, AVLEntry<K, V> lower, AVLEntry<K, V> higher, int depth) {
			this.key = key;
			this.value = value;
			this.lower = lower;
			this.higher = higher;
			this.height = depth;
		}

		AVLEntry(K key, V value) {
			this(key, value, null, null, 1);
		}

		int getBalance() {
			return (lower != null ? lower.height : 0) - (higher != null ? higher.height : 0);
		}

		void updataHeight() {
			if ((lower != null ? lower.height : 0) >= (higher != null ? higher.height : 0)) {
				height = (lower != null ? lower.height : 0) + 1;
			} else {
				height = (higher != null ? higher.height : 0) + 1;
			}
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
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof AVLEntry)) return false;
			AVLEntry<?, ?> avlEntry = (AVLEntry<?, ?>) o;
			if (getKey() != null ? !getKey().equals(avlEntry.getKey()) : avlEntry.getKey() != null) return false;
			if (getValue() != null ? !getValue().equals(avlEntry.getValue()) : avlEntry.getValue() != null) return false;
			return true;
		}

		@Override
		public int hashCode() {
			return getKey() != null ? getKey().hashCode() : 0;
		}
	}
}
