package org.swlab.lib.parser.examples.arith.vm;

public class InstrOp extends Instr {
	public static final int ADD = 1;
	public static final int SUB = 2;
	public static final int MUL = 3;
	public static final int DIV = 4;
	
	private int opcode; // ADD, SUB, MUL, DIV
	
	public InstrOp(int opcode) {
		assert opcode == ADD
				|| opcode == SUB 
				|| opcode == MUL
				|| opcode == DIV;
		this.opcode = opcode;
	}
	
	public int getOpcode() {
		return this.opcode;
	}
	
	@Override
	public String toString() {
		switch(opcode) {
		case ADD:
			return "ADD";
		case SUB:
			return "SUB";
		case MUL:
			return "MUL";
		case DIV:
			return "DIV";
		}
		assert false;
		return super.toString();
	}
}
