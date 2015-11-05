package org.datastruct;

import org.datastruct.exception.DuplicateKeyException;

import java.util.*;

@SuppressWarnings("deprecation")
public class RadixTree<V> implements Map<String, V>, Iterable<V> {

	private RadixEntry<V> rootNode;

	public RadixTree() {
		this.rootNode = new RadixEntry<>();
	}

	public void add(V data, String key) throws NullPointerException, DuplicateKeyException {
		if (data != null && key != null) {
			this.insert(data, key, this.rootNode);
		} else {
			throw new NullPointerException();
		}
	}

	private void insert(V data, String key, RadixEntry<V> node) throws DuplicateKeyException {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(numMatchChar == 0 || (numMatchChar == node.key.length() && numMatchChar < key.length())) {
			boolean add = true;
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for (RadixEntry<V> child : node.children) {
				if(child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					this.insert(data, ostanekKljuca, child);
					add = false;
					break;
				}
			}
			if (add) node.children.add(new RadixEntry(data, ostanekKljuca));
		} else if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if(node.data != null) {
				throw new DuplicateKeyException("Duplicate key: '"+key+"'");
			} else {
				node.data = data;
			}
		} else {
			RadixEntry<V> tmp1 = new RadixEntry(node.data, node.key.substring(numMatchChar, node.key.length()), node.children);
			node.key = key.substring(0, numMatchChar);
			node.children = new LinkedList<>();
			node.children.add(tmp1);
			if(numMatchChar < key.length()) {
				RadixEntry<V> tmp2 = new RadixEntry<>(data, key.substring(numMatchChar, key.length()));
				node.data = null;
				node.children.add(tmp2);
			} else {
				node.data = data;
			}
		}
	}

	private V get(String key) {
		if(this.isEmpty()) {
			return null;
		} else {
			return this.findNode(key, this.rootNode);
		}
	}

	private V findNode(String key, RadixEntry<V> node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.data;
		} else if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar >= node.key.length())) {
			String ostanekKjuca = key.substring(numMatchChar, key.length());
			for (RadixEntry<V> child : node.children) {
				if(child.key.charAt(0) == ostanekKjuca.charAt(0)) return this.findNode(ostanekKjuca, child);
			}
		}
		return null;
	}

	public boolean remove(String key) {
		return !this.isEmpty() && this.deleteNode(key, null, this.rootNode);
	}

	private boolean deleteNode(String key, RadixEntry<V> parent, RadixEntry<V> node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for (RadixEntry<V> child : node.children) {
				if(child.key.charAt(0) == ostanekKljuca.charAt(0)) return this.deleteNode(ostanekKljuca, node, child);
			}
		} else if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if(node.data != null) {
				if(node.children.isEmpty()) {
					for (Iterator<RadixEntry<V>> it = parent.children.iterator(); it.hasNext(); ) {
						if(it.next().key.equals(node.key)) it.remove();
					}
					if(parent.children.size() == 1 && parent.data == null && !parent.key.equals("")) this.mergeNodes(parent, parent.children.get(0));
				} else if(node.children.size() == 1) {
					this.mergeNodes(node, node.children.get(0));
				} else {
					node.data = null;
				}
				return true;
			}
		}
		return false;
	}

	private void mergeNodes(RadixEntry<V> parent, RadixEntry<V> child) {
		parent.key += child.key;
		parent.data = child.data;
		parent.children = child.children;
	}

	public int count() {
		return this.count(this.rootNode);
	}

	private int count(RadixEntry<V> node) {
		int size = 0;
		if(node.data != null) size++;
		for (RadixEntry<V> child : node.children) size += this.count(child);
		return size;
	}

	public List<V> asList() {
		return this.asList(this.rootNode, new LinkedList<>());
	}

	private List<V> asList(RadixEntry<V> node, List<V> list) {
		node.children.forEach(children -> list.addAll(asList(children, new LinkedList<>())));
		if(node.data != null) list.add(node.data);
		return list;
	}

	@Deprecated
	public void printTree() {
		printTree(0, this.rootNode);
	}

	@Deprecated
	private void printTree(int len, RadixEntry<V> node) {
		System.out.print("|");
		for (int i = 0; i < len; i++) {
			System.out.print("_");
		}
		if (node.data != null) {
			System.out.printf("%s => [%s]%n", node.key, node.data.toString());
		} else {
			System.out.printf("%s%n", node.key);
		}
		for (RadixEntry<V> child : node.children) {
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
	public boolean containsKey(Object key) {
		return (key instanceof String ? get((String) key) : null) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		return search(rootNode, value);
	}

	private boolean search(RadixEntry<V> node, Object value) {
		if(node.data.equals(value)) {
			return true;
		} else {
			for (RadixEntry<V> child : node.children) {
				if (this.search(child, value)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public V get(Object key) {
		return key instanceof String ? get((String) key) : null;
	}

	@Override
	public V put(String key, V value) {
		try {
			add(value, key);
			return value;
		} catch (NullPointerException | DuplicateKeyException e) {
			return null;
		}
	}

	@Override
	public V remove(Object key) {
		return !isEmpty() ? returnDeletedNode((String) key, null, rootNode) : null;
	}

	private V returnDeletedNode(String key, RadixEntry<V> parent, RadixEntry<V> node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for (RadixEntry<V> child : node.children) {
				if(child.key.charAt(0) == ostanekKljuca.charAt(0))
					return returnDeletedNode(ostanekKljuca, node, child);
			}
		} else if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if(node.data != null) {
				V data = null;
				if(node.children.isEmpty()) {
					for (Iterator<RadixEntry<V>> it = parent.children.iterator(); it.hasNext(); ) {
						RadixEntry<V> tmp = it.next();
						if (tmp.key.equals(node.key)) {
							it.remove();
							data = (V) tmp.data;
						}
					}
					if(parent.children.size() == 1 && parent.data == null && !parent.key.equals(""))
						mergeNodes(parent, parent.children.get(0));
				} else if(node.children.size() == 1) {
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
	public void putAll(Map<? extends String, ? extends V> map) {
		map.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
	}

	@Override
	public void clear() {
		rootNode = new RadixEntry();
	}

	@Override
	public Set<String> keySet() {
		return keysAsSet(rootNode, rootNode.key, new HashSet<>());
	}

	private Set<String> keysAsSet(RadixEntry<V> node, String key, Set<String> set) {
		node.children.forEach(children -> set.addAll(keysAsSet(children, key + children.key, new HashSet<>())));
		if (node.data != null) set.add(key);
		return set;
	}

	@Override
	public Collection<V> values() {
		return asList();
	}

	@Override
	public Set<Map.Entry<String, V>> entrySet() {
		return entryAsSet(rootNode, rootNode.key, new HashSet<>());
	}

	private Set<Map.Entry<String, V>> entryAsSet(RadixEntry<V> node, String key, Set<Map.Entry<String, V>> set) {
		if (node.data != null) {
			set.add(new RadixEntry<>(node.data, key + node.key, null));
		}
		node.children.forEach(children -> set.addAll(entryAsSet(children, key + node.key, new HashSet<>())));
		return set;
	}

	class RadixEntry<V> implements Map.Entry<String, V> {

		protected List<RadixEntry<V>> children;
		private V data;
		private String key;

		RadixEntry(V data, String key, List<RadixEntry<V>> children) {
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
		public V setValue(V v) {
			V ret = this.data;
			this.data = v;
			return ret;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o instanceof RadixEntry) return true;
			RadixEntry that = (RadixEntry) o;
			if (data != null ? !data.equals(that.data) : that.data != null) return false;
			if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
			return true;
		}

		@Override
		public int hashCode() {
			return getKey() != null ? getKey().hashCode() : 0;
		}
	}

	class RadixTreeIterator implements Iterator<V> {

		private Stack<Iterator> stackIt;
		private RadixEntry next;

		RadixTreeIterator() {
			this.stackIt = new Stack<>();
			this.stackIt.push(rootNode.children.iterator());
			if (!stackIt.peek().hasNext()) {
				next = null;
			} else {
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.offer(tmpNode.children.iterator());
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
				stackIt.offer(next.children.iterator());
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.offer(tmpNode.children.iterator());
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
					stackIt.poll();
					if (stackIt.isEmpty()) {
						next = null;
						return tmp;
					}
				} while (!stackIt.peek().hasNext());
				RadixEntry tmpNode = (RadixEntry) stackIt.peek().next();
				while (tmpNode.data == null) {
					stackIt.offer(tmpNode.children.iterator());
					tmpNode = (RadixEntry) stackIt.peek().next();
				}
				next = tmpNode;
			}
			return tmp;
		}
	}
}
