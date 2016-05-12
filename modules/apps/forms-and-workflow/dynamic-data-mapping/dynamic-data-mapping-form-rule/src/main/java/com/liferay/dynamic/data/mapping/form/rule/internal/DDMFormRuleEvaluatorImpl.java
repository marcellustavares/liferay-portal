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

package com.liferay.dynamic.data.mapping.form.rule.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormRuleEvaluationException;
import com.liferay.dynamic.data.mapping.form.rule.DDMFormRuleEvaluator;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;

import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true)
public class DDMFormRuleEvaluatorImpl implements DDMFormRuleEvaluator {

	@Override
	public List<DDMFormFieldRuleEvaluationResult> evaluate(
			DDMForm ddmForm, DDMFormValues ddmFormValues, Locale locale)
		throws DDMFormRuleEvaluationException {

		DDMFormRuleEvaluatorHelper ddmFormRuleEvaluatorHelper =
			new DDMFormRuleEvaluatorHelper(
				ddmForm, ddmFormValues, locale, _ddmDataProviderInstanceService,
				_ddmDataProviderTracker, _ddmExpressionFactory,
				_ddmFormValuesJSONDeserializer);

		try {
			DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
				ddmFormRuleEvaluatorHelper.createDDMFormRuleEvaluatorGraph();

			return ddmFormRuleEvaluatorGraph.execute();
		}
		catch (Exception e) {
			throw new DDMFormRuleEvaluationException(
				"An error occured during the execution of form's rules", e);
		}
	}

	@Reference(unbind = "-")
	protected void setDDMDataProviderInstanceService(
		DDMDataProviderInstanceService ddmDataProviderInstanceService) {

		_ddmDataProviderInstanceService = ddmDataProviderInstanceService;
	}

	@Reference(unbind = "-")
	protected void setDDMDataProviderTracker(
		DDMDataProviderTracker ddmDataProviderTracker) {

		_ddmDataProviderTracker = ddmDataProviderTracker;
	}

	@Reference(unbind = "-")
	protected void setDDMExpressionFactory(
		DDMExpressionFactory ddmExpressionFactory) {

		_ddmExpressionFactory = ddmExpressionFactory;
	}

	@Reference(unbind = "-")
	protected void setDDMFormValuesJSONDeserializer(
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer) {

		_ddmFormValuesJSONDeserializer = ddmFormValuesJSONDeserializer;
	}

	private DDMDataProviderInstanceService _ddmDataProviderInstanceService;
	private DDMDataProviderTracker _ddmDataProviderTracker;
	private DDMExpressionFactory _ddmExpressionFactory;
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;

}