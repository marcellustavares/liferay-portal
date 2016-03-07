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

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.exception.PortalException;

import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Eduardo Lundgren
 * @author Marcellus Tavares
 * @author Leonardo Barros
 */
public class DDMUtil {

	public static DDMForm getDDMForm(long classNameId, long classPK)
		throws PortalException {

		return getDDM().getDDMForm(classNameId, classPK);
	}

	public static DDMForm getDDMForm(String serializedJSONDDMForm)
		throws PortalException {

		return getDDM().getDDMForm(serializedJSONDDMForm);
	}

	public static String getDDMFormJSONString(DDMForm ddmForm) {
		return getDDM().getDDMFormJSONString(ddmForm);
	}

	public static DDMFormValues getDDMFormValues(
			DDMForm ddmForm, String serializedJSONDDMFormValues)
		throws PortalException {

		return getDDM().getDDMFormValues(ddmForm, serializedJSONDDMFormValues);
	}

	public static String getDDMFormValuesJSONString(
		DDMFormValues ddmFormValues) {

		return getDDM().getDDMFormValuesJSONString(ddmFormValues);
	}

	public static DDMFormLayout getDefaultDDMFormLayout(DDMForm ddmForm) {
		return getDDM().getDefaultDDMFormLayout(ddmForm);
	}

	public static Fields getFields(
			long ddmStructureId, DDMFormValues ddmFormValues)
		throws PortalException {

		return getDDM().getFields(ddmStructureId, ddmFormValues);
	}

	protected static DDM getDDM() {
		return _serviceTracker.getService();
	}

	private static final ServiceTracker<DDM, DDM> _serviceTracker =
		ServiceTrackerFactory.open(
			FrameworkUtil.getBundle(DDMUtil.class), DDM.class);

}