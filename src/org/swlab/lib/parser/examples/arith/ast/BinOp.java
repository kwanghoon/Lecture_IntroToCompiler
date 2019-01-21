package org.swlab.lib.parser.examples.arith.ast;

public class BinOp extends Expr {
	public final static int ADD = 1;
	public final static int SUB = 2;
	public final static int MUL = 3;
	public final static int DIV = 4;
	
	protected int op_kind;

	private Expr left, right;
	
	public BinOp(int op_kind, Expr left, Expr right) {
		assert op_kind == ADD 
				|| op_kind == SUB 
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

	@Override
	public String toString() {
		String opstr = null;
		switch(op_kind) {
		case ADD:
			opstr = "+";
			break;
		case SUB:
			opstr = "-";
			break;
		case MUL:
			opstr = "*";
			break;
		case DIV:
			opstr = "/";
			break;
		default:
			assert false;
		}
		String exprstr = left.toString() + " " + opstr + " " + right.toString();
		return "(" + exprstr + ")";
	}
}
