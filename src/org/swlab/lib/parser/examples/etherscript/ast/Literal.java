package org.swlab.lib.parser.examples.etherscript.ast;

public class Literal extends Expr {
	public final static int NUMBER = 1;
	public final static int BOOLEAN = 2;
	public final static int ETHER = 3;
	public final static int STRING = 4;
	public int kind;
	public String literal;
	public String unit; // null, ether, wei
}
