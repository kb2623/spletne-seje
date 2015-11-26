package org.datastruct;

import java.util.*;

@SuppressWarnings("deprecation")
public class RadixTree<K extends Sequence<K>, V> implements Map<K, V>, Iterable<V> {

	private RadixEntry rootNode;

	public RadixTree() {
		rootNode = new RadixEntry();
	}

	public V add(V data, K key) throws NullPointerException {
		if (key == null || data == null) {
			throw new NullPointerException();
		}
		return insert(data, key);
//		return insert(data, key, rootNode);
	}

	/**
	 * Rekurzivna metoda za vnasanje novega elementa
	 *
	 * @param data Nov element
	 * @param key  Kjuc novega elementa
	 * @param node Vozlisce v obdelavi
	 * @return Vrednost starega elementa
	 */
	private V insert(V data, K key, RadixEntry node) {
		int numMatchChar = node.key != null ? node.key.equalDistance(key) : 0;
		if (numMatchChar == key.length() && numMatchChar == (node.key != null ? node.key.length() : 0)) {
			return node.setValue(data);
		} else if (numMatchChar == 0 || (numMatchChar == (node.key != null ? node.key.length() : 0) && numMatchChar < key.length())) {
			K ostanekKljuca = key.subSequence(numMatchChar);
			for (RadixEntry child : node.children) {
				if (child.key.equalDistance(ostanekKljuca) > 0) {
					return insert(data, ostanekKljuca, child);
				}
			}
			node.children.add(new RadixEntry(data, ostanekKljuca));
			return null;
		} else {
			RadixEntry tmp = new RadixEntry(node.data, node.key.subSequence(numMatchChar), node.children);
			node.key = key.subSequence(0, numMatchChar);
			node.children = new LinkedList<>();
			node.children.add(tmp);
			if (numMatchChar < key.length()) {
				RadixEntry tmp1 = new RadixEntry(data, key.subSequence(numMatchChar));
				node.data = null;
				node.children.add(tmp1);
			} else {
				node.data = data;
			}
			return null;
		}
	}

	/**
	 * Iterativna metoda za vnasanje novega elemnta
	 *
	 * @param key  Kjuc novega elementa
	 * @param data Vrednost novega elementa
	 * @return Vrednost starega elementa
	 */
	private V insert(V data, K key) {
		RadixEntry curr = rootNode;
		K currKey = key;
		while (true) {
			int numMatchChar = curr.key != null ? curr.key.equalDistance(currKey) : 0;
			if (numMatchChar == currKey.length() && numMatchChar == curr.key.length()) {
				return curr.setValue(data);
			} else if (curr == rootNode || (numMatchChar == curr.key.length() && numMatchChar < currKey.length())) {
				boolean add = true;
				currKey = currKey.subSequence(numMatchChar);
				for (RadixEntry chield : curr.children) {
					if (chield.key.equalDistance(currKey) > 0) {
						curr = chield;
						add = false;
						break;
					}
				}
				if (add) {
					curr.children.add(new RadixEntry(data, currKey));
					return null;
				}
			} else {
				RadixEntry tmp = new RadixEntry(curr.data, curr.key.subSequence(numMatchChar), curr.children);
				curr.key = currKey.subSequence(0, numMatchChar);
				curr.children = new LinkedList<>();
				curr.children.add(tmp);
				if (numMatchChar < currKey.length()) {
					RadixEntry tmp1 = new RadixEntry(data, currKey.subSequence(numMatchChar));
					curr.data = null;
					curr.children.add(tmp1);
				} else {
					curr.setValue(data);
				}
				return null;
			}
		}
	}

	/**
	 * Metoda, ki iterativno isce element
	 *
	 * @param key Kjuc iskanega elemnta
	 * @return Vrednost elementa
	 */
	private V search(K key) {
		K currKey = key;
		RadixEntry curr = rootNode;
		while (curr != null) {
			int equalDistance = curr.key != null ? curr.key.equalDistance(currKey) : 0;
			if (equalDistance == currKey.length() && equalDistance == (curr.key != null ? curr.key.length() : 0)) {
				return curr.data;
			} else if (equalDistance < currKey.length() && equalDistance == (curr.key != null ? curr.key.length() : 0)) {
				currKey = currKey.subSequence(equalDistance);
				boolean found = false;
				for (RadixEntry chield : curr.children) {
					if (chield.key.equalDistance(currKey) > 0) {
						curr = chield;
						found = true;
						break;
					}
				}
				if (!found) {
					curr = null;
				}
			} else {
				curr = null;
			}
		}
		return null;
	}

	/**
	 * Metoda, ki rekuzivno isce element
	 *
	 * @param node Vozlisce, ki ga pregledujemo
	 * @param key  Kljuc iskanega elementa
	 * @return Vrednost elementa
	 */
	private V search(RadixEntry node, K key) {
		int numMatchChar = node.key != null ? node.key.equalDistance(key) : 0;
		if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.data;
		} else if (numMatchChar < key.length() && numMatchChar == node.key.length()) {
			K nKey = key.subSequence(numMatchChar);
			for (RadixEntry chield : node.children) {
				if (chield.key.equalDistance(nKey) > 0) {
					return search(chield, nKey);
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public boolean remove(K key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
//		return !isEmpty() && delete(key) != null;
		return !isEmpty() && delete(key, null, this.rootNode) != null;
	}

	/**
	 * Rekurzivna metoda za brisanje elementa
	 *
	 * @param key    Kjuc elementa
	 * @param parent Predhodnik trenutnga vozlisca
	 * @param node   Trenutno vozlisce v obdelavi
	 * @return Vrednost izbrisanega elementa
	 */
	private V delete(K key, RadixEntry parent, RadixEntry node) {
		int numMatchChar = node.key != null ? node.key.equalDistance(key) : 0;
		if (node.key == null || (numMatchChar < key.length() && numMatchChar == (node.key != null ? node.key.length() : 0))) {
			K nKey = key.subSequence(numMatchChar);
			for (RadixEntry child : node.children) {
				if (child.key.equalDistance(nKey) > 0) {
					return delete(nKey, node, child);
				}
			}
		} else if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if (node.data != null) {
				V data = node.data;
				if (node.children.isEmpty()) {
					for (Iterator<RadixEntry> it = parent.children.iterator(); it.hasNext(); ) {
						RadixEntry tmp = it.next();
						if (tmp.key.equals(node.key)) {
							it.remove();
							break;
						}
					}
					if (parent.children.size() == 1 && parent.data == null && parent.key == rootNode) {
						mergeNodes(parent, parent.children.get(0));
					}
				} else if (node.children.size() == 1) {
					mergeNodes(node, node.children.get(0));
				} else {
					node.data = null;
				}
				return data;
			}
		}
		return null;
	}

	/**
	 * Iterativna metoda za brisanje elementa
	 *
	 * @param key Kjuc elementa
	 * @return Vresnost elementa
	 */
	private V delete(K key) {
		RadixEntry curr = rootNode;
		RadixEntry prev = null;
		K currKey = key;
		while (true) {
			int numMatchChar = curr.key != null ? curr.key.equalDistance(currKey) : 0;
			if (numMatchChar == currKey.length() && numMatchChar == (curr.key != null ? curr.key.length() : 0)) {
				if (curr.data != null) {
					V data = curr.data;
					if (prev == null) {
						return curr.setValue(null);
					} else if (curr.children.isEmpty()) {
						for (Iterator<RadixEntry> it = prev.children.iterator(); it.hasNext(); ) {
							RadixEntry tmp = it.next();
							if (tmp.key.equals(curr.key)) {
								it.remove();
								break;
							}
						}
						if (prev.children.size() == 1 && prev.data == null && prev != rootNode) {
							mergeNodes(prev, prev.children.get(0));
						}
					}
					if (curr.children.size() == 1) {
						mergeNodes(curr, curr.children.get(0));
					} else {
						curr.data = null;
					}
					return data;
				}
			} else if (curr.key == null || (numMatchChar < currKey.length() && numMatchChar == curr.key.length())) {
				currKey = currKey.subSequence(numMatchChar);
				prev = curr;
				for (RadixEntry chield : curr.children) {
					if (chield.key.equalDistance(currKey) > 0) {
						curr = chield;
						break;
					}
				}
			} else {
				return null;
			}
		}
	}

	private void mergeNodes(RadixEntry parent, RadixEntry child) {
		parent.key = parent.key.append(child.key);
		parent.data = child.data;
		parent.children = child.children;
	}

	/**
	 * Metoda za stetje elementov v strukturi
	 *
	 * @return Stevilo elementov v srukturi
	 */
	public int count() {
		int size = 0;
		Stack<RadixEntry> stack = new Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null) {
				size++;
			}
			for (RadixEntry chield : curr.children) {
				stack.push(chield);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
			} else {
				curr = null;
			}
		}
		return size;
	}

	public List<V> asList() {
		List<V> list = new ArrayList<>(count());
		asList(list);
//		asList(rootNode, list);
		return list;
	}

	/**
	 * Iterativna metoda za polnjenje Lista
	 *
	 * @param list List, ki ga zelimo napolniti
	 */
	private void asList(List<V> list) {
		Stack<RadixEntry> stack = new Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null) {
				list.add(curr.data);
			}
			for (RadixEntry chield : curr.children) {
				stack.push(chield);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
			} else {
				curr = null;
			}
		}
	}

	/**
	 * Rekurzivna metoda za polnjenje Lista
	 *
	 * @param node Vozlisce, ki ga obdelujemo
	 * @param list List, ki ga zelimo napolniti
	 */
	private void asList(RadixEntry node, List<V> list) {
		node.children.forEach(children -> asList(children, list));
		if (node.data != null) {
			list.add(node.data);
		}
	}

	@Deprecated
	public void printTree() {
		printTree(0, this.rootNode);
	}

	private void printTree(int len, RadixEntry node) {
		System.out.print("|");
		for (int i = 0; i < len; i++) {
			System.out.print("_");
		}
		if (node.data != null) {
			System.out.printf("%s => [%s]%n", node.key, node.data.toString());
		} else {
			System.out.printf("%s%n", (node.key != null ? node.key : ""));
		}
		for (RadixEntry child : node.children) {
			printTree(len + (node.key != null ? node.key.length() : 0), child);
		}
	}

	@Override
	public Iterator<V> iterator() {
		return new RadixTreeIterator();
	}

	@Override
	public int size() {
		return count();
	}

	@Override
	public boolean isEmpty() {
		return this.rootNode.children.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) throws NullPointerException, ClassCastException {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) throws NullPointerException {
		if (value == null) {
			throw new NullPointerException();
		}
//		return search(value);
		return search(rootNode, value);
	}

	/**
	 * Iterativna metoda za iskanje elementa
	 *
	 * @param value Vrednost elementa
	 * @return Ali element obstaja v strukturi
	 */
	private boolean search(Object value) {
		Stack<RadixEntry> stack = new Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null && curr.data.equals(value)) {
				return true;
			} else {
				for (RadixEntry chield : curr.children) {
					stack.push(chield);
				}
				if (!stack.isEmpty()) {
					curr = stack.pop();
				} else {
					curr = null;
				}
			}
		}
		return false;
	}

	/**
	 * Rekurzivna metoda za iskanje elementa
	 *
	 * @param node Vozlisce, ki ga pregledujemo
	 * @param value Vrednost elementa
	 * @return Ali element obstaja v strukturi
	 */
	private boolean search(RadixEntry node, Object value) {
		if (node.data != null && node.data.equals(value)) {
			return true;
		} else {
			for (RadixEntry child : node.children) {
				if (this.search(child, value)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public V get(Object key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		} else {
			K sKey = (K) key;
			return search(sKey);
//			return search(rootNode, sKey);
		}
	}

	@Override
	public V put(K key, V value) throws NullPointerException {
		return add(value, key);
	}

	@Override
	public V remove(Object key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		K sKey = (K) key;
		return !isEmpty() ? delete(sKey) : null;
//		return !isEmpty() ? delete(sKey, null, rootNode) : null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) throws NullPointerException {
		map.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
	}

	@Override
	public void clear() {
		rootNode = new RadixEntry();
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>(count());
		keySet(set);
//		keySet(rootNode, rootNode.key, set);
		return set;
	}

	/**
	 * Iterativna metoda za polnjenje Seta s kjuci
	 *
	 * @param set Set, ki ga zelimo napolniti
	 */
	private void keySet(Set<K> set) {
		Stack<RadixEntry> stack = new Stack<>();
		Stack<K> kStack = new Stack<>();
		RadixEntry curr = rootNode;
		K key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				set.add(key);
			}
			for (RadixEntry chield : curr.children) {
				stack.push(chield);
				if (key != null) {
					kStack.push(key);
				}
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				if (!kStack.isEmpty()) {
					key = kStack.pop().append(curr.key);
				} else {
					key = curr.key;
				}
			} else {
				curr = null;
			}
		}
	}

	/**
	 * Rekurzivna metoda za polnjenje Seta s kjuci
	 *
	 * @param node Vozlisce, ki ga pregledujemo
	 * @param key  Kjuc, vozlisca (v trenutnem vozliscu se nahaj samo delcek kljuca)
	 * @param set  Set, ki ga zelimo napolniti
	 */
	private void keySet(RadixEntry node, String key, Set<String> set) {
		if (node.data != null) {
			set.add(key + node.key);
		}
		node.children.forEach(children -> keySet(children, key + node.key, set));
	}

	@Override
	public Collection<V> values() {
		return asList();
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = new HashSet<>(count());
		entrySet(set);
//		entrySet(rootNode, rootNode.key, set);
		return set;
	}

	/**
	 * Iterativna metoda za polnjenje Seta z zapisi v nasi strukturi
	 *
	 * @param set Set, ki ga zelmo napolniti
	 */
	private void entrySet(Set<Map.Entry<K, V>> set) {
		Stack<RadixEntry> stack = new Stack<>();
		Stack<K> kStack = new Stack<>();
		RadixEntry curr = rootNode;
		K key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				set.add(new RadixEntry(curr.data, key, null));
			}
			for (RadixEntry chiled : curr.children) {
				stack.push(chiled);
				if (key != null) {
					kStack.push(key);
				}
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				if (!kStack.isEmpty()) {
					key = kStack.pop().append(curr.key);
				} else {
					key = curr.key;
				}
			} else {
				curr = null;
			}
		}
	}

	/**
	 * Rekurzivna metoda za polnjenje Seta z zapisi v nasi podatkovni srukturi
	 *
	 * @param node Vozlisce, ki ga obdelujemo
	 * @param key  Kjuc trenutnega vozlisca (v trenutnem vozliscu je samo del kjuca)
	 * @param set  Set, ki ga zelmo napolniti
	 */
	private void entrySet(RadixEntry node, K key, Set<Map.Entry<K, V>> set) {
		if (node.data != null) {
			set.add(new RadixEntry(node.data, key.append(node.key), null));
		}
		node.children.forEach(children -> entrySet(children, key.append(node.key), set));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		makeString(builder);
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	private void makeString(StringBuilder builder) {
		Stack<RadixEntry> stack = new Stack<>();
		Stack<K> kStack = new Stack<>();
		RadixEntry curr = rootNode;
		K key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				builder.append(key).append('=').append(curr.data).append(", ");
			}
			for (RadixEntry chield : curr.children) {
				stack.push(chield);
				kStack.push(key);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				key = kStack.pop().append(curr.key);
			} else {
				curr = null;
			}
		}
	}

	class RadixEntry implements Map.Entry<K, V> {

		protected List<RadixEntry> children;
		private V data;
		private K key;

		RadixEntry(V data, K key, List<RadixEntry> children) {
			this.data = data;
			this.key = key;
			this.children = children;
		}

		RadixEntry(V data, K key) {
			this(data, key, new LinkedList<>());
		}

		RadixEntry() {
			this(null, null);
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.data;
		}

		@Override
		public V setValue(V v) throws NullPointerException {
			if (v == null) {
				throw new NullPointerException();
			}
			V ret = this.data;
			this.data = v;
			return ret;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null) return false;
			if (o instanceof Entry) {
				Entry e = (Entry) o;
				return (getKey() != null ? getKey().equals(e.getKey()) : null == e.getKey()) && (getValue() != null ? getValue().equals(e.getValue()) : null == e.getValue());
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return getKey() != null ? getKey().hashCode() : 0;
		}
	}

	class RadixTreeIterator implements Iterator<V> {

		private Stack<Iterator<RadixEntry>> stackIt;
		private RadixEntry next;

		RadixTreeIterator() {
			this.stackIt = new Stack<>();
			this.stackIt.push(rootNode.children.iterator());
			if (!stackIt.peek().hasNext()) {
				next = null;
			} else {
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public V next() throws NoSuchElementException {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			V tmp = (V) next.data;
			if (!next.children.isEmpty()) {
				stackIt.push(next.children.iterator());
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			} else if (stackIt.peek().hasNext()) {
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			} else {
				do {
					stackIt.pop();
					if (stackIt.isEmpty()) {
						next = null;
						return tmp;
					}
				} while (!stackIt.peek().hasNext());
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			}
			return tmp;
		}
	}
}
