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

package com.liferay.dynamic.data.mapping.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.Validator;

/**
 * @author Leonardo Barros
 */
@ProviderType
public enum DDMFormRuleType {

	AUTO_FILL("AUTO_FILL"), NOT_AVAILABLE("NOT_AVAILABLE"),
	READ_ONLY("READ_ONLY"), VISIBILITY("VISIBILITY");

	public static DDMFormRuleType parse(String value) {
		if (Validator.isNull(value)) {
			return null;
		}
		else if (AUTO_FILL.getValue().equals(value)) {
			return AUTO_FILL;
		}
		else if (READ_ONLY.getValue().equals(value)) {
			return READ_ONLY;
		}
		else if (VISIBILITY.getValue().equals(value)) {
			return VISIBILITY;
		}
		else {
			throw new IllegalArgumentException("Invalid value " + value);
		}
	}

	public String getValue() {
		return _value;
	}

	@Override
	public String toString() {
		return _value;
	}

	private DDMFormRuleType(String value) {
		_value = value;
	}

	private final String _value;

}