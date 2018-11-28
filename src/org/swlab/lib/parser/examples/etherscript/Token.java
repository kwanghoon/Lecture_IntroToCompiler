package org.swlab.lib.parser.examples.etherscript;

import org.swlab.lib.parser.ParserException;

public enum Token {
	END_OF_TOKEN("$"),
	
	ADDRESS("address"), ANONYMOUS("anonymous"), AS("as"), ASSEMBLY("assembly"),
	BOOL("bool"), BREAK("break"), BYTE("byte"), CONSTANT("constant"),
	CONTINUE("continue"), CONTRACT("contract"), DAYS("days"), 
	DECIMAL_NUMBER("decimal_number"), DELETE("delete"), DO("do"), ELSE("else"),
	ENUM("enum"), ETHER("ether"), EVENT("event"), EXTERNAL("external"), 
	FALSE("false"), FINNEY("finney"), FIXED("fixed"), FOR("for"), FROM("from"),
	FUNCTION("function"), HEX_LITERAL("hex_literal"), HEX_NUMBER("hex_number"), 
	HOURS("hours"),	IDENTIFIER("identifier"), IF("if"), IMPORT("import"), 
	INDEXED("indexed"),	INT("int"), INTERFACE("interface"), INTERNAL("internal"), 
	IS("is"), LET("let"), LIBRARY("library"), MAPPING("mapping"), MEMORY("memory"), 
	MINUTES("minutes"), MODIFIER("modifier"), NEW("new"), PAYABLE("payable"),
	PRAGMA("pragma"), PRAGMA_DIRECTIVE("pragma_directive"), PRIVATE("private"),
	PUBLIC("public"), PURE("pure"), RETURN("return"), RETURNS("returns"), 
	SECONDS("seconds"), STORAGE("storage"), STRING("string"), 
	STRING_LITERAL("string_literal"), STRUCT("struct"), SZABO("szabo"), 
	THROW("throw"), UFIXED("ufixed"), UINT("uint"), USING("using"), VAR("var"),
	VIEW("view"), WEEKS("weeks"), WEI("wei"), WHILE("while"), YEARS("years"),
	
	BANG("!"), BANGEQ("!="), PERCENT("%"), PERCENTEQ("%="), LOGICALAND("&&"),
	BITAND("&"), BITANDEQ("&="), OPENPAREN("("), CLOSEPAREN(")"), STAR("*"),
	STARSTAR("**"), STAREQ("*="), PLUS("+"), PLUSPLUS("++"), PLUSEQ("+="),
	COMMA(","), MINUS("-"), MINUSMINUS("--"), MINUSEQ("-="), DOT("."),
	DIVIDE("/"), DIVIDEEQ("/="), COLON(":"), COLONEQ(":="), SEMICOLON(";"),
	LT("<"), LTLT("<<"), LTLTEQ("<<="), LTEQ("<="), ASSIGN("="), EQUAL("=="),
	ARROW("=>"), GT(">"), GTEQ(">="), GTGTEQ(">>="), QMARK("?"),
	OPENBRACKET("["), CLOSEBRACKET("]"), CARET("^"), CARETEQ("^="),
	UNDERLINE("_"), OPENBRACE("{"), CLOSEBRACE("}"), BITOR("|"), BITOREQ("|="),
	LOGICALOR("||"), TILDE("~")
	;
	
	private String strToken;
	
	Token (String strToken) {
		this.strToken = strToken;
	}
	public String getStrToken() {
		return strToken;
	}
	
	public static Token findToken(String strToken) throws ParserException {
		for (Token t: Token.values()) {
			if (t.getStrToken().equals(strToken))
				return t;
		}
		throw new ParserException(strToken + " not expected.");
	}
}
