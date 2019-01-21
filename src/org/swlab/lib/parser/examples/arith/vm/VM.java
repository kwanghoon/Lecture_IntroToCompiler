package org.swlab.lib.parser.examples.arith.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class VM {
	public static void run(ArrayList<Instr> instrs, HashMap<String,Integer> env) {
		int index = 0;
		Stack<Integer> stack = new Stack<Integer>();
		
		while (index < instrs.size()) {
			interp(instrs.get(index), env, stack);
			index = index + 1;
		}
	}
	
	public static void interp(
			Instr instr, HashMap<String,Integer> env, Stack<Integer> stack) {
		if (instr instanceof InstrOp) {
			InstrOp instrOp = (InstrOp)instr;
			Integer v2 = stack.pop();
			Integer v1 = stack.pop();
			switch(instrOp.getOpcode()) {
			case InstrOp.ADD:
				stack.push(v1 + v2);
				break;
			case InstrOp.SUB:
				stack.push(v1 - v2);
				break;
			case InstrOp.MUL:
				stack.push(v1 * v2);
				break;
			case InstrOp.DIV:
				stack.push(v1 / v2);
				break;
			default:
				assert false;
			}
		} else if (instr instanceof Push) {
			Push push = (Push)instr;
			Integer v;
			
			switch(push.getOperandKind()) {
			case Push.LIT:
				v = push.getIntLit();
				stack.push(v);
				break;
			case Push.VAR:
				String varName = push.getVarName();
				v = env.get(varName);
				assert v != null;
				stack.push(v);
				break;
			default:
				assert false;
			}
		} else if (instr instanceof Pop) {
			Pop pop = (Pop)instr;
			Integer v = stack.pop(); // v is not used.
		} else if (instr instanceof Store) {
			Store store = (Store)instr;
			String varName = store.getVarName();
			Integer v = stack.pop();
			env.put(varName, v);
		} else {
			assert false;
		}
	}
}
