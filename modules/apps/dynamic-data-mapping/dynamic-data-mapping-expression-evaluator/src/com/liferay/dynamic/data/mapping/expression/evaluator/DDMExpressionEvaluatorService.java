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

package com.liferay.dynamic.data.mapping.expression.evaluator;

import com.liferay.portal.expression.ExpressionFactory;
import com.liferay.portal.kernel.exception.PortalException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true, service = DDMExpressionEvaluatorService.class)
public class DDMExpressionEvaluatorService {

	public String evaluate(
		String serializedDDMFormValues, String serializedDDMForm,
		String languageId) throws PortalException {

		DDMExpressionEvaluator ddmExpressionEvaluator =
			new DDMExpressionEvaluator(
				serializedDDMFormValues, serializedDDMForm, languageId);

		ddmExpressionEvaluator.setExpressionFactory(_expressionFactory);

		return ddmExpressionEvaluator.evaluate();
	}

	@Reference
	public void setExpressionFactory(ExpressionFactory expressionFactory) {
		_expressionFactory = expressionFactory;
	}

	private ExpressionFactory _expressionFactory;

}