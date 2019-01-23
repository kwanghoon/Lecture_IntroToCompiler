package org.swlab.lib.parser.examples.arith.parser.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.examples.arith.parser.Parser;

class LexerTest {
	
//	static final String tokenTest = 
//			"x = 123;\n" + 
//			"x = x + 1;\n" + 
//			"y = x;\n" + 
//			"y = y - 1 * 2 / 3";
	
	static final String tokenSymbol = "+ - * / = ; ( )";
	static final String tokenIdentifier = "i x123 hello x1y2z3";
	
	static final String tokenComplex = "x = 123;\n" + 
			"x = x + 1;\n" + 
			"y = x; \n" + 
			"y = y - 1 * 2 / 3";

	@Test
	void test() throws IOException, LexerException {
		StringReader reader = new StringReader(
				//tokenTest + " " + 
				//tokenSymbol + " " + 
//				tokenIdentifier + " " + 
				tokenComplex
				);
		Parser parser = new Parser();
		parser.Lexing(reader, true);
		assertTrue(true);
	}

}
