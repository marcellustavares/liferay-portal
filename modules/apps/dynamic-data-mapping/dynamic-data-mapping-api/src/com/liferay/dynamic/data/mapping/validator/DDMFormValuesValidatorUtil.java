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

package com.liferay.dynamic.data.mapping.validator;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

/**
 * @author Marcellus Tavares
 * @author Leonardo Barros
 */
public class DDMFormValuesValidatorUtil {

	public static DDMFormValuesValidator getDDMFormValuesValidator() {
		PortalRuntimePermission.checkGetBeanProperty(
			DDMFormValuesValidatorUtil.class);

		return _serviceTracker.getService();
	}

	public static void validate(DDMFormValues ddmFormValues)
		throws StorageException {

		DDMFormValuesValidator ddmFormValuesValidator =
			getDDMFormValuesValidator();

		ddmFormValuesValidator.validate(ddmFormValues);
	}

	private static final 
		ServiceTracker<DDMFormValuesValidator, 
			DDMFormValuesValidator> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			DDMFormValuesValidatorUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), DDMFormValuesValidator.class, null);

		_serviceTracker.open();
	}

}