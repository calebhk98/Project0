package com.revature.exception;

@SuppressWarnings("serial")
public class AccountHasValueException extends Exception{

	public AccountHasValueException() {}
	public AccountHasValueException(String message) {
		super(message);
		
	}
	
}
