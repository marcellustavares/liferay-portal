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

package com.liferay.portlet.dynamicdatamapping.io;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;

/**
 * @author Marcellus Tavares
 */
public class DDMFormLayoutJSONDeserializerUtil {

	public static DDMFormLayout deserialize(String serializedDDMFormLayout)
		throws PortalException {

		return getDDMFormLayoutJSONDeserializer().deserialize(
			serializedDDMFormLayout);
	}

	public static DDMFormLayoutDeserializer
		getDDMFormLayoutJSONDeserializer() {

		PortalRuntimePermission.checkGetBeanProperty(
			DDMFormLayoutJSONDeserializerUtil.class);

		return _ddmFormLayoutDeserializerRegistry.getDDMFormDeserializer();
	}

	public void setDDMFormLayoutDeserializerRegistry(
		DDMFormLayoutDeserializerRegistry ddmFormLayoutDeserializerRegistry)
			{

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_ddmFormLayoutDeserializerRegistry =
			ddmFormLayoutDeserializerRegistry;
	}

	private static DDMFormLayoutDeserializerRegistry
		_ddmFormLayoutDeserializerRegistry;

}