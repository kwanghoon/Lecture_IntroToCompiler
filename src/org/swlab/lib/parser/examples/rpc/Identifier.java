package org.swlab.lib.parser.examples.rpc;

public class Identifier extends Expr {
	private String name;

	public Identifier(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
