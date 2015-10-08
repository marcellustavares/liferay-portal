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

import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.util.CalendarFactoryImpl;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMFormValuesTestUtil;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Renato Rego
 */
public class DateDDMFormFieldValueAccessorTest {

	@Before
	public void setUp() {
		setUpCalendarFactoryUtil();
	}

	@Test
	public void testGetDateValue() {
		Calendar expectedCalendar = CalendarFactoryUtil.getCalendar(0L);

		String expectedMillisecondsString = String.valueOf(
			expectedCalendar.getTimeInMillis());

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Date", new UnlocalizedValue(expectedMillisecondsString));

		DateDDMFormFieldValueAccessor dateDDMFormFieldValueAccessor =
			new DateDDMFormFieldValueAccessor(LocaleUtil.US);

		Calendar actualCalendar = dateDDMFormFieldValueAccessor.get(
			ddmFormFieldValue);

		Assert.assertEquals(expectedCalendar, actualCalendar);
	}

	protected void setUpCalendarFactoryUtil() {
		CalendarFactoryUtil calendarFactoryUtil = new CalendarFactoryUtil();

		calendarFactoryUtil.setCalendarFactory(new CalendarFactoryImpl());
	}

}