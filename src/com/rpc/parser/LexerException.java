package com.rpc.parser;

public class LexerException extends Exception {
	private String message;

	public LexerException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
