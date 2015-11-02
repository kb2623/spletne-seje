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
		return indexOf(0) != -1;
	}

	@Override
	public Iterator<E> iterator() {
		return new LinkedArrayListIterator();
	}

	/**
	 * Metoda za brisanje vrednosti v tabeli na dolocenm mestu.
	 *
	 * @param i Mesto v tabeli
	 * @return Vrednost ki se je nahajala na izbranem mestu
	 */
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
		Object[] array = new Object[this.size];
		for (int i = 0; i < this.matrix.length; i++) {
			Object[] tmp = (Object[]) this.matrix[i];
			for (int j = 0; j < tmp.length; j++) {
				array[i * tmp.length + j] = tmp[j];
			}
		}
		return array;
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
		this.insert(this.size, e);
		return true;
	}

	/**
	 * Metoda ki poveca tabelo.
	 */
	private void resizeMatrix() {
		Object[] array = new Object[this.matrix.length + 1];
		for (int i = 0; i < this.matrix.length; i++) {
			array[i] = this.matrix[i];
		}
		array[array.length - 1] = new Object[((Object[]) this.matrix[0]).length];
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
		return insertAll(this.size, collection);
	}

	@Override
	public boolean addAll(int i, Collection<? extends E> collection) throws NullPointerException, IndexOutOfBoundsException {
		if (i < 0 || i >= this.size) {
			throw new IndexOutOfBoundsException("Bad index!!!");
		}
		return insertAll(i, collection);
	}

	/**
	 * Metoda za dodajanje vecih elementov na izbrano mesto.
	 * @param i Izbrano mesto za didajanje
	 * @param collection Elementi, ki jih zelimo dodati
	 * @return <code>true</code> ce so se dodali vsi elementi, <code>false</code> ce se niso dodali vsi elementi
	 */
	private boolean insertAll(int i, Collection<? extends E> collection) {
		int nEle = 0;
		for (Object o : collection) {
			if (o != null) {
				nEle++;
			}
		}
		// FIXME
		if (nEle == collection.size()) {
			return true;
		} else {
			return false;
		}
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
		return update(i, e);
	}

	/**
	 * Metoda za spreminjanje vrednosti na izbranem mestu v tabeli.
	 * @param i Mesto v tabeli
	 * @param e Nova vresnost
	 * @return Predhodna vrednost
	 */
	private E update(int i, E e) {
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
		this.insert(i, e);
	}

	/**
	 * Metoda za dodajane na izbano mesto.
	 * @param i Mesto v tabeli
	 * @param e Nov element
	 */
	private void insert(int i, E... e) {
		Object[] array = (Object[]) this.matrix[0];
		int oIndex = (int) (i / array.length);
		int iIndex = i % array.length;
		if (oIndex >= this.matrix.length) {
			this.resizeMatrix();
		}
		this.shiftRight(oIndex, iIndex, e.length);
		if (oIndex > 0) {
			array = (Object[]) this.matrix[oIndex];
		}
		this.size += e.length;
		for (int j = 0; j < e.length; j++) {
			array[iIndex + j] = e[j];
		}
	}

	/**
	 * Metoda ki se jo uporablja pri dodajnju novega elemeta. Tabelo <code>matrix</code> moramo pred klicem te metode povecati.
	 * @param oIndex Mesto v tabeli <code>matrix</code>
	 * @param iIndex Mesto v notranji tabeli tabele <code>matrix</code>
	 * @param size Stevilo mest, ki jih preskocimo
	 */
	private void shiftRight(int oIndex, int iIndex, int size) {
		Object[] array = (Object[]) this.matrix[this.matrix.length - 1];
		int start = this.size % array.length;
		for (int i = start; i >= 0; i--) {
			if (i == iIndex && this.matrix.length - 1 == oIndex) {
				return;
			} else if (i - 1 == -1 && this.matrix.length - 2 >= 0) {
				array[i] = ((Object[]) this.matrix[this.matrix.length - 2])[array.length - 1];
			} else {
				array[i] = array[i - 1];
			}
		}
		for (int i = this.matrix.length - 2; i >= 0; i--) {
			array = (Object[]) this.matrix[i];
			for (int j = array.length - 1; j >= 0; j--) {
				if (j == iIndex && i == oIndex) {
					return;
				} else if (j - 1 == -1 && i - 1 >= 0) {
					array[j] = ((Object[]) this.matrix[i])[array.length - 1];
				} else {
					array[j] = array[j - 1];
				}
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
			if (oIndex * array.length + i == this.size) {
				return;
			} else if (i + 1 == array.length && oIndex + 1 < this.matrix.length) {
				array[i] = ((Object[]) this.matrix[oIndex + 1])[0];
			} else {
				array[i] = array[i + 1];
			}
		}
		for (int i = oIndex + 1; i < this.matrix.length; i++) {
			array = (Object[]) this.matrix[i];
			for (int j = 0; j < array.length; j++) {
				if (array[j] == null || (j + 1 >= array.length && i + 1 == this.matrix.length)) {
					return;
				} else if (j + 1 >= array.length && i + 1 < this.matrix.length) {
					array[j] = ((Object[]) this.matrix[i + 1])[0];
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
		return new LinkedArrayListListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int i) throws ArrayIndexOutOfBoundsException {
		return new LinkedArrayListListIterator(i);
	}

	@Override
	public List<E> subList(int i, int i1) {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (int i = 0; i < this.matrix.length; i++) {
			Object[] array = (Object[]) this.matrix[i];
			for (int j = 0; j < array.length; j++) {
				builder.append(array[j].toString()).append(", ");
			}
		}
		builder.delete(builder.length() - 2, builder.length()).append(']');
		return builder.toString();
	}

	/**
	 * Razred za spredos po elementih podatkovne srukture
	 */
	class LinkedArrayListIterator implements Iterator<E> {

		private int next;
		private boolean canDel;

		LinkedArrayListIterator() {
			this.next = 0;
			this.canDel = false;
		}

		@Override
		public boolean hasNext() {
			return next < size;
		}

		@Override
		public E next() throws NoSuchElementException {
			if (this.next == size) {
				throw new NoSuchElementException();
			}
			this.next++;
			canDel = true;
			return get(this.next - 1);
		}

		@Override
		public void remove() {
			if (!canDel) {
				throw new IllegalStateException();
			} else {
				this.next--;
				delete(this.next);
				this.canDel = false;
			}
		}
	}

	/**
	 * Razred za sprehod po elementih podatkovne strukture, ki podpira tudi prehajanje na prejsnji elemnt
	 */
	class LinkedArrayListListIterator implements ListIterator<E> {

		private int next;
		private boolean canDel;
		private boolean canSet;

		LinkedArrayListListIterator(int start) throws ArrayIndexOutOfBoundsException {
			this();
			if (start < 0 || start >= size) {
				throw new ArrayIndexOutOfBoundsException("Bad index!!!");
			}
			this.next = start - 1;
		}

		LinkedArrayListListIterator() {
			this.next = 0;
			this.canDel = false;
			this.canSet = false;
		}

		@Override
		public boolean hasNext() {
			return this.next < size;
		}

		@Override
		public E next() throws NoSuchElementException {
			if (this.next + 1 > size) {
				throw new NoSuchElementException();
			}
			this.canDel = true;
			this.canSet = true;
			this.next++;
			return get(this.next - 1);
		}

		@Override
		public boolean hasPrevious() {
			return this.next - 2 > -1;
		}

		@Override
		public E previous() throws NoSuchElementException {
			if (this.next - 1 == 0) {
				throw new NoSuchElementException();
			}
			this.next--;
			return get(this.next - 1);
		}

		@Override
		public int nextIndex() {
			return this.next;
		}

		@Override
		public int previousIndex() {
			return this.next - 2;
		}

		@Override
		public void remove() throws IllegalStateException {
			if (this.canDel) {
				throw new IllegalStateException();
			}
			delete(this.next - 1);
			this.canDel = false;
		}

		@Override
		public void set(E e) throws IllegalArgumentException, IllegalStateException {
			if (!canSet) {
				throw new IllegalStateException();
			}
			if (e == null) {
				throw new IllegalArgumentException("Can not nsert null elements!!!");
			}
			update(this.next - 1, (E) e);
		}

		@Override
		public void add(E e) {
			this.canSet = false;
			this.canDel = false;
			if (e == null) {
				throw new IllegalArgumentException("Can not add null elemts");
			}
			insert(this.next - 1, (E) e);
		}
	}
}
