package com.rpc.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
	private ArrayList<Terminal> lexer;
	private CommonParserUtil pu;

	public Parser(LexicalAnalyzer lexicalAnalyzer) throws IOException, LexerException {
		lexer = lexicalAnalyzer.Lexing();
		
		pu = new CommonParserUtil(lexer);
		
		pu.rule("L' -> L", () -> { return pu.get(1); });
		pu.rule("L -> E", () -> { return pu.get(1); });
		pu.rule("L -> lam loc id . L", () -> { return new LamExpr(pu.getText(2), pu.getText(3), (Expr) pu.get(5)); });
		pu.rule("E -> E T", () -> { return new AppExpr((Expr) pu.get(1), (Expr) pu.get(2)); });
		pu.rule("E -> T", () -> { return pu.get(1); });
		pu.rule("T -> id", () -> {return new Identifier(pu.getText(1)); });
		pu.rule("T -> num", () -> { return new NumValue(Double.parseDouble(pu.getText(1))); });
		pu.rule("T -> ( L )", () -> { return pu.get(2); });
	}

	public Expr Parsing() throws FileNotFoundException, ParserException {
		return (Expr) pu.Parsing();
	}
}