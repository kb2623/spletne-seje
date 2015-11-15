package org.datastruct;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class LInkQueue<E> implements Queue<E> {

	private Node<E> first;
	private Node<E> last;

	@Override
	public int size() {
		int size = 0;
		Node<E> curr = last;
		while (curr != null) {
			size++;
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return first == null;
	}

	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		E ele = (E) o;
		Node<E> curr = first;
		while (curr != null) {
			if (curr.data.equals(ele)) {
				return true;
			} else {
				curr = curr.prev;
			}
		}
		return false;
	}

	@Override
	public Iterator iterator() {
		return new IterateLinkQueue<>();
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size()];
		Node<E> curr = last;
		for (int i = 0; curr != null; i++) {
			array[i] = curr.data;
			curr = curr.prev;
		}
		return array;
	}

	@Override
	public <T> T[] toArray(T[] ts) throws NullPointerException {
		if (ts == null) {
			throw new NullPointerException();
		}
		int size = size();
		if (ts.length >= size) {
			Node<E> curr = first;
			for (int i = 0; curr != null; i++) {
				ts[i] = (T) curr.data;
				curr = curr.prev;
			}
			return ts;
		} else {
			T[] array = (T[]) new Object[size];
			Node<E> curr = first;
			for (int i = 0; curr != null; i++) {
				array[i] = (T) curr.data;
				curr = curr.prev;
			}
			return array;
		}
	}

	@Override
	public boolean add(E o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		if (isEmpty()) {
			first = last = new Node(o, null);
		} else {
			last = new Node(o, last);
		}
		return true;
	}

	@Override
	public boolean remove(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		E ele = (E) o;
		Node<E> curr = first;
		Node<E> prev = null;
		while (curr != null) {
			if (curr.data.equals(ele)) {
				if (prev == null) {
					first = first.prev;
				} else {
					if (curr == last) {
						last = prev;
						prev.prev = null;
					} else {
						prev.prev = curr.prev;
					}
				}
				if (first == null) {
					last = null;
				}
				return true;
			} else {
				prev = curr;
				curr = curr.prev;
			}
		}
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) throws NullPointerException, IllegalStateException {
		for (E e : collection) {
			if (!add(e)) {
				throw new IllegalStateException();
			}
		}
		return true;
	}

	@Override
	public void clear() {
		first = last = null;
	}

	@Override
	public boolean retainAll(Collection<?> collection) throws NullPointerException {
		Node<E> curr = first;
		Node<E> prev = null;
		while (curr != null) {
			if (!collection.contains(curr.data)) {
				if (prev == null) {
					first = first.prev;
				} else {
					prev.prev = curr.prev;
				}
			}
			prev = curr;
			curr = curr.prev;
		}
		if (first == null) {
			last = null;
		}
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection) throws NullPointerException {
		if (!isEmpty()) {
			for (Object o : collection) {
				remove(o);
			}
		}
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> collection) throws NullPointerException {
		for (Object o : collection) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean offer(E o) {
		try {
			return add(o);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public E remove() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		if (isEmpty()) {
			return null;
		} else {
			E ret = first.data;
			first = first.prev;
			if (first == null) {
				last = null;
			}
			return ret;
		}
	}

	@Override
	public E poll() {
		try {
			return remove();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public E element() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return first.data;
	}

	@Override
	public E peek() {
		try {
			return element();
		} catch (Exception e) {
			return null;
		}
	}

	class Node<E> {

		E data;
		Node<E> prev;

		Node(E data, Node<E> next) {
			this.data = data;
			this.prev = next;
		}
	}

	class IterateLinkQueue<E> implements Iterator<E> {

		Node<E> next;
		Node<E> prev;

		IterateLinkQueue() {
			next = (Node<E>) first;
			prev = null;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public E next() throws NoSuchElementException {
			if (next == null) {
				throw new NoSuchElementException();
			}
			prev = next;
			next = next.prev;
			return prev.data;
		}

		@Override
		public void remove() throws IllegalStateException {
			if (prev == null) {
				throw new IllegalStateException();
			}
			if (prev == first) {
				first = first.prev;
			} else {

			}
			prev = null;
		}
	}
}
