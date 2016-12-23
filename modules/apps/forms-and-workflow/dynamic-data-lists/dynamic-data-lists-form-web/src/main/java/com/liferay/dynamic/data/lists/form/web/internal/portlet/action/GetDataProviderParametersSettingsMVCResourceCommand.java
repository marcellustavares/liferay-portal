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

package com.liferay.dynamic.data.lists.form.web.internal.portlet.action;

import com.liferay.dynamic.data.lists.form.web.constants.DDLFormPortletKeys;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DDLFormPortletKeys.DYNAMIC_DATA_LISTS_FORM_ADMIN,
		"mvc.command.name=getDataProviderParametersSettings"
	},
	service = MVCResourceCommand.class
)
public class GetDataProviderParametersSettingsMVCResourceCommand
	extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Locale locale = themeDisplay.getLocale();

		long ddmDataProviderInstanceId = ParamUtil.getLong(
			resourceRequest, "ddmDataProviderInstanceId");

		DDMFormValues dataProviderFormValues = getDataProviderFormValues(
			ddmDataProviderInstanceId);

		JSONObject parametersJSONObject = _jsonFactory.createJSONObject();

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			dataProviderFormValues.getDDMFormFieldValuesMap();

		parametersJSONObject.put(
			"inputs", getInputParameters(ddmFormFieldValuesMap, locale));
		parametersJSONObject.put(
			"outputs", getOutputParameters(ddmFormFieldValuesMap, locale));

		resourceResponse.setContentType(ContentTypes.APPLICATION_JSON);

		PortletResponseUtil.write(
			resourceResponse, parametersJSONObject.toJSONString());

		resourceResponse.flushBuffer();
	}

	protected DDMFormValues getDataProviderFormValues(
			long ddmDataProviderInstanceId)
		throws PortalException {

		DDMDataProviderInstance ddmDataProviderInstance =
			_ddmDataProviderInstanceLocalService.getDataProviderInstance(
				ddmDataProviderInstanceId);

		DDMDataProvider ddmDataProvider =
			_ddmDataProviderTracker.getDDMDataProvider(
				ddmDataProviderInstance.getType());

		DDMForm ddmForm = DDMFormFactory.create(ddmDataProvider.getSettings());

		return _ddmFormValuesJSONDeserializer.deserialize(
			ddmForm, ddmDataProviderInstance.getDefinition());
	}

	protected JSONArray getInputParameters(
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap,
		Locale locale) {

		List<DDMFormFieldValue> inputsDDMFormFieldValueList =
			ddmFormFieldValuesMap.get("inputParameters");

		JSONArray inputsJSONArray = _jsonFactory.createJSONArray();

		for (DDMFormFieldValue inputDDMFormFieldValue :
				inputsDDMFormFieldValueList) {

			JSONObject inputJSONObject = _jsonFactory.createJSONObject();

			Map<String, List<DDMFormFieldValue>>
				inputNestedDDMFormFieldValuesMap =
					inputDDMFormFieldValue.getNestedDDMFormFieldValuesMap();

			Value value = getValue(
				"inputParameterName", inputNestedDDMFormFieldValuesMap);

			inputJSONObject.put("name", value.getString(locale));

			value = getValue(
				"inputParameterRequired", inputNestedDDMFormFieldValuesMap);

			inputJSONObject.put(
				"required", GetterUtil.getBoolean(value.getString(locale)));

			value = getValue(
				"inputParameterType", inputNestedDDMFormFieldValuesMap);

			inputJSONObject.put("type", value.getString(locale));

			inputsJSONArray.put(inputJSONObject);
		}

		return inputsJSONArray;
	}

	protected JSONArray getOutputParameters(
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap,
		Locale locale) {

		List<DDMFormFieldValue> outputDDMFormFieldValueList =
			ddmFormFieldValuesMap.get("outputParameters");

		JSONArray outputsJSONArray = _jsonFactory.createJSONArray();

		for (DDMFormFieldValue outputDDMFormFieldValue :
				outputDDMFormFieldValueList) {

			JSONObject outputJSONObject = _jsonFactory.createJSONObject();

			Map<String, List<DDMFormFieldValue>>
				nestedOutputDDMFormFieldValuesMap =
					outputDDMFormFieldValue.getNestedDDMFormFieldValuesMap();

			Value value = getValue(
				"outputParameterName", nestedOutputDDMFormFieldValuesMap);

			outputJSONObject.put("name", value.getString(locale));

			value = getValue(
				"outputParameterType", nestedOutputDDMFormFieldValuesMap);

			outputJSONObject.put("type", value.getString(locale));

			outputsJSONArray.put(outputJSONObject);
		}

		return outputsJSONArray;
	}

	protected Value getValue(
		String fieldName,
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap) {

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			fieldName);

		if (ListUtil.isNotEmpty(ddmFormFieldValues)) {
			DDMFormFieldValue firstDDMFormFieldValue = ddmFormFieldValues.get(
				0);

			return firstDDMFormFieldValue.getValue();
		}

		return new UnlocalizedValue(StringPool.BLANK);
	}

	@Reference
	private DDMDataProviderInstanceLocalService
		_ddmDataProviderInstanceLocalService;

	@Reference
	private DDMDataProviderTracker _ddmDataProviderTracker;

	@Reference
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;

	@Reference
	private JSONFactory _jsonFactory;

}