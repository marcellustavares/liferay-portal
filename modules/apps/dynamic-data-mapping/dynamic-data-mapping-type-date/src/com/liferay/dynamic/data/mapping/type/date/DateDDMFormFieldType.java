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
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldRenderer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldType;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueParameterSerializer;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueRendererAccessor;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Renato Rego
 */
@Component(immediate = true, service = DDMFormFieldType.class)
public class DateDDMFormFieldType implements DDMFormFieldType {

	@Override
	public DDMFormFieldRenderer getDDMFormFieldRenderer() {
		return _ddmFormFieldRenderer;
	}

	@Override
	public DDMFormFieldValueAccessor<Calendar> getDDMFormFieldValueAccessor(
		Locale locale) {

		return new DateDDMFormFieldValueAccessor(locale);
	}

	@Override
	public DDMFormFieldValueParameterSerializer
		getDDMFormFieldValueParameterSerializer() {

		return new DDMFormFieldValueParameterSerializer() {

			@Override
			public String getParameterValue(
				HttpServletRequest httpServletRequest,
				String ddmFormFieldParameterName,
				String defaultDDMFormFieldParameterValue) {

				String parameterValue = ParamUtil.getString(
					httpServletRequest, ddmFormFieldParameterName,
					defaultDDMFormFieldParameterValue);

				String[] parameterValueArray = parameterValue.split(
					StringPool.COMMA);

				String dateInMillisecondsString = StringPool.BLANK;

				if (parameterValueArray.length == 3) {
					try {
						Calendar calendar = CalendarFactoryUtil.getCalendar();

						int day = Integer.parseInt(parameterValueArray[0]);
						int month = Integer.parseInt(parameterValueArray[1]);
						int year = Integer.parseInt(parameterValueArray[2]);

						calendar.set(year, month, day);

						dateInMillisecondsString = String.valueOf(
							calendar.getTimeInMillis());
					}
					catch (NumberFormatException nfe) {
						_log.error("Unable to convert String to int.", nfe);
					}
				}

				return dateInMillisecondsString;
			}

		};
	}

	@Override
	public DDMFormFieldValueRendererAccessor
		getDDMFormFieldValueRendererAccessor(Locale locale) {

		return new DateDDMFormFieldValueRendererAccessor(
			getDDMFormFieldValueAccessor(locale));
	}

	@Override
	public String getName() {
		return "ddm-date";
	}

	@Reference(service = DateDDMFormFieldRenderer.class, unbind = "-")
	protected void setDDMFormFieldRenderer(
		DDMFormFieldRenderer ddmFormFieldRenderer) {

		_ddmFormFieldRenderer = ddmFormFieldRenderer;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DateDDMFormFieldType.class);

	private DDMFormFieldRenderer _ddmFormFieldRenderer;

}