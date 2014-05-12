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

package com.liferay.portlet.dynamicdatamapping.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Pablo Carvalho
 */
public class FlatFieldRepresentation {

	public void addFieldValue(FormFieldValue fieldValue) {
		List<FormFieldValue> currentValues = _fields.get(
			fieldValue.getFieldName());

		if (currentValues == null) {
			currentValues = new ArrayList<FormFieldValue>();

			_fields.put(fieldValue.getFieldName(), currentValues);
		}

		currentValues.add(fieldValue);
	}

	public void addFieldValue(Iterable<FormFieldValue> fieldValues) {
		for (FormFieldValue fieldValue : fieldValues) {
			addFieldValue(fieldValue);
		}
	}

	public Set<String> getFieldsNames() {
		return _fields.keySet();
	}

	public List<FormFieldValue> getFieldValue(String fieldName) {
		return _fields.get(fieldName);
	}

	private Map<String, List<FormFieldValue>> _fields =
		new HashMap<String, List<FormFieldValue>>();

}