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

import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Adolfo Pérez
 */
public class DefaultDDMQueryParserTest {

	@Test
	public void testAttributeValueQueryMatchesNestedField() throws Exception {
		DDMQuery ddmQuery = _compile("/*/field1[value = 'xyz']");

		DDMFormFieldValue ddmFormFieldValue0 = new DDMFormFieldValue();
		ddmFormFieldValue0.setName("field0");

		DDMFormFieldValue ddmFormFieldValue1 = new DDMFormFieldValue();
		ddmFormFieldValue1.setName("field1");
		ddmFormFieldValue1.setValue(new UnlocalizedValue("xyz"));

		DDMFormValues ddmFormValues = new DDMFormValues(null);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue0);

		ddmFormFieldValue0.addNestedDDMFormFieldValue(ddmFormFieldValue1);

		Assert.assertEquals(ddmFormFieldValue1, ddmQuery.match(ddmFormValues));
	}

	@Test
	public void testAttributeValueQueryMatchesTopLevelField() throws Exception {
		DDMQuery ddmQuery = _compile("/field1[value = 'xyz']");

		DDMFormFieldValue ddmFormFieldValue0 = new DDMFormFieldValue();
		ddmFormFieldValue0.setName("field0");

		DDMFormFieldValue ddmFormFieldValue1 = new DDMFormFieldValue();
		ddmFormFieldValue1.setName("field1");
		ddmFormFieldValue1.setValue(new UnlocalizedValue("xyz"));

		DDMFormValues ddmFormValues = new DDMFormValues(null);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue0);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue1);

		Assert.assertEquals(ddmFormFieldValue1, ddmQuery.match(ddmFormValues));
	}

	@Test
	public void testEmptyQueryMatchesRootValue() throws Exception {
		DDMQuery ddmQuery = _compile("");

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		DDMFormValues ddmFormValues = new DDMFormValues(null);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		Assert.assertEquals(ddmFormFieldValue, ddmQuery.match(ddmFormValues));
	}

	@Test
	public void testFieldQueryMatchesNestedField() throws Exception {
		DDMQuery ddmQuery = _compile("/field0/field1");

		DDMFormFieldValue ddmFormFieldValue0 = new DDMFormFieldValue();
		ddmFormFieldValue0.setName("field0");

		DDMFormFieldValue ddmFormFieldValue1 = new DDMFormFieldValue();
		ddmFormFieldValue1.setName("field1");

		DDMFormValues ddmFormValues = new DDMFormValues(null);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue0);

		ddmFormFieldValue0.addNestedDDMFormFieldValue(ddmFormFieldValue1);

		Assert.assertEquals(ddmFormFieldValue1, ddmQuery.match(ddmFormValues));
	}

	@Test
	public void testFieldQueryMatchesTopLevelField() throws Exception {
		DDMQuery ddmQuery = _compile("/field1");

		DDMFormFieldValue ddmFormFieldValue0 = new DDMFormFieldValue();
		ddmFormFieldValue0.setName("field0");

		DDMFormFieldValue ddmFormFieldValue1 = new DDMFormFieldValue();
		ddmFormFieldValue1.setName("field1");

		DDMFormValues ddmFormValues = new DDMFormValues(null);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue0);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue1);

		Assert.assertEquals(ddmFormFieldValue1, ddmQuery.match(ddmFormValues));
	}

	@Test
	public void testRootQueryMatchesRootValue() throws Exception {
		DDMQuery ddmQuery = _compile("/");

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		DDMFormValues ddmFormValues = new DDMFormValues(null);
		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		Assert.assertEquals(ddmFormFieldValue, ddmQuery.match(ddmFormValues));
	}

	private DDMQuery _compile(String query)
		throws DDMQuerySyntaxErrorException, IOException {

		DDMQueryCompiler compiler = new DDMQueryCompilerImpl();

		return compiler.compile(query);
	}

}