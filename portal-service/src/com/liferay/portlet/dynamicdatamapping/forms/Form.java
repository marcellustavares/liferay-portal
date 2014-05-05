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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Pablo Carvalho
 */
public class Form {

	public void addPage(FormPage page) {
		_pages.add(page);
	}

	public Map<String, FormField> getFieldsMap(boolean includeNestedFields) {
		Map<String, FormField> fieldsMap = new HashMap<String, FormField>();

		for (FormPage page : _pages) {
			fieldsMap.putAll(page.getFieldsMap(includeNestedFields));
		}

		return fieldsMap;
	}

	public List<FormPage> getPages() {
		return _pages;
	}

	public void setPages(List<FormPage> pages) {
		_pages = pages;
	}

	private List<FormPage> _pages = new LinkedList<FormPage>();

}