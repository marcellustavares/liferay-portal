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

package com.liferay.dynamic.data.mapping.form.evaluator.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluator;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluator;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.DDMFormRuleEvaluatorHelper;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true)
public class DDMFormEvaluatorImpl implements DDMFormEvaluator {

	@Override
	public DDMFormEvaluationResult evaluate(
			DDMForm ddmForm, DDMFormValues ddmFormValues, Locale locale)
		throws DDMFormEvaluationException {

		try {
			DDMFormEvaluatorHelper ddmFormEvaluatorHelper =
				new DDMFormEvaluatorHelper(ddmForm, ddmFormValues, locale);

			ddmFormEvaluatorHelper.setDDMExpressionFactory(
				_ddmExpressionFactory);
			ddmFormEvaluatorHelper.setJSONFactory(_jsonFactory);

			DDMFormEvaluationResult ddmFormEvaluationResult =
				ddmFormEvaluatorHelper.evaluate();

			Map<String, DDMFormFieldEvaluationResult>
				expressionDDMFormFieldEvaluationResults =
					ddmFormEvaluationResult.getDDMFormFieldEvaluationResultsMap();

			DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
				new DDMFormRuleEvaluatorHelper(_ddmExpressionFactory, ddmForm);

			DDMFormRuleEvaluator ddmFormRuleEvaluator =
				new DDMFormRuleEvaluator(
					_ddmDataProviderInstanceService, _ddmDataProviderTracker,
					_ddmExpressionFactory, ddmForm,
					ddmFormRuleEvaluatorHelper.
						createDDMFormRuleEvaluatorGraph(),
					ddmFormValues, _ddFormValuesJSONDeserializer, locale);

			List<DDMFormFieldEvaluationResult>
				ruleDDMFormFieldEvaluationResults =
					ddmFormRuleEvaluator.evaluate();

			return merge(
				expressionDDMFormFieldEvaluationResults.values(),
				ruleDDMFormFieldEvaluationResults);
		}
		catch (Exception e) {
			throw new DDMFormEvaluationException(e);
		}
	}

	protected DDMFormEvaluationResult merge(
		Collection<DDMFormFieldEvaluationResult> expressionDDMFormFieldEvaluationResults,
		List<DDMFormFieldEvaluationResult> ruleDDMFormFieldEvaluationResults ) {

		for (DDMFormFieldEvaluationResult expressionDDMFormFieldEvaluationResult :
				expressionDDMFormFieldEvaluationResults) {

			int indexOf = ruleDDMFormFieldEvaluationResults.indexOf(
					expressionDDMFormFieldEvaluationResult);
			
			if (indexOf < 0) {
				continue;
			}

			DDMFormFieldEvaluationResult ruleDDMFormFieldEvaluationResult =
				ruleDDMFormFieldEvaluationResults.get(indexOf);

			if (!expressionDDMFormFieldEvaluationResult.isValid()) {
				if (ruleDDMFormFieldEvaluationResult.isValid()) {
					ruleDDMFormFieldEvaluationResult.setValid(false);
					ruleDDMFormFieldEvaluationResult.setErrorMessage(
						expressionDDMFormFieldEvaluationResult.getErrorMessage());
				}
			}

			if (!expressionDDMFormFieldEvaluationResult.isVisible()) {
				if (ruleDDMFormFieldEvaluationResult.isVisible()) {
					ruleDDMFormFieldEvaluationResult.setVisible(false);
				}
			}
		}

		DDMFormEvaluationResult ddmFormEvaluationResult =
			new DDMFormEvaluationResult();

		ddmFormEvaluationResult.setDDMFormFieldEvaluationResults(
			ruleDDMFormFieldEvaluationResults);

		return ddmFormEvaluationResult;
	}


	@Reference
	private DDMDataProviderInstanceService _ddmDataProviderInstanceService;

	@Reference
	private DDMDataProviderTracker _ddmDataProviderTracker;

	@Reference
	private DDMExpressionFactory _ddmExpressionFactory;

	@Reference
	private DDMFormValuesJSONDeserializer _ddFormValuesJSONDeserializer;

	@Reference
	private JSONFactory _jsonFactory;

}