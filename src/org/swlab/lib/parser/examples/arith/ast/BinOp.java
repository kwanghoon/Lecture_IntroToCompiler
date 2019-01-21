package org.swlab.lib.parser.examples.arith.ast;

public class BinOp extends Expr {
	public final static int PLUS = 1;
	public final static int MINUS = 2;
	public final static int MUL = 3;
	public final static int DIV = 4;
	
	protected int op_kind;

	private Expr left, right;
	
	public BinOp(int op_kind, Expr left, Expr right) {
		assert op_kind == PLUS 
				|| op_kind == MINUS 
				|| op_kind == MUL 
				|| op_kind == DIV;
		
		this.op_kind = op_kind;
		this.left = left;
		this.right = right;
	}

	public int getOpKind() {
		return op_kind;
	}
	
	public Expr getLeft() {
		return left;
	}

	public Expr getRight() {
		return right;
	}

}
