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

package com.liferay.portlet.dynamicdatamapping.forms;

/**
 * @author Pablo Carvalho
 */
public class FormFieldValue {

	public LocalizedValue getCalculatedValue() {
		return _calculatedValue;
	}

	public String getFieldName() {
		return _fieldName;
	}

	public FlatFieldRepresentation getNestedFieldsValues() {
		return _nestedFieldsValues;
	}

	public LocalizedValue getValueExpression() {
		return _valueExpression;
	}

	public void setCalculatedValue(LocalizedValue calculatedValue) {
		_calculatedValue = calculatedValue;
	}

	public void setFieldName(String fieldName) {
		_fieldName = fieldName;
	}

	public void setNestedFieldsValues(
		FlatFieldRepresentation nestedFieldsValues) {

		_nestedFieldsValues = nestedFieldsValues;
	}

	public void setValueExpression(LocalizedValue valueExpression) {
		_valueExpression = valueExpression;
	}

	private LocalizedValue _calculatedValue;
	private String _fieldName;
	private FlatFieldRepresentation _nestedFieldsValues;
	private LocalizedValue _valueExpression;

}