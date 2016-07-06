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

package com.liferay.dynamic.data.mapping.form.evaluator.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldStateObserver;

/**
 * @author Leonardo Barros
 */
public class DefaultDDMFormFieldStateObserver
	implements DDMFormFieldStateObserver {

	@Override
	public Object getDDMFormFieldValue(String ddmFormFieldName) {
		return null;
	}

	@Override
	public boolean isReadOnly(String ddmFormFieldName) {
		return false;
	}

	@Override
	public boolean isValid(String ddmFormFieldName) {
		return false;
	}

	@Override
	public boolean isVisible(String ddmFormFieldName) {
		return false;
	}

	@Override
	public void setReadOnly(String ddmFormFieldName, boolean value) {
	}

	@Override
	public void setValid(String ddmFormFieldName, boolean value) {
	}

	@Override
	public void setVisible(String ddmFormFieldName, boolean value) {
	}

}