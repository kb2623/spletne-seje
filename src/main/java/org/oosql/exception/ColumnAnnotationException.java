package org.oosql.exception;

public class ColumnAnnotationException extends OosqlException {
	/**
	 *
	 * @param message
	 */
	public ColumnAnnotationException(String message) {
		super("Column annotation: " + message);
	}
	/**
	 *
	 * @param message
	 * @param t
	 */
	public ColumnAnnotationException(String message, Throwable t) {
		super(message, t);
	}
}
