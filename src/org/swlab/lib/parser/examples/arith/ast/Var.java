package org.swlab.lib.parser.examples.arith.ast;

public class Var extends Expr {
	private String varName;
	
	public Var(String varName) {
		this.varName = varName;
	}
	
	public String getVarName() {
		return varName;
	}
	
	@Override
	public String toString() {
		return varName;
	}
}
