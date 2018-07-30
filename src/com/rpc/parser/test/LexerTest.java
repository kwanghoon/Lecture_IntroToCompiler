package com.rpc.parser.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.rpc.parser.LexerException;
import com.rpc.parser.LexicalAnalyzer;
import com.rpc.parser.Terminal;

class LexerTest {

	@Test
	void test1() throws LexerException, IOException {
		StringReader sr = new StringReader("lam s x.x");
		LexicalAnalyzer lexical = new LexicalAnalyzer(sr);
		ArrayList<Terminal> terminals = lexical.Lexing();
		System.out.println("----test1----");
		for (Terminal t : terminals) {
			System.out.println(t.toString() + " " + t.getToken());
		}
	}
	
	@Test
	void test2() throws LexerException, IOException {
		StringReader sr = new StringReader("lam s f. (lam s x. x) ((f) (1))) (lam c y. (lam s z. z) (y)");
		LexicalAnalyzer lexical = new LexicalAnalyzer(sr);
		ArrayList<Terminal> terminals = lexical.Lexing();
		System.out.println("----test2----");
		for (Terminal t : terminals) {
			System.out.println(t.toString() + " " + t.getToken());
		}
	}
}
