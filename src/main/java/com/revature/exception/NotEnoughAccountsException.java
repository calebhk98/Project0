package com.revature.exception;


@SuppressWarnings("serial")
public class NotEnoughAccountsException extends Exception{
	
	public NotEnoughAccountsException() {}
	public NotEnoughAccountsException(String message) {
		super(message);
		
	}
}
