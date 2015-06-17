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

package com.liferay.dynamic.data.mapping.registry;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import java.util.List;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Marcellus Tavares
 * @author Leonardo Barros
 */
public class DDMFormFieldTypeRegistryUtil {

	public static DDMFormFieldType getDDMFormFieldType(String name) {
		return getDDMFormFieldTypeRegistry().getDDMFormFieldType(name);
	}

	public static Set<String> getDDMFormFieldTypeNames() {
		return getDDMFormFieldTypeRegistry().getDDMFormFieldTypeNames();
	}

	public static DDMFormFieldTypeRegistry getDDMFormFieldTypeRegistry() {
		PortalRuntimePermission.checkGetBeanProperty(
			DDMFormFieldTypeRegistryUtil.class);

		return _serviceTracker.getService();
	}

	public static List<DDMFormFieldType> getDDMFormFieldTypes() {
		return getDDMFormFieldTypeRegistry().getDDMFormFieldTypes();
	}

	private static final 
		ServiceTracker<DDMFormFieldTypeRegistry, DDMFormFieldTypeRegistry> 
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			DDMFormFieldTypeRegistryUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), DDMFormFieldTypeRegistry.class, null);

		_serviceTracker.open();
	}

}