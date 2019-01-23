package org.swlab.lib.parser.examples.arith.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.swlab.lib.parser.CommonParserUtil;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.arith.ast.Assign;
import org.swlab.lib.parser.examples.arith.ast.BinOp;
import org.swlab.lib.parser.examples.arith.ast.Expr;
import org.swlab.lib.parser.examples.arith.ast.Lit;
import org.swlab.lib.parser.examples.arith.ast.Var;

public class Parser {
	private CommonParserUtil<Token> pu;
	
	public Parser() throws IOException {
		pu = new CommonParserUtil<Token>();
		
		new Lexer(pu);
		
		pu.ruleStartSymbol("SeqExpr'");
		pu.rule("SeqExpr' -> SeqExpr", () -> { return pu.get(1); });
		
		pu.rule("SeqExpr -> SeqExpr ; AssignExpr", () -> { 
			ArrayList<Expr> seqexpr = (ArrayList<Expr>)pu.get(1);
			Expr assignexpr = (Expr)pu.get(3);
			seqexpr.add(assignexpr);
			return seqexpr; 
		});
		pu.rule("SeqExpr -> AssignExpr", () -> {
			ArrayList<Expr> seqexpr = new ArrayList<Expr>();
			Expr assignexpr = (Expr)pu.get(1);
			seqexpr.add(assignexpr);
			return seqexpr; 
		});
		
		pu.rule("AssignExpr -> identifier = AssignExpr", () -> { 
			String identifier = pu.getText(1);
			Expr assignexpr = (Expr)pu.get(3);
			return new Assign(identifier, assignexpr); 
		});
		pu.rule("AssignExpr -> AdditiveExpr", () -> { return pu.get(1); });
		
		pu.rule("AdditiveExpr -> AdditiveExpr + MultiplicativeExpr", () -> { 
			Expr additiveexpr = (Expr)pu.get(1);
			Expr multiplicativeexpr = (Expr)pu.get(3);
			return new BinOp(BinOp.ADD, additiveexpr, multiplicativeexpr); 
		});
		pu.rule("AdditiveExpr -> AdditiveExpr - MultiplicativeExpr", () -> { 
			Expr additiveexpr = (Expr)pu.get(1);
			Expr multiplicativeexpr = (Expr)pu.get(3);
			return new BinOp(BinOp.SUB, additiveexpr, multiplicativeexpr); 
		});
		pu.rule("AdditiveExpr -> MultiplicativeExpr", () -> { return pu.get(1); });
		
		pu.rule("MultiplicativeExpr -> MultiplicativeExpr * PrimaryExpr", () -> {
			Expr multiplicativeexpr = (Expr)pu.get(1);
			Expr primaryexpr = (Expr)pu.get(3);
			return new BinOp(BinOp.MUL, multiplicativeexpr, primaryexpr); 
		});
		pu.rule("MultiplicativeExpr -> MultiplicativeExpr / PrimaryExpr", () -> {
			Expr multiplicativeexpr = (Expr)pu.get(1);
			Expr primaryexpr = (Expr)pu.get(3);
			return new BinOp(BinOp.DIV, multiplicativeexpr, primaryexpr); 
		});
		pu.rule("MultiplicativeExpr -> PrimaryExpr", () -> { return pu.get(1); });
		
		pu.rule("PrimaryExpr -> identifier", () -> { return new Var(pu.getText(1)); });
		pu.rule("PrimaryExpr -> integer_number", () -> {
			String integer_number_str = pu.getText(1);
			Integer integer_number = Integer.parseInt(integer_number_str);
			return new Lit(integer_number); 
		});
		pu.rule("PrimaryExpr -> ( AssignExpr )", () -> { 
			return pu.get(2); 
		});		
	}
	
	public Object Parsing(Reader r) throws ParserException, IOException, LexerException {
		return Parsing(r, false);
	}
	
	public Object Parsing(Reader r, boolean flag) throws ParserException, IOException, LexerException {
		return pu.Parsing(r, flag);
	}
	
	public void Lexing(Reader r) throws IOException, LexerException{
		Lexing(r, false);
	}
	
	public void Lexing(Reader r, boolean flag) throws IOException, LexerException {
		pu.Lexing(r, flag);
	}
}
