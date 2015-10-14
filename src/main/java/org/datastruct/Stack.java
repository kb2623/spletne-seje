package org.datastruct;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Stack<T> implements Queue<T> {
	
	class StackNode {
		
		private T data;
		private StackNode prev;
		
		public StackNode(T data, StackNode prev) {
			this.data = data;
			this.prev = prev;
		}
	}

	private StackNode top;

	public Stack() { 
		this.top = null; 
	}

	public Stack(T data) {
		push(data);
	}

	public void push(T e) { 
		this.top = new StackNode(e, this.top); 
	}

	public T pop() throws NoSuchElementException {
		if(this.isEmpty()) throw new NoSuchElementException();
		T ret = this.top.data;
		this.top = this.top.prev;
		return ret;
	}

	@Override
	public boolean add(T t) {
		try {
			push(t);
			return true;
		} catch (Exception e) {
			return false;
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
		top = null;
	}

	@Override
	public boolean offer(T t) {
		push(t);
		return true;
	}

	@Override
	public T remove() throws NoSuchElementException {
		return pop();
	}

	@Override
	public T poll() {
		if (isEmpty()) {
			return null;
		} else {
			return pop();
		}
	}

	@Override
	public T element() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		} else {
			return this.top.data;
		}
	}

	@Override
	public T peek() {
		if(this.isEmpty()) {
			return null;
		} else {
			return this.top.data;
		}
	}

	@Override
	public int size() {
		int size = 0;
		for (StackNode node = top; node != null; node = node.prev) {
			size++;
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (this.top == null);
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
}
