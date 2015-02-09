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

package com.liferay.portlet.dynamicdatamapping.query.impl.model;

import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

import java.util.Locale;

/**
 * @author Pablo Carvalho
 */
public class DDMFormFieldValueValueMatcher implements DDMFormFieldValueMatcher {

	@Override
	public boolean matches(DDMFormFieldValue ddmFormFieldValue) {
		if (_locale != null) {
			return localizedMatch(ddmFormFieldValue.getValue());
		}
		else {
			return unlocalizedMatch(ddmFormFieldValue.getValue());
		}
	}

	public void setExpectedValue(String expectedValue) {
		_expectedValue = expectedValue;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	protected boolean localizedMatch(Value value) {
		return _expectedValue.equals(value.getString(_locale));
	}

	protected boolean unlocalizedMatch(Value value) {
		for (Locale locale : value.getAvailableLocales()) {
			if (_expectedValue.equals(value.getString(locale))) {
				return true;
			}
		}

		return false;
	}

	private String _expectedValue;
	private Locale _locale;

}