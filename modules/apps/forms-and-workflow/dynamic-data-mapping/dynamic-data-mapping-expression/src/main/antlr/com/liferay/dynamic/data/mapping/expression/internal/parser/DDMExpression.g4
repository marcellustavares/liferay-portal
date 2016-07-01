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

expression
	: logicalOrExpression EOF
	;

logicalOrExpression
	: logicalOrExpression OR logicalAndExpression # OrExpression
	| logicalAndExpression # ToLogicalAndExpression
	;

logicalAndExpression
	: logicalAndExpression AND equalityExpression # AndExpression
	| equalityExpression # ToEqualityExpression
	;

equalityExpression
	: equalityExpression EQ comparisonExpression # EqualsExpression
	| equalityExpression NEQ comparisonExpression # NotEqualsExpression
	| comparisonExpression #ToComparisonExpression
	;

comparisonExpression
	: comparisonExpression GT additionOrSubtractionExpression # GreaterThanExpression
	| comparisonExpression GE additionOrSubtractionExpression # GreaterThanOrEqualsExpression
	| comparisonExpression LT additionOrSubtractionExpression # LessThanExpression
	| comparisonExpression LE additionOrSubtractionExpression # LessThanOrEqualsExpression
	| booleanUnaryExpression #ToBooleanUnaryExpression
	;

booleanUnaryExpression
	: NOT booleanUnaryExpression # NotExpression
	| booleanOperandExpression # ToBooleanOperandExpression
	;

booleanOperandExpression
	: logicalTerm # ToLogicalTerm
	| additionOrSubtractionExpression # ToAdditionOrSubtractionEpression
	| LPAREN logicalOrExpression RPAREN # BooleanParenthesis
	;

logicalTerm
	: ('true' | 'TRUE' | 'false' | 'FALSE') # LogicalConstant
	| IDENTIFIER # LogicalVariable
	;

additionOrSubtractionExpression
 	: additionOrSubtractionExpression PLUS multiplicationOrDivisionExpression # AdditionExpression
    | additionOrSubtractionExpression MINUS multiplicationOrDivisionExpression # SubtractionExpression
    | multiplicationOrDivisionExpression         # ToMultOrDiv
    ;

multiplicationOrDivisionExpression
    : multiplicationOrDivisionExpression MULT numericUnaryEpression # MultiplicationExpression
    | multiplicationOrDivisionExpression DIV numericUnaryEpression  # DivisionExpression
    | numericUnaryEpression # ToNumericUnaryExpression
    ;

numericUnaryEpression
    : MINUS numericUnaryEpression # MinusExpression
    | numericOperandExpression # Primary
    ;

numericOperandExpression
	: numericTerm # ToNumericTerm
	| functionCallExpression # ToFunctionCallExpression
	| LPAREN additionOrSubtractionExpression RPAREN # NumericParenthesis
	;

numericTerm
	: literal # NumericLiteral
	| IDENTIFIER # NumericVariable
	;

functionCallExpression
	: functionName=IDENTIFIER LPAREN functionParameters? RPAREN
	;

functionParameters
	: logicalOrExpression (COMMA logicalOrExpression)*
	;

literal
	: FloatingPointLiteral # FloatingPointLiteral
	| IntegerLiteral # IntegerLiteral
	| STRING # StringLiteral
	;

IntegerLiteral
    : Digits IntegerSuffix?
    ;

FloatingPointLiteral
    : DecimalFloatingPointLiteral
    ;

DecimalFloatingPointLiteral
	: Digits '.' Digits? FloatSuffix?
	| '.' Digits  FloatSuffix?
	;

AND
	: '&&'
	;

COMMA
	: ','
	;

DIV	: '/'
	;

EQ
	: '=='
	;

GE
	: '>='
	;

GT
	: '>'
	;

IDENTIFIER
	: [a-zA-Z_][a-zA-Z_0-9]*
	;

LE
	: '<='
	;

LPAREN
	: '('
	;

LT
	: '<'
	;

MINUS
	: '-'
	;

MULT
	: '*'
	;

NEQ
	: '!='
	;

NOT
	: '!'
	;

OR
	: '||'
	;

PLUS
	: '+'
	;

POW
	: '^'
	;

RPAREN
	: ')'
	;

STRING
	: '"' (~[\\"])* '"'
	;

WS
	: [ \r\t\u000C\n]+ -> skip
	;


fragment
Digits
    : Digit+
    ;

fragment
Digit
	: [0-9]
	;

fragment
IntegerSuffix
    : [lL]
    ;

fragment
FloatSuffix
    : [fFdD]
    ;
