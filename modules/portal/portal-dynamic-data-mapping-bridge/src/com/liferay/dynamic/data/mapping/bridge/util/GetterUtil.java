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

/**
 * @author Leonardo Barros
 */
public class GetterUtil {

	public static final String[] BOOLEANS = {"true", "t", "y", "on", "1"};

	public static final String NEW_LINE = "\n";

	public static final char RETURN = '\r';

	public static final String RETURN_NEW_LINE = "\r\n";

	public static boolean get(String value, boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		value = value.toLowerCase();
		value = value.trim();

		if (value.equals(BOOLEANS[0]) || value.equals(BOOLEANS[1]) ||
			value.equals(BOOLEANS[2]) || value.equals(BOOLEANS[3]) ||
			value.equals(BOOLEANS[4])) {

			return true;
		}
		else {
			return false;
		}
	}

	public static String get(String value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		value = value.trim();

		if (value.indexOf(RETURN) != -1) {
			value = value.replaceAll(RETURN_NEW_LINE, NEW_LINE);
		}

		return value;
	}

	public static boolean getBoolean(String value, boolean defaultValue) {
		return get(value, defaultValue);
	}

	public static String getString(String value, String defaultValue) {
		return get(value, defaultValue);
	}

}