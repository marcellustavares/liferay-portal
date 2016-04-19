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
import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldRenderingContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRendererConstants;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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
public class DDMFormFieldsTemplateContextHelper {

	public DDMFormFieldsTemplateContextHelper(
		DDMForm ddmForm, DDMFormRenderingContext ddmFormRenderingContext) {

		_ddmForm = ddmForm;
		_ddmFormFieldsMap = ddmForm.getDDMFormFieldsMap(true);
		_ddmFormRenderingContext = ddmFormRenderingContext;

		DDMFormValues ddmFormValues =
			ddmFormRenderingContext.getDDMFormValues();

		if (ddmFormValues == null) {
			DefaultDDMFormValuesFactory defaultDDMFormValuesFactory =
				new DefaultDDMFormValuesFactory(
					ddmForm, ddmFormRenderingContext.getLocale());

			_ddmFormValues = defaultDDMFormValuesFactory.create();
		}
		else {
			_ddmFormValues = ddmFormValues;
		}
	}

	public Map<String, JSONArray> getDDMFormFieldsTemplateContextMap()
		throws DDMFormRenderingException {

		Map<String, JSONArray> ddmFormFieldsTemplateContextMap =
			new HashMap<>();

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			_ddmFormValues.getDDMFormFieldValuesMap();

		for (Map.Entry<String, List<DDMFormFieldValue>> entry :
				ddmFormFieldValuesMap.entrySet()) {

			ddmFormFieldsTemplateContextMap.put(
				entry.getKey(),
				createDDMFormFieldValuesTemplateContext(
					entry.getValue(), StringPool.BLANK));
		}

		return ddmFormFieldsTemplateContextMap;
	}

	protected Map<String, DDMFormFieldEvaluationResult>
		createDDMFormFieldEvaluationResultsMap() {

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

	protected DDMFormFieldRenderingContext
		createDDMFormFieldRenderingContext() {

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			new DDMFormFieldRenderingContext();

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
		ddmFormFieldRenderingContext.setValue(StringPool.BLANK);

		return ddmFormFieldRenderingContext;
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
			DDMFormFieldValue ddmFormFieldValue, int index,
			String parentDDMFormFieldParameterName)
		throws DDMFormRenderingException {

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			createDDMFormFieldRenderingContext();

		JSONArray nestedDDMFormFieldsTemplateContext =
			_jsonFactory.createJSONArray();

		String ddmFormFieldParameterName = getDDMFormFieldParameterName(
			ddmFormFieldValue.getName(), ddmFormFieldValue.getInstanceId(),
			index, parentDDMFormFieldParameterName);

		Map<String, List<DDMFormFieldValue>> nestedDDMFormFieldValuesMap =
			ddmFormFieldValue.getNestedDDMFormFieldValuesMap();

		for (List<DDMFormFieldValue> nestedDDMFormFieldValues :
				nestedDDMFormFieldValuesMap.values()) {

			nestedDDMFormFieldsTemplateContext.put(
				createDDMFormFieldValuesTemplateContext(
					nestedDDMFormFieldValues, ddmFormFieldParameterName));
		}

		setDDMFormFieldRenderingContextNestedFieldsTemplateContext(
			nestedDDMFormFieldsTemplateContext, ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextName(
			ddmFormFieldParameterName, ddmFormFieldRenderingContext);

		DDMFormField ddmFormField = _ddmFormFieldsMap.get(
			ddmFormFieldValue.getName());

		setDDMFormFieldRenderingContextLabel(
			ddmFormField.getLabel(), ddmFormFieldRenderingContext);

		boolean readOnly = isReadOnly(ddmFormField);

		setDDMFormFieldRenderingContextReadOnly(
			readOnly, ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextRepeatable(
			ddmFormField.isRepeatable(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextRequired(
			ddmFormField.isRequired(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextTip(
			ddmFormField.getTip(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextValid(
			ddmFormField, ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextValue(
			ddmFormFieldValue.getValue(), ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextVisible(
			ddmFormField, ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextTemplateNamespace(
			ddmFormField, ddmFormFieldRenderingContext);
		setDDMFormFieldRenderingContextContributedAttributes(
			ddmFormField, ddmFormFieldRenderingContext);

		return toJSONObject(ddmFormFieldRenderingContext.getAttributes());
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

	protected DDMFormFieldEvaluationResult getDDMFormFieldEvaluationResult(
		String ddmFormFieldName) {

		if (_ddmFormFieldEvaluationResultsMap == null) {
			_ddmFormFieldEvaluationResultsMap =
				createDDMFormFieldEvaluationResultsMap();
		}

		return _ddmFormFieldEvaluationResultsMap.get(ddmFormFieldName);
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

	protected boolean isReadOnly(DDMFormField ddmFormField) {
		if (_ddmFormRenderingContext.isReadOnly() ||
			ddmFormField.isReadOnly()) {

			return true;
		}

		return false;
	}

	protected boolean isValid(String ddmFormFieldName) {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			getDDMFormFieldEvaluationResult(ddmFormFieldName);

		if (ddmFormFieldEvaluationResult != null) {
			return ddmFormFieldEvaluationResult.isValid();
		}

		return true;
	}

	protected boolean isVisible(String ddmFormFieldName) {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			getDDMFormFieldEvaluationResult(ddmFormFieldName);

		if (ddmFormFieldEvaluationResult != null) {
			return ddmFormFieldEvaluationResult.isVisible();
		}

		return true;
	}

	protected void setDDMFormEvaluator(DDMFormEvaluator ddmFormEvaluator) {
		_ddmFormEvaluator = ddmFormEvaluator;
	}

	protected void setDDMFormFieldRenderingContextContributedAttributes(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		DDMFormFieldRenderingContextContributor
			ddmFormFieldRenderingContextContributor =
				_ddmFormFieldTypeServicesTracker.
					getDDMFormFieldRenderingContextContributor(
						ddmFormField.getType());

		if (ddmFormFieldRenderingContextContributor != null) {
			ddmFormFieldRenderingContextContributor.addAttributes(
				ddmFormField, ddmFormFieldRenderingContext);
		}
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

	protected void setDDMFormFieldRenderingContextNestedFieldsTemplateContext(
		JSONArray nestedDDMFormFieldsTemplateContext,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		if (nestedDDMFormFieldsTemplateContext.length() == 0) {
			return;
		}

		ddmFormFieldRenderingContext.setNestedDDMFormFieldsTemplateContext(
			nestedDDMFormFieldsTemplateContext);
	}

	protected void setDDMFormFieldRenderingContextReadOnly(
		boolean readOnly,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		ddmFormFieldRenderingContext.setReadOnly(readOnly);
	}

	protected void setDDMFormFieldRenderingContextRepeatable(
		boolean repeatable,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		ddmFormFieldRenderingContext.setRepeatable(repeatable);
	}

	protected void setDDMFormFieldRenderingContextRequired(
		boolean required,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		ddmFormFieldRenderingContext.setRequired(required);
	}

	protected void setDDMFormFieldRenderingContextTemplateNamespace(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws DDMFormRenderingException {

		DDMFormFieldRenderer ddmFormFieldRenderer =
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldRenderer(
				ddmFormField.getType());

		if (ddmFormFieldRenderer == null) {
			throw new DDMFormRenderingException(
				"No DDM form field renderer registered for " +
					ddmFormField.getType());
		}

		if (ddmFormFieldRenderer instanceof BaseDDMFormFieldRenderer) {
			BaseDDMFormFieldRenderer baseDDMFormFieldRenderer =
				(BaseDDMFormFieldRenderer)ddmFormFieldRenderer;

			ddmFormFieldRenderingContext.setTemplateNamespace(
				baseDDMFormFieldRenderer.getTemplateNamespace());
		}
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

	protected void setDDMFormFieldRenderingContextValid(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws DDMFormRenderingException {

		boolean valid = isValid(ddmFormField.getName());

		String errorMessage = StringPool.BLANK;

		if (!valid) {
			DDMFormFieldValidation ddmFormFieldValidation =
				ddmFormField.getDDMFormFieldValidation();

			errorMessage = ddmFormFieldValidation.getErrorMessage();
		}

		ddmFormFieldRenderingContext.setValid(valid);
		ddmFormFieldRenderingContext.setValidationErrorMessage(errorMessage);
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
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws DDMFormRenderingException {

		boolean visible = isVisible(ddmFormField.getName());

		ddmFormFieldRenderingContext.setVisible(visible);
	}

	protected void setDDMFormFieldTypeServicesTracker(
		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker) {

		_ddmFormFieldTypeServicesTracker = ddmFormFieldTypeServicesTracker;
	}

	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	protected JSONObject toJSONObject(Map<String, Object> attributes) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			jsonObject.put(entry.getKey(), entry.getValue());
		}

		return jsonObject;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormFieldsTemplateContextHelper.class);

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