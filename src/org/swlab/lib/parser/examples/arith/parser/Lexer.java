package org.swlab.lib.parser.examples.arith.parser;

import org.swlab.lib.parser.CommonParserUtil;

public class Lexer {
	public Lexer(CommonParserUtil<Token> pu) {
		// $ is the EOT (End Of Token)
		pu.lexEndToken(Token.END_OF_TOKEN);
		
		// Remove all white spaces
		pu.lex("[ \t\n]", text -> { return null; });
		
		pu.lex("[0-9]+",  text -> { return Token.INTEGER_NUMBER; } );
		pu.lex("\\(", text -> { return Token.OPEN_PAREN; });
		pu.lex("\\)", text -> { return Token.CLOSE_PAREN; });
		
		pu.lex("\\+", text -> { return Token.ADD; });
		pu.lex("\\-", text -> { return Token.SUB; });
		pu.lex("\\*", text -> { return Token.MUL; });
		pu.lex("\\/", text -> { return Token.DIV; });
		
		pu.lex("\\=", text -> { return Token.EQ; });
		pu.lex("\\;", text -> { return Token.SEMICOLON; });
		
		pu.lex("[a-zA-Z]+[a-zA-Z0-9]*", text -> {
			return Token.IDENTIFIER;
		} );
		
	}
}
