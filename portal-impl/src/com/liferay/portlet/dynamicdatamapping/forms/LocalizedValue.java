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

import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Pablo Carvalho
 */
public class LocalizedValue {

	public LocalizedValue() {
		this(LocaleUtil.getDefault());
	}

	public LocalizedValue(Locale defaultLocale) {
		_values = new HashMap<Locale, String>();
	}

	public LocalizedValue(String defaultLanguageId) {
		this(LocaleUtil.fromLanguageId(defaultLanguageId));
	}

	public void addValue(Locale locale, String value) {
		_values.put(locale, value);
	}

	public void addValue(String languageId, String value) {
		addValue(LocaleUtil.fromLanguageId(languageId), value);
	}

	public String getValue(Locale locale) {
		String value = _values.get(locale);

		if (value == null) {
			value = _values.get(_defaultLocale);
		}

		return value;
	}

	public String getValue(String languageId) {
		return getValue(LocaleUtil.fromLanguageId(languageId));
	}

	private Locale _defaultLocale;
	private Map<Locale, String> _values;

}