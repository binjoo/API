package com.binjoo.base.exception;

public class RouteException extends RuntimeException {
	private static final long serialVersionUID = -8881710314242162338L;

	public RouteException(String msg) {
		super(msg);
	}

	public RouteException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
