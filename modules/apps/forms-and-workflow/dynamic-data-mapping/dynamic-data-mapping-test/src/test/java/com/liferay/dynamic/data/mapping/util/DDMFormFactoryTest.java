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

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormFactoryTest {

	@Test
	public void testCreateDynamicFormWithoutRules() {
		DDMForm ddmForm = DDMFormFactory.create(DynamicFormWithoutRules.class);

		List<DDMFormRule> ddmFormRules = ddmForm.getDDMFormRules();

		Assert.assertEquals(0, ddmFormRules.size());
	}

	@Test
	public void testCreateDynamicFormWithRules() {
		DDMForm ddmForm = DDMFormFactory.create(DynamicFormWithRules.class);

		List<DDMFormRule> ddmFormRules = ddmForm.getDDMFormRules();

		Assert.assertEquals(2, ddmFormRules.size());

		DDMFormRule ddmFormRule = ddmFormRules.get(0);

		Assert.assertEquals("condition1", ddmFormRule.getCondition());

		List<String> ddmFormRuleActions = ddmFormRule.getActions();

		Assert.assertEquals(2, ddmFormRuleActions.size());
		Assert.assertArrayEquals(
			new String[] {"action1", "action2"}, ddmFormRuleActions.toArray());

		ddmFormRule = ddmFormRules.get(1);

		Assert.assertEquals("TRUE", ddmFormRule.getCondition());

		ddmFormRuleActions = ddmFormRule.getActions();

		Assert.assertEquals(1, ddmFormRuleActions.size());
		Assert.assertArrayEquals(
			new String[] {"action1"}, ddmFormRuleActions.toArray());
	}

	@Test
	public void testeCreateDynamicFormWithFieldSet() {
		DDMForm ddmForm = DDMFormFactory.create(DynamicFormWithFieldSet.class);

		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		Assert.assertEquals(1, ddmFormFields.size());

		DDMFormField ddmFormField = ddmFormFields.get(0);

		Assert.assertEquals("fieldset", ddmFormField.getType());
		Assert.assertTrue(ddmFormField.isRepeatable());

		List<DDMFormField> nestedDDMFormFields =
			ddmFormField.getNestedDDMFormFields();

		Assert.assertEquals(2, nestedDDMFormFields.size());

		Assert.assertNotNull(ddmFormField.getProperty("rows"));

		List<DDMFormLayoutRow> rows =
			(List<DDMFormLayoutRow>)ddmFormField.getProperty("rows");

		Assert.assertEquals(1, rows.size());

		List<DDMFormLayoutColumn> columns = rows.get(
			0).getDDMFormLayoutColumns();

		Assert.assertEquals(1, columns.size());

		List<String> fieldNames = columns.get(0).getDDMFormFieldNames();

		Assert.assertEquals(2, fieldNames.size());

		Assert.assertTrue(fieldNames.contains("parameterName"));
		Assert.assertTrue(fieldNames.contains("parameterValue"));
	}

	@com.liferay.dynamic.data.mapping.annotations.DDMForm
	private interface DynamicFormWithFieldSet {

		@com.liferay.dynamic.data.mapping.annotations.DDMFormFieldSet(
			definition = ParametersFieldSetSettings.class, repeatable = true,
			rows = {
				@com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow(
					@com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn(
						size = 12, value = {"parameterName", "parameterValue"}
					)
				)
			}
		)
		public Map<String, List<Object>> parameters();

	}

	@com.liferay.dynamic.data.mapping.annotations.DDMForm
	private interface DynamicFormWithoutRules {
	}

	@com.liferay.dynamic.data.mapping.annotations.DDMForm(
		rules = {
			@com.liferay.dynamic.data.mapping.annotations.DDMFormRule(
				actions = {"action1", "action2"}, condition = "condition1"
			),
			@com.liferay.dynamic.data.mapping.annotations.DDMFormRule(
				actions = {"action1"}
			)
		}
	)
	private interface DynamicFormWithRules {
	}

	private interface ParametersFieldSetSettings {

		@com.liferay.dynamic.data.mapping.annotations.DDMFormField
		public String parameterName();

		@com.liferay.dynamic.data.mapping.annotations.DDMFormField
		public String parameterValue();

	}

}