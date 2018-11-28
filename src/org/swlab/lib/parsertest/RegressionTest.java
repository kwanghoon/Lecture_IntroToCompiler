package org.swlab.lib.parsertest;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;

public class RegressionTest {
	public void testOverDirectoryTree(File folder, Consumer<File> consumer) 
			throws ParserException, IOException, LexerException {
		
		if (folder.isDirectory()) {
			File[] listOfFiles = folder.listFiles();
	
			for (File file : listOfFiles) {
				if (file.isFile()) {
					System.out.println(file);
					consumer.accept(file);
				}
				else if (file.isDirectory()) {
					if (!file.toPath().toString().contains("test_result")) {
						testOverDirectoryTree(new File(file + "/"), consumer);
					}
				}
			}
		}
	}
}
