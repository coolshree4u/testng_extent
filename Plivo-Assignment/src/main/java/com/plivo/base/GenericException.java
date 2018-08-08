package com.plivo.base;

@SuppressWarnings("serial")
public class GenericException extends RuntimeException {

	  public GenericException(String message) {
	    super(message);
	  }

	  public GenericException(Throwable t) {
	    super(t);
	  }

	  public GenericException(String message, Throwable t) {
	    super(message, t);
	  }

	}
