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

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormFieldSet;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldSetFactoryHelper {

	public DDMFormFieldSetFactoryHelper(Method method) {
		_method = method;
		_ddmFormFieldSet = method.getAnnotation(DDMFormFieldSet.class);
	}

	public com.liferay.dynamic.data.mapping.model.DDMFormField
		createDDMFormField() {

		com.liferay.dynamic.data.mapping.model.DDMFormField ddmFormField =
			new com.liferay.dynamic.data.mapping.model.DDMFormField(
				_method.getName(), "fieldset");

		ddmFormField.setProperty("rows", createDDMFormLayoutRows());
		ddmFormField.setRepeatable(_ddmFormFieldSet.repeatable());

		List<com.liferay.dynamic.data.mapping.model.DDMFormField>
			nestedDDMFormFields = createDDMFormFields(
				_ddmFormFieldSet.definition());

		if (ListUtil.isNotEmpty(nestedDDMFormFields)) {
			for (com.liferay.dynamic.data.mapping.model.DDMFormField
					nestedDDMFormField : nestedDDMFormFields) {

				ddmFormField.addNestedDDMFormField(nestedDDMFormField);
			}
		}

		return ddmFormField;
	}

	protected List<com.liferay.dynamic.data.mapping.model.DDMFormField>
		createDDMFormFields(Class<?> definition) {

		if (Validator.isNull(definition)) {
			return null;
		}

		List<com.liferay.dynamic.data.mapping.model.DDMFormField>
			ddmFormFields = new ArrayList<>();

		for (Method method : definition.getDeclaredMethods()) {
			if (method.isAnnotationPresent(DDMFormField.class)) {
				DDMFormFieldFactoryHelper ddmFormFieldFactoryHelper =
					new DDMFormFieldFactoryHelper(method);

				ddmFormFieldFactoryHelper.setAvailableLocales(
					_availableLocales);
				ddmFormFieldFactoryHelper.setDefaultLocale(_defaultLocale);

				ddmFormFields.add(
					ddmFormFieldFactoryHelper.createDDMFormField());
			}
		}

		return ddmFormFields;
	}

	protected com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn
		createDDMFormLayoutColumn(
			DDMFormLayoutColumn ddmFormLayoutColumnAnnotation) {

		com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn
			ddmFormLayoutColumn =
				new com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn();

		ddmFormLayoutColumn.setDDMFormFieldNames(
			ListUtil.fromArray(ddmFormLayoutColumnAnnotation.value()));
		ddmFormLayoutColumn.setSize(ddmFormLayoutColumnAnnotation.size());

		return ddmFormLayoutColumn;
	}

	protected com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow
		createDDMFormLayoutRow(DDMFormLayoutRow ddmFormLayoutRowAnnotation) {

		com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow
			ddmFormLayoutRow =
				new com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow();

		for (DDMFormLayoutColumn ddmFormLayoutColumn :
				ddmFormLayoutRowAnnotation.value()) {

			ddmFormLayoutRow.addDDMFormLayoutColumn(
				createDDMFormLayoutColumn(ddmFormLayoutColumn));
		}

		return ddmFormLayoutRow;
	}

	protected List<com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow>
		createDDMFormLayoutRows() {

		List<com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow>
			ddmFormLayoutRows = new ArrayList<>();

		for (DDMFormLayoutRow ddmFormLayoutRowAnnotation :
				_ddmFormFieldSet.rows()) {

			ddmFormLayoutRows.add(
				createDDMFormLayoutRow(ddmFormLayoutRowAnnotation));
		}

		return ddmFormLayoutRows;
	}

	protected void setAvailableLocales(Set<Locale> availableLocales) {
		_availableLocales = availableLocales;
	}

	protected void setDefaultLocale(Locale defaultLocale) {
		_defaultLocale = defaultLocale;
	}

	private Set<Locale> _availableLocales;
	private final DDMFormFieldSet _ddmFormFieldSet;
	private Locale _defaultLocale;
	private final Method _method;

}