package org.swlab.lib.parser.examples.arith.parser.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.arith.ast.Expr;
import org.swlab.lib.parser.examples.arith.ast.Interp;
import org.swlab.lib.parser.examples.arith.parser.Parser;

class ParserTest {

	private String example1 = "x = 1 + 2 - 3 * 4 / 5";
	private String example2 = "x=123; x=x+1; y=x; y=y-1*2/3";
	
	@Test
	void testParser1() throws IOException, ParserException, LexerException {
		StringReader reader = new StringReader(example1);
		Parser parser = new Parser();
		
		ArrayList<Expr> exprSeq =(ArrayList<Expr>)parser.Parsing(reader);
		
		Expr.prettyPrint(exprSeq);
		
		HashMap<String,Integer> env = new HashMap<String,Integer>();
		Interp.seq(exprSeq, env);
		
		assertTrue( env.get("x") == 1 );
	}
	
	@Test
	void testParser2() throws IOException, ParserException, LexerException {
		StringReader reader = new StringReader(example2);
		Parser parser = new Parser();
		
		ArrayList<Expr> exprSeq =(ArrayList<Expr>)parser.Parsing(reader);
		
		Expr.prettyPrint(exprSeq);
		
		HashMap<String,Integer> env = new HashMap<String,Integer>();
		Interp.seq(exprSeq, env);
		
		assertTrue( env.get("x") == 124 );
		assertTrue( env.get("y") == 124 );
	}

}
