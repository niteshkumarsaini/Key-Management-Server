package com.KMSDemo.KMSDemo.Exceptions;

public class KeysNotFound extends RuntimeException{
	public KeysNotFound() {
		super("Key is not found on server");
	}
	public KeysNotFound(String message) {
		super(message);
		
	}

}
