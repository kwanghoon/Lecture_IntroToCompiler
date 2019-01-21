package org.swlab.lib.parser.examples.arith.ast;

public class Assign extends Expr {
	private String varName;
	private Expr rhs;
	
	public Assign(String varName, Expr rhs) {
		this.varName = varName;
		this.rhs = rhs;
	}

	public String getVarName() {
		return varName;
	}

	public Expr getRhs() {
		return rhs;
	}
	
	@Override
	public String toString() {
		String exprStr = varName + " = " + rhs.toString();
		return "(" + exprStr + ")";
	}
}
