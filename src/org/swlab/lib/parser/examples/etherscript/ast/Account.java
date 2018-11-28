package org.swlab.lib.parser.examples.etherscript.ast;

import java.util.ArrayList;
import java.util.HashMap;

public class Account extends Stmt {
	public HashMap<String,Expr> properties;	// Property -> Identifier
	public String name;
	public ArrayList<Expr> argExprs; // 
}
