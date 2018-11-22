package org.swlab.lib.parser;

public class Nonterminal extends Stkelem {
	private Object tree;

	public Nonterminal(Object tree) {
		super();
		this.tree = tree;
	}

	public Object getTree() {
		return tree;
	}

	public void setTree(Object tree) {
		this.tree = tree;
	}
}
