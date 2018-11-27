package org.swlab.lib.parser.examples.etherscript.ast;

import java.util.ArrayList;
import java.util.HashMap;

public class SendTransaction extends Stmt {
	public String retVar; // optional
	public String contractName;
	public String functionName;
	public ArrayList<Expr> argExprs;
	public HashMap<String,String> properties;	// Property -> identifier 
}
