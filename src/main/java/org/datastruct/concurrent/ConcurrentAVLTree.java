package org.datastruct.concurrent;

import org.datastruct.AVLTree;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentAVLTree<K, V> extends AVLTree<K, V> {

	private ReadWriteLock lock;

	public ConcurrentAVLTree() {
		super();
		lock = new ReentrantReadWriteLock();
	}

	public ConcurrentAVLTree(Comparator<K> keyCmp) {
		super(keyCmp);
		lock = new ReentrantReadWriteLock();
	}

	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return super.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return super.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsKey(Object o) {
		lock.readLock().lock();
		try {
			return super.containsKey(o);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsValue(Object o) {
		lock.readLock().lock();
		try {
			return super.containsValue(o);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V get(Object o) {
		lock.readLock().lock();
		try {
			return super.get(o);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V put(K key, V value) throws NullPointerException {
		lock.writeLock().lock();
		try {
			return super.put(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V remove(Object o) {
		lock.writeLock().lock();
		try {
			return super.remove(o);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) throws NullPointerException {
		lock.writeLock().lock();
		try {
			for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
				super.put(e.getKey(), e.getValue());
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			super.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Set<K> keySet() {
		lock.readLock().lock();
		try {
			return super.keySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Collection<V> values() {
		lock.readLock().lock();
		try {
			return super.values();
		} finally {
			lock.readLock().lock();
		}
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		lock.readLock().lock();
		try {
			return super.entrySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public String toString() {
		lock.readLock().lock();
		try {
			return super.toString();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public String printTree() {
		lock.readLock().lock();
		try {
			return super.printTree();
		} finally {
			lock.readLock().unlock();
		}
	}
}
