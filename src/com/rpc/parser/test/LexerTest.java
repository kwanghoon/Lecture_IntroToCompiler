package com.rpc.parser.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.rpc.parser.LexerException;
import com.rpc.parser.LexicalAnalyzer;
import com.rpc.parser.Terminal;
import com.rpc.parser.Token;

class LexerTest {

	@Test
	void test1() throws LexerException, IOException {
		StringReader sr = new StringReader("lam^c x.x");
		LexicalAnalyzer lexical = new LexicalAnalyzer(sr);
		ArrayList<Terminal> terminals = lexical.Lexing();
		System.out.println("----test1----");
		assertTrue(terminals.get(0).getToken() == Token.LAM);
		assertTrue(terminals.get(1).getToken() == Token.LOC);
		assertTrue(terminals.get(2).getToken() == Token.ID);
		assertTrue(terminals.get(3).getToken() == Token.DOT);
		assertTrue(terminals.get(4).getToken() == Token.ID);
		assertTrue(terminals.get(5).getToken() == Token.END_OF_TOKEN);
		
		for (Terminal t : terminals) {
			System.out.println(t.toString() + " " + t.getToken());
		}
	}
	
	@Test
	void test2() throws LexerException, IOException {
		StringReader sr = new StringReader("((lam^s f. ((lam^s x. x) (f 1))) (lam^c y. ((lam^s z. z) y)))");
		LexicalAnalyzer lexical = new LexicalAnalyzer(sr);
		ArrayList<Terminal> terminals = lexical.Lexing();
		System.out.println("----test2----");
		for (Terminal t : terminals) {
			System.out.println(t.toString() + " " + t.getToken());
		}
	}
}
