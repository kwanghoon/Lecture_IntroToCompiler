package com.rpc.parser.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.rpc.lib.LexerException;
import com.rpc.lib.ParserException;
import com.rpc.parser.AppExpr;
import com.rpc.parser.Expr;
import com.rpc.parser.LamExpr;
import com.rpc.parser.Parser;

class ParserTest {

	@Test
	void test1() throws IOException, LexerException, ParserException {
		StringReader sr = new StringReader("lam^c x.x");
		Parser parser = new Parser();
		
		Expr e = parser.Parsing(sr);
		
		assertTrue(e instanceof LamExpr);
	}

	@Test
	void test2() throws IOException, LexerException, ParserException {
		StringReader sr = new StringReader("((lam^s f. ((lam^s x. x) (f 1))) (lam^c y. ((lam^s z. z) y)))");
		Parser parser = new Parser();
		
		Expr e = parser.Parsing(sr);
		
		assertTrue(e instanceof AppExpr && ((AppExpr) e).getFun() instanceof LamExpr);
	}
	
	@Test
	void test3() throws IOException, LexerException, ParserException {
		StringReader sr = new StringReader("(lam^s f. (lam^s x. x) (f 1)) (lam^c y. (lam^s z. z) y)");
		Parser parser = new Parser();
		
		Expr e = parser.Parsing(sr);
		
		assertTrue(e instanceof AppExpr && ((AppExpr) e).getFun() instanceof LamExpr);
	}


}
