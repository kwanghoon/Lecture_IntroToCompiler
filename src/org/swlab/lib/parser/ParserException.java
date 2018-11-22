package org.swlab.lib.parser;

public class ParserException extends Exception {
	private String message;

	public ParserException(String message) {
		super();
		this.message = message;
		System.err.println(message);
	}

	public ParserException(String message, String culprit, int err_line_index, int err_ch_index) {
		this.message = message;
		System.err.println(message + " " + culprit + " " + err_line_index + ", " + err_ch_index);
	}
}
