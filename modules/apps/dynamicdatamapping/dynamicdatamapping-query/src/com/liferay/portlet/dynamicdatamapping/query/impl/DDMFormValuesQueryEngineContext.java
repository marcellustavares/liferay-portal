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

package com.liferay.portlet.dynamicdatamapping.query.impl;

import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueMatcher;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesQueryEngineContext {

	public DDMFormValuesQueryEngineContext(
		List<DDMFormFieldValueMatcher> ddmFormFieldValueMatchers) {

		_ddmFormFieldValueMatchers = ddmFormFieldValueMatchers;
	}

	public DDMFormFieldValueMatcher getDDMFormFieldValueMatcher(int depth) {
		return _ddmFormFieldValueMatchers.get(depth);
	}

	public boolean isLastDDMFormFieldValueMatcher(int depth) {
		if (_ddmFormFieldValueMatchers.size() > depth + 1) {
			return true;
		}

		return false;
	}

	private List<DDMFormFieldValueMatcher> _ddmFormFieldValueMatchers;

}