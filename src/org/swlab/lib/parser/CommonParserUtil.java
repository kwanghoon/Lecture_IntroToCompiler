package org.swlab.lib.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.swlab.lib.parser.TokenInterface;

public class CommonParserUtil<Token extends TokenInterface<Token>> {
	private boolean DEBUG;
	
	// Lexer part
	private int lineno;
	private String endOfTok;

	private BufferedReader br;
	private ArrayList<String> lineArr;
	private ArrayList<Terminal<Token>> lexer;

	// Important: Use LinkedHashMap instead of HashMap
	//            to maintain the order in which each lexical 
	//            rule is added.
	private LinkedHashMap<String, TokenBuilder<Token>> tokenBuilders;

	// Parser part
	private String startSymbol;
	
	private ArrayList<String> action_table;
	private ArrayList<String> goto_table;
	private LinkedHashMap<Integer, String> grammar_rules;

	private LinkedHashMap<String, TreeBuilder> treeBuilders;

	private Stack<Stkelem> stack;

	private int productionRuleIdx;
	
	// Configuration
	private String workingdir = "./";

	public CommonParserUtil() throws IOException {
		this.lexer = new ArrayList<Terminal<Token>>();

		stack = new Stack<>();

		action_table = new ArrayList<>();
		goto_table = new ArrayList<>();
		grammar_rules = new LinkedHashMap<>();

		treeBuilders = new LinkedHashMap<>();
		tokenBuilders = new LinkedHashMap<>();
	}
	/*
	 * public CommonParserUtil(ArrayList<Terminal> lexer) throws IOException {
	 * super(); this.lexer = lexer;
	 * 
	 * stack = new Stack<>();
	 * 
	 * action_table = new ArrayList<>(); goto_table = new ArrayList<>();
	 * grammar_rules = new HashMap<>();
	 * 
	 * treeBuilders = new HashMap<>(); tokenBuilders = new HashMap<>();
	 * 
	 * readInitialize(); }
	 */

	public Object get(int i) {
		String productionRuleStr = grammar_rules.get(productionRuleIdx);
		String[] splitRule = productionRuleStr.split("[\t ]");
		int length = splitRule.length - 2;

		int last_stack_tree_index = stack.size() - 1;
		int offset = (length * 2) - ((i - 1) * 2 + 1);
		Nonterminal nt = (Nonterminal) stack.get(last_stack_tree_index - offset);

		return nt.getTree();
	}

	public String getText(int i) {
		String productionRuleStr = grammar_rules.get(productionRuleIdx);
		String[] splitRule = productionRuleStr.split("[\t ]");
		int length = splitRule.length - 2;

		int last_stack_tree_index = stack.size() - 1;
		int offset = (length * 2) - ((i - 1) * 2 + 1);
		Terminal<Token> nt = (Terminal<Token>) stack.get(last_stack_tree_index - offset);

		return nt.getSyntax();
	}

	public void ruleStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}
	
	public void rule(String productionRule, TreeBuilder tb) {
		treeBuilders.put(productionRule.trim(), tb);
	}

	public void lex(String regExp, TokenBuilder<Token> tb) {
		tokenBuilders.put(regExp, tb);
	}
	
	public void lexKeyword(String regExp, TokenBuilder<Token> tb) {
		tokenBuilders.put(regExp + "(?![a-zA-Z])", tb);
	}

	public void lexEndToken(String regExp, TokenBuilder<Token> tb) {
		tokenBuilders.put(regExp, tb);
		endOfTok = regExp;
	}

	public void Lexing(Reader r) throws IOException, LexerException {
		Lexing(r, false);
	}
	
	public void Lexing(Reader r, boolean debug) throws IOException, LexerException {
		DEBUG = debug;
		
		br = new BufferedReader(r);
		lineArr = new ArrayList<>();

		String read_string = br.readLine();

		while (true) {
			String next_string = br.readLine();

			if (next_string != null) {
				lineArr.add(read_string + "\n");
				read_string = next_string;
			}
			else {
				lineArr.add(read_string);
				break;
			}
		}

		lineno = 1;
		TokenBuilder<Token> tb;

		Object[] keys = tokenBuilders.keySet().toArray();
		Pattern[] ps = new Pattern[keys.length];
		
		for(int i = 0; i< keys.length; i++)
			ps[i] = Pattern.compile((String)keys[i]);
		
		for (int idx = 0; idx < lineArr.size(); idx++) {
			String line = lineArr.get(idx);
			String str = "";
			int front_idx = 0;

			while (front_idx < line.length()) {
				int i;
				
				for (i = 0; i < keys.length; i++) {
					String regExp = (String) keys[i];
					Pattern p = ps[i];
					Matcher matcher = p.matcher(line).region(front_idx, line.length());
					
					if (matcher.lookingAt()) {
						int startIdx = matcher.start();
						int endIdx = matcher.end();
						
						str = line.substring(startIdx, endIdx);
						matcher.region(endIdx, line.length());
						
						tb = tokenBuilders.get(regExp);
						if (tb.tokenBuilder(str) != null) {
							lexer.add(new Terminal<Token>(str, tb.tokenBuilder(str), startIdx, lineno));
						}
						
						str = "";
						front_idx = endIdx;
						break;
					}
				}
				
				if (i >= keys.length)
					throw new LexerException("No Pattern Matching " + front_idx + ", " + line.substring(front_idx));
				
			}
			lineno++;
		}

		tb = tokenBuilders.get(endOfTok);
		Terminal<Token> epsilon = new Terminal<Token>(endOfTok, tb.tokenBuilder(endOfTok), -1, -1);
		lexer.add(epsilon);
		
		if (debug) {
			for(Terminal<Token> t : lexer) {
				System.out.print(t.getToken() + "[" + t + "] ");
			}
		}
	}

	public Object Parsing (Reader r) throws ParserException, IOException, LexerException {
		return Parsing(r, false);
	}
	
	public Object Parsing(Reader r, boolean debug) throws ParserException, IOException, LexerException {
		DEBUG = debug;
		
		readInitialize();

		Lexing(r, debug);

		stack.clear();
		stack.push(new ParseState("0"));
		Object tree = null;

		while (!lexer.isEmpty()) {
			ParseState currentState = (ParseState) stack.lastElement();
			Terminal<Token> currentTerminal = lexer.get(0);
			
			if (debug) {
				System.out.println("Line " + currentTerminal.getLineIndex() 
						+ ", " + currentTerminal.getChIndex() 
						+ " : " + currentTerminal.getSyntax());
				for(int i = 0; i < stack.size(); i++) {
					Stkelem e = stack.get(i);
					if (e == null) System.out.print("null");
					else System.out.print(e);
					if (i < stack.size()-1) System.out.print(" :: ");
				}
				System.out.println();
			}

			ArrayList<String> data_arr = Check_state(currentState, currentTerminal, lexer);
			String order = data_arr.get(0); // Accept | Reduce | Shift

			switch (order) {
			case "Accept":
				lexer.remove(0);
				return ((Nonterminal) stack.get(1)).getTree();
			case "Shift":
				String state = data_arr.get(1);
				stack.push(currentTerminal);
				stack.push(new ParseState(state));
				lexer.remove(0);
				break;
			case "Reduce":
				int grammar_rule_num = Integer.parseInt(data_arr.get(1));
				productionRuleIdx = grammar_rule_num;

				String grammar_rule = grammar_rules.get(grammar_rule_num);
				String[] reduce_arr = grammar_rule.split("->", 2);
				String lhs = reduce_arr[0].trim();
				String rhs;
				int rhsCount;

				if (1 < reduce_arr.length && reduce_arr[1].length() > 0) // -> "empty"
				{
					rhs = reduce_arr[1].trim();
					rhsCount = rhs.split(" ").length; // *2 => pop count.
				}
				else {
					rhs = "";
					rhsCount = 0;
				}
				
				TreeBuilder tb = treeBuilders.get(grammar_rules.get(grammar_rule_num));

				if (tb != null) {
					tree = tb.treeBuilder();
				}
				else {
					throw new ParserException("Unexpected grammar rule " + grammar_rule_num 
							+ "\n"
							+ "In reduce " + grammar_rule 
							+ " at " + currentState + " " + currentTerminal);
				}

				// pop stack
				while (rhsCount != 0) {
					stack.pop();
					stack.pop();
					rhsCount--;
				}

				currentState = (ParseState) stack.lastElement();

				stack.push(new Nonterminal(tree));
				
				stack.push(get_st(currentState, lhs, lexer));
				break;
			}
		}
		throw new ParserException("Empty Token in Lexer");
	}

	private void readInitialize() throws IOException {
		try {
			if(DEBUG) {
				System.out.println(getWorkingdir() + "grammar_rules.txt");
				System.out.println(getWorkingdir() + "action_table.txt");
				System.out.println(getWorkingdir() + "goto_table.txt");
			}

			FileReader grammarFReader = new FileReader(getWorkingdir() + "grammar_rules.txt");
			FileReader actionFReader = new FileReader(getWorkingdir() + "action_table.txt");
			FileReader gotoFReader = new FileReader(getWorkingdir() + "goto_table.txt");
	
			BufferedReader grammarBReader = new BufferedReader(grammarFReader);
			BufferedReader actionBReader = new BufferedReader(actionFReader);
			BufferedReader gotoBReader = new BufferedReader(gotoFReader);
	
			String tmpLine;
			
			grammar_rules.clear();
			action_table.clear();
			goto_table.clear();
	
			while ((tmpLine = grammarBReader.readLine()) != null) {
				// grammarNumber: grammar
				String[] arr = tmpLine.split(":", 2); // : can appear in a production rule.
	
				int grammerNum = Integer.parseInt(arr[0].trim());
				String grammer = arr[1].trim();
	
				grammar_rules.put(grammerNum, grammer);
			}
	
			while ((tmpLine = actionBReader.readLine()) != null) {
				action_table.add(tmpLine);
			}
	
			while ((tmpLine = gotoBReader.readLine()) != null) {
				goto_table.add(tmpLine);
			}
			
			grammarBReader.close();
			actionBReader.close();
			gotoBReader.close();
		} catch (FileNotFoundException e) {
			createGrammarRules();
		}
	}
	
	private void createGrammarRules() throws IOException {
		// CFG "L'" [
		// 		ProductionRule "L'" [Nonterminal "L"],
		// 		ProductionRule "L" [Nonterminal "E"],
		// 		ProductionRule "L" [Terminal "lam", Terminal "loc", Terminal "id", Terminal ".", Nonterminal "L"],
		// 		ProductionRule "E" [Nonterminal "E", Nonterminal "T"],
		// 		ProductionRule "E" [Nonterminal "T"],
		// 		ProductionRule "T" [Terminal "id"],
		// 		ProductionRule "T" [Terminal "num"],
		// 		ProductionRule "T" [Terminal "(", Nonterminal "L", Terminal ")"]
		// ]
		Object[] objGrammar = treeBuilders.keySet().toArray();

		// HashMap�뜝�룞�삕 �뜝�떕�벝�삕 Key�뜝�룞�삕 �뜝�룞�삕�뜝�떇�눦�삕 �뜝�룞�삕�뜝�룜�뵆�뜝�룞�삕�뜝�룞�삕
		ArrayList<String> nonterminals = new ArrayList<>();

		// nonterminal setting
		for (int i = 0; i < objGrammar.length; i++) {
			String grammar = (String) objGrammar[i];
			String[] data = grammar.split("->", 2); // symbol -> g1 g2 g3 ...

			if (!nonterminals.contains(data[0].trim())) {
				nonterminals.add(data[0].trim());
			}
		}

		assert startSymbol != null;
		
		String fileContent = "CFG \"" + startSymbol + "\" [\n";

		for (int i = 0; i < objGrammar.length; i++) {
			String grammar = (String) objGrammar[i];
			String[] data = grammar.split("->", 2);

			fileContent += "\tProductionRule \"" + data[0].trim() + "\" [";

			// data[0] �뜝�룞�삕 ProductionRule �뜝�듅源띿�뜝�룞�삕�뜝�떛源띿
			// data[1] �뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 Nonterminal Terminal �뜝�떎�뙋�삕 �뜝�떗�슱�삕
			if (data.length > 1 && data[1].trim().length() > 0) {
				String[] tok = data[1].trim().split("[ \t\n]");
	
				for (int j = 0; j < tok.length; j++) {
					if (nonterminals.contains(tok[j])) { // �뜝�룞�삕�뜝�룞�삕 token�뜝�룞�삕 Nonterminal�뜝�룞�삕 �뜝�룞�삕�뜝��
						fileContent += "Nonterminal \"";
					}
					else {
						fileContent += "Terminal \"";
					}
	
					fileContent += tok[j] + "\"";
	
					if (j < tok.length - 1) {
						fileContent += ", ";
					}
				}
			}

			fileContent += "]";
			if (i < objGrammar.length - 1) {
				fileContent += ",\n";
			}
			else
				fileContent += "\n";

		}

		fileContent += "]";

		String directory = System.getProperty("user.dir") + getWorkingdir();
		String grammarPath = directory + "\\mygrammar.grm";
		
		// file �뜝�룞�삕�뜝��
		try {
			PrintWriter writer = new PrintWriter(grammarPath);
			writer.println(fileContent);
			writer.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String grammarRulesPath = directory + "/grammar_rules.txt";
		String actionTablePath = directory + "/action_table.txt";
		String gotoTablePath = directory + "/goto_table.txt";
		
		ProcessBuilder pb = new ProcessBuilder("cmd", "/c",
				directory + "/genlrparser-exe.exe",
				"\"" + grammarPath + "\" -output \""
						+ grammarRulesPath + "\" \"" + actionTablePath + "\" \"" + gotoTablePath + "\"");
		try {
			System.out.println("genlrparser is starting...");
			Process p = pb.start();

			System.out.println("Waiting for genlrparser...");
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String readStr = reader.readLine();
			System.out.println("genlrparser: " + readStr);
			if (readStr.equals("Done"))
				readInitialize();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ParseState get_st(ParseState current_state, String index, ArrayList<Terminal<Token>> Tokens)
			throws FileNotFoundException, ParserException {
		int count = 0;

		String start_state;
		int location = 0;

		while (count < goto_table.size()) {
			String[] st_tr_arr = goto_table.get(count).split("[\t ]");
			while (location < st_tr_arr.length) {
				start_state = st_tr_arr[location];
				if (current_state.toString().equals(start_state)) {
					location++;
					if (st_tr_arr[location].equals(index)) {
						location++;
						if (!st_tr_arr[location].equals(""))
							return new ParseState(st_tr_arr[location]);
						else
							break;
					}
					else
						break;
				}
				else
					break;
			}
			count++;
			location = 0;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(	"Not found in Goto Table: " + current_state + " " + index + "\n");

		// For locating the parsing error.
		int err_ch_index = -1;
		int err_line_index = -1;
		String culprit = "no hint";

		if (Tokens.isEmpty() == false) {
			Terminal<Token> t = Tokens.get(0);
			err_ch_index = t.getChIndex();
			err_line_index = t.getLineIndex();
			culprit = t.getSyntax();
		}

		throw new ParserException("Line : Char : " + "Parsing error (state not found)" + sb.toString(), culprit,
				err_line_index, err_ch_index);
	}

	private ArrayList<String> Check_state(ParseState current_state, Terminal<Token> terminal, ArrayList<Terminal<Token>> Tokens)
			throws ParserException {
		int index = 0;
		while (index < action_table.size()) {
			String action_table_str = action_table.get(index);
			// state_num Terminal [Accept | Reduce grammar_rule_num | Shift state_num]
			String[] data = action_table_str.split("[\t ]");
			ArrayList<String> return_data = new ArrayList<>();

			if (current_state.toString().equals(data[0])) {
				int index1 = 1;
				Token index_Token;

				while (data[index1].equals("") || data[index1].equals("\t")) {
					index1++;
					continue;
				}

//				index_Token = Token.findToken(data[index1]);
				index_Token = terminal.getToken().toToken(data[index1]);

				if (terminal.getToken() == index_Token) {
					for (int i = index1 + 1; i < data.length; i++) {
						if (data[i].equals(""))
							continue;

						return_data.add(data[i]);
					}
					return return_data;
				}
			}
			index++;
		}

		StringBuilder sb = new StringBuilder();

		sb.append("Unexpected terminal " + terminal.toString()
				+ " at " + current_state.toString() + " in the action table.");
		sb.append("\n");
//
//		sb.append("but didn't found at Action Table... Plz Check it");
//		sb.append("\n");
//		sb.append("[" + terminal.getLineIndex() + "," + terminal.getChIndex() + "]");
//		sb.append("\n");

		// For locating the parsing error.
		int err_ch_index = -1;
		int err_line_index = -1;
		String culprit = "no hint";

		if (Tokens.isEmpty() == false) {
			Terminal<Token> t = Tokens.get(0);
			err_ch_index = t.getChIndex();
			err_line_index = t.getLineIndex();
			culprit = t.getSyntax();
		}

		throw new ParserException("Line " + terminal.getLineIndex() + " : Char " + terminal.getChIndex() + " : "
				+ "Parsing error " + sb.toString(), culprit, err_line_index, err_ch_index);
	}

	public String getWorkingdir() {
		return workingdir;
	}

	public void setWorkingdir(String workingdir) {
		this.workingdir = workingdir;
	}
}
