package org.swlab.lib.parser.examples.solidity;

import java.io.IOException;
import java.io.Reader;

import org.swlab.lib.parser.CommonParserUtil;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.solidity.Token;

public class Parser {
	private CommonParserUtil pu;
	
	public Parser() throws IOException, LexerException {
		pu = new CommonParserUtil();
		LexicalAnalysis(pu);
		SyntacticAnalysis(pu);
	}
	
	public void LexicalAnalysis(CommonParserUtil pu) {
		pu.lex("[ \t\n]", text -> { return null; });
		pu.lexEndToken("$", text -> { return Token.END_OF_TOKEN; });
		
		pu.lex("address", text -> { return Token.ADDRESS; });
		pu.lex("anonymous", text -> { return Token.ANONYMOUS; });
		pu.lex("as(?!sembly)", text -> { return Token.AS; });
		pu.lex("assembly", text -> { return Token.ASSEMBLY; });
		pu.lex("bool", text -> { return Token.BOOL; });
		pu.lex("break", text -> { return Token.BREAK; });
		// BYTE
		pu.lex("constant", text -> { return Token.CONSTANT; });
		pu.lex("continue", text -> { return Token.CONTINUE; });
		pu.lex("contract", text -> { return Token.CONTRACT; });
		pu.lex("days", text -> { return Token.DAYS; });
		// DECIMAL_NUMBER
		pu.lex("delete", text -> { return Token.DELETE; });
		pu.lex("do", text -> { return Token.DO; });
		pu.lex("else", text -> { return Token.ELSE; });
		pu.lex("enum", text -> { return Token.ENUM; });
		pu.lex("ether", text -> { return Token.ETHER; });
		pu.lex("event", text -> { return Token.EVENT; });
		pu.lex("external", text -> { return Token.EXTERNAL; });
		pu.lex("false", text -> { return Token.FALSE; });
		pu.lex("finney", text -> { return Token.FINNEY; });
		pu.lex("fixed", text -> { return Token.FIXED; });
		pu.lex("for", text -> { return Token.FOR; });
		pu.lex("from", text -> { return Token.FROM; });
		pu.lex("function", text -> { return Token.FUNCTION; });

		pu.lex("0x[0-9a-fA-F]+", text -> { return Token.HEX_NUMBER; });
		pu.lex("hex(\\\"([0-9a-fA-F]{2})*\\\"|\\\'([0-9a-fA-F]{2})*\\\')",
				text -> { return Token.HEX_LITERAL; });
		
		pu.lex("int(8[08]?|16[08]?|24[08]?|32|40|48|56|64|72|96|104|112|"
				+ "120|128|136|144|152|176|184|192|200|208|"
				+ "216|224|232|256)?", 
				text -> { return Token.INT; });
		
		pu.lex("uint(8[08]?|16[08]?|24[08]?|32|40|48|56|64|72|96|104|112|"
				+ "120|128|136|144|152|176|184|192|200|208|"
				+ "216|224|232|256)?", 
				text -> { return Token.UINT; });
		
		pu.lex("byte(s(1[0-9]?|2[0-9]?|3(0|1|2)?|[4-9])?)?", 
				text -> { return Token.BYTE; });
		
		pu.lex("string", text -> { return Token.STRING; });
		
		// DECIMAL_NUMBER must be after HEX_NUMBER, INT, UINT
		pu.lex("[+-]?[0-9]+", text -> { return Token.DECIMAL_NUMBER; });
		
		pu.lex("hours", text -> { return Token.HOURS; });
		// IDENTIFIER
		pu.lex("if", text -> { return Token.IF; });
		pu.lex("import", text -> { return Token.IMPORT; });
		pu.lex("indexed", text -> { return Token.INDEXED; });
		// INT
		pu.lex("interface", text -> { return Token.INTERFACE; });
		pu.lex("internal", text -> { return Token.INTERNAL; });
		pu.lex("is", text -> { return Token.IS; });
		pu.lex("let", text -> { return Token.LET; });
		pu.lex("library", text -> { return Token.LIBRARY; });
		pu.lex("mapping", text -> { return Token.MAPPING; });
		pu.lex("memory", text -> { return Token.MEMORY; });
		pu.lex("minutes", text -> { return Token.MINUTES; });
		pu.lex("modifier", text -> { return Token.MODIFIER; });
		pu.lex("new", text -> { return Token.NEW; });
		pu.lex("payable", text -> { return Token.PAYABLE; });
		pu.lex("pragma", text -> { return Token.PRAGMA; });
		pu.lex("([^;]+)\\;", text -> { return Token.PRAGMA_DIRECTIVE; });
		pu.lex("private", text -> { return Token.PRIVATE; });
		pu.lex("public", text -> { return Token.PUBLIC; });
		pu.lex("pure", text -> { return Token.PURE; });
		pu.lex("return", text -> { return Token.RETURN; });
		pu.lex("returns", text -> { return Token.RETURNS; });
		pu.lex("seconds", text -> { return Token.SECONDS; });
		pu.lex("storage", text -> { return Token.STORAGE; });
		pu.lex("\\\"([^\\\"\\r\\n\\\\]|\\\\.)*\\\"", 
				text -> { return Token.STRING_LITERAL; } ); 
		pu.lex("struct", text -> { return Token.STRUCT; });
		pu.lex("szabo", text -> { return Token.SZABO; });
		pu.lex("throw", text -> { return Token.THROW; });
		// UFIXED
		// UINT
		pu.lex("using", text -> { return Token.USING; });
		pu.lex("var", text -> { return Token.VAR; });
		pu.lex("view", text -> { return Token.VIEW; });
		pu.lex("weeks", text -> { return Token.WEEKS; });
		pu.lex("wei", text -> { return Token.WEI; });
		pu.lex("while", text -> { return Token.WHILE; });
		pu.lex("years", text -> { return Token.YEARS; });
		
		pu.lex("\\!(?!=)", text -> { return Token.BANG; });
		pu.lex("\\!\\=", text -> { return Token.BANGEQ; });
		
		pu.lex("\\%(?!=)", text -> { return Token.PERCENT; });
		pu.lex("\\%\\=", text -> { return Token.PERCENTEQ; });
		
		pu.lex("\\&\\&", text -> { return Token.LOGICALAND; });
		pu.lex("\\&(?!&)", text -> { return Token.BITAND; });
		
		pu.lex("\\(", text -> { return Token.OPENPAREN; });
		pu.lex("\\)", text -> { return Token.CLOSEPAREN; });
		
		pu.lex("\\*(?!(\\*|\\=))", text -> { return Token.STAR; });
		pu.lex("\\*\\*", text -> { return Token.STARSTAR; });
		pu.lex("\\*\\=", text -> { return Token.STAREQ; });
		
		pu.lex("\\+(?!(\\+|\\=|[0-9]))", text -> { return Token.PLUS; });
		pu.lex("\\+\\+", text -> { return Token.PLUSPLUS; });
		pu.lex("\\+\\=", text -> { return Token.PLUSEQ; });
		
		pu.lex("\\,", text -> { return Token.COMMA; });
		
		pu.lex("\\-(?!(\\-|\\=|[0-9]))", text -> { return Token.MINUS; });
		pu.lex("\\-\\-", text -> { return Token.MINUSMINUS; });
		pu.lex("\\-\\=", text -> { return Token.MINUSEQ; });
		
		pu.lex("\\.", text -> { return Token.DOT; });
		
		pu.lex("\\/(?!=)", text -> { return Token.DIVIDE; });
		pu.lex("\\/\\=", text -> { return Token.DIVIDEEQ; });
		
		pu.lex("\\:(?!=)", text -> { return Token.COLON; });
		pu.lex("\\:\\=", text -> { return Token.COLONEQ; });
		
		pu.lex("\\;", text -> { return Token.SEMICOLON; });
		
		pu.lex("\\<(?!(\\<|\\=))", text -> { return Token.LT; });
		pu.lex("\\<\\<", text -> { return Token.LTLT; });
		pu.lex("\\<\\<\\=", text -> { return Token.LTLTEQ; });
		pu.lex("\\<\\=", text -> { return Token.LTEQ; });
		
		pu.lex("\\=(?!(\\=|\\>))", text -> { return Token.ASSIGN; });
		pu.lex("\\=\\=", text -> { return Token.EQUAL; });
		pu.lex("\\=\\>", text -> { return Token.ARROW; });
		
		pu.lex("\\>(?!(\\=|\\>))", text -> { return Token.GT; });
		pu.lex("\\>\\=", text -> { return Token.GTEQ; });
		pu.lex("\\>\\>\\=", text -> { return Token.GTGTEQ; });
		
		pu.lex("\\?", text -> { return Token.QMARK; });
	
		pu.lex("\\[", text -> { return Token.OPENBRACKET; });
		pu.lex("\\]", text -> { return Token.CLOSEBRACKET; });
		
		pu.lex("\\^(?!=)", text -> { return Token.CARET; });
		pu.lex("\\^\\=", text -> { return Token.CARETEQ; });
		
		pu.lex("\\_(?!(\\_|\\$|[a-zA-Z0-9]))", text -> { return Token.UNDERLINE; });

		pu.lex("\\{", text -> { return Token.OPENBRACE; });
		pu.lex("\\}", text -> { return Token.CLOSEBRACE; });
		
		pu.lex("\\|(?!(\\=|\\|))", text -> { return Token.BITOR; });
		pu.lex("\\|\\=", text -> { return Token.BITOREQ; });
		pu.lex("\\|\\|", text -> { return Token.LOGICALOR; });
		
		pu.lex("\\~", text -> { return Token.TILDE; });
		
		// This rule of IDENTIFIER is added after all rules
		// for the alphabetic tokens and the underline token are added.
		pu.lex("[a-zA-Z_$]+[0-9_$]*", text -> {	return Token.IDENTIFIER; });
	}
	
	public void SyntacticAnalysis(CommonParserUtil pu) {
		
	}
	
	public Object Parsing(Reader r) throws ParserException, IOException, LexerException {
		return pu.Parsing(r);
	}
	
	public void Lexing(Reader r) throws IOException, LexerException {
		Lexing(r, false);
	}
	public void Lexing(Reader r, boolean b) throws IOException, LexerException{
		pu.Lexing(r, b);
	}
}
