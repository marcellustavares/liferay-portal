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

package com.liferay.dynamic.data.mapping.type.textbox;

import com.liferay.dynamic.data.mapping.type.textbox.TextboxDDMFormFieldValueAccessor;
import com.liferay.dynamic.data.mapping.type.textbox.TextboxDDMFormFieldValueRendererAccessor;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.util.test.DDMFormValuesTestUtil;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Renato Rego
 */
public class TextboxDDMFormFieldValueRendererAccessorTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		setUpHtmlUtil();
	}

	@Test
	public void testGet() {
		DDMFormFieldValue ddmFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"Textbox", new UnlocalizedValue(StringUtil.randomString()));

		TextboxDDMFormFieldValueRendererAccessor
			textboxDDMFormFieldValueRendererAccessor =
				createTextboxDDMFormFieldValueRendererAccessor(LocaleUtil.US);

		textboxDDMFormFieldValueRendererAccessor.get(ddmFormFieldValue);

		Mockito.verify(_html).escape(Matchers.anyString());
	}

	protected TextboxDDMFormFieldValueRendererAccessor
		createTextboxDDMFormFieldValueRendererAccessor(Locale locale) {

		TextboxDDMFormFieldValueAccessor textboxDDMFormFieldValueAccessor =
			new TextboxDDMFormFieldValueAccessor(locale);

		return new TextboxDDMFormFieldValueRendererAccessor(
			textboxDDMFormFieldValueAccessor);
	}

	protected void setUpHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(_html);
	}

	@Mock
	private Html _html;

}