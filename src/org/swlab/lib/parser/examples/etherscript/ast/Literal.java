package org.swlab.lib.parser.examples.etherscript.ast;

public class Literal extends Expr {
	final static int NUMBER = 1;
	final static int BOOLEAN = 2;
	final static int ETHER = 3;
	public String literal;
	public String unit; // null, ether, wei
}
