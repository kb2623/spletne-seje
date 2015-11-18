package org.datastruct;

import java.util.*;

@SuppressWarnings("deprecation")
public class RadixTree<V> implements Map<String, V>, Iterable<V> {

	private RadixEntry rootNode;

	public RadixTree() {
		this.rootNode = new RadixEntry();
	}

	public V add(V data, String key) throws NullPointerException {
		if (key == null || data == null) {
			throw new NullPointerException();
		}
		return this.insert(data, key, this.rootNode);
	}

	private V insert(V data, String key, RadixEntry node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if (numMatchChar == 0 || (numMatchChar == node.key.length() && numMatchChar < key.length())) {
			boolean add = true;
			V ret = null;
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for (RadixEntry child : node.children) {
				if (child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					ret = this.insert(data, ostanekKljuca, child);
					add = false;
					break;
				}
			}
			if (add) {
				node.children.add(new RadixEntry(data, ostanekKljuca));
			}
			return ret;
		} else if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if (node.data != null) {
				return node.setValue(data);
			} else {
				node.data = data;
				return null;
			}
		} else {
			RadixEntry tmp1 = new RadixEntry(node.data, node.key.substring(numMatchChar, node.key.length()), node.children);
			node.key = key.substring(0, numMatchChar);
			node.children = new LinkedList<>();
			node.children.add(tmp1);
			if (numMatchChar < key.length()) {
				RadixEntry tmp2 = new RadixEntry(data, key.substring(numMatchChar, key.length()));
				node.data = null;
				node.children.add(tmp2);
			} else {
				node.data = data;
			}
			return null;
		}
	}

	private V get(String key) {
		if (this.isEmpty()) {
			return null;
		} else {
			return this.findNode(key, this.rootNode);
		}
	}

	private V findNode(String key, RadixEntry node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.data;
		} else if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar >= node.key.length())) {
			String ostanekKjuca = key.substring(numMatchChar, key.length());
			for (RadixEntry child : node.children) {
				if (child.key.charAt(0) == ostanekKjuca.charAt(0)) {
					return this.findNode(ostanekKjuca, child);
				}
			}
		}
		return null;
	}

	public boolean remove(String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		return !this.isEmpty() && this.deleteNode(key, null, this.rootNode);
	}

	private boolean deleteNode(String key, RadixEntry parent, RadixEntry node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if (node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for (RadixEntry child : node.children) {
				if (child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					return this.deleteNode(ostanekKljuca, node, child);
				}
			}
		} else if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if (node.data != null) {
				if(node.children.isEmpty()) {
					for (Iterator<RadixEntry> it = parent.children.iterator(); it.hasNext(); ) {
						if (it.next().key.equals(node.key)) {
							it.remove();
						}
					}
					if (parent.children.size() == 1 && parent.data == null && !parent.key.equals("")) {
						this.mergeNodes(parent, parent.children.get(0));
					}
				} else if (node.children.size() == 1) {
					this.mergeNodes(node, node.children.get(0));
				} else {
					node.data = null;
				}
				return true;
			}
		}
		return false;
	}

	private void mergeNodes(RadixEntry parent, RadixEntry child) {
		parent.key += child.key;
		parent.data = child.data;
		parent.children = child.children;
	}

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
		return this.asList(this.rootNode, new LinkedList<>());
	}

	private List<V> asList(RadixEntry node, List<V> list) {
		node.children.forEach(children -> list.addAll(asList(children, new LinkedList<>())));
		if (node.data != null) {
			list.add(node.data);
		}
		return list;
	}

	@Deprecated
	public void printTree() {
		printTree(0, this.rootNode);
	}

	@Deprecated
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
		for (RadixEntry child : node.children) {
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
		return search(rootNode, value);
	}

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
	public V get(Object key) throws NullPointerException, ClassCastException {
		if (key == null) {
			throw new NullPointerException();
		}
		return get((String) key);
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
		return !isEmpty() ? returnDeletedNode((String) key, null, rootNode) : null;
	}

	private V returnDeletedNode(String key, RadixEntry parent, RadixEntry node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if (node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for (RadixEntry child : node.children) {
				if (child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					return returnDeletedNode(ostanekKljuca, node, child);
				}
			}
		} else if (numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if (node.data != null) {
				V data = null;
				if (node.children.isEmpty()) {
					for (Iterator<RadixEntry> it = parent.children.iterator(); it.hasNext(); ) {
						RadixEntry tmp = it.next();
						if (tmp.key.equals(node.key)) {
							it.remove();
							data = (V) tmp.data;
						}
					}
					if (parent.children.size() == 1 && parent.data == null && !parent.key.equals("")) {
						mergeNodes(parent, parent.children.get(0));
					}
				} else if (node.children.size() == 1) {
					data = node.data;
					mergeNodes(node, node.children.get(0));
				} else {
					data = node.data;
					node.data = null;
				}
				return data;
			}
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> map) throws NullPointerException {
		map.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
	}

	@Override
	public void clear() {
		rootNode = new RadixEntry();
	}

	@Override
	public Set<String> keySet() {
		Set<String> set = new HashSet<>();
		keysAsSet(rootNode, rootNode.key, set);
		return set;
	}

	private void keysAsSet(RadixEntry node, String key, Set<String> set) {
		if (node.data != null) {
			set.add(key + node.key);
		}
		node.children.forEach(children -> keysAsSet(children, key + node.key, set));
	}

	@Override
	public Collection<V> values() {
		return asList();
	}

	@Override
	public Set<Map.Entry<String, V>> entrySet() {
		Set<Map.Entry<String, V>> set = new HashSet<>();
		entryAsSet(rootNode, rootNode.key, set);
		return set;
	}

	private void entryAsSet(RadixEntry node, String key, Set<Map.Entry<String, V>> set) {
		if (node.data != null) {
			set.add(new RadixEntry(node.data, key + node.key, null));
		}
		node.children.forEach(children -> entryAsSet(children, key + node.key, set));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		makeString(rootNode, rootNode.key, builder);
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	private void makeString(RadixEntry node, String key, StringBuilder builder) {
		if (node.data != null) {
			builder.append(key + node.key).append('=').append(node.data);
			builder.append(", ");
		}
		node.children.forEach(children -> makeString(children, key + node.key, builder));
	}

	class RadixEntry implements Map.Entry<String, V> {

		protected List<RadixEntry> children;
		private V data;
		private String key;

		RadixEntry(V data, String key, List<RadixEntry> children) {
			this.data = data;
			this.key = key;
			this.children = children;
		}

		RadixEntry(V data, String key) {
			this(data, key, new LinkedList<>());
		}

		RadixEntry() {
			this(null, "");
		}

		int getNumberOfMatchingCharacters(String key) {
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
