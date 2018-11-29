package org.swlab.lib.parser;

public interface TokenInterface<Token> {
	Token toToken(String s) throws ParserException;
	String toString(Token tok);
}
