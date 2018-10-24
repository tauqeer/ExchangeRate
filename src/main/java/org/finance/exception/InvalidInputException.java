package org.finance.exception;

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 5282197414432567564L;
	private int statusCode;

	public InvalidInputException(int status,String message){
		super(message);
		statusCode=status;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
}