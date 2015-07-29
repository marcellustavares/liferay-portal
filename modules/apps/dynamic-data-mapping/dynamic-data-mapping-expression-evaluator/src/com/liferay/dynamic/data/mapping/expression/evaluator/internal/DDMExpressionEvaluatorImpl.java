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

package com.liferay.dynamic.data.mapping.expression.evaluator.internal;

import com.liferay.dynamic.data.mapping.expression.evaluator.DDMExpressionEvaluator;
import com.liferay.dynamic.data.mapping.expression.evaluator.DDMExpressionEvaluatorException;
import com.liferay.dynamic.data.mapping.expression.evaluator.DDMFormExpressionEvaluatorResult;
import com.liferay.portal.expression.ExpressionFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true)
public class DDMExpressionEvaluatorImpl implements DDMExpressionEvaluator {

	public DDMFormExpressionEvaluatorResult evaluate(
			DDMForm ddmForm, DDMFormValues ddmFormValues, Locale locale)
		throws DDMExpressionEvaluatorException {

		try {
			DDMExpressionEvaluatorHelper ddmExpressionEvaluatorHelper =
				new DDMExpressionEvaluatorHelper(
					ddmForm, ddmFormValues, locale);

			ddmExpressionEvaluatorHelper.setExpressionFactory(
				_expressionFactory);

			return ddmExpressionEvaluatorHelper.evaluate();
		}
		catch (PortalException pe) {
			throw new DDMExpressionEvaluatorException(pe);
		}
	}

	@Reference
	protected void setExpressionFactory(ExpressionFactory expressionFactory) {
		_expressionFactory = expressionFactory;
	}

	private ExpressionFactory _expressionFactory;

}