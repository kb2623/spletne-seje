package org.datastruct;

import java.io.*;
import java.util.*;

public class BinomskaKopica<Element extends Comparable<Element>> implements Queue<Element> {

	public class BinHeapaNode<T> {
		public T data;
		public BinHeapaNode<T> parent;
		public BinHeapaNode<T> sibling;
		public BinHeapaNode<T> child;
		public int depth;
		public BinHeapaNode(T data, BinHeapaNode<T> parent, BinHeapaNode<T> sibling, BinHeapaNode<T> child, int depth) {
			this.data = data;
			this.parent = parent;
			this.sibling = sibling;
			this.child = child;
			this.depth = depth;
		}
	}

	private BinHeapaNode<Element> topNode;
	private Comparator<Element> cmp;

	public BinomskaKopica() {
		this.topNode = null;
		this.cmp = null;
	}

	public BinomskaKopica(Comparator<Element> comparator) {
		this.topNode = null;
		this.cmp = comparator;
	}

	public void setComparator(Comparator<Element> comparator) {
		List<Element> list = this.asList();
		this.cmp = comparator;
		this.topNode = null;
		for(Element t: list) {
			this.add(t);
		}
	}

	private int compare(Element o1, Element o2) {
		if(this.cmp != null) {
			return this.cmp.compare(o1, o2);
		} else {
			return o1.compareTo(o2);
		}
	}

	public void add(Element e) {
		if(this.isEmpty()) {
			this.topNode = new BinHeapaNode<>(e, null, null, null, 0);
		} else {
			this.topNode = new BinHeapaNode<>(e, null, this.topNode, null, 0);
			while(this.topNode.sibling != null && this.topNode.depth == this.topNode.sibling.depth) {
				this.topNode = this.merge(this.topNode, this.topNode.sibling);
			}
		}
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Element> collection) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return false;
	}

	@Override
	public void clear() {

	}

	@Override
	public boolean offer(Element element) {
		return false;
	}

	@Override
	public Element remove() {
		return null;
	}

	@Override
	public Element poll() {
		return null;
	}

	@Override
	public Element element() {
		return null;
	}

	@Override
	public Element peek() {
		return null;
	}

	private BinHeapaNode<Element> merge(BinHeapaNode<Element> prevNode, BinHeapaNode<Element> nextNode) {
		if(this.compare(prevNode.data, nextNode.data) < 0) {
			prevNode.parent = nextNode;
			prevNode.sibling = nextNode.child;
			nextNode.child = prevNode;
			nextNode.depth = 1 + prevNode.depth;
			return nextNode;
		} else {
			prevNode.sibling = nextNode.sibling;
			nextNode.parent = prevNode;
			nextNode.sibling = prevNode.child;
			prevNode.child = nextNode;
			prevNode.depth = 1 + nextNode.depth;
			return prevNode;
		}
	}

	@SuppressWarnings("all")
	public Element removeFirst(){
		if(this.isEmpty()) {
			throw new java.util.NoSuchElementException();
		} else {
			BinHeapaNode<Element> prevMax = null, currMax = this.topNode;
			BinHeapaNode<Element> curr = this.topNode;
			while(curr.sibling != null) {
				if(this.compare(curr.sibling.data, currMax.data) > 0) 	{
					currMax = curr.sibling;
					prevMax = curr;
				}
				curr = curr.sibling;
			}
			if(prevMax != null) {
				prevMax.sibling = currMax.sibling;
			} else {
				this.topNode = this.topNode.sibling;
			}
			this.removeNode(currMax);
			return currMax.data;
		}
	}

	private BinHeapaNode<Element> fixAll(BinHeapaNode<Element> node) {
		if(node.sibling == null) {
			return node;
		} else if(node.depth == node.sibling.depth) {
			while(node.sibling != null && node.depth == node.sibling.depth) {
				node = this.merge(node, node.sibling);
			}
			return node;
		} else {
			node.sibling = fixAll(node.sibling);
			return node;
		}
	}

	public Element getFirst() {
		if(this.isEmpty()) {
			throw new java.util.NoSuchElementException();
		} else {
			Element max = this.topNode.data;
			BinHeapaNode<Element> curr = this.topNode;
			while(curr != null) {
				if(this.compare(max, curr.data) < 0) {
					max = curr.data;
				}
				curr = curr.sibling;
			}
			return max;
		}
	}

	public int size() {
		if(this.isEmpty()) {
			return 0;
		} else {
			return this.size(this.topNode);
		}
	}

	private int size(BinHeapaNode<Element> node) {
		if(node == null) {
			return 0;
		} else {
			return 1 + this.size(node.child) + this.size(node.sibling);
		}
	}

	public int depth() {
		if(this.isEmpty()) {
			return 0;
		} else {
			BinHeapaNode<Element> tmp = this.topNode;
			while(tmp.sibling != null) {
				tmp = tmp.sibling;
			}
			return tmp.depth;
		}
	}

	public boolean isEmpty() {
		return (this.topNode == null);
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<Element> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T[] toArray(T[] ts) {
		return null;
	}

	public boolean exists(Element e) {
		return (this.findNode(e) != null);
	}

	public Element remove(Element e) {
		if(this.isEmpty()) {
			throw new java.util.NoSuchElementException();
		}
		BinHeapaNode<Element> node, prev;
		if(null == (node = this.findNode(e))) {
			throw new java.util.NoSuchElementException();
		}
		Element ret = node.data;
		while(node.parent != null) {
			node.data = node.parent.data;
			node = node.parent;
		}
		if(node == this.topNode) {
			this.topNode = this.topNode.sibling;
		} else {
			prev = this.topNode;
			while(prev.sibling != node) {
				prev = prev.sibling;
			}
			prev.sibling = node.sibling;
		}
		this.removeNode(node);
		return ret;
	}

	private BinHeapaNode<Element> findNode(Element e) {
		BinHeapaNode<Element> curr = this.topNode;
		Stack<BinHeapaNode<Element>> stack = new Stack<>();
		while(curr != null || !stack.isEmpty()) {
			if(curr == null) {
				curr = stack.pop().sibling;
			} else if(this.compare(curr.data, e) == 0) {
				break;
			} else if(this.compare(curr.data, e) > 0) {
				stack.push(curr);
				curr = curr.child;
			} else {
				curr = curr.sibling;
			}
		}
		return curr;
	}

	private void removeNode(BinHeapaNode<Element> node) {
		if(null != node.child) {
			node.sibling = null;
			BinHeapaNode<Element> curr, prev;
			Stack<BinHeapaNode<Element>> stack = new Stack<>();
			curr = node.child;
			while(curr != null) {
				curr.parent = null;
				stack.push(curr);
				curr = curr.sibling;
			}
			curr = this.topNode;
			prev = null;
			while(curr != null && !stack.isEmpty()) {
				if(curr.depth == stack.peek().depth) {
					if(prev != null) {
						prev.sibling = this.merge(stack.pop(), curr);
						curr = prev.sibling.sibling;
						prev = prev.sibling;
					} else {
						this.topNode = this.merge(stack.pop(), this.topNode);
						prev = this.topNode;
						curr = this.topNode.sibling;
					}
				} else {
					if(prev != null) {
						prev.sibling = stack.pop();
						prev.sibling.sibling = curr;
						prev = prev.sibling;
					} else {
						this.topNode = stack.pop();
						this.topNode.sibling = curr;
						prev = this.topNode;
					}
				}
			}
			while(!stack.isEmpty()) {
				if(prev != null) {
					prev.sibling = stack.pop();
					prev.sibling.sibling = null;
					prev = prev.sibling;
				} else {
					this.topNode = stack.pop();
					this.topNode.sibling = null;
					prev = this.topNode;
				}
			}
			this.topNode = fixAll(this.topNode);
		}
	}

	public List<Element> asList() {
		if(this.isEmpty()) {
			return null;
		}
		List<Element> list = new ArrayList<>(this.size());
		Stack<BinHeapaNode<Element>> stack = new Stack<>();
		BinHeapaNode<Element> curr = this.topNode;
		while(curr != null || !stack.isEmpty()) {
			if(curr == null) {
				curr = stack.pop();
				list.add(curr.data);
				curr = curr.sibling;
			} else if(curr.child != null) {
				stack.push(curr);
				curr = curr.child;
			} else {
				list.add(curr.data);
				curr = curr.sibling;
			}
		}
		return list;
	}

	public String print() {
		StringBuilder builder = new StringBuilder();
		int tabs = 0;
		BinHeapaNode<Element> node = this.topNode;
		while(node != null) {
			if(node.child == null) {
				if(node.parent == null) {
					builder.append(node.data);
					node = node.sibling;
				} else {
					for(int i = 1; i < tabs; i++) {
						builder.append('\t');
					}
					builder.append(node.parent.data).append('\t').append(node.data);
					node = node.parent.sibling;
					tabs--;
				}
			} else {
				node = node.child;
				tabs++;
			}
		}
		return builder.toString();
	}

	public void save(OutputStream outputStream) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeByte(3);
		out.writeInt(this.size());
		Stack<BinHeapaNode<Element>> stack = new Stack<>();
		BinHeapaNode<Element> node = this.topNode;
		while(node != null) {
			stack.push(node);
			node = node.sibling;
		}
		while(!stack.isEmpty()) {
			this.save(out, stack.pop());
		}
	}

	private void save(ObjectOutputStream out, BinHeapaNode<Element> node) throws IOException {
		if(node.child != null) {
			this.save(out, node.child);
			out.writeObject(node.data);
			if(node.sibling != null) {
				this.save(out, node.sibling);
			}
		} else {
			out.writeObject(node.data);
		}
	}

	@SuppressWarnings("unchecked")
	public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(inputStream);
		if(in.readByte() == 3) {
			this.topNode = null;
			for(int size = in.readInt(); size > 0; size--) {
				this.add((Element) in.readObject());
			}
		} else {
			this.topNode = null;
			for(int size = in.readInt(); size > 0; size--) {
				this.add((Element) in.readObject());
			}
		}
	}
}
