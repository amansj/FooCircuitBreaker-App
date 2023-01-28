package com.foo.exception;

public class CircuitBreakerOpenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3329369925568020401L;
	
	public CircuitBreakerOpenException(String msg) {
		super(msg);
	}
	
	
}
