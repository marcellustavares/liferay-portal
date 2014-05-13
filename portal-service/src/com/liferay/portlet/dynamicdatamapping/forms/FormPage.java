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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pablo Carvalho
 */
public class FormPage {

	public Map<String, FormField> getFieldsMap(boolean includeNestedFields) {
		Map<String, FormField> fieldsMap = new HashMap<String, FormField>();

		for (FormSection section : _sections) {
			fieldsMap.putAll(section.getFieldsMap(includeNestedFields));
		}

		return fieldsMap;
	}

	public List<FormSection> getSections() {
		return _sections;
	}

	public void setSections(List<FormSection> sections) {
		_sections = sections;
	}

	private List<FormSection> _sections;

}