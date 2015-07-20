package org.datastruct;

import java.util.NoSuchElementException;

public class Stack<T> {
	
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

	public T peek() {
		if(this.isEmpty()) return null;
		else return this.top.data;
	}
	
	public boolean isEmpty() {
		return (this.top == null);
	}
}
