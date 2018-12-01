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

public class Parser {
	private CommonParserUtil<Token> pu;
	
	public Parser() throws IOException, LexerException {
		pu = new CommonParserUtil<Token>();
		Lexer.LexicalAnalysis(pu);
		SyntacticAnalysis(pu);
	}
	
	public void SyntacticAnalysis(CommonParserUtil<Token> pu) {
		pu.ruleStartSymbol("Program'");

		pu.rule("Program' -> Program", () -> {return pu.get(1);});
		pu.rule("Program -> Statement", () -> { 
			ArrayList<Stmt> arrList = new ArrayList<Stmt>();
			arrList.add((Stmt)pu.get(1));
			return arrList; 
		});
		pu.rule("Program -> Statement Program", () -> {
			ArrayList<Stmt> arrList = (ArrayList<Stmt>)pu.get(2);
			arrList.add(0, (Stmt)pu.get(1));
			return arrList;
		});
		pu.rule("Statement -> account { Properties } identifier OptArgExprs ;", () -> {
			Account acc = new Account();
			acc.properties = (HashMap<String,Expr>)pu.get(3);
			acc.name = (String)pu.getText(5);
			acc.argExprs = (ArrayList<Expr>)pu.get(6);
			return acc;
		});
		pu.rule("Statement -> identifier . OptIdentifier ( Exprs ) { Properties } ;", () -> {
			SendTransaction sendTr = new SendTransaction();
			sendTr.retVar = null;
			sendTr.contractName = (String)pu.getText(1);
			String functionName = (String)pu.get(3);
			sendTr.functionName = functionName == null ? "" : functionName;
			sendTr.argExprs = (ArrayList<Expr>)pu.get(5);
			sendTr.properties = (HashMap<String,Expr>)pu.get(8);
			return sendTr;
		});
		pu.rule("Statement -> identifier = identifier . OptIdentifier ( Exprs ) { Properties } ;", () -> {
			SendTransaction sendTr = new SendTransaction();
			sendTr.retVar = (String)pu.getText(1);
			sendTr.contractName = (String)pu.getText(1+2);
			String functionName = (String)pu.get(3+2);
			sendTr.functionName = functionName == null ? "" : functionName;
			sendTr.argExprs = (ArrayList<Expr>)pu.get(5+2);
			sendTr.properties = (HashMap<String,Expr>)pu.get(8+2);
			return sendTr;
		});
		pu.rule("Statement -> Type identifier ;", () -> {
			VarDecl varDecl = new VarDecl();
			varDecl.type = (Type)pu.get(1);
			varDecl.name = (String)pu.getText(2);
			return varDecl;
		});
		pu.rule("Statement -> assert Expr ;", () -> {
			Assert assertStmt = new Assert();
			assertStmt.conditional = (Expr)pu.get(2);
			return assertStmt;
		});
		pu.rule("OptIdentifier -> identifier", () -> {
			return pu.getText(1);
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
		pu.rule("Properties -> Property , Properties", () -> {
			Property prop = (Property)pu.get(1);
			HashMap<String,Expr> properties = (HashMap<String,Expr>)pu.get(3);
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
			prop.name = Property.by;
			prop.expr = (Expr)pu.get(3);
			return prop;
		});
		pu.rule("Property -> contract : Expr", () -> {
			Property prop = new Property();
			prop.name = Property.contract;
			prop.expr = (Expr)pu.get(3);
			return prop;
		});
		pu.rule("Property -> value : Expr", () -> {
			Property prop = new Property();
			prop.name = Property.value;
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
		pu.rule("Expr -> ExprAssign", () -> {
			return pu.get(1);
		});
		pu.rule("ExprAssign -> identifier = ExprAssign", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.ASSIGN;
			Var id = new Var();
			id.name = (String)pu.getText(1);
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
		pu.rule("ExprLogicalAnd -> ExprLogicalAnd && ExprEqual", () -> {
			PrimOp primOp = new PrimOp();
			primOp.kind = PrimOp.LOGICALAND;
			primOp.op1 = (Expr)pu.get(1);
			primOp.op2 = (Expr)pu.get(3);
			return primOp;
		});
		pu.rule("ExprLogicalAnd -> ExprEqual", () -> { return pu.get(1); });
		
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
			fieldAccess.field = (String)pu.getText(3);
			return fieldAccess;
		});
		pu.rule("PostfixExpression -> PostfixExpression . balance", () -> {
			FieldAccess fieldAccess = new FieldAccess();
			fieldAccess.obj = (Expr)pu.get(1);
			fieldAccess.field = (String)pu.getText(3);
			return fieldAccess;
		});
		pu.rule("PostfixExpression -> ExprPrim", () -> {
			return pu.get(1);
		});
		pu.rule("ExprPrim -> identifier", () -> {
			Var var = new Var();
			var.name = pu.getText(1);
			return var;
		});
		pu.rule("ExprPrim -> number_literal", () -> {
			return pu.get(1);
		});
		pu.rule("ExprPrim -> boolean_literal", () -> {
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
		pu.rule("number_literal -> hex_number", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.HEX_NUMBER;
			lit.literal = pu.getText(1);
			return lit;
		});
		
		pu.rule("number_literal -> hex_number number_unit", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.HEX_NUMBER;
			lit.literal = pu.getText(1);
			lit.unit = (String)pu.get(2);
			return lit;
		});
		
		pu.rule("number_literal -> decimal_number", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.DECIMAL_NUMBER;
			lit.literal = pu.getText(1);
			return lit;
		});
		
		pu.rule("number_literal -> decimal_number number_unit", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.DECIMAL_NUMBER;
			lit.literal = pu.getText(1);
			lit.unit = (String)pu.get(2);			
			return lit;			
		});
		
		pu.rule("number_unit -> wei", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> szabo", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> finney", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> ether", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> seconds", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> minutes", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> hours", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> days", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> weeks", () -> { return pu.getText(1); });
		
		pu.rule("number_unit -> years", () -> { return pu.getText(1); });
		
		pu.rule("boolean_literal -> true", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.BOOLEAN;
			lit.literal = pu.getText(1);
			return lit;
		});
		
		pu.rule("boolean_literal -> false", () -> {
			Literal lit = new Literal();
			lit.kind = Literal.BOOLEAN;
			lit.literal = pu.getText(1);
			return lit;
		});
		
		pu.rule("ExprPrim -> ( Expr )", () -> {
			return pu.get(2);
		});		
	}
	
	public Object Parsing(Reader r) throws ParserException, IOException, LexerException {
		return Parsing(r, false);
	}
	
	public Object Parsing(Reader r, boolean flag) throws ParserException, IOException, LexerException {
		return pu.Parsing(r, flag);
	}
	
	public void Lexing(Reader r) throws IOException, LexerException{
		Lexing(r, false);
	}
	
	public void Lexing(Reader r, boolean flag) throws IOException, LexerException{
		pu.Lexing(r, flag);
	}
}
