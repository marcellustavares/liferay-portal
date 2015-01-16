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
 * @author Adolfo Perez
 * @author Marcellus Tavares
 */
public class DDMFormFieldValueValuePredicateMatcher
	implements DDMFormFieldValuePredicateMatcher {

	@Override
	public boolean match(DDMFormFieldValue ddmFormFieldValue) {
		Value value = ddmFormFieldValue.getValue();

		if (_locale != null) {
			if (_value.equals(value.getString(_locale))) {
				return true;
			}

			return false;
		}

		for (Locale locale : value.getAvailableLocales()) {
			if (_value.equals(value.getString(locale))) {
				return true;
			}
		}

		return false;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	@Override
	public void setValue(String value) {
		_value = value;
	}

	private Locale _locale;
	private String _value;

}