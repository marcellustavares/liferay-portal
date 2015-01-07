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

package com.liferay.portlet.dynamicdatamapping.query;

import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.util.List;

/**
* @author Adolfo Pérez
*/
final class DDMQueryImpl extends DDMQuery {

	public DDMQueryImpl(DDMQueryMatcher matcher) {
		_matcher = matcher;
	}

	@Override
	public DDMFormFieldValue match(DDMFormValues ddmFormValues) {
		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			DDMFormFieldValue matchingDDMFormFieldValue = _matcher.match(
				ddmFormFieldValue);

			if (matchingDDMFormFieldValue != null) {
				return matchingDDMFormFieldValue;
			}
		}

		return null;
	}

	private final DDMQueryMatcher _matcher;

}