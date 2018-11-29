package org.swlab.lib.parser;

public class Terminal<Token> extends Stkelem {
	private String syntax;
	private Token token;
	private int chIndex;
	private int lineIndex;
	
	public Terminal() {
		
	}

	public Terminal(String syntax, Token token, int chIndex, int lineIndex) {
		super();
		this.syntax = syntax;
		this.token = token;
		this.chIndex = chIndex;
		this.lineIndex = lineIndex;
	}

	public String getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
	
	public int getChIndex() {
		return chIndex;
	}

	public void setChIndex(int chIndex) {
		this.chIndex = chIndex;
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	@Override
	public String toString() {
		return syntax;
	}
}
