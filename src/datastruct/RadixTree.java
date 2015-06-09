package datastruct;

import java.util.*;

public class RadixTree<T> implements Map<String, T> {

	class RadixNode {

		private T data;
		private String key;
		private List<RadixNode> children;

		public RadixNode(T data, String key) {
			this.data = data;
			this.key = key;
			this.children = new LinkedList<>();
		}

		public RadixNode() {
			this(null, "");
		}

		public int getNumberOfMatchingCharacters(String key) {
			int numChar = 0;
			while(numChar < key.length() && numChar < this.key.length()) {
				if(key.charAt(numChar) != this.key.charAt(numChar)) {
					break;
				} else {
					numChar++;
				}
			}
			return numChar;
		}
	}

	private RadixNode rootNode;

	public RadixTree() {
		this.rootNode = new RadixNode();
	}

	public void add(T data, String key) throws NullPointerException, DuplicateKeyException {
		if(data != null && key != null) {
			this.insert(data, key, this.rootNode);
		} else {
			throw new NullPointerException();
		}
	}

	private void insert(T data, String key, RadixNode node) throws DuplicateKeyException {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(numMatchChar == 0 || (numMatchChar == node.key.length() && numMatchChar < key.length())) {
			boolean add = true;
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for(RadixNode child : node.children) {
				if(child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					this.insert(data, ostanekKljuca, child);
					add = false;
					break;
				}
			}
			if(add) {
				node.children.add(new RadixNode(data, ostanekKljuca));
			}
		} else if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if(node.data != null) {
				throw new DuplicateKeyException("Duplicate key: '"+key+"'");
			} else {
				node.data = data;
			}
		} else {
			RadixNode tmp1 = new RadixNode(node.data, node.key.substring(numMatchChar, node.key.length()));
			tmp1.children = node.children;
			node.key = key.substring(0, numMatchChar);
			node.children = new LinkedList<>();
			node.children.add(tmp1);
			if(numMatchChar < key.length()) {
				RadixNode tmp2 = new RadixNode(data, key.substring(numMatchChar, key.length()));
				node.data = null;
				node.children.add(tmp2);
			} else {
				node.data = data;
			}
		}
	}

	private T get(String key) {
		if(this.isEmpty()) {
			return null;
		} else {
			return this.findNode(key, this.rootNode);
		}
	}

	private T findNode(String key, RadixNode node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.data;
		} else if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar >= node.key.length())) {
			String ostanekKjuca = key.substring(numMatchChar, key.length());
			for(RadixNode child : node.children) {
				if(child.key.charAt(0) == ostanekKjuca.charAt(0)) {
					return this.findNode(ostanekKjuca, child);
				}
			}
		}
		return null;
	}

	public boolean remove(String key) {
		return !this.isEmpty() && this.deleteNode(key, null, this.rootNode);
	}

	private boolean deleteNode(String key, RadixNode parent, RadixNode node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for(RadixNode child : node.children) {
				if(child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					return this.deleteNode(ostanekKljuca, node, child);
				}
			}
		} else if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if(node.data != null) {
				if(node.children.isEmpty()) {
					Iterator<RadixNode> it = parent.children.iterator();
					while(it.hasNext()) {
						if(it.next().key.equals(node.key)) {
							it.remove();
						}
					}
					if(parent.children.size() == 1 && parent.data == null && !parent.key.equals("")) {
						this.mergeNodes(parent, parent.children.get(0));
					}
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

	private void mergeNodes(RadixNode parent, RadixNode child) {
		parent.key += child.key;
		parent.data = child.data;
		parent.children = child.children;
	}

	public int count() {
		return this.count(this.rootNode);
	}

	private int count(RadixNode node) {
		int size = 0;
		if(node.data != null) {
			size++;
		}
		for(RadixNode child : node.children) {
			size += this.count(child);
		}
		return size;
	}

	public List<T> asList() {
		return this.asList(this.rootNode, new ArrayList<>());
	}

	private List<T> asList(RadixNode node, List<T> list) {
		for(RadixNode child : node.children) {
			this.asList(child, list);
		}
		if(node.data != null) {
			list.add(node.data);
		}
		return list;
	}

	@Deprecated
	public void printTree() {
		this.printTree(0, this.rootNode);
	}

	@Deprecated
	private void printTree(int len, RadixNode node) {
		System.out.print("|");
		for (int i = 0; i < len; i++) {
			System.out.print("_");
		}
		if (node.data != null) {
			System.out.printf("%s => [%s]%n", node.key, node.data.toString());
		} else {
			System.out.printf("%s%n", node.key);
		}
		for (RadixNode child : node.children) {
			this.printTree(len + node.key.length(), child);
		}
	}

	public Iterator<T> iterator() {
		return new RadixTree<T>.MyIterator();
	}

	private class MyIterator implements Iterator<T> {

		private final Stack<Iterator<RadixNode>> stackIt;
		private RadixNode next;

		public MyIterator() {
			this.stackIt = new Stack<>();
			this.stackIt.push(rootNode.children.iterator());
			if(!this.stackIt.peek().hasNext()) {
				this.next = null;
			} else {
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public T next() {
			if(!this.hasNext()) {
				return null;
			}
			T tmp = this.next.data;
			if(!this.next.children.isEmpty()) {
				this.stackIt.push(this.next.children.iterator());
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			} else if(this.stackIt.peek().hasNext()) {
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			} else {
				do{
					this.stackIt.pop();
					if(this.stackIt.isEmpty()) {
						this.next = null;
						return tmp;
					}
				} while(!this.stackIt.peek().hasNext());
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			}
			return tmp;
		}
	}

	@Override
	public int size() {
		return count();
	}

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

	private boolean search(RadixNode node, Object value) {
		if(node.data.equals(value)) {
			return true;
		} else {
			for(RadixNode child : node.children) {
				if (this.search(child, value)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public T get(Object key) {
		return key instanceof String ? get((String) key) : null;
	}

	@Override
	public T put(String key, T value) {
		try {
			add(value, key);
			return value;
		} catch (NullPointerException | DuplicateKeyException e) {
			return null;
		}
	}

	@Override
	public T remove(Object key) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends T> map) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public Collection<T> values() {
		return asList();
	}

	@Override
	public Set<Entry<String, T>> entrySet() {
		return null;
	}
}
