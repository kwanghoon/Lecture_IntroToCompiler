package org.swlab.lib.parser.examples.etherscript;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.swlab.lib.parser.CommonParserUtil;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.etherscript.ast.Account;
import org.swlab.lib.parser.examples.etherscript.ast.Stmt;

public class Parser {
	private CommonParserUtil pu;
	
	public Parser() throws IOException, LexerException {
		pu = new CommonParserUtil();
		Lexer.LexicalAnalysis(pu);
		SyntacticAnalysis(pu);
	}
	
	public void SyntacticAnalysis(CommonParserUtil pu) {
		pu.rule("Program' -> Program", () -> {return pu.get(1);});
		pu.rule("Program -> Statement", () -> { 
			return new ArrayList<Stmt>().add((Stmt)pu.get(1)); 
		});
		pu.rule("Program -> Statement Program", () -> {
			ArrayList<Stmt> arrList = (ArrayList<Stmt>)pu.get(2);
			arrList.add(0, (Stmt)pu.get(1));
			return arrList;
		});
		/// HERE HERE HERE
		pu.rule("Statement -> account { Properties } identifier ;", () -> {
			Account acc = new Account();
			acc.properties = (HashMap<String,String>)pu.get(3);
			acc.name = (String)pu.get(5);
			return acc;
		});
	}
	
	public Object Parsing(Reader r) throws ParserException, IOException, LexerException {
		return pu.Parsing(r);
	}
	
	public void Lexing(Reader r) throws IOException, LexerException{
		pu.Lexing(r);
	}
}
