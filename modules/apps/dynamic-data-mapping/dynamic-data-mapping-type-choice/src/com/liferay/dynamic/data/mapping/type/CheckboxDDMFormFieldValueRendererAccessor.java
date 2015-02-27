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

package com.liferay.dynamic.data.mapping.type;

import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueAccessor;
import com.liferay.portlet.dynamicdatamapping.registry.DDMFormFieldValueRendererAccessor;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

/**
 * @author Renato Rego
 */
public class CheckboxDDMFormFieldValueRendererAccessor
	extends DDMFormFieldValueRendererAccessor<Boolean> {

	public CheckboxDDMFormFieldValueRendererAccessor(
		DDMFormFieldValueAccessor<Boolean> ddmFormFieldValueAccessor) {

		_ddmFormFieldValueAccessor = ddmFormFieldValueAccessor;
	}

	@Override
	public Boolean get(DDMFormFieldValue ddmFormFieldValue) {
		Boolean value = _ddmFormFieldValueAccessor.get(ddmFormFieldValue);

		return value;
	}

	@Override
	public Class<Boolean> getAttributeClass() {
		return Boolean.class;
	}

	private final DDMFormFieldValueAccessor<Boolean> _ddmFormFieldValueAccessor;

}