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

package com.liferay.dynamic.data.mapping.expression.model;

/**
 * @author Marcellus Tavares
 */
public interface ExpressionVisitor<T> {

	public T visitAndExpression(AndExpression andExpression);

	public T visitArithmeticExpression(
		ArithmeticExpression arithmeticExpression);

	public T visitComparisonExpression(
		ComparisonExpression comparisonExpression);

	public T visitFunctionCallExpression(
		FunctionCallExpression functionCallExpression);

	public T visitMinusExpression(MinusExpression minusExpression);

	public T visitNotExpression(NotExpression notExpression);

	public T visitOrExpression(OrExpression orExpression);

	public T visitTerm(Term term);

}