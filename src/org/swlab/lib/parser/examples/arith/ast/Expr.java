package org.swlab.lib.parser.examples.arith.ast;

import java.util.ArrayList;

public abstract class Expr {
	public void prettyPrint() {
		System.out.print(this.toString());
	}
	
	public static void prettyPrint(ArrayList<Expr> exprSeq) {
		int index = 0;
		while (index < exprSeq.size()) {
			String delimeter = index + 1 < exprSeq.size() ? ";" : "";
			System.out.println(exprSeq.get(index).toString() + delimeter);
			index = index + 1;
		}
	}
}
