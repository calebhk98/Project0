package com.revature.exception;

@SuppressWarnings("serial")
public class NegativeValueInputException extends Exception{

	public NegativeValueInputException() {}
	public NegativeValueInputException(String message) {
		super(message);
		
	}
	
}
