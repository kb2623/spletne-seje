package org.datastruct.exception;

public class DuplicateKeyException extends RuntimeException {
	
	private static final long serialVersionUID = -230488720133969073L;

	public DuplicateKeyException(String msg) {
		super(msg);
	}
}
