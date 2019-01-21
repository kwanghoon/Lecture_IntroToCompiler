package org.swlab.lib.parser.examples.arith.ast.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.swlab.lib.parser.examples.arith.ast.*;

class InterpTest {

	@Test
	void test() {
		ArrayList<Expr> exprList =  new ArrayList<Expr>();
		
		// x = 123
		Expr expr1 = new Assign("x", new Lit(123));
		
		// x = x + 1
		Expr expr2 = new Assign("x", 
				new BinOp(BinOp.PLUS, new Var("x"), new Lit(1)));
		
		// y = x
		Expr expr3 = new Assign("y", new Var("x"));
		
		// y = y - 1 * 2 / 3
		Expr expr4 = new Assign("y",
				new BinOp(BinOp.MINUS,
					new Var("y"),
					new BinOp(BinOp.DIV,
							new BinOp(BinOp.MUL, new Lit(1), new Lit(2)),
							new Lit(3))));
		
		// expr1 ; expr2 ; expr3 ; expr4
		Expr[] exprs = { expr1, expr2, expr3, expr4 };
		exprList = new ArrayList<Expr>(Arrays.asList(exprs));
		
		HashMap<String,Integer> env = new HashMap<String,Integer>();
		Interp.seq(exprList, env);
		
		assertTrue( env.get("x") == 124 );
		assertTrue( env.get("y") == 124 );
	}

}
