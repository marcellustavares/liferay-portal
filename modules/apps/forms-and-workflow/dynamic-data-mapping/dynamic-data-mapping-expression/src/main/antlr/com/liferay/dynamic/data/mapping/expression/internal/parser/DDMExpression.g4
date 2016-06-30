grammar DDMExpression;

options {
	language = Java;
}

@header {
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
}

/* Lexer */

AND : '&&' ; //  substituir para and
OR  : '||' ; //  substituir para or

//TRUE  : 'true' ;
//FALSE : 'false' ;

MULT  : '*' ;
DIV   : '/' ;
PLUS  : '+' ;
MINUS : '-' ;

GT : '>' ;
GE : '>=' ;
LT : '<' ;
LE : '<=' ;
EQ : '==' ; //  substituir para =
NEQ : '!=' ; //  substituir para <>
POW : '^' ;

NOT : '!' ; //  substituir para not
COMMA : ',' ;

LPAREN : '(' ;
RPAREN : ')' ;

//DECIMAL : [0-9]+('.'[0-9]+)? ;
IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]* ;
STRING : '"' (~[\\"])* '"';

WS : [ \r\t\u000C\n]+ -> skip ;


/* Parser */

expression
	: logicalOrExpression EOF
	//: plusOrMinus EOF
	;
logicalOrExpression
	: logicalOrExpression OR logicalAndExpression # Or
	| logicalAndExpression # ToLogicAnd
	;

logicalAndExpression
	: logicalAndExpression AND equalityExpression #And
	| equalityExpression # ToEquality
	;

equalityExpression
	: equalityExpression EQ comparison_expr # Eq
	| equalityExpression NEQ comparison_expr # NEQ
	| comparison_expr #Tocomparison
	;

comparison_expr
	: comparison_expr GT plusOrMinus # Greater
	| comparison_expr GE plusOrMinus # GreaterThanEQ
	| comparison_expr LT plusOrMinus # LessThan
	| comparison_expr LE plusOrMinus # LessThanEq
	| unaryNot #ToUnaryNot
	;

unaryNot
	: NOT unaryNot # ChangeBoolean
	| primaryBooleanExpression # PrimaryBoolean
	;

primaryBooleanExpression
	: logical_entity #ToLogicalEntity
	| plusOrMinus #ToPlus
	| LPAREN logicalOrExpression RPAREN #BooleanParen
	;

plusOrMinus
 	: plusOrMinus PLUS multOrDiv    # Plus
    | plusOrMinus MINUS multOrDiv   # Minus
    | multOrDiv         # ToMultOrDiv
    ;

multOrDiv
    : multOrDiv MULT unaryMinus # Multiplication
    | multOrDiv DIV unaryMinus  # Division
    | unaryMinus  # ToUnary
    ;

//pow
//    : unaryMinus (POW pow)? # Power
//    ;

unaryMinus
    : MINUS unaryMinus # ChangeSign
    | primaryExpression # Primary
    ;

functionCall
	: functionName=IDENTIFIER LPAREN functionParams? RPAREN
	;

functionParams
	: logicalOrExpression (COMMA logicalOrExpression)*
	;

primaryExpression
	: numeric_entity # Atom
	| functionCall #Function
	| LPAREN plusOrMinus RPAREN # Paren
	;

logical_entity : ('true' | 'TRUE' | 'false' | 'FALSE') # LogicalConst
               | IDENTIFIER     # LogicalVariable
               ;

numeric_entity //: DECIMAL              # NumericConst
			   : literal #ToLiteral
               | IDENTIFIER           # NumericVariable
               //| STRING				 # StringConst
               ;


literal
	:FloatingPointLiteral # Double
	| IntegerLiteral # Integer
	| STRING # String
	;

IntegerLiteral
    :   DecimalIntegerLiteral
    ;

fragment
DecimalIntegerLiteral
    :   DecimalNumeral IntegerTypeSuffix?
    ;

fragment
IntegerTypeSuffix
    :   [lL]
    ;

fragment
DecimalNumeral
    :   '0'
    |   NonZeroDigit (Digits? | Underscores Digits)
    ;


fragment
Digits
    :   Digit (DigitOrUnderscore* Digit)?
    ;

fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

fragment
DigitOrUnderscore
    :   Digit
    |   '_'
    ;

fragment
Underscores
    :   '_'+
    ;

FloatingPointLiteral
    :   DecimalFloatingPointLiteral
    ;

fragment
DecimalFloatingPointLiteral
    :   Digits '.' Digits? ExponentPart? FloatTypeSuffix?
    |   '.' Digits ExponentPart? FloatTypeSuffix?
    |   Digits ExponentPart FloatTypeSuffix?
    |   Digits FloatTypeSuffix
    ;

fragment
ExponentPart
    :   ExponentIndicator SignedInteger
    ;

fragment
ExponentIndicator
    :   [eE]
    ;

fragment
SignedInteger
    :   Sign? Digits
    ;

fragment
Sign
    :   [+-]
    ;

fragment
FloatTypeSuffix
    :   [fFdD]
    ;