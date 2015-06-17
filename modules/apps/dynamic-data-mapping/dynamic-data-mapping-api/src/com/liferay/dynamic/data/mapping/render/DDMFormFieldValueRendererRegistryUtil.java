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

package com.liferay.dynamic.data.mapping.render;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

/**
 * @author Marcellus Tavares
 * @author Leonardo Barros
 */
public class DDMFormFieldValueRendererRegistryUtil {

	public static DDMFormFieldValueRenderer getDDMFormFieldValueRenderer(
		String ddmFormFieldType) {

		DDMFormFieldValueRendererRegistry ddmFormFieldValueRendererRegistry =
			getDDMFormFieldValueRendererRegistry();

		return ddmFormFieldValueRendererRegistry.getDDMFormFieldValueRenderer(
			ddmFormFieldType);
	}

	public static DDMFormFieldValueRendererRegistry
		getDDMFormFieldValueRendererRegistry() {

		PortalRuntimePermission.checkGetBeanProperty(
			DDMFormFieldValueRendererRegistryUtil.class);

		return _serviceTracker.getService();
	}

	private static final 
		ServiceTracker<DDMFormFieldValueRendererRegistry, 
			DDMFormFieldValueRendererRegistry> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			DDMFormFieldValueRendererRegistryUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), DDMFormFieldValueRendererRegistry.class, 
			null);

		_serviceTracker.open();
	}

}