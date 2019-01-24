package org.swlab.lib.parser.examples.arith.comp;

import java.util.ArrayList;

import org.swlab.lib.parser.examples.arith.ast.Assign;
import org.swlab.lib.parser.examples.arith.ast.BinOp;
import org.swlab.lib.parser.examples.arith.ast.Expr;
import org.swlab.lib.parser.examples.arith.ast.Lit;
import org.swlab.lib.parser.examples.arith.ast.Var;
import org.swlab.lib.parser.examples.arith.vm.Instr;
import org.swlab.lib.parser.examples.arith.vm.InstrOp;
import org.swlab.lib.parser.examples.arith.vm.Pop;
import org.swlab.lib.parser.examples.arith.vm.Push;
import org.swlab.lib.parser.examples.arith.vm.Store;

public class Compiler {
	public static ArrayList<Instr> compile(ArrayList<Expr> exprSeq) {
		ArrayList<Instr> instrs = new ArrayList<Instr>();
		
		int index = 0;
		while (index < exprSeq.size()) {
			ArrayList<Instr> subInstrs = compile(exprSeq.get(index));
			
			instrs.addAll(subInstrs);
			instrs.add(new Pop());
			
			index = index + 1;
		}
		
		return instrs;
	}
	
	public static ArrayList<Instr> compile(Expr expr) {
		ArrayList<Instr> instrs = new ArrayList<Instr>();
		if (expr instanceof BinOp) {
			BinOp binOpExpr = (BinOp)expr;
			
			ArrayList<Instr> leftInstrs = compile(binOpExpr.getLeft());
			ArrayList<Instr> rightInstrs = compile(binOpExpr.getRight());
			
			instrs.addAll(leftInstrs);
			instrs.addAll(rightInstrs);
			
			switch(binOpExpr.getOpKind()) {
			case BinOp.ADD:
				instrs.add(new InstrOp(InstrOp.ADD));
				break;
			case BinOp.SUB:
				instrs.add(new InstrOp(InstrOp.SUB));
				break;
			case BinOp.MUL:
				instrs.add(new InstrOp(InstrOp.MUL));
				break;
			case BinOp.DIV:
				instrs.add(new InstrOp(InstrOp.DIV));
				break;
			default:
				assert false;
			}
		} else if (expr instanceof Assign) {
			Assign assignExpr = (Assign)expr;
			
			String varName = assignExpr.getVarName();
			Expr rhs = assignExpr.getRhs();
			
			ArrayList<Instr> rhsInstrs = compile(rhs);
			
			instrs.addAll(rhsInstrs);
			instrs.add(new Store(varName));
			instrs.add(new Push(varName));
			
		} else if (expr instanceof Lit) {
			Lit litExpr = (Lit)expr;
			
			Integer intLitV = litExpr.getInteger();
			
			instrs.add(new Push(intLitV));
		} else if (expr instanceof Var) {
			Var varExpr = (Var)expr;
			
			String varName = varExpr.getVarName();
			
			instrs.add(new Push(varName));
		}
		else {
			assert false; 
		}

		return instrs;
	}
}
