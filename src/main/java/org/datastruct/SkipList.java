package org.datastruct;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Comparator;

public class SkipList<Element> implements Collection<Element> {

	protected class ListNode<Element> {

		protected List<ListNode<Element>> next;
		protected Element data;

		public ListNode(int numCons) {
			next = new LinkedList<>();
			for (int i = 0; i < numCons; i++) {
				next.add(null);
			}
			data = null;
		}

		public ListNode(Element data, int numCons) {
			this.data = data;
			this.next = new LinkedList<>();
			this.next.add(null);
			for (int i = 1; i < numCons; i++) {
				if ((int)(Math.random() * 2) == 1) {
					this.next.add(null);
				} else {
					break;
				}
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (o == null) {
				return false;
			} else if (o instanceof ListNode) {
				ListNode ln = (ListNode) o;
				return next.equals(ln.next) && data.equals(ln.data);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			int hash = 0;
			hash = next.hashCode();
			hash += 31 * data.hashCode();
			return hash;
		}
	}

	private int maxCone;
	private ListNode<Element> sentinel;
	private Comparator cmp;

	public SkipList(int maxCone, Comparator cmp) {
		this.maxCone = maxCone;
		sentinel = new ListNode(maxCone);
		this.cmp = cmp;
	}

	@Override
	public boolean add(Element e) throws NullPointerException {
		if (e == null) throw new NullPointerException();
		if (cmp == null) throw new NullPointerException();
		ListNode nNode = new ListNode(e, maxCone);
		Stack<ListNode<Element>> stack = new Stack<>();
		ListNode<Element> curr = sentinel;
		for (int i = maxCone - 1; i >= 0; i--) {
			ListNode<Element> tmp = curr.next.get(i);
			if (tmp != null) {
				int iCmp = cmp.compare(tmp.data, nNode.data);
				if (iCmp == 0) {
					return false;
				} else if (iCmp < 0) {
					curr = tmp;
					while (curr.next.get(i) != null) {
						tmp = curr.next.get(i);
						iCmp = cmp.compare(tmp.data, nNode.data);
						if (iCmp == 0) {
							return false;
						} else if (iCmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
			stack.offer(curr);
		}
		for (int i = 0; i < nNode.next.size(); i++) {
			curr = stack.poll();
			nNode.next.set(i, curr.next.get(i));
			curr.next.set(i, nNode);
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Element> c) {
		// FIXME
		return false;
	}

	@Override
	public void clear() {
		for (int i = 0; i < maxCone; i++) {
			sentinel.next.set(i, null);
		}
	}

	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) throw new NullPointerException();
		try {
			Element e = (Element) o;
			ListNode<Element> curr = sentinel;
			for (int i = maxCone - 1; i >= 0; i--) {
				ListNode<Element> tmp = curr.next.get(i);
				if (tmp != null) {
					int iCmp = cmp.compare(tmp.data, e);
					if (iCmp == 0) {
						// return tmp.data.equals(e);
						return true;
					} else if (iCmp < 0) {
						curr = tmp;
						while (curr.next.get(i) != null) {
							tmp = curr.next.get(i);
							iCmp = cmp.compare(tmp.data, e);
							if (iCmp == 0) {
								// return curr.data.equals(e);
								return true;
							} else if (iCmp < 0) {
								curr = tmp;
							} else {
								break;
							}
						}
					}
				}
			}
		} catch (ClassCastException e) {
			for (ListNode<Element> curr = sentinel.next.get(0); curr != null; curr = curr.next.get(0)) {
				if (curr.equals(o)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) throws NullPointerException {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (o instanceof SkipList) {
			// FIXME
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		// FIXME
		return 0;
	}

	@Override
	public boolean isEmpty() {
		if (sentinel.next.get(0) == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Iterator<Element> iterator() {
		return new Iterator<Element>() {

			private ListNode<Element> next;

			{
				next = sentinel.next.get(0);
			}

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public Element next() {
				Element ret = next.data;
				next = next.next.get(0);
				return ret;
			}
		};
	}

	@Override
	public boolean remove(Object o) throws ClassCastException, NullPointerException {
		if (o == null) throw new NullPointerException();
		if (cmp == null) throw new NullPointerException();
		Element e = (Element) o;
		Stack<ListNode<Element>> stack = new Stack<>();
		ListNode<Element> curr = sentinel;
		ListNode<Element> found = null;
		for (int i = maxCone - 1; i >= 0; i--) {
			ListNode<Element> tmp = curr.next.get(i);
			if (tmp != null) {
				int iCmp = cmp.compare(tmp.data, e);
				if (iCmp == 0) {
					found = tmp;
				} else if (iCmp < 0) {
					curr = tmp;
					while (curr.next.get(i) != null) {
						tmp = curr.next.get(i);
						iCmp = cmp.compare(tmp.data, e);
						if (iCmp == 0) {
							found = tmp;
							break;
						} else if (iCmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
			stack.offer(curr);
		}
		for (int i = 0; i < maxCone && found != null; i++) {
			curr = stack.poll();
			if (found.next.size() > i) {
				curr.next.set(i, found.next.get(i));
			} else {
				break;
			}
		}
		return found != null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// FIXME
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// FIXME
		return false;
	}

	@Override
	public int size() {
		int i = 0;
		for (ListNode<Element> node = sentinel.next.get(0); node != null; node = node.next.get(0)) {
			i++;
		}
		return i;
	}

	@Override
	public Object[] toArray() {
		Object[] ret = new Object[size()];
		ListNode<Element> node = sentinel.next.get(0);
		for (int i = 0; i < ret.length; i++) {
			ret[i] = node.data;
			node = node.next.get(0);
		}
		return ret;
	}

	@Override
	public <Type> Type[] toArray(Type[] a) throws ClassCastException {
		int size = size();
		if (size < a.length) {
			ListNode<Element> curr = sentinel.next.get(0);
			for (int i = 0; i < size; i++) {
				a[i] = (Type) curr.data;
				curr = curr.next.get(0);
			}
			return a;
		} else {
			return (Type[]) toArray();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (ListNode<Element> curr = sentinel.next.get(0); curr != null; curr = curr.next.get(0)) {
			sb.append(curr.data);
			if (curr.next.get(0) != null) {
				sb.append(", ");
			}
		}
		sb.append(']');
		return sb.toString();
	}

	public String izpis() {
		StringBuilder sb = new StringBuilder();
		for (ListNode<Element> curr = sentinel.next.get(0); curr != null; curr = curr.next.get(0)) {
			sb.append(curr.data).append('\t');
			for (int i = 0; i < curr.next.size(); i++) {
				ListNode<Element> tmp = curr.next.get(i);
				if (tmp == null) {
					sb.append('n');
				} else {
					sb.append(tmp.data);
				}
				sb.append('\t');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
