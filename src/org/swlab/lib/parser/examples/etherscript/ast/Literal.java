package org.swlab.lib.parser.examples.etherscript.ast;

public class Literal extends Expr {
	public final static int DECIMAL_NUMBER = 1;
	public final static int HEX_NUMBER = 2;
	public final static int BOOLEAN = 3;
	public final static int ETHER = 4;
	public final static int STRING = 5;
	public int kind;
	public String literal;
	public String unit; // null, ether, wei
}
