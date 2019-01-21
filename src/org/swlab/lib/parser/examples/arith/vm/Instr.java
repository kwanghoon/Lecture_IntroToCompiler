package org.swlab.lib.parser.examples.arith.vm;

import java.util.ArrayList;

public abstract class Instr {
	public void prettyPrint() {
		System.out.print(this.toString());
	}
	
	public static void prettyPrint(ArrayList<Instr> instrs) {
		int index = 0;
		while (index < instrs.size()) {
			System.out.println(instrs.get(index).toString());
			index = index + 1;
		}
	}
}
