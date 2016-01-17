package org.datastruct;

import java.util.*;

@SuppressWarnings("deprecation")
public class RadixTree<V> implements IMap<String, V>, Iterable<V> {

	private RadixEntry rootNode;

	public RadixTree() {
		rootNode = new RadixEntry();
	}

	public V add(V data, String key) throws NullPointerException {
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
	private V insert(V data, String key, RadixEntry node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.setValue(data);
		} else if (numMatchChar == 0 || (numMatchChar == node.key.length() && numMatchChar < key.length())) {
			String ostanekKljuca = key.substring(numMatchChar);
			RadixEntry chield = node.children.get(ostanekKljuca.charAt(0));
			if (chield != null) {
				return insert(data, ostanekKljuca, chield);
			} else {
				node.children.put(ostanekKljuca.charAt(0), new RadixEntry(data, ostanekKljuca));
				return null;
			}
		} else {
			RadixEntry tmp = new RadixEntry(node.data, node.key.substring(numMatchChar), node.children);
			node.key = key.substring(0, numMatchChar);
			node.children = new SkipMap<>(5);
			node.children.put(tmp.key.charAt(0), tmp);
			if (numMatchChar < key.length()) {
				RadixEntry tmp1 = new RadixEntry(data, key.substring(numMatchChar));
				node.data = null;
				node.children.put(tmp1.key.charAt(0), tmp1);
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
	private V insert(V data, String key) {
		RadixEntry curr = rootNode;
		String currKey = key;
		while (true) {
			int numMatchChar = curr.getNumberOfMatchingCharacters(currKey);
			if (numMatchChar == currKey.length() && numMatchChar == curr.key.length()) {
				return curr.setValue(data);
			} else if (numMatchChar == 0 || (numMatchChar == curr.key.length() && numMatchChar < currKey.length())) {
				currKey = currKey.substring(numMatchChar);
				RadixEntry chield = curr.children.get(currKey.charAt(0));
				if (chield != null) {
					curr = chield;
				} else {
					curr.children.put(currKey.charAt(0), new RadixEntry(data, currKey));
					return null;
				}
			} else {
				RadixEntry tmp = new RadixEntry(curr.data, curr.key.substring(numMatchChar), curr.children);
				curr.key = currKey.substring(0, numMatchChar);
				curr.children = new SkipMap<>(5);
				curr.children.put(tmp.key.charAt(0), tmp);
				if (numMatchChar < currKey.length()) {
					RadixEntry tmp1 = new RadixEntry(data, currKey.substring(numMatchChar));
					curr.data = null;
					curr.children.put(tmp1.key.charAt(0), tmp1);
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
	private V search(String key) {
		String currKey = key;
		RadixEntry curr = rootNode;
		while (curr != null) {
			int numMatchChar = curr.getNumberOfMatchingCharacters(currKey);
			if (numMatchChar == currKey.length() && numMatchChar == curr.key.length()) {
				return curr.data;
			} else if (numMatchChar < currKey.length() && numMatchChar == curr.key.length()) {
				currKey = currKey.substring(numMatchChar);
				curr = curr.children.get(currKey.charAt(0));
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
	private V search(RadixEntry node, String key) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.data;
		} else if (numMatchChar < key.length() && numMatchChar == node.key.length()) {
			String nKey = key.substring(numMatchChar);
			RadixEntry chield = node.children.get(nKey.charAt(0));
			if (chield != null) {
				return search(chield, nKey);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean remove(String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		return !isEmpty() && delete(key) != null;
//		return !isEmpty() && delete(key, null, this.rootNode) != null;
	}

	/**
	 * Rekurzivna metoda za brisanje elementa
	 *
	 * @param key    Kjuc elementa
	 * @param parent Predhodnik trenutnga vozlisca
	 * @param node   Trenutno vozlisce v obdelavi
	 * @return Vrednost izbrisanega elementa
	 */
	private V delete(String key, RadixEntry parent, RadixEntry node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if (node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar);
			RadixEntry chield = node.children.get(ostanekKljuca.charAt(0));
			if (chield != null) {
				return delete(ostanekKljuca, node, chield);
			} else {
				return null;
			}
		} else if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if (node.data != null) {
				V data = node.data;
				if (node.children.isEmpty()) {
					parent.children.remove(node.key.charAt(0));
					if (parent.children.size() == 1 && parent.data == null && !parent.key.equals("")) {
						mergeNodes(parent, node);
					}
				} else if (node.children.size() == 1) {
					mergeNodes(node, node.children.getAt(0));
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
	private V delete(String key) {
		RadixEntry curr = rootNode;
		RadixEntry prev = null;
		String currKey = key;
		V data = null;
		while (curr != null) {
			int numMatchChar = curr.getNumberOfMatchingCharacters(currKey);
			if (numMatchChar == currKey.length() && numMatchChar == curr.key.length()) {
				if (curr.data != null) {
					data = curr.data;
					if (curr.children.isEmpty()) {
						prev.children.remove(curr.key.charAt(0));
						if (prev.children.size() == 1 && prev.data == null && prev != rootNode) {
							mergeNodes(prev, prev.children.getAt(0));
						}
					} else if (curr.children.size() == 1 && prev != rootNode) {
						mergeNodes(curr, curr.children.getAt(0));
					} else {
						curr.data = null;
					}
				}
				curr = null;
			} else if (curr.key.equals("") || (numMatchChar < currKey.length() && numMatchChar == curr.key.length())) {
				currKey = currKey.substring(numMatchChar);
				prev = curr;
				curr = curr.children.get(currKey.charAt(0));
			} else {
				curr = null;
			}
		}
		return data;
	}

	private void mergeNodes(RadixEntry parent, RadixEntry child) {
		parent.key += child.key;
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
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null) {
				size++;
			}
			for (RadixEntry chield : curr.children.values()) {
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
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null) {
				list.add(curr.data);
			}
			for (RadixEntry chield : curr.children.values()) {
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
		node.children.values().forEach(children -> asList(children, list));
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
			System.out.printf("%s%n", node.key);
		}
		for (RadixEntry child : node.children.values()) {
			printTree(len + node.key.length(), child);
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
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		while (curr != null) {
			if (curr.data != null && curr.data.equals(value)) {
				return true;
			} else {
				for (RadixEntry chield : curr.children.values()) {
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
	 * @param node  Vozlisce, ki ga pregledujemo
	 * @param value Vrednost elementa
	 * @return Ali element obstaja v strukturi
	 */
	private boolean search(RadixEntry node, Object value) {
		if (node.data != null && node.data.equals(value)) {
			return true;
		} else {
			for (RadixEntry child : node.children.values()) {
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
			String sKey = (String) key;
			return search(sKey);
//			return search(rootNode, sKey);
		}
	}

	@Override
	public V put(String key, V value) throws NullPointerException {
		return add(value, key);
	}

	@Override
	public V remove(Object key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		String sKey = (String) key;
		return !isEmpty() ? delete(sKey) : null;
//		return !isEmpty() ? delete(sKey, null, rootNode) : null;
	}

	@Override
	public void clear() {
		rootNode = new RadixEntry();
	}

	@Override
	public Set<String> keySet() {
		Set<String> set = new HashSet<>(count());
		keySet(set);
//		keySet(rootNode, rootNode.key, set);
		return set;
	}

	/**
	 * Iterativna metoda za polnjenje Seta s kjuci
	 *
	 * @param set Set, ki ga zelimo napolniti
	 */
	private void keySet(Set<String> set) {
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		org.datastruct.Stack<String> kStack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		String key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				set.add(key);
			}
			for (RadixEntry chield : curr.children.values()) {
				stack.push(chield);
				kStack.push(key);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				key = kStack.pop() + curr.key;
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
		node.children.values().forEach(children -> keySet(children, key + node.key, set));
	}

	@Override
	public Collection<V> values() {
		return asList();
	}

	@Override
	public Set<Map.Entry<String, V>> entrySet() {
		Set<Map.Entry<String, V>> set = new HashSet<>(count());
		entrySet(set);
//		entrySet(rootNode, rootNode.key, set);
		return set;
	}

	/**
	 * Iterativna metoda za polnjenje Seta z zapisi v nasi strukturi
	 *
	 * @param set Set, ki ga zelmo napolniti
	 */
	private void entrySet(Set<Map.Entry<String, V>> set) {
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		org.datastruct.Stack<String> kStack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		String key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				set.add(new RadixEntry(curr.data, key, null));
			}
			for (RadixEntry chiled : curr.children.values()) {
				stack.push(chiled);
				kStack.push(key);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				key = kStack.pop() + curr.key;
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
	private void entrySet(RadixEntry node, String key, Set<Map.Entry<String, V>> set) {
		if (node.data != null) {
			set.add(new RadixEntry(node.data, key + node.key, null));
		}
		node.children.values().forEach(children -> entrySet(children, key + node.key, set));
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
		org.datastruct.Stack<RadixEntry> stack = new org.datastruct.Stack<>();
		org.datastruct.Stack<String> kStack = new org.datastruct.Stack<>();
		RadixEntry curr = rootNode;
		String key = rootNode.key;
		while (curr != null) {
			if (curr.data != null) {
				builder.append(key).append('=').append(curr.data).append(", ");
			}
			for (RadixEntry chield : curr.children.values()) {
				stack.push(chield);
				kStack.push(key);
			}
			if (!stack.isEmpty()) {
				curr = stack.pop();
				key = kStack.pop() + curr.key;
			} else {
				curr = null;
			}
		}
	}

	class RadixEntry implements Map.Entry<String, V> {

		SkipMap<Character, RadixEntry> children;
		V data;
		String key;

		private RadixEntry(V data, String key, SkipMap<Character, RadixEntry> children) {
			this.data = data;
			this.key = key;
			this.children = children;
		}

		private RadixEntry(V data, String key) {
			this(data, key, new SkipMap<>(5));
		}

		private RadixEntry() {
			this(null, "");
		}

		private int getNumberOfMatchingCharacters(String key) {
			int numChar = 0;
			while (numChar < key.length() && numChar < this.key.length()) {
				if (key.charAt(numChar) != this.key.charAt(numChar)) {
					break;
				} else {
					numChar++;
				}
			}
			return numChar;
		}

		@Override
		public String getKey() {
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

	private class RadixTreeIterator implements Iterator<V> {

		private org.datastruct.Stack<Iterator<RadixEntry>> stackIt;
		private RadixEntry next;

		private RadixTreeIterator() {
			this.stackIt = new org.datastruct.Stack<>();
			this.stackIt.push(rootNode.children.values().iterator());
			if (!stackIt.peek().hasNext()) {
				next = null;
			} else {
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.values().iterator());
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
				stackIt.push(next.children.values().iterator());
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.values().iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			} else if (stackIt.peek().hasNext()) {
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.push(tmpNode.children.values().iterator());
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
					stackIt.push(tmpNode.children.values().iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			}
			return tmp;
		}
	}
}

