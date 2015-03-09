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

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMFormValuesTestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Renato Rego
 */
@PrepareForTest({JSONFactoryUtil.class})
@RunWith(PowerMockRunner.class)
public class SelectDDMFormFieldValueAccessorTest extends PowerMockito {

	@Before
	public void setUpJSONFactoryUtil() {
		spy(JSONFactoryUtil.class);

		when(
			JSONFactoryUtil.getJSONFactory()
		).thenReturn(
			new JSONFactoryImpl()
		);
	}

	@Test
	public void testGetSelectValue() throws Exception {
		JSONArray expectedJSONArray = createJSONArray();

		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Select", new UnlocalizedValue(expectedJSONArray.toString()));

		SelectDDMFormFieldValueAccessor selectDDMFormFieldValueAccessor =
			new SelectDDMFormFieldValueAccessor(LocaleUtil.US);

		JSONArray actualJSONArray = selectDDMFormFieldValueAccessor.get(
			ddmFormFieldValue);

		Assert.assertEquals(
			expectedJSONArray.toString(), actualJSONArray.toString());
	}

	protected JSONArray createJSONArray() throws Exception {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		jsonArray.put("value 1");

		return jsonArray;
	}

}