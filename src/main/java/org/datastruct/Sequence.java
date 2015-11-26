package org.datastruct;

public interface Sequence<E> {
	/**
	 * @param s
	 * @return
	 */
	int equalDistance(E s);

	/**
	 * @return
	 */
	int length();

	/**
	 * @param s
	 * @return
	 */
	E append(E s);

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	E subSequence(int start, int end);

	/**
	 * @param start
	 * @return
	 */
	default E subSequence(int start) {
		return subSequence(start, length());
	}
}
