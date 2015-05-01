package datastruct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RadixTree<T> {

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

	public T get(String key) {
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
		if(this.isEmpty()) {
			return false;
		} else {
			return this.deleteNode(key, null, this.rootNode);
		}
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

	public boolean isEmpty() {
		return this.rootNode.children.isEmpty();
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
}
