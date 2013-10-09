package com.ojt;

public class OJTException extends RuntimeException {

	public OJTException() {
		super();
	}
	
	public OJTException(final String message) {
		super(message);
	}
	
	public OJTException(final Exception cause) {
		super(cause);
	}
	
	public OJTException(final String message, final Exception cause) {
		super(message, cause);
	}
}
