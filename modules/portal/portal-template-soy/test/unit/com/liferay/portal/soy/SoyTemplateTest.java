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

package com.liferay.portal.soy;

import com.liferay.portal.kernel.template.Template;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Bruno Basto
 */
public class SoyTemplateTest {

	@Before
	public void setUp() throws Exception {
		_soyManagerTestHelper.setUp();
	}

	@After
	public void tearDown() {
		_soyManagerTestHelper.tearDown();
	}

	@Test
	public void testGetJavaScriptProcessorSimple() throws Exception {
		Template template = _soyManagerTestHelper.getTemplate("simple.soy");

		template.put("namespace", "soy.test.simple");

		String javaScriptProcessor = template.getJavaScriptProcessor();

		Assert.assertNotNull(javaScriptProcessor);
	}

	@Test
	public void testGetJavaScriptProcessorWithContext() throws Exception {
		Template template = _soyManagerTestHelper.getTemplate("context.soy");

		template.put("namespace", "soy.test.withContext");

		String javaScriptProcessor = template.getJavaScriptProcessor();

		Assert.assertNotNull(javaScriptProcessor);
	}

	private final SoyManagerTestHelper _soyManagerTestHelper =
		new SoyManagerTestHelper();

}