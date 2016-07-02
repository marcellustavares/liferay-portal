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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;
@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DDMExpressionLexer extends Lexer {

	static {RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int IntegerLiteral =
		1, FloatingPointLiteral = 2, DecimalFloatingPointLiteral = 3, AND = 4,
		COMMA = 5, DIV = 6, EQ = 7, FALSE = 8, GE = 9, GT = 10, LE = 11,
		LPAREN = 12, LT=13, MINUS = 14, MULT = 15, NEQ = 16, NOT = 17, OR = 18,
		PLUS = 19, RPAREN = 20, STRING = 21, TRUE = 22, IDENTIFIER = 23,
		WS = 24;
	public static String[] modeNames = {"DEFAULT_MODE"};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'",
		"'\\u0005'", "'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'",
		"'\\u000B'", "'\f'", "'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'",
		"'\\u0011'", "'\\u0012'",
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'"
	};
	public static final String[] ruleNames = {
		"IntegerLiteral", "FloatingPointLiteral", "DecimalFloatingPointLiteral",
		"AND", "COMMA", "DIV", "EQ", "FALSE", "GE", "GT", "LE", "LPAREN", "LT",
		"MINUS", "MULT", "NEQ", "NOT", "OR", "PLUS", "RPAREN", "STRING", "TRUE",
		"IDENTIFIER", "WS", "Digits", "Digit", "IntegerSuffix", "FloatSuffix"
	};

	public DDMExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(
			this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	@Override
	public String getGrammarFileName() {return "DDMExpression.g4"; }

	@Override
	public String[] getTokenNames() {return tokenNames; }

	@Override
	public String[] getRuleNames() {return ruleNames; }

	@Override
	public String getSerializedATN() {return _serializedATN; }

	@Override
	public String[] getModeNames() {return modeNames; }

	@Override
	public ATN getATN() {return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\32\u00c7\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\5\2>\n\2\3\3\3\3"+
		"\3\4\3\4\3\4\5\4E\n\4\3\4\5\4H\n\4\3\4\3\4\3\4\5\4M\n\4\5\4O\n\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5Z\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\5"+
		"\bc\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\to\n\t\3\n\3\n\3\n\3"+
		"\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3"+
		"\21\3\21\5\21\u0085\n\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u008d\n\22"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u0096\n\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\7\26\u009e\n\26\f\26\16\26\u00a1\13\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u00ad\n\27\3\30\3\30\7\30\u00b1\n"+
		"\30\f\30\16\30\u00b4\13\30\3\31\6\31\u00b7\n\31\r\31\16\31\u00b8\3\31"+
		"\3\31\3\32\6\32\u00be\n\32\r\32\16\32\u00bf\3\33\3\33\3\34\3\34\3\35\3"+
		"\35\2\2\36\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\2\65\2\67\2"+
		"9\2\3\2\t\4\2$$^^\5\2C\\aac|\6\2\62;C\\aac|\5\2\13\f\16\17\"\"\3\2\62"+
		";\4\2NNnn\6\2FFHHffhh\u00d6\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37"+
		"\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3"+
		"\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\3;\3\2\2\2\5?\3\2\2\2\7N\3\2"+
		"\2\2\tY\3\2\2\2\13[\3\2\2\2\r]\3\2\2\2\17b\3\2\2\2\21n\3\2\2\2\23p\3\2"+
		"\2\2\25s\3\2\2\2\27u\3\2\2\2\31x\3\2\2\2\33z\3\2\2\2\35|\3\2\2\2\37~\3"+
		"\2\2\2!\u0084\3\2\2\2#\u008c\3\2\2\2%\u0095\3\2\2\2\'\u0097\3\2\2\2)\u0099"+
		"\3\2\2\2+\u009b\3\2\2\2-\u00ac\3\2\2\2/\u00ae\3\2\2\2\61\u00b6\3\2\2\2"+
		"\63\u00bd\3\2\2\2\65\u00c1\3\2\2\2\67\u00c3\3\2\2\29\u00c5\3\2\2\2;=\5"+
		"\63\32\2<>\5\67\34\2=<\3\2\2\2=>\3\2\2\2>\4\3\2\2\2?@\5\7\4\2@\6\3\2\2"+
		"\2AB\5\63\32\2BD\7\60\2\2CE\5\63\32\2DC\3\2\2\2DE\3\2\2\2EG\3\2\2\2FH"+
		"\59\35\2GF\3\2\2\2GH\3\2\2\2HO\3\2\2\2IJ\7\60\2\2JL\5\63\32\2KM\59\35"+
		"\2LK\3\2\2\2LM\3\2\2\2MO\3\2\2\2NA\3\2\2\2NI\3\2\2\2O\b\3\2\2\2PQ\7(\2"+
		"\2QZ\7(\2\2RZ\7(\2\2ST\7c\2\2TU\7p\2\2UZ\7f\2\2VW\7C\2\2WX\7P\2\2XZ\7"+
		"F\2\2YP\3\2\2\2YR\3\2\2\2YS\3\2\2\2YV\3\2\2\2Z\n\3\2\2\2[\\\7.\2\2\\\f"+
		"\3\2\2\2]^\7\61\2\2^\16\3\2\2\2_`\7?\2\2`c\7?\2\2ac\7?\2\2b_\3\2\2\2b"+
		"a\3\2\2\2c\20\3\2\2\2de\7h\2\2ef\7c\2\2fg\7n\2\2gh\7u\2\2ho\7g\2\2ij\7"+
		"H\2\2jk\7C\2\2kl\7N\2\2lm\7U\2\2mo\7G\2\2nd\3\2\2\2ni\3\2\2\2o\22\3\2"+
		"\2\2pq\7@\2\2qr\7?\2\2r\24\3\2\2\2st\7@\2\2t\26\3\2\2\2uv\7>\2\2vw\7?"+
		"\2\2w\30\3\2\2\2xy\7*\2\2y\32\3\2\2\2z{\7>\2\2{\34\3\2\2\2|}\7/\2\2}\36"+
		"\3\2\2\2~\177\7,\2\2\177 \3\2\2\2\u0080\u0081\7#\2\2\u0081\u0085\7?\2"+
		"\2\u0082\u0083\7>\2\2\u0083\u0085\7@\2\2\u0084\u0080\3\2\2\2\u0084\u0082"+
		"\3\2\2\2\u0085\"\3\2\2\2\u0086\u0087\7p\2\2\u0087\u0088\7q\2\2\u0088\u008d"+
		"\7v\2\2\u0089\u008a\7P\2\2\u008a\u008b\7Q\2\2\u008b\u008d\7V\2\2\u008c"+
		"\u0086\3\2\2\2\u008c\u0089\3\2\2\2\u008d$\3\2\2\2\u008e\u008f\7~\2\2\u008f"+
		"\u0096\7~\2\2\u0090\u0096\7~\2\2\u0091\u0092\7q\2\2\u0092\u0096\7t\2\2"+
		"\u0093\u0094\7Q\2\2\u0094\u0096\7T\2\2\u0095\u008e\3\2\2\2\u0095\u0090"+
		"\3\2\2\2\u0095\u0091\3\2\2\2\u0095\u0093\3\2\2\2\u0096&\3\2\2\2\u0097"+
		"\u0098\7-\2\2\u0098(\3\2\2\2\u0099\u009a\7+\2\2\u009a*\3\2\2\2\u009b\u009f"+
		"\7$\2\2\u009c\u009e\n\2\2\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2"+
		"\2\2\u00a2\u00a3\7$\2\2\u00a3,\3\2\2\2\u00a4\u00a5\7v\2\2\u00a5\u00a6"+
		"\7t\2\2\u00a6\u00a7\7w\2\2\u00a7\u00ad\7g\2\2\u00a8\u00a9\7V\2\2\u00a9"+
		"\u00aa\7T\2\2\u00aa\u00ab\7W\2\2\u00ab\u00ad\7G\2\2\u00ac\u00a4\3\2\2"+
		"\2\u00ac\u00a8\3\2\2\2\u00ad.\3\2\2\2\u00ae\u00b2\t\3\2\2\u00af\u00b1"+
		"\t\4\2\2\u00b0\u00af\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2"+
		"\u00b3\3\2\2\2\u00b3\60\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b7\t\5\2"+
		"\2\u00b6\u00b5\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9"+
		"\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bb\b\31\2\2\u00bb\62\3\2\2\2\u00bc"+
		"\u00be\5\65\33\2\u00bd\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00bd\3"+
		"\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\64\3\2\2\2\u00c1\u00c2\t\6\2\2\u00c2"+
		"\66\3\2\2\2\u00c3\u00c4\t\7\2\2\u00c48\3\2\2\2\u00c5\u00c6\t\b\2\2\u00c6"+
		":\3\2\2\2\23\2=DGLNYbn\u0084\u008c\u0095\u009f\u00ac\u00b2\u00b8\u00bf"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];

		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}

}