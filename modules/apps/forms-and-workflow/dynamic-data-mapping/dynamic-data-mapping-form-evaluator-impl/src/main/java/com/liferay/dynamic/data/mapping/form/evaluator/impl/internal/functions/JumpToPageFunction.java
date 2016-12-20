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

package com.liferay.dynamic.data.mapping.form.evaluator.impl.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;

import java.util.Map;

/**
 * @author Inácio Nery
 */
public class JumpToPageFunction implements DDMExpressionFunction {

	public JumpToPageFunction(Map<String, String> pageFlow) {
		_pageFlow = pageFlow;
	}

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 2) {
			throw new IllegalArgumentException("Two parameters are expected");
		}

		String fromPage = parameters[0].toString();
		String toPage = parameters[1].toString();

		_pageFlow.put(fromPage, toPage);

		return true;
	}

	private final Map<String, String> _pageFlow;

}