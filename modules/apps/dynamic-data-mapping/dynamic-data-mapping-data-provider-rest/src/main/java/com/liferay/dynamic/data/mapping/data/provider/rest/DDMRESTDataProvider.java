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

package com.liferay.dynamic.data.mapping.data.provider.rest;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.rest.DDMRESTDataProviderSettings.RESTSettings;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, property = "ddm.data.provider.name=rest")
public class DDMRESTDataProvider implements DDMDataProvider {

	@Override
	public Set<KeyValuePair> getData(
			DDMDataProviderContext ddmDataProviderContext)
		throws DDMDataProviderException {

		try {
			DDMRESTDataProviderResult dataProviderResult =
				getCachedDataProviderResult(ddmDataProviderContext);

			if (Validator.isNotNull(dataProviderResult)) {
				return dataProviderResult._getKeyValueSet();
			}

			return doGetData(ddmDataProviderContext);
		}
		catch (PortalException pe) {
			throw new DDMDataProviderException(pe);
		}
	}

	@Override
	public String getValue(
			DDMDataProviderContext ddmDataProviderContext, String key)
		throws DDMDataProviderException {

		try {
			DDMRESTDataProviderResult dataProviderResult =
				getCachedDataProviderResult(ddmDataProviderContext);

			if (Validator.isNull(dataProviderResult)) {
				doGetData(ddmDataProviderContext);
			}

			dataProviderResult = getCachedDataProviderResult(
				ddmDataProviderContext);

			KeyValuePair keyValuePair = new KeyValuePair(key, null);

			return dataProviderResult._getValue(keyValuePair);
		}
		catch (PortalException pe) {
			throw new DDMDataProviderException(pe);
		}
	}

	protected Set<KeyValuePair> doGetData(
			DDMDataProviderContext ddmDataProviderContext)
		throws PortalException {

		RESTSettings restSettings = ddmDataProviderContext.getSettings(
			RESTSettings.class);

		HttpRequest httpRequest = HttpRequest.get(restSettings.url());

		httpRequest.basicAuthentication(
			restSettings.username(), restSettings.password());
		httpRequest.query(ddmDataProviderContext.getParameters());

		HttpResponse httpResponse = httpRequest.send();

		JSONArray jsonArray = _jsonFactory.createJSONArray(httpResponse.body());

		List<KeyValuePair> results = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String key = jsonObject.getString(restSettings.key());
			String value = jsonObject.getString(restSettings.value());

			results.add(new KeyValuePair(key, value));
		}

		DDMRESTDataProviderResult dataProviderResult =
			new DDMRESTDataProviderResult(results);

		_portalCache.put(restSettings.url(), dataProviderResult);

		return dataProviderResult._getKeyValueSet();
	}

	protected DDMRESTDataProviderResult getCachedDataProviderResult(
		DDMDataProviderContext ddmDataProviderContext) {

		RESTSettings restSettings = ddmDataProviderContext.getSettings(
			RESTSettings.class);

		return _portalCache.get(restSettings.url());
	}

	@Reference
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private static final
		PortalCache<String, DDMRESTDataProviderResult>
			_portalCache = MultiVMPoolUtil.getPortalCache(
				DDMRESTDataProvider.class.getName());

	private JSONFactory _jsonFactory;

	private class DDMRESTDataProviderResult implements Serializable {

		private DDMRESTDataProviderResult(List<KeyValuePair> keyValueSet) {
			for (KeyValuePair keyValuePair : keyValueSet) {
				_keyValueMap.put(keyValuePair, keyValuePair.getValue());
			}
		}

		private Set<KeyValuePair> _getKeyValueSet() {
			return _keyValueMap.keySet();
		}

		private String _getValue(KeyValuePair keyValuePair) {
			if (_keyValueMap.containsKey(keyValuePair)) {
				return _keyValueMap.get(keyValuePair);
			}

			return StringPool.BLANK;
		}

		private final Map<KeyValuePair, String> _keyValueMap = new TreeMap<>(
			new KeyValuePairComparator(false, true));

	}

}