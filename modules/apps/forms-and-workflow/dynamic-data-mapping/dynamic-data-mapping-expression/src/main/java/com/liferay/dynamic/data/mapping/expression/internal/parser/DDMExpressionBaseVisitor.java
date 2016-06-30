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
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link DDMExpressionVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @author Brian Wing Shun Chan
 */
public class DDMExpressionBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements DDMExpressionVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToMultOrDiv(@NotNull DDMExpressionParser.ToMultOrDivContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitFunctionParams(@NotNull DDMExpressionParser.FunctionParamsContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToUnary(@NotNull DDMExpressionParser.ToUnaryContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitMultiplication(@NotNull DDMExpressionParser.MultiplicationContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitOr(@NotNull DDMExpressionParser.OrContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitString(@NotNull DDMExpressionParser.StringContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitEq(@NotNull DDMExpressionParser.EqContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToPlus(@NotNull DDMExpressionParser.ToPlusContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitFunction(@NotNull DDMExpressionParser.FunctionContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitLessThan(@NotNull DDMExpressionParser.LessThanContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitPrimaryBoolean(@NotNull DDMExpressionParser.PrimaryBooleanContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitChangeSign(@NotNull DDMExpressionParser.ChangeSignContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToLogicalEntity(@NotNull DDMExpressionParser.ToLogicalEntityContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitPrimary(@NotNull DDMExpressionParser.PrimaryContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitGreaterThanEQ(@NotNull DDMExpressionParser.GreaterThanEQContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitLessThanEq(@NotNull DDMExpressionParser.LessThanEqContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitDivision(@NotNull DDMExpressionParser.DivisionContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitPlus(@NotNull DDMExpressionParser.PlusContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitLogicalVariable(@NotNull DDMExpressionParser.LogicalVariableContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitExpression(@NotNull DDMExpressionParser.ExpressionContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToUnaryNot(@NotNull DDMExpressionParser.ToUnaryNotContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitChangeBoolean(@NotNull DDMExpressionParser.ChangeBooleanContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitBooleanParen(@NotNull DDMExpressionParser.BooleanParenContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitNumericVariable(@NotNull DDMExpressionParser.NumericVariableContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitDouble(@NotNull DDMExpressionParser.DoubleContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitAtom(@NotNull DDMExpressionParser.AtomContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitInteger(@NotNull DDMExpressionParser.IntegerContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToEquality(@NotNull DDMExpressionParser.ToEqualityContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitAnd(@NotNull DDMExpressionParser.AndContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitFunctionCall(@NotNull DDMExpressionParser.FunctionCallContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitTocomparison(@NotNull DDMExpressionParser.TocomparisonContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitGreater(@NotNull DDMExpressionParser.GreaterContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitNEQ(@NotNull DDMExpressionParser.NEQContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToLiteral(@NotNull DDMExpressionParser.ToLiteralContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitMinus(@NotNull DDMExpressionParser.MinusContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitToLogicAnd(@NotNull DDMExpressionParser.ToLogicAndContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitParen(@NotNull DDMExpressionParser.ParenContext ctx) { return visitChildren(ctx); }

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling {@link
	 * #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override public T visitLogicalConst(@NotNull DDMExpressionParser.LogicalConstContext ctx) { return visitChildren(ctx); }
}