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

package com.liferay.dynamic.data.mapping.form.renderer.internal;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluator;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextFactory;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRendererConstants;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class DDMFormRendererHelper {

	public DDMFormRendererHelper(
		DDMForm ddmForm, DDMFormRenderingContext ddmFormRenderingContext) {

		_ddmForm = ddmForm;
		_ddmFormFieldsMap = ddmForm.getDDMFormFieldsMap(true);
		_ddmFormRenderingContext = ddmFormRenderingContext;

		DDMFormValues ddmFormValues =
			ddmFormRenderingContext.getDDMFormValues();

		if (ddmFormValues == null) {
			_ddmFormValues = createDefaultDDMFormValues();
		}
		else {
			_ddmFormValues = ddmFormValues;
		}
	}

	public Map<String, JSONArray> getTemplateContextDDMFormFieldsMap()
		throws DDMFormRenderingException {

		Map<String, JSONArray> templateContextDDMFormFieldsValuesMap =
			new HashMap<>();

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			_ddmFormValues.getDDMFormFieldValuesMap();

		for (Map.Entry<String, List<DDMFormFieldValue>> entry :
				ddmFormFieldValuesMap.entrySet()) {

			templateContextDDMFormFieldsValuesMap.put(
				entry.getKey(),
				createDDMFormFieldValuesTemplateContext(
					entry.getValue(), StringPool.BLANK));
		}

		return templateContextDDMFormFieldsValuesMap;
	}

	protected DDMFormFieldRenderingContext
		createDDMFormFieldRenderingContext() {

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			new DDMFormFieldRenderingContext();

		ddmFormFieldRenderingContext.setChildElementsHTML(StringPool.BLANK);
		ddmFormFieldRenderingContext.setHttpServletRequest(
			_ddmFormRenderingContext.getHttpServletRequest());
		ddmFormFieldRenderingContext.setHttpServletResponse(
			_ddmFormRenderingContext.getHttpServletResponse());
		ddmFormFieldRenderingContext.setLabel(StringPool.BLANK);
		ddmFormFieldRenderingContext.setLocale(
			_ddmFormRenderingContext.getLocale());
		ddmFormFieldRenderingContext.setName(StringPool.BLANK);
		ddmFormFieldRenderingContext.setPortletNamespace(
			_ddmFormRenderingContext.getPortletNamespace());
		ddmFormFieldRenderingContext.setReadOnly(
			_ddmFormRenderingContext.isReadOnly());
		ddmFormFieldRenderingContext.setValue(StringPool.BLANK);

		return ddmFormFieldRenderingContext;
	}

	protected JSONObject createDDMFormFieldTemplateContext(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws DDMFormRenderingException {

		DDMFormFieldTemplateContextFactory ddmFormFieldTemplateContextFactory =
			_ddmFormFieldTypeServicesTracker.
				getDDMFormFieldTemplateContextFactory(ddmFormField.getType());

		if (ddmFormFieldTemplateContextFactory == null) {
			throw new DDMFormRenderingException(
				"No DDM form field template context factory registered for " +
					ddmFormField.getType());
		}

		return ddmFormFieldTemplateContextFactory.create(
			ddmFormField, ddmFormFieldRenderingContext);
	}

	protected JSONArray createDDMFormFieldValuesTemplateContext(
			List<DDMFormFieldValue> ddmFormFieldValues,
			String parentDDMFormFieldParameterName)
		throws DDMFormRenderingException {

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		int index = 0;

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			jsonArray.put(
				createDDMFormFieldValueTemplateContext(
					ddmFormFieldValue, index++,
					parentDDMFormFieldParameterName));
		}

		return jsonArray;
	}

	protected JSONObject createDDMFormFieldValueTemplateContext(
			DDMFormFieldValue ddmFormFieldValue,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws DDMFormRenderingException {

		DDMFormField ddmFormField = _ddmFormFieldsMap.get(
			ddmFormFieldValue.getName());

		setDDMFormFieldRenderingContextLabel(
			ddmFormField.getLabel(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextRequired(
			ddmFormField.isRequired(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextRequired(
			ddmFormField.isRequired(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextTip(
			ddmFormField.getTip(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextValue(
			ddmFormFieldValue.getValue(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextVisible(
			ddmFormField.getVisibilityExpression(), ddmFormFieldValue.getName(),
			ddmFormFieldRenderingContext);

		return createDDMFormFieldTemplateContext(
			ddmFormField, ddmFormFieldRenderingContext);
	}

	protected JSONObject createDDMFormFieldValueTemplateContext(
			DDMFormFieldValue ddmFormFieldValue, int index,
			String parentDDMFormFieldParameterName)
		throws DDMFormRenderingException {

		String ddmFormFieldParameterName = getDDMFormFieldParameterName(
			ddmFormFieldValue.getName(), ddmFormFieldValue.getInstanceId(),
			index, parentDDMFormFieldParameterName);

		Map<String, List<DDMFormFieldValue>> nestedDDMFormFieldValuesMap =
			ddmFormFieldValue.getNestedDDMFormFieldValuesMap();

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (List<DDMFormFieldValue> nestedDDMFormFieldValues :
				nestedDDMFormFieldValuesMap.values()) {

			jsonArray.put(
				createDDMFormFieldValuesTemplateContext(
					nestedDDMFormFieldValues, ddmFormFieldParameterName));
		}

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			createDDMFormFieldRenderingContext();

		setDDMFormFieldRenderingContextChildTemplateContext(
			jsonArray, ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextName(
			ddmFormFieldParameterName, ddmFormFieldRenderingContext);

		return createDDMFormFieldValueTemplateContext(
			ddmFormFieldValue, ddmFormFieldRenderingContext);
	}

	protected DDMFormFieldValue createDefaultDDMFormFieldValue(
		DDMFormField ddmFormField) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName(ddmFormField.getName());

		Value value = createDefaultValue(ddmFormField);

		ddmFormFieldValue.setValue(value);

		for (DDMFormField nestedDDMFormField :
				ddmFormField.getNestedDDMFormFields()) {

			ddmFormFieldValue.addNestedDDMFormFieldValue(
				createDefaultDDMFormFieldValue(nestedDDMFormField));
		}

		return ddmFormFieldValue;
	}

	protected DDMFormValues createDefaultDDMFormValues() {
		DDMFormValues ddmFormValues = new DDMFormValues(_ddmForm);

		ddmFormValues.setDefaultLocale(_ddmFormRenderingContext.getLocale());

		for (DDMFormField ddmFormField : _ddmForm.getDDMFormFields()) {
			DDMFormFieldValue ddmFormFieldValue =
				createDefaultDDMFormFieldValue(ddmFormField);

			ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
		}

		return ddmFormValues;
	}

	protected Value createDefaultLocalizedValue(String defaultValueString) {
		Value value = new LocalizedValue(_ddmFormRenderingContext.getLocale());

		value.addString(
			_ddmFormRenderingContext.getLocale(), defaultValueString);

		return value;
	}

	protected Value createDefaultValue(DDMFormField ddmFormField) {
		LocalizedValue predefinedValue = ddmFormField.getPredefinedValue();

		String defaultValueString = GetterUtil.getString(
			predefinedValue.getString(_ddmFormRenderingContext.getLocale()));

		if (ddmFormField.isLocalizable()) {
			return createDefaultLocalizedValue(defaultValueString);
		}

		return new UnlocalizedValue(defaultValueString);
	}

	protected Map<String, DDMFormFieldEvaluationResult>
		createInitialStateDDMFormFieldEvaluationResultsMap() {

		try {
			DDMFormEvaluationResult ddmFormEvaluationResult =
				_ddmFormEvaluator.evaluate(
					_ddmForm, _ddmFormValues,
					_ddmFormRenderingContext.getLocale());

			return ddmFormEvaluationResult.
				getDDMFormFieldEvaluationResultsMap();
		}
		catch (DDMFormEvaluationException ddmfee) {
			_log.error("Unable to evaluate the form", ddmfee);
		}

		return new HashMap<>();
	}

	protected Map<String, DDMFormFieldEvaluationResult> evaluateDDMForm(
			Locale locale)
		throws DDMFormRenderingException {

		try {
			DDMFormEvaluationResult ddmFormEvaluationResult =
				_ddmFormEvaluator.evaluate(_ddmForm, _ddmFormValues, locale);

			return ddmFormEvaluationResult.
				getDDMFormFieldEvaluationResultsMap();
		}
		catch (DDMFormEvaluationException ddmfee) {
			throw new DDMFormRenderingException(ddmfee);
		}
	}

	protected String getAffixedDDMFormFieldParameterName(
		String ddmFormFieldParameterName) {

		StringBundler sb = new StringBundler(5);

		sb.append(_ddmFormRenderingContext.getPortletNamespace());
		sb.append(DDMFormRendererConstants.DDM_FORM_FIELD_NAME_PREFIX);
		sb.append(ddmFormFieldParameterName);
		sb.append(
			DDMFormRendererConstants.DDM_FORM_FIELD_LANGUAGE_ID_SEPARATOR);
		sb.append(
			LocaleUtil.toLanguageId(_ddmFormRenderingContext.getLocale()));

		return sb.toString();
	}

	protected String getDDMFormFieldParameterName(
		String ddmFormFieldName, String instanceId, int index,
		String parentDDMFormFieldParameterName) {

		StringBundler sb = new StringBundler(7);

		if (Validator.isNotNull(parentDDMFormFieldParameterName)) {
			sb.append(parentDDMFormFieldParameterName);
			sb.append(DDMFormRendererConstants.DDM_FORM_FIELDS_SEPARATOR);
		}

		sb.append(ddmFormFieldName);
		sb.append(DDMFormRendererConstants.DDM_FORM_FIELD_PARTS_SEPARATOR);
		sb.append(instanceId);
		sb.append(DDMFormRendererConstants.DDM_FORM_FIELD_PARTS_SEPARATOR);
		sb.append(index);

		return sb.toString();
	}

	protected boolean isFieldVisible(String fieldName) {
		if (_ddmFormFieldEvaluationResultsMap == null) {
			_ddmFormFieldEvaluationResultsMap =
				createInitialStateDDMFormFieldEvaluationResultsMap();
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			_ddmFormFieldEvaluationResultsMap.get(fieldName);

		if (ddmFormFieldEvaluationResult != null) {
			return ddmFormFieldEvaluationResult.isVisible();
		}

		return true;
	}

	protected void setDDMFormEvaluator(DDMFormEvaluator ddmFormEvaluator) {
		_ddmFormEvaluator = ddmFormEvaluator;
	}

	protected void setDDMFormFieldRenderingContextChildTemplateContext(
		JSONArray childTemplateContext,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		ddmFormFieldRenderingContext.setChildTemplateContext(
			childTemplateContext);
	}

	protected void setDDMFormFieldRenderingContextLabel(
		LocalizedValue label,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<Locale, String> values = label.getValues();

		if (values.isEmpty()) {
			return;
		}

		ddmFormFieldRenderingContext.setLabel(
			label.getString(ddmFormFieldRenderingContext.getLocale()));
	}

	protected void setDDMFormFieldRenderingContextName(
		String ddmFormFieldParameterName,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		String name = getAffixedDDMFormFieldParameterName(
			ddmFormFieldParameterName);

		ddmFormFieldRenderingContext.setName(name);
	}

	protected void setDDMFormFieldRenderingContextRequired(
		boolean required,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		ddmFormFieldRenderingContext.setRequired(required);
	}

	protected void setDDMFormFieldRenderingContextTip(
		LocalizedValue tip,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<Locale, String> values = tip.getValues();

		if (values.isEmpty()) {
			return;
		}

		ddmFormFieldRenderingContext.setTip(
			tip.getString(ddmFormFieldRenderingContext.getLocale()));
	}

	protected void setDDMFormFieldRenderingContextValue(
		Value value,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		if (value == null) {
			return;
		}

		ddmFormFieldRenderingContext.setValue(
			value.getString(ddmFormFieldRenderingContext.getLocale()));
	}

	protected void setDDMFormFieldRenderingContextVisible(
			String visibilityExpression, String fieldName,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws DDMFormRenderingException {

		boolean visible = isFieldVisible(fieldName);

		if (Validator.isNotNull(visibilityExpression)) {
			Map<String, DDMFormFieldEvaluationResult>
				ddmFormFieldEvaluationResultsMap = evaluateDDMForm(
					ddmFormFieldRenderingContext.getLocale());

			DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
				ddmFormFieldEvaluationResultsMap.get(fieldName);

			visible = ddmFormFieldEvaluationResult.isVisible();
		}

		ddmFormFieldRenderingContext.setVisible(visible);
	}

	protected void setDDMFormFieldTypeServicesTracker(
		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker) {

		_ddmFormFieldTypeServicesTracker = ddmFormFieldTypeServicesTracker;
	}

	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormRendererHelper.class);

	private final DDMForm _ddmForm;
	private DDMFormEvaluator _ddmFormEvaluator;
	private Map<String, DDMFormFieldEvaluationResult>
		_ddmFormFieldEvaluationResultsMap;
	private final Map<String, DDMFormField> _ddmFormFieldsMap;
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;
	private final DDMFormRenderingContext _ddmFormRenderingContext;
	private final DDMFormValues _ddmFormValues;
	private JSONFactory _jsonFactory;

}