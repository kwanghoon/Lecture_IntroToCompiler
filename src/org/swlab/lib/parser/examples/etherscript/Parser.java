package org.swlab.lib.parser.examples.etherscript;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.swlab.lib.parser.CommonParserUtil;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.etherscript.ast.Account;
import org.swlab.lib.parser.examples.etherscript.ast.Assert;
import org.swlab.lib.parser.examples.etherscript.ast.Expr;
import org.swlab.lib.parser.examples.etherscript.ast.FieldAccess;
import org.swlab.lib.parser.examples.etherscript.ast.Literal;
import org.swlab.lib.parser.examples.etherscript.ast.PrimOp;
import org.swlab.lib.parser.examples.etherscript.ast.Property;
import org.swlab.lib.parser.examples.etherscript.ast.SendTransaction;
import org.swlab.lib.parser.examples.etherscript.ast.Stmt;
import org.swlab.lib.parser.examples.etherscript.ast.Type;
import org.swlab.lib.parser.examples.etherscript.ast.Var;
import org.swlab.lib.parser.examples.etherscript.ast.VarDecl;
import org.swlab.lib.parser.examples.rpc.Identifier;

public class Parser {
	private CommonParserUtil pu;
	
	public Parser() throws IOException, LexerException {
		pu = new CommonParserUtil();
		Lexer.LexicalAnalysis(pu);
		SyntacticAnalysis(pu);
	}
	
	public void SyntacticAnalysis(CommonParserUtil pu) {
		pu.rule("Program' -> Program", () -> {return pu.get(1);});
		pu.rule("Program -> Statement", () -> { 
			return new ArrayList<Stmt>().add((Stmt)pu.get(1)); 
		});
		pu.rule("Program -> Statement Program", () -> {
			ArrayList<Stmt> arrList = (ArrayList<Stmt>)pu.get(2);
			arrList.add(0, (Stmt)pu.get(1));
			return arrList;
		});
		pu.rule("Statement -> account { Properties } identifier OptArgExprs ;", () -> {
			Account acc = new Account();
			acc.properties = (HashMap<String,Expr>)pu.get(3);
			acc.name = (String)pu.get(5);
			acc.argExprs = (ArrayList<Expr>)pu.get(6);
			return acc;
		});
		pu.rule("Statement -> identifier . OptIdentifier ( Exprs ) { Properties } ", () -> {
			SendTransaction sendTr = new SendTransaction();
			sendTr.retVar = null;
			sendTr.contractName = (String)pu.get(1);
			String functionName = (String)pu.get(3);
			sendTr.functionName = functionName == null ? "" : functionName;
			sendTr.argExprs = (ArrayList<Expr>)pu.get(5);
			sendTr.properties = (HashMap<String,Expr>)pu.get(8);
			return sendTr;
		});
		pu.rule("Statement -> identifier = identifier . OptIdentifier ( Exprs ) { Properties } ", () -> {
			SendTransaction sendTr = new SendTransaction();
			sendTr.retVar = (String)pu.get(1);
			sendTr.contractName = (String)pu.get(1+2);
			String functionName = (String)pu.get(3+2);
			sendTr.functionName = functionName == null ? "" : functionName;
			sendTr.argExprs = (ArrayList<Expr>)pu.get(5+2);
			sendTr.properties = (HashMap<String,Expr>)pu.get(8+2);
			return sendTr;
		});
		pu.rule("Statement -> Type identifier ", () -> {
			VarDecl varDecl = new VarDecl();
			varDecl.type = (Type)pu.get(1);
			varDecl.name = (String)pu.get(2);
			return varDecl;
		});
		pu.rule("Statement -> assert Expr ;", () -> {
			Assert assertStmt = new Assert();
			assertStmt.conditional = (Expr)pu.get(2);
			return assertStmt;
		});
		pu.rule("OptIdentifier -> identifier", () -> {
			return pu.get(1);
		});
		pu.rule("OptIdentifier -> ", () -> {
			return null;
		});
		pu.rule("Properties -> Property", () -> {
			Property prop = (Property)pu.get(1);
			HashMap<String,Expr> properties = new HashMap<String,Expr>();
			properties.put(prop.name, prop.expr);
			return properties;
		});
		pu.rule("Properties -> Property Properties", () -> {
			Property prop = (Property)pu.get(1);
			HashMap<String,Expr> properties = (HashMap<String,Expr>)pu.get(2);
			properties.put(prop.name, prop.expr);
			return properties;
		});
		pu.rule("Property -> balance : Expr", () -> {
			Property prop = new Property();
			prop.name = Property.balance;
			prop.expr = (Expr)pu.get(3);
			return prop;
		});
		pu.rule("Property -> by : Expr", () -> {
			Property prop = new Property();
			prop.name = Property.balance;
			prop.expr = (Expr)pu.get(3);
			return prop;
		});
		pu.rule("Property -> contract : Expr", () -> {
			Property prop = new Property();
			prop.name = Property.balance;
			prop.expr = (Expr)pu.get(3);
			return prop;
		});
		pu.rule("Property -> value : Expr", () -> {
			Property prop = new Property();
			prop.name = Property.balance;
			prop.expr = (Expr)pu.get(3);
			return prop;
		});
		pu.rule("Type -> bool", () -> {
			Type type = new Type();
			type.kind = Type.BOOL;
			return type;
		});
		pu.rule("Type -> uint", () -> {
			Type type = new Type();
			type.kind = Type.UINT;
			return type;
		});
		pu.rule("Type -> address", () -> {
			Type type = new Type();
			type.kind = Type.ADDRESS;
			return type;
		});
		pu.rule("Type -> string", () -> {
			Type type = new Type();
			type.kind = Type.STRING;
			return type;
		});
		pu.rule("OptArgExprs -> ", () -> {
			return null;
		});
		pu.rule("OptArgExprs -> ( Exprs )", () -> {
			return pu.get(2);
		});
		pu.rule("Exprs -> ", () -> {
			ArrayList<Expr> exprs = new ArrayList<Expr>();
			return exprs;
		});
		pu.rule("Exprs -> MoreThanOneExpr", () -> {
			return pu.get(1);
		});
		pu.rule("MoreThanOneExpr -> Expr", () -> {
			ArrayList<Expr> exprs = new ArrayList<Expr>();
			exprs.add((Expr)pu.get(1));
			return exprs;
		});
		pu.rule("MoreThanOneExpr -> Expr , MoreThanOneExpr", () -> {
			Expr expr = (Expr)pu.get(1);
			ArrayList<Expr> exprs = (ArrayList<Expr>)pu.get(3);
			exprs.add(0, expr);
			return exprs;
		});
		pu.rule("ExprAssign -> identifer = ExprAssign", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.ASSIGN;
			Var id = new Var();
			id.name = (String)pu.get(1);
			primOp.op1 = id;
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprAssign -> ExprLogicalOr", () -> {
			return pu.get(1);
		});
		pu.rule("ExprLogicalOr -> ExprLogicalOr || ExprLogicalAnd", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.LOGICALOR;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprLogicalOr -> ExprLogicalAnd", () -> {
			return pu.get(1);
		});	
		pu.rule("ExprLogicalAnd -> ExprLogicalAnd &&= ExprEqual", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.LOGICALAND;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprEqual -> ExprEqual == ExprInequal", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.EQ;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprEqual -> ExprEqual != ExprInequal", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.NOTEQ;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprEqual -> ExprInequal", () -> {
			return pu.get(1);
		});	
		pu.rule("ExprInequal -> ExprInequal < ExprAddition", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.LT;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprInequal -> ExprInequal > ExprAddition", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.GT;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprInequal -> ExprInequal <= ExprAddition", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.LE;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprInequal -> ExprInequal >= ExprAddition", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.GE;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprInequal -> ExprAddition", () -> {
			return pu.get(1);
		});	
		pu.rule("ExprAddition -> ExprAddition + ExprMultiplication", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.PLUS;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprAddition -> ExprAddition - ExprMultiplication", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.MINUS;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprAddition -> ExprMultiplication", () -> {
			return pu.get(1);
		});	
		pu.rule("ExprMultiplication -> ExprMultiplication * ExprUnaryArithLogical", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.MULT;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprMultiplication -> ExprMultiplication / ExprLogicaExprUnaryArithLogicallNot", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.DIV;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprMultiplication -> ExprUnaryArithLogical", () -> {
			return pu.get(1);
		});	
		pu.rule("ExprUnaryArithLogical -> ! ExprUnaryArithLogical", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.LOGICALNOT;
			primOp.op1 = (Expr)pu.get(2);
			return primOp;
		});
		pu.rule("ExprUnaryArithLogical -> + ExprUnaryArithLogical", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.UNARYPLUS;
			primOp.op1 = (Expr)pu.get(2);
			return primOp;
		});
		pu.rule("ExprUnaryArithLogical -> - ExprUnaryArithLogical", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.UNARYMINUS;
			primOp.op1 = (Expr)pu.get(2);
			return primOp;
		});
		pu.rule("ExprUnaryArithLogical -> PostfixExpression", () -> {
			return pu.get(1);
		});
		pu.rule("PostfixExpression -> PostfixExpression . identifier", () -> {
			FieldAccess fieldAccess = new FieldAccess();
			fieldAccess.obj = (Expr)pu.get(1);
			fieldAccess.field = (String)pu.get(3);
			return fieldAccess;
		});
		pu.rule("PostfixExpression -> ExprPrime", () -> {
			return pu.get(1);
		});
		pu.rule("ExprPrim -> identifier", () -> {
			return pu.getText(1);
		});
		pu.rule("ExprPrim -> number_literal", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.NUMBER;
			lit.literal = pu.getText(1);
			return lit;
		});
		pu.rule("ExprPrim -> bool_literal", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.BOOLEAN;
			lit.literal = pu.getText(1);
			return lit;			
		});
		pu.rule("ExprPrim -> string_literal", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.STRING;
			lit.literal = pu.getText(1);
			return lit;			
		});
		pu.rule("ExprPrim -> ( Expr )", () -> {
			return pu.get(2);
		});		
	}
	
	public Object Parsing(Reader r) throws ParserException, IOException, LexerException {
		return pu.Parsing(r);
	}
	
	public void Lexing(Reader r) throws IOException, LexerException{
		pu.Lexing(r);
	}
}
