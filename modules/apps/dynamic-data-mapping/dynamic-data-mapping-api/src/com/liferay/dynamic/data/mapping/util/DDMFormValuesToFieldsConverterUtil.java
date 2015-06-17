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

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesToFieldsConverterUtil {

	public static Fields convert(
			DDMStructure ddmStructure, DDMFormValues ddmFormValues)
		throws PortalException {

		return getDDMFormValuesToFieldsConverter().convert(
			ddmStructure, ddmFormValues);
	}

	public static DDMFormValuesToFieldsConverter
		getDDMFormValuesToFieldsConverter() {

		PortalRuntimePermission.checkGetBeanProperty(
			DDMFormValuesToFieldsConverterUtil.class);

		return _serviceTracker.getService();
	}

	private static final
		ServiceTracker<DDMFormValuesToFieldsConverter,
			DDMFormValuesToFieldsConverter> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			DDMFormValuesToFieldsConverterUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), DDMFormValuesToFieldsConverter.class,
			null);

		_serviceTracker.open();
	}

}