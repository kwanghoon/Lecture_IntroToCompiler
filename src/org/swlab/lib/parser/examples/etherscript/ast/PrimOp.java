package org.swlab.lib.parser.examples.etherscript.ast;

public class PrimOp extends Expr {
	static final int PLUS = 1;
	static final int MINUS = 2;
	static final int MULTIPLICATION = 2;
	public Expr op1, op2;
}
