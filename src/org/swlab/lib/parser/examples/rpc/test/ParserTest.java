package org.swlab.lib.parser.examples.rpc.test;

//import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.rpc.AppExpr;
import org.swlab.lib.parser.examples.rpc.Expr;
import org.swlab.lib.parser.examples.rpc.LamExpr;
import org.swlab.lib.parser.examples.rpc.Parser;

import static junit.framework.TestCase.assertTrue;

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
