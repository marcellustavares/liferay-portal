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

package com.liferay.dynamic.data.mapping.io.json;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portlet.dynamicdatamapping.io.BaseDDMFormDeserializerTestCase;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormDeserializer;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;

import org.junit.Before;

import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * @author Marcellus Tavares
 */
@PrepareForTest({LocaleUtil.class})
public class DDMFormJSONDeserializerTest
	extends BaseDDMFormDeserializerTestCase {

	@Before
	public void setUp() {
		setUpDDMFormDeserializer();
		setUpLocaleUtil();
		setUpJSONFactoryUtil();
	}

	@Override
	protected DDMForm deserialize(String serializedDDMForm)
		throws PortalException {

		return _ddmFormJSONDeserializerImpl.deserialize(serializedDDMForm);
	}

	@Override
	protected String getDeserializerType() {
		return "json";
	}

	@Override
	protected String getTestFileExtension() {
		return ".json";
	}

	private void setUpDDMFormDeserializer() {
		_ddmFormJSONDeserializerImpl = new DDMFormJSONDeserializerImpl();
	}

	private DDMFormDeserializer _ddmFormJSONDeserializerImpl;

}