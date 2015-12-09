package org.datastruct;

import java.util.*;

public class ArraySet<E> implements Set<E> {

	private ArrayList<E> list;
	private CompareEle<E> cmp;

	public ArraySet(int initialCapacity, Comparator<E> cmp) {
		list = new ArrayList<>(initialCapacity);
		this.cmp = new CompareEle<>(cmp);
	}

	public ArraySet() {
		this(100, (e1, e2) -> e1.hashCode() - e2.hashCode());
	}

	public ArraySet(Collection<? extends E> c, Comparator<E> cmp) {
		list = new ArrayList<>(c);
		this.cmp = new CompareEle<>(cmp);
	}

	public ArraySet(Collection<? extends E> c) {
		this(c, (e1, e2) -> e1.hashCode() - e2.hashCode());
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		E ele = (E) o;
		return false;
	}

	@Override
	public Iterator<E> iterator() {
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

	@Override
	public boolean add(E e) {
		return false;
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
	public boolean addAll(Collection<? extends E> collection) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return false;
	}

	@Override
	public void clear() {

	}

	private class CompareEle<E> implements Comparator<E> {

		private Comparator<E> cmp;

		private CompareEle(Comparator<E> cmp) {
			this.cmp = cmp;
		}

		@Override
		public int compare(E e1, E e2) {
			int res = cmp.compare(e1, e2);
			if (res == 0) {
				if (e1.equals(e2)) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return res;
			}
		}
	}
}
