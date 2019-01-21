package org.swlab.lib.parser.examples.arith.ast;

public abstract class Expr {
	public final static int PLUS = 1;
	public final static int MINUS = 2;
	public final static int MUL = 3;
	public final static int DIV = 4;
	public final static int ASSIGN = 5;
	
	protected int op_kind;

	public int getOpKind() {
		return op_kind;
	}
	
	
}
