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
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorContext {

	public DDMFormRuleEvaluatorContext(
		DDMDataProviderInstanceService ddmDataProviderInstanceService,
		DDMDataProviderTracker ddmDataProviderTracker,
		DDMExpressionFactory ddmExpressionFactory,
		List<DDMFormField> ddmFormFields, DDMFormValues ddmFormValues,
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer,
		Locale locale) {

		_ddmDataProviderInstanceService = ddmDataProviderInstanceService;
		_ddmDataProviderTracker = ddmDataProviderTracker;
		_ddmExpressionFactory = ddmExpressionFactory;
		_ddmFormFields = ddmFormFields;
		_ddmFormValues = ddmFormValues;
		_ddmFormValuesJSONDeserializer = ddmFormValuesJSONDeserializer;
		_locale = locale;
	}

	public DDMDataProviderInstanceService getDDMDataProviderInstanceService() {
		return _ddmDataProviderInstanceService;
	}

	public DDMDataProviderTracker getDDMDataProviderTracker() {
		return _ddmDataProviderTracker;
	}

	public DDMExpressionFactory getDDMExpressionFactory() {
		return _ddmExpressionFactory;
	}

	public Map<String, DDMFormFieldRuleEvaluationResult>
		getDDMFormFieldRuleEvaluationResults() {

		return _ddmFormFieldRuleEvaluationResults;
	}

	public List<DDMFormField> getDDMFormFields() {
		return _ddmFormFields;
	}

	public DDMFormValues getDDMFormValues() {
		return _ddmFormValues;
	}

	public DDMFormValuesJSONDeserializer getDDMFormValuesJSONDeserializer() {
		return _ddmFormValuesJSONDeserializer;
	}

	public Locale getLocale() {
		return _locale;
	}

	private final DDMDataProviderInstanceService
		_ddmDataProviderInstanceService;
	private final DDMDataProviderTracker _ddmDataProviderTracker;
	private final DDMExpressionFactory _ddmExpressionFactory;
	private final Map<String, DDMFormFieldRuleEvaluationResult>
		_ddmFormFieldRuleEvaluationResults = new HashMap<>();
	private final List<DDMFormField> _ddmFormFields;
	private final DDMFormValues _ddmFormValues;
	private final DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;
	private final Locale _locale;

}