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

import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueRendererAccessor;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

import java.text.Format;

import java.util.Calendar;

/**
 * @author Renato Rego
 */
public class DateDDMFormFieldValueRendererAccessor
	extends DDMFormFieldValueRendererAccessor {

	public DateDDMFormFieldValueRendererAccessor(
		DDMFormFieldValueAccessor<Calendar> ddmFormFieldValueAccessor) {

		_ddmFormFieldValueAccessor = ddmFormFieldValueAccessor;
	}

	@Override
	public String get(DDMFormFieldValue ddmFormFieldValue) {
		Calendar valueCalendar = _ddmFormFieldValueAccessor.get(
			ddmFormFieldValue);

		if (Validator.isNotNull(valueCalendar)) {
			Format format = FastDateFormatFactoryUtil.getDate(
				_ddmFormFieldValueAccessor.getLocale());

			return format.format(valueCalendar);
		}
		else {
			return StringPool.BLANK;
		}
	}

	private final DDMFormFieldValueAccessor<Calendar> _ddmFormFieldValueAccessor;

}