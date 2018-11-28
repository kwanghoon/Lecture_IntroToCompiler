package org.swlab.lib.parser.examples.etherscript.ast;

public class PrimOp extends Expr {
	public static final int ASSIGN = 1;
	
	public static final int LOGICALOR = 2;
	public static final int LOGICALAND = 3;
	
	public static final int EQ = 4;
	public static final int NOTEQ = 5;
	
	public static final int LT = 6;
	public static final int GT = 7;
	public static final int LE = 8;
	public static final int GE = 9;
	
	public static final int PLUS = 10;
	public static final int MINUS = 11;
	public static final int MULT = 12;
	public static final int DIV = 13;
	
	public static final int LOGICALNOT = 14;
	public static final int UNARYPLUS = 15;
	public static final int UNARYMINUS = 16;
	
	public int kind;
	public Expr op1, op2;
}
