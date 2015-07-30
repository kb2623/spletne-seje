package org.oosql.exception;

public abstract class OosqlException  extends Exception {
	/**
	 *
	 * @param message
	 */
	public OosqlException(String message) {
		super(message);
	}
	/**
	 *
	 * @param message
	 * @param throwable
	 */
	public OosqlException(String message, Throwable throwable) {
		super(message + " > " + throwable.getMessage(), throwable);
	}
}
