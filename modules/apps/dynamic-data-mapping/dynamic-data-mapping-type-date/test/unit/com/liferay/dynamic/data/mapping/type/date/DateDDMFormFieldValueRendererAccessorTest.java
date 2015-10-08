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
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.util.CalendarFactoryImpl;
import com.liferay.portal.util.FastDateFormatFactoryImpl;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMFormValuesTestUtil;

import java.text.Format;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Renato Rego
 */
public class DateDDMFormFieldValueRendererAccessorTest {

	@Before
	public void setUp() {
		setUpCalendarFactoryUtil();
		setUpFastDateFormatFactoryUtil();
	}

	@Test
	public void testGetDateRenderedValue() {
		Calendar calendar = CalendarFactoryUtil.getCalendar(0L);

		Format format = FastDateFormatFactoryUtil.getDate(LocaleUtil.US);

		String expectedFormat = format.format(calendar);

		String millisecondsString = String.valueOf(calendar.getTimeInMillis());

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Date", new UnlocalizedValue(millisecondsString));

		DateDDMFormFieldValueRendererAccessor
			dateDDMFormFieldValueRendererAccessor =
				createDateDDMFormFieldValueRendererAccessor(LocaleUtil.US);

		String actualFormat = dateDDMFormFieldValueRendererAccessor.get(
			ddmFormFieldValue);

		Assert.assertEquals(expectedFormat, actualFormat);

		format = FastDateFormatFactoryUtil.getDate(LocaleUtil.BRAZIL);

		expectedFormat = format.format(calendar);

		dateDDMFormFieldValueRendererAccessor =
			createDateDDMFormFieldValueRendererAccessor(LocaleUtil.BRAZIL);

		actualFormat = dateDDMFormFieldValueRendererAccessor.get(
			ddmFormFieldValue);

		Assert.assertEquals(expectedFormat, actualFormat);
	}

	protected DateDDMFormFieldValueRendererAccessor
		createDateDDMFormFieldValueRendererAccessor(Locale locale) {

		DateDDMFormFieldValueAccessor dateDDMFormFieldValueAccessor =
			new DateDDMFormFieldValueAccessor(locale);

		return new DateDDMFormFieldValueRendererAccessor(
			dateDDMFormFieldValueAccessor);
	}

	protected void setUpCalendarFactoryUtil() {
		CalendarFactoryUtil calendarFactoryUtil = new CalendarFactoryUtil();

		calendarFactoryUtil.setCalendarFactory(new CalendarFactoryImpl());
	}

	protected void setUpFastDateFormatFactoryUtil() {
		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			new FastDateFormatFactoryImpl());
	}

}