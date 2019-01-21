package org.swlab.lib.parser.examples.arith.vm;

public class Store extends Instr {
	private String varName;
	
	public Store(String varName) {
		this.varName = varName;
	}
	
	public String getVarName() {
		return this.varName;
	}
}
