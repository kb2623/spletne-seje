package org.datastruct;

@FunctionalInterface
public interface ObjectCreator<E> {
	/**
	 * @param pool
	 * @param args
	 * @return
	 */
	E create(ObjectPool pool, Object... args);
}
