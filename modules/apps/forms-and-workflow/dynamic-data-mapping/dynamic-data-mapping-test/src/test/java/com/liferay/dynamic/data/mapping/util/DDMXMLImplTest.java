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

import com.liferay.dynamic.data.mapping.BaseDDMTestCase;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.Field;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.util.impl.DDMXMLImpl;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.xml.SAXReaderImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Inácio Nery
 */
public class DDMXMLImplTest extends BaseDDMTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setUpConfigurationFactoryUtil();
		setUpDDMXML();
		setUpDDMFormJSONDeserializer();
		setUpDDMStructureLocalServiceUtil();
		setUpLanguageUtil();
	}

	@Test
	public void testGetFields() throws Exception {
		String definition = read("ddm-structure-select-field-multiple.json");

		DDMStructure structure = createStructure("Test", definition);

		String xml = read("test-structure-select-field-multiple.xsd");

		Fields fields = _ddmXML.getFields(structure, xml);

		Assert.assertFalse(fields.getNames().isEmpty());

		for (Field field : fields) {
			Assert.assertTrue(structure.hasField(field.getName()));

			if (structure.getFieldRequired(field.getName())) {
				String value = (String)field.getValue(LocaleUtil.US);

				value = StringUtil.removeChars(
					value, CharPool.RETURN, CharPool.NEW_LINE, CharPool.TAB,
					CharPool.SPACE);

				Assert.assertTrue(Validator.isNotNull(value));
			}
		}
	}

	protected void setUpDDMXML() throws Exception {
		java.lang.reflect.Field field = ReflectionUtil.getDeclaredField(
			DDMXMLImpl.class, "_saxReader");

		field.set(_ddmXML, new SAXReaderImpl());
	}

	private final DDMXMLImpl _ddmXML = new DDMXMLImpl();

}