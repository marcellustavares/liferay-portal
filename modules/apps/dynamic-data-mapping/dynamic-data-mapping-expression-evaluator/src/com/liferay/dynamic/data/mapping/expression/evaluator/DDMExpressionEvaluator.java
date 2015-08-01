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

import com.liferay.portal.expression.Expression;
import com.liferay.portal.expression.ExpressionEvaluationException;
import com.liferay.portal.expression.ExpressionFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormValuesJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Carvalho
 */
public class DDMExpressionEvaluator {

	public DDMExpressionEvaluator(
		String serializedDDMFormValues, String serializedDDMForm,
		String languageId) {

		_serializedDDMFormValues = serializedDDMFormValues;
		_serializedDDMForm = serializedDDMForm;
		_languageId = languageId;
	}

	public String evaluate() throws PortalException {
		initializePOJOs();

		return doEvaluate().toString();
	}

	public void setExpressionFactory(ExpressionFactory expressionFactory) {
		_expressionFactory = expressionFactory;
	}

	private void createVariables(
		Expression<Boolean> expression,
		List<DDMFormFieldValue> ddmFormFieldValues,
		Set<DDMFormFieldValue> ancestorDDMFormFieldValues) {

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			String name = ddmFormFieldValue.getName();

			DDMFormField ddmFormField = _ddmFormFieldsMap.get(name);

			if (ddmFormField.isRepeatable() &&
				!ancestorDDMFormFieldValues.contains(ddmFormFieldValue)) {

				continue;
			}

			Value value = ddmFormFieldValue.getValue();

			String stringValue = value.getString(_locale);

			String dataType = ddmFormField.getDataType();

			if (dataType.equals("boolean")) {
				expression.setBooleanVariableValue(
					name, Boolean.valueOf(stringValue));
			}
			else if (dataType.equals("integer")) {
				expression.setIntegerVariableValue(
					name, Integer.valueOf(stringValue));
			}
			else if (dataType.equals("string")) {
				expression.setStringVariableValue(name, stringValue);
			}

			createVariables(
				expression, ddmFormFieldValue.getNestedDDMFormFieldValues(),
				ancestorDDMFormFieldValues);
		}
	}

	private void createVariables(
		Expression<Boolean> expression,
		Set<DDMFormFieldValue> ancestorDDMFormFieldValues) {

		createVariables(
			expression, _rootDDMFormFieldValues, ancestorDDMFormFieldValues);
	}

	private JSONObject doEvaluate() throws PortalException {
		JSONObject root = JSONFactoryUtil.createJSONObject();

		JSONArray rootDDMFormFieldValuesResult = evaluateDDMFormFieldValuesList(
			_rootDDMFormFieldValues, new HashSet<DDMFormFieldValue>());

		root.put("fields", rootDDMFormFieldValuesResult);

		return root;
	}

	private boolean evaluateBooleanExpression(
			String expressionString,
			Set<DDMFormFieldValue> ancestorDDMFormFieldValues)
		throws ExpressionEvaluationException {

		Expression<Boolean> expression =
			_expressionFactory.createBooleanExpression(expressionString);

		createVariables(expression, ancestorDDMFormFieldValues);

		return expression.evaluate();
	}

	private JSONObject evaluateDDMFormFieldValue(
			DDMFormFieldValue ddmFormFieldValue,
			Set<DDMFormFieldValue> ancestorDDMFormFieldValues)
		throws PortalException {

		ancestorDDMFormFieldValues.add(ddmFormFieldValue);

		String name = ddmFormFieldValue.getName();

		DDMFormField ddmFormField = _ddmFormFieldsMap.get(name);

		JSONObject result = evaluateDDMFormFieldValue(
			ddmFormFieldValue, ancestorDDMFormFieldValues, ddmFormField);

		ancestorDDMFormFieldValues.remove(ddmFormFieldValue);

		return result;
	}

	private JSONObject evaluateDDMFormFieldValue(
			DDMFormFieldValue ddmFormFieldValue,
			Set<DDMFormFieldValue> ancestorDDMFormFieldValues,
			DDMFormField ddmFormField)
		throws PortalException {

		try {
			String validationExpression =
				ddmFormField.getValidationExpression();

			boolean valid = evaluateBooleanExpression(
				validationExpression, ancestorDDMFormFieldValues);

			String visibilityExpression =
				ddmFormField.getVisibilityExpression();

			boolean visible = evaluateBooleanExpression(
				visibilityExpression, ancestorDDMFormFieldValues);

			JSONArray nestedFieldsResultsJSONArray =
				evaluateDDMFormFieldValuesList(
					ddmFormFieldValue.getNestedDDMFormFieldValues(),
					ancestorDDMFormFieldValues);

			JSONObject result = JSONFactoryUtil.createJSONObject();

			result.put("instanceId", ddmFormFieldValue.getInstanceId());

			result.put("name", ddmFormFieldValue.getName());

			result.put("nestedFields", nestedFieldsResultsJSONArray);

			result.put("valid", valid);

			result.put("visible", visible);

			return result;
		}
		catch (ExpressionEvaluationException eee) {
			throw new PortalException(eee);
		}
	}

	private JSONArray evaluateDDMFormFieldValuesList(
			List<DDMFormFieldValue> ddmFormFieldValuesList,
			Set<DDMFormFieldValue> ancestorDDMFormFieldValues)
		throws PortalException {

		JSONArray result = JSONFactoryUtil.createJSONArray();

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValuesList) {
			JSONObject ddmFormFieldValueResult = evaluateDDMFormFieldValue(
				ddmFormFieldValue, ancestorDDMFormFieldValues);

			result.put(ddmFormFieldValueResult);
		}

		return result;
	}

	private void initializePOJOs() throws PortalException {
		DDMForm ddmForm = DDMFormJSONDeserializerUtil.deserialize(
			_serializedDDMForm);

		DDMFormValues ddmFormValues =
			DDMFormValuesJSONDeserializerUtil.deserialize(
				ddmForm, _serializedDDMFormValues);

		_ddmFormFieldsMap = ddmForm.getDDMFormFieldsMap(true);

		_locale = LocaleUtil.fromLanguageId(_languageId);

		_rootDDMFormFieldValues = ddmFormValues.getDDMFormFieldValues();
	}

	private Map<String, DDMFormField> _ddmFormFieldsMap;
	private ExpressionFactory _expressionFactory;
	private final String _languageId;
	private Locale _locale;
	private List<DDMFormFieldValue> _rootDDMFormFieldValues;
	private final String _serializedDDMForm;
	private final String _serializedDDMFormValues;

}