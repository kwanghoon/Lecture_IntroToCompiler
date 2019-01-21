package org.swlab.lib.parser.examples.arith.ast;

public class BinOp extends Expr {	
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

	public Expr getLeft() {
		return left;
	}

	public Expr getRight() {
		return right;
	}

}
