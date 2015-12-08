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

package com.liferay.portal.template.soy;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.template.ClassLoaderResourceParser;

import java.net.URL;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

/**
 * @author Marcellus Tavares
 */
public class SoyTemplateResourceParser extends ClassLoaderResourceParser {

	public SoyTemplateResourceParser() {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		BundleReference bundleReference = (BundleReference)classLoader;

		Bundle bundle = bundleReference.getBundle();

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		populateSoyBundleProvidersMap(bundleWiring.getProvidedWires("soy"));
	}

	@Override
	public URL getURL(String templateId) {
		String[] templateIdParts = StringUtil.split(
			templateId, StringPool.POUND);

		if (templateIdParts.length != 3) {
			throw new IllegalArgumentException(
				String.format(
					"The templateId %s do not map to a valid soy template " +
						"reference",
					templateId));
		}

		String providerBundleKey = getProviderBundleKey(
			templateIdParts[0], templateIdParts[1]);

		Bundle bundle = _soyBundleProvidersMap.get(providerBundleKey);

		return bundle.getResource(templateIdParts[2]);
	}

	protected String getProviderBundleKey(BundleCapability bundleCapability) {
		Map<String, Object> attributes = bundleCapability.getAttributes();

		String type = (String)attributes.get("type");

		Version version = (Version)attributes.get("version");

		return getProviderBundleKey(type, version.getQualifier());
	}

	protected String getProviderBundleKey(String type, String version) {
		return type.concat(StringPool.DOUBLE_UNDERLINE).concat(version);
	}

	protected void populateSoyBundleProvidersMap(
		List<BundleWire> soyBundleWireProviders) {

		for (BundleWire providerBundleWire : soyBundleWireProviders) {
			String providerBundleKey = getProviderBundleKey(
				providerBundleWire.getCapability());

			BundleRevision bundleRevision = providerBundleWire.getProvider();

			_soyBundleProvidersMap.put(
				providerBundleKey, bundleRevision.getBundle());
		}
	}

	private final Map<String, Bundle> _soyBundleProvidersMap =
		new ConcurrentHashMap<>();

}