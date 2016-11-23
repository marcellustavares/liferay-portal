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

package com.liferay.dynamic.data.mapping.type.fieldset.internal;

import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class FieldSetDDMFormFieldTemplateContextContributorTest {

	@Test
	public void testGetParameters() {
		FieldSetDDMFormFieldTemplateContextContributor
			fieldSetDDMFormFieldTemplateContextContributor =
				new FieldSetDDMFormFieldTemplateContextContributor();

		DDMFormField ddmFormField = new DDMFormField();

		ddmFormField.setRepeatable(true);
		ddmFormField.setProperty("title", "Field Set Title");

		DDMFormLayoutRow ddmFormLayoutRow1 = new DDMFormLayoutRow();

		DDMFormLayoutColumn ddmFormLayoutColumn1 = new DDMFormLayoutColumn();

		ddmFormLayoutColumn1.setDDMFormFieldNames(
			Arrays.asList("field0", "field1"));

		DDMFormLayoutColumn ddmFormLayoutColumn2 = new DDMFormLayoutColumn();

		ddmFormLayoutColumn2.setDDMFormFieldNames(Arrays.asList("field2"));

		ddmFormLayoutRow1.addDDMFormLayoutColumn(ddmFormLayoutColumn1);
		ddmFormLayoutRow1.addDDMFormLayoutColumn(ddmFormLayoutColumn2);

		DDMFormLayoutRow ddmFormLayoutRow2 = new DDMFormLayoutRow();

		DDMFormLayoutColumn ddmFormLayoutColumn3 = new DDMFormLayoutColumn();

		ddmFormLayoutColumn3.setDDMFormFieldNames(Arrays.asList("field3"));

		ddmFormLayoutRow2.addDDMFormLayoutColumn(ddmFormLayoutColumn3);

		ddmFormField.setProperty(
			"rows", Arrays.asList(ddmFormLayoutRow1, ddmFormLayoutRow2));

		Map<String, Object> parameters =
			fieldSetDDMFormFieldTemplateContextContributor.getParameters(
				ddmFormField, new DDMFormFieldRenderingContext());

		Assert.assertEquals(3, parameters.size());
		Assert.assertEquals(true, MapUtil.getBoolean(parameters, "repeatable"));
		Assert.assertNotNull(parameters.get("rows"));

		List<DDMFormLayoutRow> rows = (List<DDMFormLayoutRow>)parameters.get(
			"rows");

		Assert.assertEquals(2, rows.size());

		ddmFormLayoutRow1 = rows.get(0);

		List<DDMFormLayoutColumn> columns =
			ddmFormLayoutRow1.getDDMFormLayoutColumns();

		Assert.assertEquals(2, columns.size());

		ddmFormLayoutColumn1 = columns.get(0);

		List<String> fieldNames = ddmFormLayoutColumn1.getDDMFormFieldNames();

		Assert.assertEquals(2, fieldNames.size());
		Assert.assertEquals("field0", fieldNames.get(0));
		Assert.assertEquals("field1", fieldNames.get(1));

		ddmFormLayoutColumn2 = columns.get(1);

		fieldNames = ddmFormLayoutColumn2.getDDMFormFieldNames();

		Assert.assertEquals(1, fieldNames.size());
		Assert.assertEquals("field2", fieldNames.get(0));

		ddmFormLayoutRow2 = rows.get(1);

		columns = ddmFormLayoutRow2.getDDMFormLayoutColumns();

		Assert.assertEquals(1, columns.size());

		ddmFormLayoutColumn3 = columns.get(0);

		fieldNames = ddmFormLayoutColumn3.getDDMFormFieldNames();

		Assert.assertEquals(1, fieldNames.size());
		Assert.assertEquals("field3", fieldNames.get(0));
	}

}