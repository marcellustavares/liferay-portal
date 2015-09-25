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

package com.liferay.dynamic.data.mapping.data.provider.internal.servlet;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderSettings;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.registry.DDMFormFactory;
import com.liferay.osgi.service.tracker.map.ServiceTrackerCustomizerFactory;
import com.liferay.osgi.service.tracker.map.ServiceTrackerCustomizerFactory.ServiceWrapper;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/ddm-data-provider",
		"osgi.http.whiteboard.servlet.name=Dynamic Data Mapping Data Provider Servlet",
		"osgi.http.whiteboard.servlet.pattern=/ddm-data-provider/*"
	},
	service = Servlet.class
)
public class DDMDataProviderServlet extends HttpServlet {

	@Activate
	protected void activate(BundleContext bundleContext)
		throws InvalidSyntaxException {

		_ddmDataProviderServiceTrackerMap =
			ServiceTrackerMapFactory.singleValueMap(
				bundleContext, DDMDataProvider.class, "ddm.data.provider.name",
			ServiceTrackerCustomizerFactory.<DDMDataProvider>serviceWrapper(
				bundleContext));

		_ddmDataProviderServiceTrackerMap.open();

		_ddmDataProviderSettingsServiceTrackerMap =
			ServiceTrackerMapFactory.singleValueMap(
				bundleContext, DDMDataProviderSettings.class,
				"ddm.data.provider.name");

		_ddmDataProviderSettingsServiceTrackerMap.open();
	}

	@Deactivate
	protected void deactivate() {
		_ddmDataProviderServiceTrackerMap.close();

		_ddmDataProviderSettingsServiceTrackerMap.close();
	}

	@Override
	protected void doGet(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		String cmd = ParamUtil.getString(request, "cmd");

		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		response.setContentType(ContentTypes.APPLICATION_JSON);
		response.setStatus(HttpServletResponse.SC_OK);

		if (Validator.equals(cmd, "list")) {
			Set<String> dataProviderKeys =
				_ddmDataProviderServiceTrackerMap.keySet();

			ServletResponseUtil.write(
				response, jsonSerializer.serializeDeep(dataProviderKeys));
		}
		else if (Validator.equals(cmd, "getSettings")) {
			String name = ParamUtil.getString(request, "name");

			DDMDataProviderSettings ddmDataProviderSettings =
				_ddmDataProviderSettingsServiceTrackerMap.getService(name);

			DDMForm ddmForm = DDMFormFactory.create(
				ddmDataProviderSettings.getSettings());

			ServletResponseUtil.write(
				response, _ddmFormJSONSerializer.serialize(ddmForm));
		}
		else {
			JSONArray optionsJSONArray = JSONFactoryUtil.createJSONArray();

			try {
				String name = ParamUtil.getString(request, "name");
				String settings = ParamUtil.getString(request, "settings");

				JSONObject datasourceJONObject =
					JSONFactoryUtil.createJSONObject(settings);

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

				DDMDataProvider ddmDataProvider =
					_ddmDataProviderServiceTrackerMap.getService(
						name).getService();

				List<KeyValuePair> data = ddmDataProvider.getData(
					ddmDataProviderContext);

				for (KeyValuePair keyValuePair : data) {
					JSONObject optionJSONObject =
						JSONFactoryUtil.createJSONObject();

					optionJSONObject.put("label", keyValuePair.getKey());
					optionJSONObject.put("value", keyValuePair.getValue());

					optionsJSONArray.put(optionJSONObject);
				}
			}
			catch (Exception e) {
			}

			ServletResponseUtil.write(response, optionsJSONArray.toString());
		}
	}

	@Reference
	protected void setDDMFormJSONSerializer(
		DDMFormJSONSerializer ddmFormJSONSerializer) {

		_ddmFormJSONSerializer = ddmFormJSONSerializer;
	}

	@Reference
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private static final long serialVersionUID = 1L;

	private ServiceTrackerMap<String, ServiceWrapper<DDMDataProvider>>
		_ddmDataProviderServiceTrackerMap;
	private ServiceTrackerMap<String, DDMDataProviderSettings>
		_ddmDataProviderSettingsServiceTrackerMap;
	private DDMFormJSONSerializer _ddmFormJSONSerializer;
	private JSONFactory _jsonFactory;

}