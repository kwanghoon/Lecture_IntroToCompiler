package org.swlab.lib.parser;

public class ParseState extends Stkelem {
	private String state;
	
	public ParseState(String state) {
		super();
		this.state = state;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String toString() {
		return state;
	}
}
