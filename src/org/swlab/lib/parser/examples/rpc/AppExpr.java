package org.swlab.lib.parser.examples.rpc;

public class AppExpr extends Expr {
	private Expr fun;
	private Expr arg;
	
	public AppExpr(Expr fun, Expr arg) {
		super();
		this.fun = fun;
		this.arg = arg;
	}

	public Expr getFun() {
		return fun;
	}

	public void setFun(Expr fun) {
		this.fun = fun;
	}

	public Expr getArg() {
		return arg;
	}

	public void setArg(Expr arg) {
		this.arg = arg;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
