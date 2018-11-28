package org.swlab.lib.parser.examples.etherscript.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

import org.junit.Test;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.etherscript.Parser;
import org.swlab.lib.parsertest.RegressionTest;

public class ParserTest extends RegressionTest {

	@Test
	public void parserTest() throws IOException, LexerException, ParserException {
		Parser parser = new Parser();
		
		Consumer<File> consumer = file -> {
			FileReader fr;
			try {
				fr = new FileReader(file);
				parser.Parsing(fr);
				return;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LexerException e) {
				e.printStackTrace();
			}
			assert false;
		};
		
		String base = System.getProperty("user.dir");
		testOverDirectoryTree(
			new File(base + 
					"/src/org/swlab/lib/parser/examples/etherscript/test/testcase"),
			consumer);
	}
}
