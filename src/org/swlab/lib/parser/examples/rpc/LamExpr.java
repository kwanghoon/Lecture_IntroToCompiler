package org.swlab.lib.parser.examples.rpc;

public class LamExpr extends Expr {
	private String loc;
	private String id;
	private Expr term;
	
	public LamExpr(String loc, String id, Expr term) {
		super();
		this.loc = loc;
		this.id = id;
		this.term = term;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Expr getTerm() {
		return term;
	}

	public void setTerm(Expr term) {
		this.term = term;
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
}
