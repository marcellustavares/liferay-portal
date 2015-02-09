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

import com.liferay.portlet.dynamicdatamapping.query.api.DDMFormValuesQuery;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormValuesFilter;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adolfo Pérez
 * @author Marcellus Tavares
 */
public class DDMFormValuesQueryImpl implements DDMFormValuesQuery {

	public DDMFormValuesQueryImpl(
		List<DDMFormValuesFilter> ddmFormValuesFilters) {

		_ddmFormFieldValueFilters = ddmFormValuesFilters;
	}

	@Override
	public List<DDMFormFieldValue> selectDDMFormFieldValues() {
		DDMFormValuesFilter firstFilter = _ddmFormFieldValueFilters.get(0);

		List<DDMFormFieldValue> ddmFormFieldValues = firstFilter.filter(
			_ddmFormValues);

		for (int i = 1; i < _ddmFormFieldValueFilters.size(); i++) {
			DDMFormValuesFilter currentFilter =_ddmFormFieldValueFilters.get(i);

			List<DDMFormFieldValue> nestedDDMFormFieldValues =
				getNestedDDMFormFieldValues(ddmFormFieldValues);

			ddmFormFieldValues = currentFilter.filter(nestedDDMFormFieldValues);
		}

		return ddmFormFieldValues;
	}

	@Override
	public DDMFormFieldValue selectSingleDDMFormFieldValue() {
		List<DDMFormFieldValue> ddmFormFieldValues = selectDDMFormFieldValues();

		if (ddmFormFieldValues.isEmpty()) {
			return null;
		}

		return ddmFormFieldValues.get(0);
	}

	@Override
	public void setDDMFormValues(DDMFormValues ddmFormValues) {
		_ddmFormValues = ddmFormValues;
	}

	private List<DDMFormFieldValue> getNestedDDMFormFieldValues(
		List<DDMFormFieldValue> ddmFormFieldValues) {

		List<DDMFormFieldValue> nestedDDMFormFieldValues = new ArrayList<>();

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			nestedDDMFormFieldValues.addAll(
				ddmFormFieldValue.getNestedDDMFormFieldValues());
		}

		return nestedDDMFormFieldValues;
	}

	private List<DDMFormValuesFilter> _ddmFormFieldValueFilters;
	private DDMFormValues _ddmFormValues;

}