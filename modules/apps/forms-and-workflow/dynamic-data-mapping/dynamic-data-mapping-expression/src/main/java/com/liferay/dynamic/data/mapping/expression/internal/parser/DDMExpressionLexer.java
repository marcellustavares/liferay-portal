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
		T__3=1, T__2=2, T__1=3, T__0=4, AND=5, OR=6, MULT=7, DIV=8, PLUS=9, MINUS=10, 
		GT=11, GE=12, LT=13, LE=14, EQ=15, NEQ=16, POW=17, NOT=18, COMMA=19, LPAREN=20, 
		RPAREN=21, IDENTIFIER=22, STRING=23, WS=24, IntegerLiteral=25, FloatingPointLiteral=26;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'"
	};
	public static final String[] ruleNames = {
		"T__3", "T__2", "T__1", "T__0", "AND", "OR", "MULT", "DIV", "PLUS", "MINUS", 
		"GT", "GE", "LT", "LE", "EQ", "NEQ", "POW", "NOT", "COMMA", "LPAREN", 
		"RPAREN", "IDENTIFIER", "STRING", "WS", "IntegerLiteral", "DecimalIntegerLiteral", 
		"IntegerTypeSuffix", "DecimalNumeral", "Digits", "Digit", "NonZeroDigit", 
		"DigitOrUnderscore", "Underscores", "FloatingPointLiteral", "DecimalFloatingPointLiteral", 
		"ExponentPart", "ExponentIndicator", "SignedInteger", "Sign", "FloatTypeSuffix"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\34\u0102\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\3\2\3\2\3"+
		"\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f"+
		"\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\7\27\u0094"+
		"\n\27\f\27\16\27\u0097\13\27\3\30\3\30\7\30\u009b\n\30\f\30\16\30\u009e"+
		"\13\30\3\30\3\30\3\31\6\31\u00a3\n\31\r\31\16\31\u00a4\3\31\3\31\3\32"+
		"\3\32\3\33\3\33\5\33\u00ad\n\33\3\34\3\34\3\35\3\35\3\35\5\35\u00b4\n"+
		"\35\3\35\3\35\3\35\5\35\u00b9\n\35\5\35\u00bb\n\35\3\36\3\36\7\36\u00bf"+
		"\n\36\f\36\16\36\u00c2\13\36\3\36\5\36\u00c5\n\36\3\37\3\37\5\37\u00c9"+
		"\n\37\3 \3 \3!\3!\5!\u00cf\n!\3\"\6\"\u00d2\n\"\r\"\16\"\u00d3\3#\3#\3"+
		"$\3$\3$\5$\u00db\n$\3$\5$\u00de\n$\3$\5$\u00e1\n$\3$\3$\3$\5$\u00e6\n"+
		"$\3$\5$\u00e9\n$\3$\3$\3$\5$\u00ee\n$\3$\3$\3$\5$\u00f3\n$\3%\3%\3%\3"+
		"&\3&\3\'\5\'\u00fb\n\'\3\'\3\'\3(\3(\3)\3)\2\2*\3\3\5\4\7\5\t\6\13\7\r"+
		"\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25"+
		")\26+\27-\30/\31\61\32\63\33\65\2\67\29\2;\2=\2?\2A\2C\2E\34G\2I\2K\2"+
		"M\2O\2Q\2\3\2\13\5\2C\\aac|\6\2\62;C\\aac|\4\2$$^^\5\2\13\f\16\17\"\""+
		"\4\2NNnn\3\2\63;\4\2GGgg\4\2--//\6\2FFHHffhh\u0109\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2E\3\2\2\2\3S\3\2\2\2\5X\3\2\2\2\7^\3\2\2\2\tc\3\2\2\2\13i\3\2"+
		"\2\2\rl\3\2\2\2\17o\3\2\2\2\21q\3\2\2\2\23s\3\2\2\2\25u\3\2\2\2\27w\3"+
		"\2\2\2\31y\3\2\2\2\33|\3\2\2\2\35~\3\2\2\2\37\u0081\3\2\2\2!\u0084\3\2"+
		"\2\2#\u0087\3\2\2\2%\u0089\3\2\2\2\'\u008b\3\2\2\2)\u008d\3\2\2\2+\u008f"+
		"\3\2\2\2-\u0091\3\2\2\2/\u0098\3\2\2\2\61\u00a2\3\2\2\2\63\u00a8\3\2\2"+
		"\2\65\u00aa\3\2\2\2\67\u00ae\3\2\2\29\u00ba\3\2\2\2;\u00bc\3\2\2\2=\u00c8"+
		"\3\2\2\2?\u00ca\3\2\2\2A\u00ce\3\2\2\2C\u00d1\3\2\2\2E\u00d5\3\2\2\2G"+
		"\u00f2\3\2\2\2I\u00f4\3\2\2\2K\u00f7\3\2\2\2M\u00fa\3\2\2\2O\u00fe\3\2"+
		"\2\2Q\u0100\3\2\2\2ST\7v\2\2TU\7t\2\2UV\7w\2\2VW\7g\2\2W\4\3\2\2\2XY\7"+
		"H\2\2YZ\7C\2\2Z[\7N\2\2[\\\7U\2\2\\]\7G\2\2]\6\3\2\2\2^_\7V\2\2_`\7T\2"+
		"\2`a\7W\2\2ab\7G\2\2b\b\3\2\2\2cd\7h\2\2de\7c\2\2ef\7n\2\2fg\7u\2\2gh"+
		"\7g\2\2h\n\3\2\2\2ij\7(\2\2jk\7(\2\2k\f\3\2\2\2lm\7~\2\2mn\7~\2\2n\16"+
		"\3\2\2\2op\7,\2\2p\20\3\2\2\2qr\7\61\2\2r\22\3\2\2\2st\7-\2\2t\24\3\2"+
		"\2\2uv\7/\2\2v\26\3\2\2\2wx\7@\2\2x\30\3\2\2\2yz\7@\2\2z{\7?\2\2{\32\3"+
		"\2\2\2|}\7>\2\2}\34\3\2\2\2~\177\7>\2\2\177\u0080\7?\2\2\u0080\36\3\2"+
		"\2\2\u0081\u0082\7?\2\2\u0082\u0083\7?\2\2\u0083 \3\2\2\2\u0084\u0085"+
		"\7#\2\2\u0085\u0086\7?\2\2\u0086\"\3\2\2\2\u0087\u0088\7`\2\2\u0088$\3"+
		"\2\2\2\u0089\u008a\7#\2\2\u008a&\3\2\2\2\u008b\u008c\7.\2\2\u008c(\3\2"+
		"\2\2\u008d\u008e\7*\2\2\u008e*\3\2\2\2\u008f\u0090\7+\2\2\u0090,\3\2\2"+
		"\2\u0091\u0095\t\2\2\2\u0092\u0094\t\3\2\2\u0093\u0092\3\2\2\2\u0094\u0097"+
		"\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096.\3\2\2\2\u0097"+
		"\u0095\3\2\2\2\u0098\u009c\7$\2\2\u0099\u009b\n\4\2\2\u009a\u0099\3\2"+
		"\2\2\u009b\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d"+
		"\u009f\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a0\7$\2\2\u00a0\60\3\2\2\2"+
		"\u00a1\u00a3\t\5\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a2"+
		"\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\b\31\2\2"+
		"\u00a7\62\3\2\2\2\u00a8\u00a9\5\65\33\2\u00a9\64\3\2\2\2\u00aa\u00ac\5"+
		"9\35\2\u00ab\u00ad\5\67\34\2\u00ac\u00ab\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"+
		"\66\3\2\2\2\u00ae\u00af\t\6\2\2\u00af8\3\2\2\2\u00b0\u00bb\7\62\2\2\u00b1"+
		"\u00b8\5? \2\u00b2\u00b4\5;\36\2\u00b3\u00b2\3\2\2\2\u00b3\u00b4\3\2\2"+
		"\2\u00b4\u00b9\3\2\2\2\u00b5\u00b6\5C\"\2\u00b6\u00b7\5;\36\2\u00b7\u00b9"+
		"\3\2\2\2\u00b8\u00b3\3\2\2\2\u00b8\u00b5\3\2\2\2\u00b9\u00bb\3\2\2\2\u00ba"+
		"\u00b0\3\2\2\2\u00ba\u00b1\3\2\2\2\u00bb:\3\2\2\2\u00bc\u00c4\5=\37\2"+
		"\u00bd\u00bf\5A!\2\u00be\u00bd\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be"+
		"\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c3\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3"+
		"\u00c5\5=\37\2\u00c4\u00c0\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5<\3\2\2\2"+
		"\u00c6\u00c9\7\62\2\2\u00c7\u00c9\5? \2\u00c8\u00c6\3\2\2\2\u00c8\u00c7"+
		"\3\2\2\2\u00c9>\3\2\2\2\u00ca\u00cb\t\7\2\2\u00cb@\3\2\2\2\u00cc\u00cf"+
		"\5=\37\2\u00cd\u00cf\7a\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cd\3\2\2\2\u00cf"+
		"B\3\2\2\2\u00d0\u00d2\7a\2\2\u00d1\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3"+
		"\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4D\3\2\2\2\u00d5\u00d6\5G$\2\u00d6"+
		"F\3\2\2\2\u00d7\u00d8\5;\36\2\u00d8\u00da\7\60\2\2\u00d9\u00db\5;\36\2"+
		"\u00da\u00d9\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dd\3\2\2\2\u00dc\u00de"+
		"\5I%\2\u00dd\u00dc\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e0\3\2\2\2\u00df"+
		"\u00e1\5Q)\2\u00e0\u00df\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00f3\3\2\2"+
		"\2\u00e2\u00e3\7\60\2\2\u00e3\u00e5\5;\36\2\u00e4\u00e6\5I%\2\u00e5\u00e4"+
		"\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e8\3\2\2\2\u00e7\u00e9\5Q)\2\u00e8"+
		"\u00e7\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00f3\3\2\2\2\u00ea\u00eb\5;"+
		"\36\2\u00eb\u00ed\5I%\2\u00ec\u00ee\5Q)\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee"+
		"\3\2\2\2\u00ee\u00f3\3\2\2\2\u00ef\u00f0\5;\36\2\u00f0\u00f1\5Q)\2\u00f1"+
		"\u00f3\3\2\2\2\u00f2\u00d7\3\2\2\2\u00f2\u00e2\3\2\2\2\u00f2\u00ea\3\2"+
		"\2\2\u00f2\u00ef\3\2\2\2\u00f3H\3\2\2\2\u00f4\u00f5\5K&\2\u00f5\u00f6"+
		"\5M\'\2\u00f6J\3\2\2\2\u00f7\u00f8\t\b\2\2\u00f8L\3\2\2\2\u00f9\u00fb"+
		"\5O(\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc"+
		"\u00fd\5;\36\2\u00fdN\3\2\2\2\u00fe\u00ff\t\t\2\2\u00ffP\3\2\2\2\u0100"+
		"\u0101\t\n\2\2\u0101R\3\2\2\2\27\2\u0095\u009c\u00a4\u00ac\u00b3\u00b8"+
		"\u00ba\u00c0\u00c4\u00c8\u00ce\u00d3\u00da\u00dd\u00e0\u00e5\u00e8\u00ed"+
		"\u00f2\u00fa\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
