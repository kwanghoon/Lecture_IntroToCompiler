package org.swlab.lib.parser.examples.solidity.test;

//import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;

//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.swlab.lib.parser.LexerException;
import org.swlab.lib.parser.ParserException;
import org.swlab.lib.parser.examples.solidity.Parser;

import static junit.framework.TestCase.assertTrue;

class ParserTest {

	static final String tokensAlpha = "address anonymous as assembly bool break byte constant"
			+ "continue contract days delete do else enum ether event external false"
			+ "finney fixed for from function hours if import indexed int interface"
			+ "internal is let library mapping memory minutes modifier new payable"
			+ "pragma private public pure return returns seconds storage struct szabo"
			+ "throw using var view weeks wei while years";
	
	static final String tokensSymbol = "! != % %= && & &= ( ) * ** *= + ++ += , - -- -= . /"
			+ "/= : := ; < << <<= <= = == => > >= >>= ? [ ] ^ ^= _ { } | |= || ~";
			
	static final String tokensDecimal = "0 +0 -0 +123 -123 123"; 
	
	static final String tokensIdentifier = "i x123 hello $x $1 _x _1 $xy x$y xy$"
			+ "_xy x_y xy_  __";
			
	static final String tokensString = "\"hello\"  \"\" \"hello world\\n\" ";
	
	static final String tokensHex = 
			"hex\"1122FF\" hex'1122FF' 0x1122FFAA 0x1abCdEf 0xf ";
	
	static final String tokensIntUintByte = 
			"int int8 int16 int48 int160 int232 int256 "
			+ "uint uint24 uint112 uint208 uint256 "
			+ "byte bytes bytes1 bytes2 bytes3 bytes4 bytes5 bytes6 bytes7 bytes8 "
			+ "bytes9 bytes10 bytes11 bytes12 bytes13 bytes14 bytes15 bytes16 "
			+ "bytes17 bytes18 bytes19 bytes20 bytes21 bytes22 bytes23 bytes24 "
			+ "bytes25 bytes26 bytes27 bytes28 bytes29 bytes30 bytes31 bytes32 ";
	
	static final String tokensPragma =
			"pragma solidity ^0.4.25;";
	
	static final String idwithkeywordprefix =
			"addressOf address:0x123456789ABCDEF";
	
	@Test
	void lexical_analsis_test1() throws IOException, LexerException, ParserException {
		StringReader sr = new StringReader(
			tokensAlpha + tokensSymbol + tokensDecimal + tokensIdentifier
			+ tokensString + tokensHex + tokensIntUintByte + tokensPragma
			+ idwithkeywordprefix);
		Parser parser = new Parser();
		
		parser.Lexing(sr,true);
		
		assertTrue(true);
	}
}
