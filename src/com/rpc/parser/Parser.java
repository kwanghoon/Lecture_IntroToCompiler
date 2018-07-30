package com.rpc.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Parser {
	private ArrayList<Terminal> lexer;
	private Token currentTok;
	private Stack<Stkelem> stack;

	private ArrayList<String> action_table;
	private ArrayList<String> goto_table;
	private HashMap<Integer, String> grammar_rules;

	public Parser(LexicalAnalyzer lexicalAnalyzer) throws IOException, LexerException {
		lexer = lexicalAnalyzer.Lexing();
		stack = new Stack<>();

		action_table = new ArrayList<>();
		goto_table = new ArrayList<>();
		grammar_rules = new HashMap<>();

		readInitialize();
	}

	public void readInitialize() throws IOException {
		FileReader grammarFReader = new FileReader("grammar_rules.txt");
		FileReader actionFReader = new FileReader("action_table.txt");
		FileReader gotoFReader = new FileReader("goto_table.txt");

		BufferedReader grammarBReader = new BufferedReader(grammarFReader);
		BufferedReader actionBReader = new BufferedReader(actionFReader);
		BufferedReader gotoBReader = new BufferedReader(gotoFReader);

		String tmpLine;

		while ((tmpLine = grammarBReader.readLine()) != null) {
			// grammerNumber: grammer
			String[] arr = tmpLine.split(":");

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
	}

	public Expr Parsing() throws FileNotFoundException, ParserException {
		stack.clear();
		stack.push(new ParseState("0"));
		Object tree = null;

		while (!lexer.isEmpty()) {
			ParseState currentState = (ParseState) stack.lastElement();
			Terminal currentTerminal = lexer.get(0);

			ArrayList<String> data_arr = Check_state(currentState, currentTerminal, lexer);
			String order = data_arr.get(0); // Accept | Reduce | Shift

			switch (order) {
			case "Accept":
				lexer.remove(0);
				return (Expr) ((Nonterminal) stack.get(1)).getTree();
			case "Shift":
				String state = data_arr.get(1);
				stack.push(currentTerminal);
				stack.push(new ParseState(state));
				lexer.remove(0);
				break;
			case "Reduce":
				int grammar_rule_num = Integer.parseInt(data_arr.get(1));

				String[] reduce_arr = grammar_rules.get(grammar_rule_num).split("->");
				String lhs = reduce_arr[0].trim();
				String rhs;
				int rhsCount;

				if (!(reduce_arr.length == 1)) // -> "empty"
				{
					rhs = reduce_arr[1].trim();
					rhsCount = rhs.split(" ").length; // *2 => pop count.
				}
				else {
					rhs = "";
					rhsCount = 0;
				}

				int last_stack_tree_index = stack.size() - 1;

				if (grammar_rules.get(grammar_rule_num).equals("E' -> E")) {
					Nonterminal E = (Nonterminal) stack.get(last_stack_tree_index - 1);
					tree = E.getTree();
				}
				else if (grammar_rules.get(grammar_rule_num).equals("E -> lam T T . T")) {
					
				}
				else if (grammar_rules.get(grammar_rule_num).equals("E -> T")) {
					Nonterminal T = (Nonterminal) stack.get(last_stack_tree_index - 1);
					tree = T.getTree();
				}
				else if (grammar_rules.get(grammar_rule_num).equals("T -> ( E )")) {
					Nonterminal E = (Nonterminal) stack.get(last_stack_tree_index - 3);
					tree = E.getTree();
				}
				else if (grammar_rules.get(grammar_rule_num).equals("T -> id")) {
					Terminal idTerminal = (Terminal) stack.get(last_stack_tree_index - 1);
					Identifier id = new Identifier(idTerminal.getSyntax());
					tree = id;
				}
				else if (grammar_rules.get(grammar_rule_num).equals("T -> num")) {
					Terminal num = (Terminal) stack.get(last_stack_tree_index - 1);
					NumValue numValue = new NumValue(Double.parseDouble(num.getSyntax()));
					tree = numValue;
				}
				else {
					throw new ParserException("Unexpected grammar rule " + grammar_rule_num);
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

	// Common Function

	private ParseState get_st(ParseState current_state, String index, ArrayList<Terminal> Tokens)
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

		sb.append(
				"Expect Trans Table content is \"" + current_state.toString() + " " + index + " <Destination State>\"");
		sb.append("\n");
		sb.append("but didn't found at Trans Table... Plz Check it");
		sb.append("\n");

		// For locating the parsing error.
		int err_ch_index = -1;
		int err_line_index = -1;
		String culprit = "no hint";

		if (Tokens.isEmpty() == false) {
			Terminal t = Tokens.get(0);
			err_ch_index = t.getChIndex();
			err_line_index = t.getLineIndex();
			culprit = t.getSyntax();
		}

		throw new ParserException("Line : Char : " + "Parsing error (state not found)" + sb.toString(), culprit,
				err_line_index, err_ch_index);
	}

	private ArrayList<String> Check_state(ParseState current_state, Terminal terminal, ArrayList<Terminal> Tokens)
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

				index_Token = Token.findToken(data[index1]);

				if (terminal.getToken() == index_Token) {
					String return_string = new String();
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

		sb.append("Expect Parsing table content is \"" + current_state.toString() + " " + terminal.toString()
				+ " <Shift/Reduce/Accecpt>\"");
		sb.append("\n");

		sb.append("but didn't found at Parsing Table... Plz Check it");
		sb.append("\n");
		sb.append("[" + terminal.getLineIndex() + "," + terminal.getChIndex() + "]");
		sb.append("\n");

		// For locating the parsing error.
		int err_ch_index = -1;
		int err_line_index = -1;
		String culprit = "no hint";

		if (Tokens.isEmpty() == false) {
			Terminal t = Tokens.get(0);
			err_ch_index = t.getChIndex();
			err_line_index = t.getLineIndex();
			culprit = t.getSyntax();
		}

		throw new ParserException("Line " + terminal.getLineIndex() + " : Char " + terminal.getChIndex() + " : "
				+ "Parsing error " + sb.toString(), culprit, err_line_index, err_ch_index);
	}
}
