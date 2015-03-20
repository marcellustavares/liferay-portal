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

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMFormValuesTestUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Renato Rego
 */
public class ParagraphDDMFormFieldValueAccessorTest {

	@Test
	public void testGetWithLocalizedValue() {
		Value value = new LocalizedValue(LocaleUtil.US);

		value.addString(LocaleUtil.BRAZIL, "Portuguese value");
		value.addString(LocaleUtil.US, "English value");

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue("Paragraph", value);

		ParagraphDDMFormFieldValueAccessor paragraphDDMFormFieldValueAccessor =
			new ParagraphDDMFormFieldValueAccessor(LocaleUtil.US);

		Assert.assertEquals(
			"English value",
			paragraphDDMFormFieldValueAccessor.get(ddmFormFieldValue));

		paragraphDDMFormFieldValueAccessor =
			new ParagraphDDMFormFieldValueAccessor(LocaleUtil.BRAZIL);

		Assert.assertEquals(
			"Portuguese value",
			paragraphDDMFormFieldValueAccessor.get(ddmFormFieldValue));
	}

	@Test
	public void testGetWithUnlocalizedValue() {
		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Paragraph", new UnlocalizedValue("Scott Joplin"));

		ParagraphDDMFormFieldValueAccessor paragraphDDMFormFieldValueAccessor =
			new ParagraphDDMFormFieldValueAccessor(LocaleUtil.US);

		Assert.assertEquals(
			"Scott Joplin",
			paragraphDDMFormFieldValueAccessor.get(ddmFormFieldValue));
	}

}