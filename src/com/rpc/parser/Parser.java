package com.rpc.parser;

import java.io.IOException;
import java.io.Reader;

import com.rpc.lib.CommonParserUtil;
import com.rpc.lib.LexerException;
import com.rpc.lib.ParserException;

public class Parser {
	private CommonParserUtil pu;

	public Parser() throws IOException, LexerException {
		pu = new CommonParserUtil();
		
		pu.lex("[ \t\n]", text -> { return null; });
		pu.lex("[0-9]+", text -> { return Token.NUM; });
		pu.lex("[a-zA-Z]+[0-9]*", text -> {
			if (text.equalsIgnoreCase("lam"))
					return Token.LAM;
			else
					return Token.ID; });
		pu.lex("\\^[cs]", text -> { return Token.LOC; });
		pu.lex("\\(", text -> { return Token.OPENPAREN; });
		pu.lex("\\)", text -> { return Token.CLOSEPAREN; });
		pu.lex("\\.", text -> { return Token.DOT; });
		pu.lexEndToken("$", text -> { return Token.END_OF_TOKEN; });
		
		pu.rule("L' -> L", () -> { return pu.get(1); });
		pu.rule("L -> E", () -> { return pu.get(1); });
		pu.rule("L -> lam loc id . L", () -> { return new LamExpr(getLoc(pu.getText(2)), pu.getText(3), (Expr) pu.get(5)); });
		pu.rule("E -> E T", () -> { return new AppExpr((Expr) pu.get(1), (Expr) pu.get(2)); });
		pu.rule("E -> T", () -> { return pu.get(1); });
		pu.rule("T -> id", () -> {return new Identifier(pu.getText(1)); });
		pu.rule("T -> num", () -> { return new NumValue(Double.parseDouble(pu.getText(1))); });
		pu.rule("T -> ( L )", () -> { return pu.get(2); });
	}

	public Expr Parsing(Reader r) throws ParserException, IOException, LexerException {
		return (Expr) pu.Parsing(r);
	}
	
	private String getLoc(String loc) {
		if (loc.equals("^s"))
			return "s";
		else
			return "c";
	}
}