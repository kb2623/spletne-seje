package org.datastruct;

import java.io.*;
import java.util.*;

public class PrioritetnaVrsta<T extends Comparable<T>> implements Queue<T> {

	private Object[] heap;
	private int end;
	private Comparator<T> cmp;
	private static final byte ADD_SIZE = 2;

	public PrioritetnaVrsta(int maxSize, Comparator<T> comparator) {
		this.heap = new Object[maxSize];
		this.end = 0;
		this.cmp = comparator;
	}

	public PrioritetnaVrsta(int maxSize) {
		this(maxSize, null);
	}

	public PrioritetnaVrsta(Comparator<T> comparator) {
		this(100, comparator);
	}

	public PrioritetnaVrsta() {
		this(100, null);
	}

	public void setComparator(Comparator<T> comparator) {
		List<T> list = this.asList();
		this.heap = new Object[list.size()];
		this.end = 0;
		this.cmp = comparator;
		for(T t : list) {
			this.add(t);
		}
	}

	private int compare(T o1, T o2) {
		if(this.cmp == null) {
			return o1.compareTo(o2);
		} else {
			return this.cmp.compare(o1, o2);
		}
	}

	public boolean add(T e) {
		// FIXME popravi return
		if(this.heap.length <= this.end) {
			Object newHeap[] = new Object[this.heap.length * PrioritetnaVrsta.ADD_SIZE];
			System.arraycopy(this.heap, 0, newHeap, 0, this.heap.length);
			this.heap = newHeap;
		}
		this.heap[this.end++] = e;
		this.bubbleUp();
		return true;
	}

	@Override
	public boolean remove(Object o) {
		return (remove((T) o) != null) ? true : false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
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
		this.end = 0;
	}

	@Override
	public boolean offer(T t) {
		try {
			return add(t);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public T remove() throws NoSuchElementException {
		return removeFirst();
	}

	@Override
	public T poll() {
		if (this.isEmpty()) {
			return null;
		} else {
			return removeFirst();
		}
	}

	@Override
	public T element() throws NoSuchElementException {
		return getFirst();
	}

	@Override
	public T peek() {
		if (this.isEmpty()) {
			return null;
		} else {
			return getFirst();
		}
	}

	@SuppressWarnings("unchecked")
	private void bubbleUp() {
		for(int pIndex = this.end - 1; pIndex >= 0; pIndex = (pIndex % 2 == 1)?(pIndex - 1) / 2 : (pIndex - 2) / 2) {
			T pValue = (T) this.heap[pIndex];
			int cIndex = pIndex * 2 + 1;
			if(cIndex < this.end) {
				T cValue = (T) this.heap[cIndex];
				if(cIndex + 1 < this.end && this.compare(cValue, (T) this.heap[cIndex + 1]) < 0) {
					cValue = (T) this.heap[++cIndex];
				}
				if(this.compare(pValue, cValue) >= 0) {
					return;
				}
				this.swap(pIndex, cIndex);
			}
		}
	}

	private void swap(int source, int target) {
		Object tmp = this.heap[target];
		this.heap[target] = this.heap[source];
		this.heap[source] = tmp;
	}

	@SuppressWarnings("unchecked")
	public T removeFirst() throws NoSuchElementException {
		if(this.isEmpty()) {
			throw new NoSuchElementException();
		} else {
			T tmp = (T) this.heap[0];
			this.swap(0, --this.end);
			this.bubbleDown(0);
			return tmp;
		}
	}


	@SuppressWarnings("unchecked")
	private void bubbleDown(int start) {
		int pIndex = start, cIndex = pIndex * 2 + 1;
		while(cIndex < this.end) {
			T cValue = (T) this.heap[cIndex] , pValue = (T) this.heap[pIndex];
			if(cIndex + 1 < this.end && this.compare(cValue, (T) this.heap[cIndex + 1]) < 0) {
				cValue = (T) this.heap[++cIndex];
			}
			if(this.compare(pValue, cValue) >= 0) {
				return;
			}
			this.swap(pIndex, cIndex);
			pIndex = cIndex;
			cIndex = pIndex * 2 + 1;
		}
	}

	@SuppressWarnings("unchecked")
	public T getFirst() throws NoSuchElementException {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		} else {
			return (T) this.heap[0];
		}
	}

	public int size() {
		return this.end;
	}

	public int depth() {
		if (this.end == 0) {
			return 0;
		}
		return (int) (Math.log(this.end) / Math.log(2)) + 1;
	}

	public boolean isEmpty() {
		return (this.end == 0);
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T1> T1[] toArray(T1[] t1s) {
		return null;
	}

	public boolean exists(T e) {
		return this.exists(e, 0);
	}

	@SuppressWarnings("all")
	private boolean exists(T e, int index) {
		if(index >= this.end) {
			return false;
		} else if(this.compare((T) this.heap[index], e) < 0) {
			return false;
		} else if(this.compare((T) this.heap[index], e) == 0) {
			return true;
		} else {
			return this.exists(e, index * 2 + 1) || this.exists(e, index * 2 + 2);
		}
	}

	@SuppressWarnings("unchecked")
	public T remove(T e) {
		int index = this.search(e, 0);
		if(this.isEmpty() || index == -1) {
			throw new java.util.NoSuchElementException();
		} else {
			T tmp = (T) this.heap[index];
			this.swap(index, --this.end);
			this.bubbleDown(index);
			return tmp;
		}
	}

	@SuppressWarnings("unchecked")
	private int search(T e, int index) {
		if(index >= this.end) {
			return -1;
		} else if(this.compare((T) this.heap[index], e) < 0) {
			return -1;
		} else if(this.compare((T) this.heap[index], e) == 0) {
			return index;
		} else {
			int left = this.search(e, index * 2 + 1);
			if(left > -1) {
				return left;
			} else {
				return this.search(e, index * 2 + 2);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> asList() {
		if(this.isEmpty()) {
			return null;
		} else {
			List<T> newList = new ArrayList<>(this.size());
			for(int i = 0; i < this.size(); i++) {
				newList.add((T) this.heap[i]);
			}
			return newList;
		}
	}

	public String print() {
		StringBuilder builder = new StringBuilder();
		this.print(0, 0, builder);
		return builder.toString();
	}

	private void print(int ele, int numTabs, StringBuilder builder) {
		if(ele < this.size()) {
			this.print(ele*2+1, numTabs+1, builder);
			for(int i = 0; i < numTabs; i++) {
				builder.append('\t');
			}
			builder.append(this.heap[ele]);
			this.print(ele*2+2, numTabs+1, builder);
		}
	}

	public void save(OutputStream outputStream) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeByte(1);
		out.writeInt(this.size());
		for(int i = 0; i < this.size(); i++) {
			out.writeObject(this.heap[i]);
		}
	}

	@SuppressWarnings("unchecked")
	public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(inputStream);
		if(in.readByte() != 1) {
			int size = in.readInt();
			this.heap = new Object[size];
			this.end = 0;
			for(int i = 0; i < size; i++) {
				this.add((T) in.readObject());
			}
		} else {
			int size = in.readInt();
			this.end = size;
			this.heap = new Object[size];
			for(int i = 0; i < size; i++) {
				this.heap[i] = in.readObject();
			}
		}
	}
}
