package org.swlab.lib.parser.examples.arith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.arith.ast.Expr;
import org.swlab.lib.parser.examples.arith.comp.Compiler;
import org.swlab.lib.parser.examples.arith.parser.Parser;
import org.swlab.lib.parser.examples.arith.vm.Instr;
import org.swlab.lib.parser.examples.arith.vm.VM;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String base = System.getProperty("user.dir");
		String prj = "src/org/swlab/lib/parser/examples/arith/test";
		File file = null;
		
		while(true) {
			try {
				System.out.print("Enter your file name: ");
				String filename = scan.next();
				file = new File(base + "/" + prj + "/" + filename);
				FileReader fr = new FileReader(file);
				Parser parser = new Parser();
		
				ArrayList<Expr> exprSeq = (ArrayList<Expr>)parser.Parsing(fr);
				
				System.out.println("\nParsing:");
				Expr.prettyPrint(exprSeq);
				
				ArrayList<Instr> instrs = (ArrayList<Instr>)Compiler.compile(exprSeq);
				
				System.out.println("\nCompiling:");
				Instr.prettyPrint(instrs);
				
				System.out.println("\nRunning VM:");
				HashMap<String,Integer> env = new HashMap<String,Integer>();
				VM.run(instrs, env);
				
				System.out.println("\nEnvironment:");
				Set<String> vars = env.keySet();
				for(String var : vars) {
					System.out.println(var + " = " + env.get(var));
				}
				
				System.out.println("\nSuccessfully done.");
			}
			catch(FileNotFoundException e) {
				System.err.println("Not found: " + file);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LexerException e) {
				e.printStackTrace();
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}
	}

}
