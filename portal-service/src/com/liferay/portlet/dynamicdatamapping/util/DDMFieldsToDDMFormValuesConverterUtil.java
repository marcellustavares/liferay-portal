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
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;

/**
 * @author Marcellus Tavares
 */
public class DDMFieldsToDDMFormValuesConverterUtil {

	public static DDMFormValues convert(
			DDMStructure ddmStructure, Fields ddmFields)
		throws Exception {

		return getDDMFieldsToDDMFormValuesConverter().convert(
			ddmStructure, ddmFields);
	}

	public static DDMFieldsToDDMFormValuesConverter
		getDDMFieldsToDDMFormValuesConverter() {

		PortalRuntimePermission.checkGetBeanProperty(
			DDMFieldsToDDMFormValuesConverterUtil.class);

		return _ddmFieldsToDDMFormValuesConverter;
	}

	public void setDDMFieldsToDDMFormValuesConverter(
		DDMFieldsToDDMFormValuesConverter ddmFieldsToDDMFormValuesConverter) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_ddmFieldsToDDMFormValuesConverter = ddmFieldsToDDMFormValuesConverter;
	}

	private static DDMFieldsToDDMFormValuesConverter
		_ddmFieldsToDDMFormValuesConverter;

}