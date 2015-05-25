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

package com.liferay.dynamic.data.mapping.type.date;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author Renato Rego
 */
public class DateDDMFormFieldValueAccessor
	extends DDMFormFieldValueAccessor<Calendar> {

	public DateDDMFormFieldValueAccessor(Locale locale) {
		super(locale);
	}

	@Override
	public Calendar get(DDMFormFieldValue ddmFormFieldValue) {
		Value value = ddmFormFieldValue.getValue();

		try {
			long valueLong = Long.parseLong(value.getString(locale));

			Calendar valueCalendar = CalendarFactoryUtil.getCalendar(valueLong);

			return valueCalendar;
		}
		catch (NumberFormatException nfe) {
			_log.error("Unable to convert String to Long.", nfe);

			return null;
		}
	}

	@Override
	public Class<Calendar> getAttributeClass() {
		return Calendar.class;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DateDDMFormFieldValueAccessor.class);

}