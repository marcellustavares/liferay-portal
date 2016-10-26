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

package com.liferay.portlet.dynamicdatamapping.util;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesFactoryUtil {

	public static DDMFormValues create(
		DDMForm ddmForm, HttpServletRequest httpServletRequest) {

		return getDDMFormValuesFactory().create(ddmForm, httpServletRequest);
	}

	public static DDMFormValues create(
		DDMForm ddmForm, PortletRequest portletRequest) {

		return getDDMFormValuesFactory().create(ddmForm, portletRequest);
	}

	public static DDMFormValues create(
		DDMForm ddmForm, ServiceContext serviceContext) {

		return getDDMFormValuesFactory().create(ddmForm, serviceContext);
	}

	public static DDMFormValuesFactory getDDMFormValuesFactory() {
		PortalRuntimePermission.checkGetBeanProperty(
			DDMFormValuesFactoryUtil.class);

		return _ddmFormValuesFactory;
	}

	public void setDDMFormValuesFactory(
		DDMFormValuesFactory ddmFormValuesFactory) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_ddmFormValuesFactory = ddmFormValuesFactory;
	}

	private static DDMFormValuesFactory _ddmFormValuesFactory;

}