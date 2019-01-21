package org.swlab.lib.parser.examples.arith.vm.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.swlab.lib.parser.examples.arith.vm.Instr;
import org.swlab.lib.parser.examples.arith.vm.InstrOp;
import org.swlab.lib.parser.examples.arith.vm.Pop;
import org.swlab.lib.parser.examples.arith.vm.Push;
import org.swlab.lib.parser.examples.arith.vm.Store;
import org.swlab.lib.parser.examples.arith.vm.VM;

class VMTest {

	@Test
	void test() {
		Instr i1 = new Push(2);
		Instr i2 = new Push(1);
		Instr i3 = new Store("x");
		Instr i4 = new Push("x");
		Instr i5 = new InstrOp(InstrOp.ADD);
		Instr i6 = new Store("y");
		Instr i7 = new Push("y");
		Instr i8 = new Pop();
		
		Instr[] instrArr = { i1, i2, i3, i4, i5, i6, i7, i8 };
		ArrayList<Instr> instrs = new ArrayList<Instr>(Arrays.asList(instrArr));
		HashMap<String,Integer> env = new HashMap<String,Integer>();
		
		VM.run(instrs, env);
		
		assertTrue(env.get("y") == 3);
	}

}
