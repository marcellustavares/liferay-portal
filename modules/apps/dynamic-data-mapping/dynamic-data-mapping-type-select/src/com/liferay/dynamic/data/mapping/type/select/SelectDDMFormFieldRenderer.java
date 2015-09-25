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
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.registry.BaseDDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.registry.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

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
	protected void activate(
			BundleContext bundleContext, Map<String, Object> properties)
		throws InvalidSyntaxException {

		_templateResource = getTemplateResource(
			"/META-INF/resources/select.soy");

		_serviceTracker = ServiceTrackerMapFactory.singleValueMap(
			bundleContext, DDMDataProvider.class, "ddm.data.provider.name");

		_serviceTracker.open();
	}

	protected List<Object> getOptions(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		String datasourceType = (String)ddmFormField.getProperty(
			"datasourceType");

		if (Validator.equals(datasourceType, "datasource")) {
			String datasourceSettings = (String)ddmFormField.getProperty(
				"datasource");

			try {
				JSONObject datasourceJONObject =
					JSONFactoryUtil.createJSONObject(datasourceSettings);

				String datasourceName = datasourceJONObject.getString(
					"datasourceName");

				JSONArray fields = datasourceJONObject.getJSONArray(
					"fieldValues");

				Map<String, Object> properties = new HashMap<>();

				for (int i = 0; i < fields.length(); i++) {
					JSONObject field = fields.getJSONObject(i);

					String key = field.getString("name");
					String value = field.getString("value");

					properties.put(key, value);
				}

				DDMDataProviderContext ddmDataProviderContext =
					new DDMDataProviderContext(properties);

				DDMDataProvider ddmDataProvider = _serviceTracker.getService(
					datasourceName);

				List<Object> options = new ArrayList<>();

				List<KeyValuePair> data = ddmDataProvider.getData(
					ddmDataProviderContext);

				for (KeyValuePair keyValuePair : data) {
					Map<String, String> optionMap = new HashMap<>();

					optionMap.put("label", keyValuePair.getKey());
					optionMap.put("status", StringPool.BLANK);
					optionMap.put("value", keyValuePair.getValue());

					options.add(optionMap);
				}

				return options;
			}
			catch (Exception e) {
				return Collections.emptyList();
			}
		}
		else {
			SelectDDMFormFieldContextHelper selectDDMFormFieldContextHelper =
				new SelectDDMFormFieldContextHelper(
					ddmFormField.getDDMFormFieldOptions(),
					ddmFormFieldRenderingContext.getValue(),
					ddmFormField.getPredefinedValue(),
					ddmFormFieldRenderingContext.getLocale());

			return selectDDMFormFieldContextHelper.getOptions();
		}
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
		template.put(
			"options", getOptions(ddmFormField, ddmFormFieldRenderingContext));
	}

	private ServiceTrackerMap<String, DDMDataProvider> _serviceTracker;
	private TemplateResource _templateResource;

}