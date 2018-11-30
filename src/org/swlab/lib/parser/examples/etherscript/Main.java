package org.swlab.lib.parser.examples.etherscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String base = System.getProperty("user.dir");
		String prj = "src/org/swlab/lib/parser/examples/etherscript/test/testcase";
		File file = null;
		
		while(true) {
			try {
				System.out.print("Enter the script file name: ");
				String filename = scan.next();
				file = new File(base + "/" + prj + "/" + filename);
				FileReader fr = new FileReader(file);
				Parser parser = new Parser();
		
				parser.Parsing(fr, true);
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
