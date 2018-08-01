package com.rpc.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class LexicalAnalyzer {
	private int lineno;
	private BufferedReader br;
	private ArrayList<String> strArr;
	private ArrayList<Terminal> lexer;

	public LexicalAnalyzer(Reader isr) {
		br = new BufferedReader(isr);
		strArr = new ArrayList<>();
		lexer = new ArrayList<>();
	}

	public ArrayList<Terminal> Lexing() throws IOException, LexerException {
		String read_string = br.readLine();

		while (true) {
			String next_string = br.readLine();

			if (next_string != null) {
				strArr.add(read_string + "\n");
				read_string = next_string;
			}
			else {
				strArr.add(read_string);
				break;
			}
		}

		lineno = 1;

		for (int idx = 0; idx < strArr.size(); idx++) {
			String line = strArr.get(0);
			String str = "";
			int strIdx = 0;
			Token currentTok;

			while (line != null && strIdx < line.length()) {
				char ch = line.charAt(strIdx);
				int front_idx = strIdx + 1;

				if (ch >= '0' && ch <= '9') { // num
					do {
						strIdx++;
						str = str + ch;
						if (strIdx < line.length()) {
							ch = line.charAt(strIdx);
						}
						else
							break;
					} while (ch >= '0' && ch <= '9');
					currentTok = Token.NUM;
				}
				else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) { // id
					do {
						strIdx++;
						str = str + ch;
						if (strIdx < line.length()) {
							ch = line.charAt(strIdx);
						}
						else
							break;
					} while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));

					if (strIdx < line.length()) {
						ch = line.charAt(strIdx);
						while ((ch >= '0' && ch <= '9')) {
							strIdx++;
							str = str + ch;
							if (strIdx < line.length()) {
								ch = line.charAt(strIdx);
							}
							else
								break;
						}
					}
					
					switch (str.toLowerCase()) {
					case "lam":
						str = "lam";
						currentTok = Token.LAM;
						break;
					default:
						currentTok = Token.ID;
					}
				}
				else if (ch == ' ' || ch == '\t' || ch == '\n') {
					strIdx++;
					continue;
				}
				else if (ch == '^') {
					strIdx++;
					str = str + ch;
					if (strIdx < line.length()) {
						ch = line.charAt(strIdx);

						strIdx++;
						str = str + ch;
						switch (str.toLowerCase()) {
						case "^c":
							str = "c";
							currentTok = Token.CLIENT;
							break;
						case "^s":
							str = "s";
							currentTok = Token.SERVER;
							break;
						default:
							throw new LexerException("Error: " + ch + "");
						}
					}
					else
						break;
				}
				else {
					switch (ch) {
					case '(':
						currentTok = Token.OPENPAREN;
						break;
					case ')':
						currentTok = Token.CLOSEPAREN;
						break;
					case '.':
						currentTok = Token.DOT;
						break;
					default:
						throw new LexerException("Error: " + ch + "");
					}
					str = str + ch;
					strIdx++;
				}

				lexer.add(new Terminal(str, currentTok, front_idx, strIdx + 1));
				str = "";
			}
			lineno++;
		}

		Terminal epsilon = new Terminal("$", Token.END_OF_TOKEN, -1, -1);
		lexer.add(epsilon);

		return lexer;
	}
}
