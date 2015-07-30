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

package com.liferay.dynamic.data.mapping.bridge.util;

import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class MapUtil {

	public static boolean getBoolean(Map<String, ?> map, String key) {
		return getBoolean(map, key, false);
	}

	public static boolean getBoolean(
		Map<String, ?> map, String key, boolean defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof Boolean) {
			return (Boolean)value;
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return GetterUtil.getBoolean(array[0], defaultValue);
		}

		return GetterUtil.getBoolean(String.valueOf(value), defaultValue);
	}

	public static String getString(Map<String, ?> map, String key) {
		return getString(map, key, _BLANK);
	}

	public static String getString(
		Map<String, ?> map, String key, String defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof String) {
			return GetterUtil.getString((String)value, defaultValue);
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0 ) {
				return defaultValue;
			}

			return GetterUtil.getString(array[0], defaultValue);
		}

		return GetterUtil.getString(String.valueOf(value), defaultValue);
	}

	private static final String _BLANK = "";

}