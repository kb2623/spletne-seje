package org.oosql.exception;

public class TableAnnotationException extends OosqlException {
	/**
	 *
	 * @param message
	 */
	public TableAnnotationException(String message) {
		super("Table annotation: " + message);
	}
	/**
	 *
	 * @param message
	 * @param t
	 */
	public TableAnnotationException(String message, Throwable t) {
		super(message, t);
	}
}
