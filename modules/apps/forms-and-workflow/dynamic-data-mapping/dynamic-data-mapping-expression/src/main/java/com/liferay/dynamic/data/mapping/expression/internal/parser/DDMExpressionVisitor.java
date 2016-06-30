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
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DDMExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DDMExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code ToMultOrDiv}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToMultOrDiv(@NotNull DDMExpressionParser.ToMultOrDivContext ctx);

	/**
	 * Visit a parse tree produced by {@link DDMExpressionParser#functionParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParams(@NotNull DDMExpressionParser.FunctionParamsContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ToUnary}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToUnary(@NotNull DDMExpressionParser.ToUnaryContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Multiplication}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplication(@NotNull DDMExpressionParser.MultiplicationContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link DDMExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(@NotNull DDMExpressionParser.OrContext ctx);

	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(@NotNull DDMExpressionParser.StringContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Eq}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq(@NotNull DDMExpressionParser.EqContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ToPlus}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToPlus(@NotNull DDMExpressionParser.ToPlusContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(@NotNull DDMExpressionParser.FunctionContext ctx);

	/**
	 * Visit a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThan(@NotNull DDMExpressionParser.LessThanContext ctx);

	/**
	 * Visit a parse tree produced by the {@code PrimaryBoolean}
	 * labeled alternative in {@link DDMExpressionParser#unaryNot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryBoolean(@NotNull DDMExpressionParser.PrimaryBooleanContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ChangeSign}
	 * labeled alternative in {@link DDMExpressionParser#unaryMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChangeSign(@NotNull DDMExpressionParser.ChangeSignContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ToLogicalEntity}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLogicalEntity(@NotNull DDMExpressionParser.ToLogicalEntityContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Primary}
	 * labeled alternative in {@link DDMExpressionParser#unaryMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(@NotNull DDMExpressionParser.PrimaryContext ctx);

	/**
	 * Visit a parse tree produced by the {@code GreaterThanEQ}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanEQ(@NotNull DDMExpressionParser.GreaterThanEQContext ctx);

	/**
	 * Visit a parse tree produced by the {@code LessThanEq}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanEq(@NotNull DDMExpressionParser.LessThanEqContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Division}
	 * labeled alternative in {@link DDMExpressionParser#multOrDiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivision(@NotNull DDMExpressionParser.DivisionContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Plus}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlus(@NotNull DDMExpressionParser.PlusContext ctx);

	/**
	 * Visit a parse tree produced by the {@code LogicalVariable}
	 * labeled alternative in {@link DDMExpressionParser#logical_entity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalVariable(@NotNull DDMExpressionParser.LogicalVariableContext ctx);

	/**
	 * Visit a parse tree produced by {@link DDMExpressionParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull DDMExpressionParser.ExpressionContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ToUnaryNot}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToUnaryNot(@NotNull DDMExpressionParser.ToUnaryNotContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ChangeBoolean}
	 * labeled alternative in {@link DDMExpressionParser#unaryNot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChangeBoolean(@NotNull DDMExpressionParser.ChangeBooleanContext ctx);

	/**
	 * Visit a parse tree produced by the {@code BooleanParen}
	 * labeled alternative in {@link DDMExpressionParser#primaryBooleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanParen(@NotNull DDMExpressionParser.BooleanParenContext ctx);

	/**
	 * Visit a parse tree produced by the {@code NumericVariable}
	 * labeled alternative in {@link DDMExpressionParser#numeric_entity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericVariable(@NotNull DDMExpressionParser.NumericVariableContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Double}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDouble(@NotNull DDMExpressionParser.DoubleContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Atom}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(@NotNull DDMExpressionParser.AtomContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link DDMExpressionParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(@NotNull DDMExpressionParser.IntegerContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ToEquality}
	 * labeled alternative in {@link DDMExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToEquality(@NotNull DDMExpressionParser.ToEqualityContext ctx);

	/**
	 * Visit a parse tree produced by the {@code And}
	 * labeled alternative in {@link DDMExpressionParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(@NotNull DDMExpressionParser.AndContext ctx);

	/**
	 * Visit a parse tree produced by {@link DDMExpressionParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(@NotNull DDMExpressionParser.FunctionCallContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Tocomparison}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTocomparison(@NotNull DDMExpressionParser.TocomparisonContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Greater}
	 * labeled alternative in {@link DDMExpressionParser#comparison_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreater(@NotNull DDMExpressionParser.GreaterContext ctx);

	/**
	 * Visit a parse tree produced by the {@code NEQ}
	 * labeled alternative in {@link DDMExpressionParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNEQ(@NotNull DDMExpressionParser.NEQContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ToLiteral}
	 * labeled alternative in {@link DDMExpressionParser#numeric_entity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLiteral(@NotNull DDMExpressionParser.ToLiteralContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Minus}
	 * labeled alternative in {@link DDMExpressionParser#plusOrMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinus(@NotNull DDMExpressionParser.MinusContext ctx);

	/**
	 * Visit a parse tree produced by the {@code ToLogicAnd}
	 * labeled alternative in {@link DDMExpressionParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLogicAnd(@NotNull DDMExpressionParser.ToLogicAndContext ctx);

	/**
	 * Visit a parse tree produced by the {@code Paren}
	 * labeled alternative in {@link DDMExpressionParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParen(@NotNull DDMExpressionParser.ParenContext ctx);

	/**
	 * Visit a parse tree produced by the {@code LogicalConst}
	 * labeled alternative in {@link DDMExpressionParser#logical_entity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalConst(@NotNull DDMExpressionParser.LogicalConstContext ctx);
}
