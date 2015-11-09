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

package com.liferay.dynamic.data.mapping.type.select;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderSettings;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializerUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.registry.BaseDDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.registry.DDMFormFactory;
import com.liferay.dynamic.data.mapping.registry.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Renato Rego
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=select",
	service = DDMFormFieldRenderer.class
)
public class SelectDDMFormFieldRenderer extends BaseDDMFormFieldRenderer {

	@Override
	public String getTemplateLanguage() {
		return TemplateConstants.LANG_TYPE_SOY;
	}

	@Override
	public String getTemplateNamespace() {
		return "ddm.select";
	}

	@Override
	public TemplateResource getTemplateResource() {
		return _templateResource;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_templateResource = getTemplateResource(
			"/META-INF/resources/select.soy");
	}

	protected List<Object> getOptions(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws PortalException, PrincipalException {

		DDMFormFieldOptions ddmFormFieldOptions;

		String dataSourceType = (String)ddmFormField.getProperty(
			"dataSourceType");

		if (Validator.equals(dataSourceType, "manual")) {
			ddmFormFieldOptions = ddmFormField.getDDMFormFieldOptions();
		}
		else {
			long ddmDataProviderId = GetterUtil.getLong(
				ddmFormField.getProperty("ddmDataProviderId"));

			ddmFormFieldOptions = getOptionsFromDataProvider(ddmDataProviderId);
		}

		SelectDDMFormFieldContextHelper selectDDMFormFieldContextHelper =
			new SelectDDMFormFieldContextHelper(
				ddmFormFieldOptions, ddmFormFieldRenderingContext.getValue(),
				ddmFormField.getPredefinedValue(),
				ddmFormFieldRenderingContext.getLocale());

		return selectDDMFormFieldContextHelper.getOptions();
	}

	@Override
	protected void populateRequiredContext(
		Template template, DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		super.populateRequiredContext(
			template, ddmFormField, ddmFormFieldRenderingContext);

		template.put(
			"multiple",
			ddmFormField.isMultiple() ? "multiple" : StringPool.BLANK);

		try {
			template.put(
				"options",
				getOptions(ddmFormField, ddmFormFieldRenderingContext));
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "unregisterDDMDataProvider"
	)
	protected synchronized void registerDDMDataProvider(
		DDMDataProvider ddmDataProvider, Map<String, Object> properties) {

		Object dataProviderName = properties.get("ddm.data.provider.name");

		_ddmDataProviders.put(
			String.valueOf(dataProviderName), ddmDataProvider);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "unregisterDDMDataProviderSettings"
	)
	protected synchronized void registerDDMDataProviderSettings(
		DDMDataProviderSettings ddmDataProviderSettings,
		Map<String, Object> properties) {

		Object dataProviderName = properties.get("ddm.data.provider.name");

		_ddmDataProviderSettings.put(
			String.valueOf(dataProviderName), ddmDataProviderSettings);
	}

	@Reference
	protected void setDDMDataProviderService(
		DDMDataProviderService ddmDataProviderService) {

		_ddmDataProviderService = ddmDataProviderService;
	}

	protected synchronized void unregisterDDMDataProvider(
		DDMDataProvider ddmDataProvider, Map<String, Object> properties) {

		Object dataProviderName = properties.get("ddm.data.provider.name");

		_ddmDataProviders.remove(String.valueOf(dataProviderName));
	}

	protected synchronized void unregisterDDMDataProviderSettings(
		DDMDataProviderSettings ddmDataProviderSettings,
		Map<String, Object> properties) {

		Object dataProviderName = properties.get("ddm.data.provider.name");

		_ddmDataProviderSettings.remove(String.valueOf(dataProviderName));
	}

	private DDMFormFieldOptions getOptionsFromDataProvider(
			long ddmDataProviderId)
		throws PortalException, PrincipalException {

		com.liferay.dynamic.data.mapping.model.DDMDataProvider
			ddmDataProviderEntry = _ddmDataProviderService.getDataProvider(
				ddmDataProviderId);

		String type = ddmDataProviderEntry.getType();

		DDMDataProvider ddmDataProvider = _ddmDataProviders.get(type);

		DDMDataProviderSettings ddmDataProviderSettings =
			_ddmDataProviderSettings.get(type);

		DDMForm ddmForm = DDMFormFactory.create(
			ddmDataProviderSettings.getSettings());

		DDMFormValues ddmFormValues =
			DDMFormValuesJSONDeserializerUtil.deserialize(
				ddmForm, ddmDataProviderEntry.getData());

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Map<String, Object> properties = new HashMap<>();

		for (Map.Entry<String, List<DDMFormFieldValue>> entry
			: ddmFormFieldValuesMap.entrySet()) {

			List<DDMFormFieldValue> value = entry.getValue();

			DDMFormFieldValue ddmFormFieldValue = value.get(0);

			properties.put(
				entry.getKey(),
				ddmFormFieldValue.getValue().getString(LocaleUtil.US));
		}

		DDMDataProviderContext ddmDataProviderContext =
			new DDMDataProviderContext(properties);

		List<KeyValuePair> data = ddmDataProvider.getData(
			ddmDataProviderContext);

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		for (KeyValuePair kvp : data) {
			ddmFormFieldOptions.addOptionLabel(
				kvp.getKey(), LocaleUtil.US, kvp.getValue());
		}

		return ddmFormFieldOptions;
	}

	private final Map<String, DDMDataProvider> _ddmDataProviders =
		new ConcurrentHashMap<>();
	private DDMDataProviderService _ddmDataProviderService;
	private final Map<String, DDMDataProviderSettings>
		_ddmDataProviderSettings = new ConcurrentHashMap<>();
	private TemplateResource _templateResource;

}