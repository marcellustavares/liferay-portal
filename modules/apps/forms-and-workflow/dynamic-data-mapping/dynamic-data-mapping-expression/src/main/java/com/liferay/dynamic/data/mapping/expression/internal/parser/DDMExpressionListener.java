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

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DDMExpressionParser}.
 */
public interface DDMExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code ToMultOrDiv}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 */
	void enterToMultOrDiv(@NotNull DDMExpressionParser.ToMultOrDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToMultOrDiv}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 */
	void exitToMultOrDiv(@NotNull DDMExpressionParser.ToMultOrDivContext ctx);

	/**
	 * Enter a parse tree produced by {@link DDMExpressionParser#functionParams}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParams(@NotNull DDMExpressionParser.FunctionParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DDMExpressionParser#functionParams}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParams(@NotNull DDMExpressionParser.FunctionParamsContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ToUnary}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 */
	void enterToUnary(@NotNull DDMExpressionParser.ToUnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToUnary}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 */
	void exitToUnary(@NotNull DDMExpressionParser.ToUnaryContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Multiplication}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 */
	void enterMultiplication(@NotNull DDMExpressionParser.MultiplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Multiplication}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 */
	void exitMultiplication(@NotNull DDMExpressionParser.MultiplicationContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Or}
	 * labeled alternative in {@link DDMExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterOr(@NotNull DDMExpressionParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link DDMExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitOr(@NotNull DDMExpressionParser.OrContext ctx);

	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterString(@NotNull DDMExpressionParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitString(@NotNull DDMExpressionParser.StringContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Eq}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEq(@NotNull DDMExpressionParser.EqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Eq}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEq(@NotNull DDMExpressionParser.EqContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ToPlus}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterToPlus(@NotNull DDMExpressionParser.ToPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToPlus}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitToPlus(@NotNull DDMExpressionParser.ToPlusContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Function}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunction(@NotNull DDMExpressionParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunction(@NotNull DDMExpressionParser.FunctionContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void enterLessThan(@NotNull DDMExpressionParser.LessThanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void exitLessThan(@NotNull DDMExpressionParser.LessThanContext ctx);

	/**
	 * Enter a parse tree produced by the {@code PrimaryBoolean}
	 * labeled alternative in {@link DDMExpressionParser#unaryNot}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryBoolean(@NotNull DDMExpressionParser.PrimaryBooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryBoolean}
	 * labeled alternative in {@link DDMExpressionParser#unaryNot}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryBoolean(@NotNull DDMExpressionParser.PrimaryBooleanContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ChangeSign}
	 * labeled alternative in {@link DDMExpressionParser#unaryMinus}.
	 * @param ctx the parse tree
	 */
	void enterChangeSign(@NotNull DDMExpressionParser.ChangeSignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ChangeSign}
	 * labeled alternative in {@link DDMExpressionParser#unaryMinus}.
	 * @param ctx the parse tree
	 */
	void exitChangeSign(@NotNull DDMExpressionParser.ChangeSignContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ToLogicalEntity}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterToLogicalEntity(@NotNull DDMExpressionParser.ToLogicalEntityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToLogicalEntity}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitToLogicalEntity(@NotNull DDMExpressionParser.ToLogicalEntityContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Primary}
	 * labeled alternative in {@link DDMExpressionParser#unaryMinus}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(@NotNull DDMExpressionParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Primary}
	 * labeled alternative in {@link DDMExpressionParser#unaryMinus}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(@NotNull DDMExpressionParser.PrimaryContext ctx);

	/**
	 * Enter a parse tree produced by the {@code GreaterThanEQ}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanEQ(@NotNull DDMExpressionParser.GreaterThanEQContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanEQ}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanEQ(@NotNull DDMExpressionParser.GreaterThanEQContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LessThanEq}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void enterLessThanEq(@NotNull DDMExpressionParser.LessThanEqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanEq}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void exitLessThanEq(@NotNull DDMExpressionParser.LessThanEqContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Division}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 */
	void enterDivision(@NotNull DDMExpressionParser.DivisionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Division}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 */
	void exitDivision(@NotNull DDMExpressionParser.DivisionContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Plus}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 */
	void enterPlus(@NotNull DDMExpressionParser.PlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Plus}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 */
	void exitPlus(@NotNull DDMExpressionParser.PlusContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LogicalVariable}
	 * labeled alternative in {@link DDMExpressionParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void enterLogicalVariable(@NotNull DDMExpressionParser.LogicalVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalVariable}
	 * labeled alternative in {@link DDMExpressionParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void exitLogicalVariable(@NotNull DDMExpressionParser.LogicalVariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link DDMExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull DDMExpressionParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DDMExpressionParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull DDMExpressionParser.ExpressionContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ToUnaryNot}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void enterToUnaryNot(@NotNull DDMExpressionParser.ToUnaryNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToUnaryNot}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void exitToUnaryNot(@NotNull DDMExpressionParser.ToUnaryNotContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ChangeBoolean}
	 * labeled alternative in {@link DDMExpressionParser#unaryNot}.
	 * @param ctx the parse tree
	 */
	void enterChangeBoolean(@NotNull DDMExpressionParser.ChangeBooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ChangeBoolean}
	 * labeled alternative in {@link DDMExpressionParser#unaryNot}.
	 * @param ctx the parse tree
	 */
	void exitChangeBoolean(@NotNull DDMExpressionParser.ChangeBooleanContext ctx);

	/**
	 * Enter a parse tree produced by the {@code BooleanParen}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanParen(@NotNull DDMExpressionParser.BooleanParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanParen}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanParen(@NotNull DDMExpressionParser.BooleanParenContext ctx);

	/**
	 * Enter a parse tree produced by the {@code NumericVariable}
	 * labeled alternative in {@link DDMExpressionParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void enterNumericVariable(@NotNull DDMExpressionParser.NumericVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumericVariable}
	 * labeled alternative in {@link DDMExpressionParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void exitNumericVariable(@NotNull DDMExpressionParser.NumericVariableContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Double}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterDouble(@NotNull DDMExpressionParser.DoubleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Double}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitDouble(@NotNull DDMExpressionParser.DoubleContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterAtom(@NotNull DDMExpressionParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitAtom(@NotNull DDMExpressionParser.AtomContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterInteger(@NotNull DDMExpressionParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitInteger(@NotNull DDMExpressionParser.IntegerContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ToEquality}
	 * labeled alternative in {@link DDMExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterToEquality(@NotNull DDMExpressionParser.ToEqualityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToEquality}
	 * labeled alternative in {@link DDMExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitToEquality(@NotNull DDMExpressionParser.ToEqualityContext ctx);

	/**
	 * Enter a parse tree produced by the {@code And}
	 * labeled alternative in {@link DDMExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterAnd(@NotNull DDMExpressionParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code And}
	 * labeled alternative in {@link DDMExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitAnd(@NotNull DDMExpressionParser.AndContext ctx);

	/**
	 * Enter a parse tree produced by {@link DDMExpressionParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(@NotNull DDMExpressionParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link DDMExpressionParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(@NotNull DDMExpressionParser.FunctionCallContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Tocomparison}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterTocomparison(@NotNull DDMExpressionParser.TocomparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Tocomparison}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitTocomparison(@NotNull DDMExpressionParser.TocomparisonContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Greater}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void enterGreater(@NotNull DDMExpressionParser.GreaterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Greater}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void exitGreater(@NotNull DDMExpressionParser.GreaterContext ctx);

	/**
	 * Enter a parse tree produced by the {@code NEQ}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterNEQ(@NotNull DDMExpressionParser.NEQContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NEQ}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitNEQ(@NotNull DDMExpressionParser.NEQContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ToLiteral}
	 * labeled alternative in {@link DDMExpressionParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void enterToLiteral(@NotNull DDMExpressionParser.ToLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToLiteral}
	 * labeled alternative in {@link DDMExpressionParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void exitToLiteral(@NotNull DDMExpressionParser.ToLiteralContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Minus}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 */
	void enterMinus(@NotNull DDMExpressionParser.MinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Minus}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 */
	void exitMinus(@NotNull DDMExpressionParser.MinusContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ToLogicAnd}
	 * labeled alternative in {@link DDMExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterToLogicAnd(@NotNull DDMExpressionParser.ToLogicAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ToLogicAnd}
	 * labeled alternative in {@link DDMExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitToLogicAnd(@NotNull DDMExpressionParser.ToLogicAndContext ctx);

	/**
	 * Enter a parse tree produced by the {@code Paren}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParen(@NotNull DDMExpressionParser.ParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Paren}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParen(@NotNull DDMExpressionParser.ParenContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LogicalConst}
	 * labeled alternative in {@link DDMExpressionParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void enterLogicalConst(@NotNull DDMExpressionParser.LogicalConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalConst}
	 * labeled alternative in {@link DDMExpressionParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void exitLogicalConst(@NotNull DDMExpressionParser.LogicalConstContext ctx);
}
