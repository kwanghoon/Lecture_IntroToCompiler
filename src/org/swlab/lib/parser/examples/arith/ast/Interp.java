package org.swlab.lib.parser.examples.arith.ast;

import java.util.ArrayList;
import java.util.HashMap;

public class Interp {
	public static void seq(
			ArrayList<Expr> exprList, HashMap<String, Integer> env) {
		int index = 0;
		
		while (index < exprList.size()) {
			Integer retV = expr(exprList.get(index), env); // retV is not used.
			index = index + 1;
		}
	}
	
	public static Integer expr(Expr expr, HashMap<String, Integer> env) {
		if (expr instanceof BinOp) {
			BinOp binOpExpr = (BinOp)expr;
			
			Integer leftV = expr(binOpExpr.getLeft(), env);
			Integer rightV = expr(binOpExpr.getRight(), env);
			
			switch(binOpExpr.getOpKind()) {
			case BinOp.PLUS:
				return leftV + rightV;
			case BinOp.MINUS:
				return leftV - rightV;
			case BinOp.MUL:
				return leftV * rightV;
			case BinOp.DIV:
				return leftV / rightV;
			default:
				assert false;
			}
		} else if (expr instanceof Assign) {
			Assign assignExpr = (Assign)expr;
			
			String varName = assignExpr.getVarName();
			Expr rhs = assignExpr.getRhs();
			
			Integer rhsV = expr(rhs, env);
			env.put(varName, rhsV);
			
			return rhsV;
		} else if (expr instanceof Lit) {
			Lit litExpr = (Lit)expr;
			
			Integer intLitV = litExpr.getInteger();
			
			return intLitV;
		} else if (expr instanceof Var) {
			Var varExpr = (Var)expr;
			
			String varName = varExpr.getVarName();
			Integer varV = env.get(varName);
			assert varV != null;
			
			return varV;
		}
		else {
			assert false; 
		}
		return 0; // unreachable!
	}
}
