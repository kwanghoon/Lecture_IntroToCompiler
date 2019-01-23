package org.swlab.lib.parser.examples.arith.parser;

import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.TokenInterface;

public enum Token implements TokenInterface<Token> {
	END_OF_TOKEN("$"),
	
	OPEN_PAREN("("), CLOSE_PAREN(")"),
	IDENTIFIER("identifier"),
	INTEGER_NUMBER("integer_number"),
	ADD("+"), SUB("-"), MUL("*"), DIV("/"),
	EQ("="), SEMICOLON(";");

	private String strToken;
	
	private Token(String strToken) {
		this.strToken = strToken;
	}
	
	@Override
	public Token toToken(String str) throws ParserException {
		for(Token token : Token.values()) {
			if (token.strToken.equals(str))
				return token;
		}
		throw new ParserException("Token.toToken: " + str + " not expected.");
	}

	@Override
	public String toString(Token tok) {
		return tok.strToken;
	}

}
