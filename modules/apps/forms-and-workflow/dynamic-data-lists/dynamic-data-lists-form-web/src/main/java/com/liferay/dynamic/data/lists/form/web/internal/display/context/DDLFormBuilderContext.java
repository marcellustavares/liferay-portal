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

package com.liferay.dynamic.data.lists.form.web.internal.display.context;

import com.liferay.dynamic.data.lists.form.web.internal.util.DDMFormTemplateContextVisitor;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextFactory;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormSuccessPageSettings;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.dynamic.data.mapping.util.DDMFormLayoutFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Marcellus Tavares
 */
public class DDLFormBuilderContext {

	public DDLFormBuilderContext(
		Optional<DDLRecordSet> recordSetOptional,
		DDMFormTemplateContextFactory ddmFormTemplateContextFactory,
		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker,
		Locale locale, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, JSONFactory jsonFactory) {

		_recordSetOptional = recordSetOptional;
		_ddmFormFieldTypeServicesTracker = ddmFormFieldTypeServicesTracker;
		_ddmFormTemplateContextFactory = ddmFormTemplateContextFactory;
		_locale = locale;
		_httpServletRequest = httpServletRequest;
		_httpServletResponse = httpServletResponse;
		_jsonFactory = jsonFactory;

	}

	public Map<String, Object> create() {
		Optional<Map<String, Object>> contextOptional = _recordSetOptional.map(
			this::createStateContext);

		return contextOptional.orElseGet(this::createEmptyStateContext);
	}

	protected Map<String, Object> createEmptyStateContext() {
		Map<String, Object> emptyStateContext = new HashMap<>();

		emptyStateContext.put("pages", new ArrayList<>());
		emptyStateContext.put("rules", new ArrayList<>());

		Map<String, Object> successPage = new HashMap<>();

		successPage.put("body", StringPool.BLANK);
		successPage.put("enabled", Boolean.FALSE);
		successPage.put("title", StringPool.BLANK);

		emptyStateContext.put("successPage", successPage);

		return emptyStateContext;
	}

	protected Map<String, Object> createStateContext(DDLRecordSet recordSet) {
		try {
			return doCreateStateContext(recordSet);
		}
		catch (PortalException pe) {
			pe.printStackTrace();
		}

		return createEmptyStateContext();
	}

	protected Map<String, Object> doCreateStateContext(DDLRecordSet recordSet)
		throws PortalException {

		Map<String, Object> stateContext = new HashMap<>();

		DDMStructure ddmStructure = recordSet.getDDMStructure();

		DDMForm ddmForm = ddmStructure.getDDMForm();
		DDMFormLayout ddmFormLayout = ddmStructure.getDDMFormLayout();

		stateContext.put("pages", createFormContext(ddmForm, ddmFormLayout).get("pages"));
		stateContext.put("rules", new ArrayList<>());

		Map<String, Object> successPage = new HashMap<>();

		DDMFormSuccessPageSettings ddmFormSuccessPageSettings =
			ddmForm.getDDMFormSuccessPageSettings();

		successPage.put("body", ddmFormSuccessPageSettings.getBody());
		successPage.put("enabled", ddmFormSuccessPageSettings.isEnabled());
		successPage.put("title", ddmFormSuccessPageSettings.getTitle());

		stateContext.put("successPage", successPage);

		return stateContext;
	}

	protected Map<String, Object> createFormContext(
			DDMForm ddmForm, DDMFormLayout ddmFormLayout)
		throws PortalException {

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(_locale);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setHttpServletResponse(_httpServletResponse);
		ddmFormRenderingContext.setPortletNamespace(StringPool.BLANK);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormLayout, ddmFormRenderingContext);

		populateDDMFormFieldSettingsContext(
			ddmFormTemplateContext, ddmForm.getDDMFormFieldsMap(true));

		return ddmFormTemplateContext;
	}

	private void populateDDMFormFieldSettingsContext(
		Map<String, Object> ddmFormTemplateContext,
		Map<String, DDMFormField> ddmFormFieldsMap) {

		DDMFormTemplateContextVisitor ddmFormTemplateContextVisitor =
			new DDMFormTemplateContextVisitor(ddmFormTemplateContext);

		ddmFormTemplateContextVisitor.onVisitField(
			new Consumer<Map<String,Object>>() {

				@Override
				public void accept(Map<String, Object> fieldContext) {
					String fieldName = MapUtil.getString(
						fieldContext, "fieldName");

					try {
						fieldContext.put(
							"settingsContext",
							doCreateDDMFormFieldSettingContext(
								ddmFormFieldsMap.get(fieldName)));
					}
					catch (PortalException pe) {
						pe.printStackTrace();
					}
				}

			});

		ddmFormTemplateContextVisitor.visit();

	}

	protected Map<String, Object> doCreateDDMFormFieldSettingContext(
			DDMFormField ddmFormField)
		throws PortalException {

		DDMFormFieldType ddmFormFieldType =
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldType(
				ddmFormField.getType());

		DDMForm ddmForm = DDMFormFactory.create(
			ddmFormFieldType.getDDMFormFieldTypeSettings());

		DDMFormLayout ddmFormLayout = DDMFormLayoutFactory.create(
			ddmFormFieldType.getDDMFormFieldTypeSettings());

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(_locale);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setHttpServletResponse(_httpServletResponse);
		ddmFormRenderingContext.setPortletNamespace(StringPool.BLANK);
		ddmFormRenderingContext.setContainerId("settings");

		DDMFormValues ddmFormValues =
			doCreateDDMFormFieldSettingContextDDMFormValues(
				ddmForm, ddmFormField);

		ddmFormRenderingContext.setDDMFormValues(ddmFormValues);

		return _ddmFormTemplateContextFactory.create(
			ddmForm, ddmFormLayout, ddmFormRenderingContext);
	}

	protected DDMFormValues doCreateDDMFormFieldSettingContextDDMFormValues(
		DDMForm ddmFormFieldTypeSettingsDDMForm,
		DDMFormField ddmFormField) {

		Map<String, Object> ddmFormFieldProperties =
			ddmFormField.getProperties();

		DDMFormValues ddmFormValues = new DDMFormValues(
			ddmFormFieldTypeSettingsDDMForm);

		for (DDMFormField ddmFormFieldTypeSetting :
				ddmFormFieldTypeSettingsDDMForm.getDDMFormFields()) {

			DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

			String propertyName = ddmFormFieldTypeSetting.getName();

			ddmFormFieldValue.setName(propertyName);

			Value value = doCreateDDMFormFieldValue(
				ddmFormFieldTypeSetting,
				ddmFormFieldProperties.get(propertyName),
				ddmFormField.getDDMForm().getAvailableLocales());

			ddmFormFieldValue.setValue(value);

			ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
		}

		return ddmFormValues;
	}

	protected Value doCreateDDMFormFieldValue(
		DDMFormField ddmFormFieldTypeSetting, Object propertyValue,
		Set<Locale> availableLocales) {

		if (ddmFormFieldTypeSetting.isLocalizable()) {
			return (LocalizedValue)propertyValue;
		}

		String dataType = ddmFormFieldTypeSetting.getDataType();

		if (Objects.equals(dataType, "ddm-options")) {
			return doCreateDDMFormFieldValue(
				(DDMFormFieldOptions)propertyValue, availableLocales);
		}
		else if (Objects.equals(
					ddmFormFieldTypeSetting.getType(), "validation")) {

			return doCreateDDMFormFieldValue(
				(DDMFormFieldValidation)propertyValue);
		}
		else {
			return new UnlocalizedValue(String.valueOf(propertyValue));
		}
	}

	protected Value doCreateDDMFormFieldValue(
		DDMFormFieldOptions ddmFormFieldOptions,
		Set<Locale> availableLocales) {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		for (Locale availableLocale : availableLocales) {
			jsonObject.put(
				LocaleUtil.toLanguageId(availableLocale),
				createOptions(ddmFormFieldOptions, availableLocale));

		}

		return new UnlocalizedValue(jsonObject.toString());
	}

	protected JSONArray createOptions(
		DDMFormFieldOptions ddmFormFieldOptions, Locale locale) {

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			JSONObject jsonObject = _jsonFactory.createJSONObject();

			LocalizedValue label = ddmFormFieldOptions.getOptionLabels(
				optionValue);

			jsonObject.put("label", label.getString(locale));
			jsonObject.put("value", optionValue);

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	protected Value doCreateDDMFormFieldValue(
		DDMFormFieldValidation ddmFormFieldValidation) {

		return null;
	}

	private DDMFormTemplateContextFactory _ddmFormTemplateContextFactory;
	private final Optional<DDLRecordSet> _recordSetOptional;
	private Locale _locale;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;
	private JSONFactory _jsonFactory;

}