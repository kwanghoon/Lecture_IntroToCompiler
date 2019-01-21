package org.swlab.lib.parser.examples.arith.vm;

public class Push extends Instr {
	public static final int VAR = 1;
	public static final int LIT = 2;
	
	private int operand_kind;
	private String varName;
	private Integer intLit;
	
	public Push(String varName) {
		this.operand_kind = VAR;
		this.varName = varName;
	}
	
	public Push(Integer intLit) {
		this.operand_kind = LIT;
		this.intLit = intLit;
	}

	public int getOperandKind() {
		return operand_kind;
	}

	public String getVarName() {
		assert this.operand_kind == VAR;
		return varName;
	}

	public Integer getIntLit() {
		assert this.operand_kind == LIT;
		return intLit;
	}
}
