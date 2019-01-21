package org.swlab.lib.parser.examples.arith.ast;

public class Lit extends Expr {
	private Integer integerLit;
	
	public Lit(Integer i) {
		this.integerLit = i;
	}
	
	public Integer getInteger() {
		return integerLit;
	}
}
