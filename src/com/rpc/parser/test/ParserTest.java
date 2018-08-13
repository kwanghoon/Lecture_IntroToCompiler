package com.rpc.parser.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.rpc.parser.AppExpr;
import com.rpc.parser.Expr;
import com.rpc.parser.LamExpr;
import com.rpc.parser.LexerException;
import com.rpc.parser.LexicalAnalyzer;
import com.rpc.parser.Parser;
import com.rpc.parser.ParserException;

class ParserTest {

	@Test
	void test1() throws IOException, LexerException, ParserException {
		StringReader sr = new StringReader("lam^c x.x");
		LexicalAnalyzer lexical = new LexicalAnalyzer(sr);
		Parser parser = new Parser(lexical);
		
		Expr e = parser.Parsing();
		
		assertTrue(e instanceof LamExpr);
	}

	@Test
	void test2() throws IOException, LexerException, ParserException {
		StringReader sr = new StringReader("((lam^s f. ((lam^s x. x) (f 1))) (lam^c y. ((lam^s z. z) y)))");
		LexicalAnalyzer lexical = new LexicalAnalyzer(sr);
		Parser parser = new Parser(lexical);
		
		Expr e = parser.Parsing();
		
		assertTrue(e instanceof AppExpr && ((AppExpr) e).getFun() instanceof LamExpr);
	}
	
	@Test
	void test3() throws IOException, LexerException, ParserException {
		StringReader sr = new StringReader("(lam^s f. (lam^s x. x) (f 1)) (lam^c y. (lam^s z. z) y)");
		LexicalAnalyzer lexical = new LexicalAnalyzer(sr);
		Parser parser = new Parser(lexical);
		
		Expr e = parser.Parsing();
		
		assertTrue(e instanceof AppExpr && ((AppExpr) e).getFun() instanceof LamExpr);
	}


}
