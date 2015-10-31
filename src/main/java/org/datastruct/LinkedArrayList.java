package org.datastruct;

import java.util.*;

public class LinkedArrayList<E> implements List<E> {

	private int size = 0;
	private Object[] matrix;

	public LinkedArrayList() {
		this.matrix = new Object[1];
		this.matrix[0] = new Object[100];
	}

	public LinkedArrayList(int tabSize) {
		this.matrix = new Object[1];
		this.matrix[0] = new Object[tabSize];
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {

			private int curr = 0;
			private int prev = -1;

			@Override
			public boolean hasNext() {
				return curr != size;
			}

			@Override
			public E next() throws NoSuchElementException {
				if (this.curr == size) {
					throw new NoSuchElementException();
				}
				this.prev = this.curr;
				this.curr++;
				return get(this.prev);
			}

			@Override
			public void remove() {
				if (this.prev == -1) {
					throw new IllegalStateException();
				} else {
					delete(this.prev);
					this.prev = -1;
				}
			}
		};
	}

	private E delete(int i) {
		Object[] array = (Object[]) this.matrix[0];
		int oIndex = (int) (i / array.length);
		int iIndex = i % array.length;
		if (oIndex > 0) {
			array = (Object[]) this.matrix[oIndex];
		}
		E ret = (E) array[iIndex];
		this.shiftLeft(oIndex, iIndex);
		this.size--;
		return ret;
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
	public boolean add(E e) throws NullPointerException {
		if (e == null) {
			throw new NullPointerException("Can not insert null elements into " + getClass().getName());
		}
		Object[] array = (Object[]) this.matrix[0];
		int oIndex = (int) (this.size / array.length);
		int iIndex = this.size % array.length;
		if (oIndex > 0) {
			if (oIndex == this.matrix.length) {
				this.resizeMatrix();
			}
			array = (Object[]) this.matrix[oIndex];
		}
		array[iIndex] = e;
		this.size++;
		return true;
	}

	private void resizeMatrix() {
		Object[] array = new Object[this.matrix.length + 1];
		for (int i = 0; i < this.matrix.length; i++) {
			array[i] = this.matrix[i];
		}
		this.matrix = array;
	}

	@Override
	public boolean remove(Object o) {
		int index = this.indexOf(o);
		if (index == -1) {
			return false;
		} else {
			return remove(index) != null;
		}
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object o : collection) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) throws NullPointerException {
		for (Object o : collection) {
			if (!add((E) o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(int i, Collection<? extends E> collection) throws NullPointerException, IndexOutOfBoundsException {
		if (i < 0 || i >= this.size) {
			throw new IndexOutOfBoundsException("Bad index!!!");
		}
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
		Object[] array = new Object[((Object[]) this.matrix[0]).length];
		this.matrix = array;
		this.size = 0;
	}

	@Override
	public E get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i >= this.size) {
			throw new IndexOutOfBoundsException("Bad index!!!");
		}
		Object[] array = (Object[]) this.matrix[0];
		int oIndex = (int) (i / array.length);
		int iIndex = i % array.length;
		if (oIndex > 0) {
			array = (Object[]) this.matrix[oIndex];
			return (E) array[iIndex];
		} else {
			return (E) array[i];
		}
	}

	@Override
	public E set(int i, E e) {
		if (i < 0 || i >= this.size) {
			throw new IndexOutOfBoundsException("Bad index");
		}
		if (e == null) {
			throw new NullPointerException("Can not insert null elements in " + getClass());
		}
		Object[] array = (Object[]) this.matrix[0];
		int oIndex = (int) (i / array.length);
		int iIndex = i % array.length;
		if (oIndex > 0) {
			array = (Object[]) this.matrix[oIndex];
		}
		E ret = (E) array[iIndex];
		array[iIndex] = e;
		return ret;
	}

	@Override
	public void add(int i, E e) throws NullPointerException, IndexOutOfBoundsException {
		if (i < 0 || i >= this.size) {
			throw new IndexOutOfBoundsException("Bad index in " + getClass().getName());
		}
		if (e == null) {
			throw new NullPointerException("Can not insert null elements into " + getClass().getName());
		}
		Object[] array = (Object[]) this.matrix[0];
		int oIndex = (int) (i / array.length);
		int iIndex = i % array.length;
		this.shiftRight(oIndex, iIndex);
		if (oIndex > 0) {
			array = (Object[]) this.matrix[oIndex];
		}
		this.size++;
		if ((int) (this.size / array.length) == this.matrix.length) {
			this.resizeMatrix();
		}
		this.shiftLeft(oIndex, iIndex);
		array[iIndex] = e;
	}

	private void shiftRight(int oIndex, int iIndex) {
		int start = (int) (this.size / ((Object[]) this.matrix[0]).length);
		for (int i = start; start >= 0; i++) {
			if (i == 0) {

			} else {

			}
		}
	}

	@Override
	public E remove(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i >= this.size) {
			throw new IndexOutOfBoundsException("Bad index");
		}
		return delete(i);
	}

	private void shiftLeft(int oIndex, int iIndex) {
		Object[] array = (Object[]) this.matrix[oIndex];
		for (int i = iIndex; i < array.length; i++) {
			if (array[i] == null) {
				return;
			} else if (i + 1 == array.length && oIndex + 1 < this.matrix.length) {
				array[i] = ((Object[]) this.matrix[oIndex + 1])[0];
			} else {
				array[i] = array[i + 1];
			}
		}
		for (int i = oIndex + 1; i < this.matrix.length; i++) {
			array = (Object[]) this.matrix[i];
			for (int j = 1; j < array.length; j++) {
				if (array[j] == null) {
					return;
				} else if (j + 1 == array.length && i + 1 < this.matrix.length) {
					array[j] = ((Object[]) this.matrix[j + 1])[0];
				} else {
					array[j] = array[j + 1];
				}
			}
		}
	}

	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < this.matrix.length; i++) {
			Object[] array = (Object[]) this.matrix[i];
			for (int j = 0; j < array.length; j++) {
				if (array[j].equals(o)) {
					return i * array.length + j;
				}
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		for (int i = this.matrix.length - 1; i >= 0; i--) {
			Object[] array = (Object[]) this.matrix[i];
			for (int j = array.length - 1; j >= 0; j--) {
				if (array[j].equals(o)) {
					return i * array.length + j;
				}
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int i) {
		return null;
	}

	@Override
	public List<E> subList(int i, int i1) {
		return null;
	}
}
