/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.dynamicdatamapping.storage;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eudaldo Alonso
 */
public class Attributes implements Serializable {

	public void addAttribute(String name, String value) {
		_attributes.put(name, value);
	}

	public Serializable getAttribute(String name) {
		return _attributes.get(name);
	}

	public Map<String, String> getAttributes() {
		return _attributes;
	}

	private Map<String, String> _attributes = new HashMap<String, String>();

}