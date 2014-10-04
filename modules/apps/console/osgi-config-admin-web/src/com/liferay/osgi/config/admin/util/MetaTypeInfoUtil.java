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

package com.liferay.osgi.config.admin.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Kamesh Sampath TODO: add service tracker for MetaTypeService to
 *         update the map
 */
public class MetaTypeInfoUtil {

	public static DDMForm attributeForm(String servicePID) {
		DDMForm ddmForm = new DDMForm();

		AttributeDefinition[] attributeDefinitions = _attributeDefinition(
			servicePID, ObjectClassDefinition.REQUIRED);

		_addFieldToForm(ddmForm, attributeDefinitions, true);

		attributeDefinitions = _attributeDefinition(
			servicePID, ObjectClassDefinition.OPTIONAL);

		_addFieldToForm(ddmForm, attributeDefinitions, false);

		return ddmForm;
	}

	public static void fillOCD(
		MetaTypeInformation mInfo,
		Collection<ObjectClassDefinition> ocdContainer, String... pids) {

		for (String pid : pids) {
			ObjectClassDefinition ocd = mInfo.getObjectClassDefinition(
				pid, null);

			if (ocd != null) {
				_ocdMap.put(pid, ocd);
				ocdContainer.add(ocd);
			}
		}
	}

	private static void _addFieldToForm(
		DDMForm ddmForm, AttributeDefinition[] attributeDefinitions,
		boolean required) {

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String name = attributeDefinition.getName();
			String type = _attributeToDDMType(attributeDefinition);

			DDMFormField ddmFormField = new DDMFormField(name, type);
			ddmFormField.setRequired(required);

			if (attributeDefinition.getCardinality() > 1) {
				ddmFormField.setRepeatable(true);
			}

			ddmForm.getDDMFormFields().add(ddmFormField);
		}
	}

	private static AttributeDefinition[] _attributeDefinition(
		String servicePID, int filter) {

		AttributeDefinition[] attributeDefinitions = null;

		if (_ocdMap != null) {
			ObjectClassDefinition ocd = _ocdMap.get(servicePID);
			attributeDefinitions = ocd.getAttributeDefinitions(filter);
		}

		return attributeDefinitions;
	}

	private static String _attributeToDDMType(
		AttributeDefinition attributeDefinition) {

		int type = attributeDefinition.getType();

		switch (type) {
			case AttributeDefinition.DOUBLE:
			case AttributeDefinition.FLOAT: {
				return "ddm-decimal";
			}

			case AttributeDefinition.INTEGER:
			case AttributeDefinition.LONG:
			case AttributeDefinition.SHORT: {
				return "ddm-integer";
			}

			case AttributeDefinition.BOOLEAN: {
				return "radio";
			}

			default: {
				return "text";
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(MetaTypeInfoUtil.class);

	private static ConcurrentHashMap<String, ObjectClassDefinition> _ocdMap =
		new ConcurrentHashMap<String, ObjectClassDefinition>();

}