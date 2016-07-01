// Generated from DDMExpression.g4 by ANTLR 4.3

/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.expression.internal.parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DDMExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__3=1, T__2=2, T__1=3, T__0=4, IntegerLiteral=5, FloatingPointLiteral=6, 
		DecimalFloatingPointLiteral=7, AND=8, COMMA=9, DIV=10, EQ=11, GE=12, GT=13, 
		IDENTIFIER=14, LE=15, LPAREN=16, LT=17, MINUS=18, MULT=19, NEQ=20, NOT=21, 
		OR=22, PLUS=23, POW=24, RPAREN=25, STRING=26, WS=27;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'", "'\\u001B'"
	};
	public static final String[] ruleNames = {
		"T__3", "T__2", "T__1", "T__0", "IntegerLiteral", "FloatingPointLiteral", 
		"DecimalFloatingPointLiteral", "AND", "COMMA", "DIV", "EQ", "GE", "GT", 
		"IDENTIFIER", "LE", "LPAREN", "LT", "MINUS", "MULT", "NEQ", "NOT", "OR", 
		"PLUS", "POW", "RPAREN", "STRING", "WS", "Digits", "Digit", "IntegerSuffix", 
		"FloatSuffix"
	};


	public DDMExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DDMExpression.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\35\u00b6\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\6\3\6\5\6Z\n\6\3\7\3\7\3\b\3\b\3\b\5\ba\n\b\3\b"+
		"\5\bd\n\b\3\b\3\b\3\b\5\bi\n\b\5\bk\n\b\3\t\3\t\3\t\3\n\3\n\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\7\17~\n\17\f\17\16\17\u0081"+
		"\13\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33"+
		"\7\33\u009e\n\33\f\33\16\33\u00a1\13\33\3\33\3\33\3\34\6\34\u00a6\n\34"+
		"\r\34\16\34\u00a7\3\34\3\34\3\35\6\35\u00ad\n\35\r\35\16\35\u00ae\3\36"+
		"\3\36\3\37\3\37\3 \3 \2\2!\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32"+
		"\63\33\65\34\67\359\2;\2=\2?\2\3\2\t\5\2C\\aac|\6\2\62;C\\aac|\4\2$$^"+
		"^\5\2\13\f\16\17\"\"\3\2\62;\4\2NNnn\6\2FFHHffhh\u00ba\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2"+
		"\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33"+
		"\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2"+
		"\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2"+
		"\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\3A\3\2\2\2\5F\3\2\2\2\7L\3\2\2\2"+
		"\tQ\3\2\2\2\13W\3\2\2\2\r[\3\2\2\2\17j\3\2\2\2\21l\3\2\2\2\23o\3\2\2\2"+
		"\25q\3\2\2\2\27s\3\2\2\2\31v\3\2\2\2\33y\3\2\2\2\35{\3\2\2\2\37\u0082"+
		"\3\2\2\2!\u0085\3\2\2\2#\u0087\3\2\2\2%\u0089\3\2\2\2\'\u008b\3\2\2\2"+
		")\u008d\3\2\2\2+\u0090\3\2\2\2-\u0092\3\2\2\2/\u0095\3\2\2\2\61\u0097"+
		"\3\2\2\2\63\u0099\3\2\2\2\65\u009b\3\2\2\2\67\u00a5\3\2\2\29\u00ac\3\2"+
		"\2\2;\u00b0\3\2\2\2=\u00b2\3\2\2\2?\u00b4\3\2\2\2AB\7v\2\2BC\7t\2\2CD"+
		"\7w\2\2DE\7g\2\2E\4\3\2\2\2FG\7H\2\2GH\7C\2\2HI\7N\2\2IJ\7U\2\2JK\7G\2"+
		"\2K\6\3\2\2\2LM\7V\2\2MN\7T\2\2NO\7W\2\2OP\7G\2\2P\b\3\2\2\2QR\7h\2\2"+
		"RS\7c\2\2ST\7n\2\2TU\7u\2\2UV\7g\2\2V\n\3\2\2\2WY\59\35\2XZ\5=\37\2YX"+
		"\3\2\2\2YZ\3\2\2\2Z\f\3\2\2\2[\\\5\17\b\2\\\16\3\2\2\2]^\59\35\2^`\7\60"+
		"\2\2_a\59\35\2`_\3\2\2\2`a\3\2\2\2ac\3\2\2\2bd\5? \2cb\3\2\2\2cd\3\2\2"+
		"\2dk\3\2\2\2ef\7\60\2\2fh\59\35\2gi\5? \2hg\3\2\2\2hi\3\2\2\2ik\3\2\2"+
		"\2j]\3\2\2\2je\3\2\2\2k\20\3\2\2\2lm\7(\2\2mn\7(\2\2n\22\3\2\2\2op\7."+
		"\2\2p\24\3\2\2\2qr\7\61\2\2r\26\3\2\2\2st\7?\2\2tu\7?\2\2u\30\3\2\2\2"+
		"vw\7@\2\2wx\7?\2\2x\32\3\2\2\2yz\7@\2\2z\34\3\2\2\2{\177\t\2\2\2|~\t\3"+
		"\2\2}|\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080\36"+
		"\3\2\2\2\u0081\177\3\2\2\2\u0082\u0083\7>\2\2\u0083\u0084\7?\2\2\u0084"+
		" \3\2\2\2\u0085\u0086\7*\2\2\u0086\"\3\2\2\2\u0087\u0088\7>\2\2\u0088"+
		"$\3\2\2\2\u0089\u008a\7/\2\2\u008a&\3\2\2\2\u008b\u008c\7,\2\2\u008c("+
		"\3\2\2\2\u008d\u008e\7#\2\2\u008e\u008f\7?\2\2\u008f*\3\2\2\2\u0090\u0091"+
		"\7#\2\2\u0091,\3\2\2\2\u0092\u0093\7~\2\2\u0093\u0094\7~\2\2\u0094.\3"+
		"\2\2\2\u0095\u0096\7-\2\2\u0096\60\3\2\2\2\u0097\u0098\7`\2\2\u0098\62"+
		"\3\2\2\2\u0099\u009a\7+\2\2\u009a\64\3\2\2\2\u009b\u009f\7$\2\2\u009c"+
		"\u009e\n\4\2\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2\2\u009f\u009d\3\2"+
		"\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2\2\2\u00a2"+
		"\u00a3\7$\2\2\u00a3\66\3\2\2\2\u00a4\u00a6\t\5\2\2\u00a5\u00a4\3\2\2\2"+
		"\u00a6\u00a7\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a9"+
		"\3\2\2\2\u00a9\u00aa\b\34\2\2\u00aa8\3\2\2\2\u00ab\u00ad\5;\36\2\u00ac"+
		"\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2"+
		"\2\2\u00af:\3\2\2\2\u00b0\u00b1\t\6\2\2\u00b1<\3\2\2\2\u00b2\u00b3\t\7"+
		"\2\2\u00b3>\3\2\2\2\u00b4\u00b5\t\b\2\2\u00b5@\3\2\2\2\f\2Y`chj\177\u009f"+
		"\u00a7\u00ae\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
