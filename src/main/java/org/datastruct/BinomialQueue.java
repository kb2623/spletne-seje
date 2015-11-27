package org.datastruct;

import java.util.*;

public class BinomialQueue<E> implements Queue<E> {

	private Node root;
	private NodeComparator cmp;

	public BinomialQueue(Comparator<E> cmp) {
		this.cmp = new NodeComparator(cmp);
		root = null;
	}

	public BinomialQueue() {
		this((e1, e2) -> e1.hashCode() - e2.hashCode());
	}

	@Override
	public int size() {
		int size = 0;
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr == null) {
				curr = stack.pop();
				size++;
				curr = curr.sibling;
			} else if (curr.chield != null) {
				stack.push(curr);
				curr = curr.chield;
			} else {
				size++;
				curr = curr.sibling;
			}
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		E e = (E) o;
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (curr != null || !stack.isEmpty()) {
			if (curr == null) {
				curr = stack.pop();
			} else if (cmp.cmp.compare(curr.data, e) == 0) {
				break;
			} else if (cmp.cmp.compare(curr.data, e) < 0) {
				if (curr.sibling != null) {
					stack.push(curr.sibling);
				}
				curr = curr.chield;
			} else {
				curr = curr.sibling;
			}
		}
		return curr != null;
	}

	@Override
	public Iterator<E> iterator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size()];
		return fillArray(array);
	}

	private <T> T[] fillArray(T[] array) {
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		int i = 0;
		while (!stack.isEmpty() || curr != null) {
			if (curr == null) {
				curr = stack.pop();
				array[i] = (T) curr.data;
				i++;
				curr = curr.sibling;
			} else if (curr.chield != null) {
				stack.push(curr);
				curr = curr.chield;
			} else {
				array[i] = (T) curr.data;
				i++;
				curr = curr.sibling;
			}
		}
		return array;
	}

	@Override
	public <T> T[] toArray(T[] ts) throws NullPointerException {
		if (ts == null) {
			throw new NullPointerException();
		} else if (ts.length >= size()) {
			return fillArray(ts);
		} else {
			return (T[]) toArray();
		}
	}

	@Override
	public boolean add(E e) throws NullPointerException {
		if (e == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			root = new Node(e, null, null);
		} else {
			root = new Node(e, root, null);
			while (root.sibling != null && root.depth == root.sibling.depth) {
				root = merge(root, root.sibling);
			}
		}
		return true;
	}

	private Node merge(Node prev, Node next) {
		if (cmp.compare(next, prev) < 0) {
			prev.sibling = next.chield;
			next.chield = prev;
			next.updateDepth();
			return next;
		} else {
			prev.sibling = next.sibling;
			next.sibling = prev.chield;
			prev.chield = next;
			prev.updateDepth();
			return prev;
		}
	}

	@Override
	public boolean remove(Object o) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object e : collection) {
			if (!contains(e)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) throws NullPointerException {
		boolean ret = true;
		for (E e : collection) {
			if (!add(e)) {
				ret = false;
			}
		}
		return ret;
	}

	@Override
	public boolean removeAll(Collection<?> collection) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> collection) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		root = null;
	}

	@Override
	public boolean offer(E e) {
		if (e == null) {
			return false;
		} else {
			return add(e);
		}
	}

	private E removeFirst() {
		if (isEmpty()) {
			return null;
		} else {
			Node min = root, minPrev = null;
			Node curr = root.sibling, prev = root;
			while (curr != null) {
				if (cmp.compare(min, curr) > 0) {
					minPrev = prev;
					min = curr;
				}
				prev = curr;
				curr = curr.sibling;
			}
			if (minPrev != null) {
				minPrev.sibling = min.sibling;
			} else {
				root = root.sibling;
			}
			removeNode(min);
			return min.data;
		}
	}

	private void removeNode(Node node) {
		if (null != node.chield) {
			node.sibling = null;
			Node curr, prev;
			Stack<Node> stack = new Stack<>();
			curr = node.chield;
			while (curr != null) {
				stack.push(curr);
				curr = curr.sibling;
			}
			curr = root;
			prev = null;
			while (curr != null && !stack.isEmpty()) {
				if (curr.depth == stack.peek().depth) {
					if (prev != null) {
						prev.sibling = merge(stack.pop(), curr);
						curr = prev.sibling.sibling;
						prev = prev.sibling;
					} else {
						root = this.merge(stack.pop(), root);
						prev = root;
						curr = root.sibling;
					}
				} else {
					if (prev != null) {
						prev.sibling = stack.pop();
						prev.sibling.sibling = curr;
						prev = prev.sibling;
					} else {
						root = stack.pop();
						root.sibling = curr;
						prev = root;
					}
				}
			}
			while (!stack.isEmpty()) {
				if (prev != null) {
					prev.sibling = stack.pop();
					prev.sibling.sibling = null;
					prev = prev.sibling;
				} else {
					root = stack.pop();
					root.sibling = null;
					prev = root;
				}
			}
			fixAll();
		}
	}

	private void fixAll() {
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (curr.sibling != null) {
			stack.push(curr);
			curr = curr.sibling;
		}
		while (!stack.isEmpty()) {
			curr = stack.pop();
			if (curr.depth == curr.sibling.depth) {
				while (curr.sibling != null && curr.depth == curr.sibling.depth) {
					curr = merge(curr, curr.sibling);
				}
				if (!stack.isEmpty()) {
					stack.peek().sibling = curr;
				} else {
					root = curr;
					curr = null;
				}
			}
		}
	}

	@Override
	public E remove() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return removeFirst();
	}

	@Override
	public E poll() {
		return removeFirst();
	}

	private Node getFirst() {
		if (isEmpty()) {
			return null;
		} else {
			Node ret = root, curr = root;
			while (curr != null) {
				if (cmp.compare(ret, curr) < 0) {
					ret = curr;
				}
				curr = curr.sibling;
			}
			return ret;
		}
	}

	@Override
	public E element() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		} else {
			return getFirst().data;
		}
	}

	@Override
	public E peek() {
		if (isEmpty()) {
			return null;
		} else {
			return getFirst().data;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (curr != null || !stack.isEmpty()) {
			if (curr == null) {
				curr = stack.pop();
				builder.append(curr.data).append(", ");
				curr = curr.sibling;
			} else if (curr.chield != null) {
				stack.push(curr);
				curr = curr.chield;
			} else {
				builder.append(curr.data).append(", ");
				curr = curr.sibling;
			}
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append(']');
		return builder.toString();
	}

	private class Node {

		E data;
		Node sibling;
		Node chield;
		int depth;

		Node(E data, Node sibling, Node chield, int depth) {
			this.data = data;
			this.sibling = sibling;
			this.chield = chield;
			this.depth = depth;
		}

		Node(E data, Node sibling, Node chield) {
			this(data, sibling, chield, 0);
			updateDepth();
		}

		void updateDepth() {
			if (chield != null) {
				depth = chield.depth + 1;
			} else {
				depth = 1;
			}
		}
	}

	private class NodeComparator implements Comparator<Node> {

		private Comparator<E> cmp;

		NodeComparator(Comparator<E> cmp) {
			this.cmp = cmp;
		}

		@Override
		public int compare(Node n1, Node n2) {
			int cmp = this.cmp.compare(n1.data, n2.data);
			if (cmp == 0) {
				if (n1.data.equals(n2.data)) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return cmp;
			}
		}
	}
}
