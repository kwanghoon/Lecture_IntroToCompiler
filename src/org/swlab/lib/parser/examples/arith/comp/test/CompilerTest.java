package org.swlab.lib.parser.examples.arith.comp.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.swlab.lib.parser.examples.arith.ast.Expr;
import org.swlab.lib.parser.examples.arith.ast.Interp;
import org.swlab.lib.parser.examples.arith.ast.test.InterpTest;
import org.swlab.lib.parser.examples.arith.vm.Instr;
import org.swlab.lib.parser.examples.arith.vm.VM;

class CompilerTest {

	@Test
	void test() {
		ArrayList<Expr> exprSeq = InterpTest.exampleExpr1();
		
		Expr.prettyPrint(exprSeq);
		
		ArrayList<Instr> instrs = 
				org.swlab.lib.parser.examples.arith.comp.Compiler.compile(exprSeq);
		
		Instr.prettyPrint(instrs);

		HashMap<String,Integer> envInterp = new HashMap<String,Integer>();
		Interp.seq(exprSeq, envInterp);
		
		HashMap<String,Integer> envVM = new HashMap<String,Integer>();
		VM.run(instrs, envVM);
		
		
		assertTrue( envInterp.get("x") == 124 );
		assertTrue( envInterp.get("y") == 124 );
		
		assertTrue( envVM.get("x") == 124 );
		assertTrue( envVM.get("y") == 124 );
	}

}
