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

/**
 * @author Adolfo Pérez
 */
public class MoveToChildrenDDMQueryMatcher implements DDMQueryMatcher {

	public MoveToChildrenDDMQueryMatcher(DDMQueryMatcher matcher) {
		_matcher = matcher;
	}

	@Override
	public DDMFormFieldValue match(DDMFormFieldValue ddmFormFieldValue) {
		for (DDMFormFieldValue childDDMFormFieldValue :
				ddmFormFieldValue.getNestedDDMFormFieldValues()) {

			DDMFormFieldValue matchedDDMFormFieldValue = _matcher.match(
				childDDMFormFieldValue);

			if (matchedDDMFormFieldValue != null) {
				return matchedDDMFormFieldValue;
			}
		}

		return null;
	}

	private final DDMQueryMatcher _matcher;

}