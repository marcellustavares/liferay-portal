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
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateException;

import java.io.Writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(PowerMockRunner.class)
public class FieldSetDDMFormFieldRendererTest extends PowerMockito {

	@Test
	public void testPopulateOptionalContext() throws Exception {
		FieldSetDDMFormFieldRenderer fieldSetDDMFormFieldRenderer =
			new FieldSetDDMFormFieldRenderer();

		field(
			FieldSetDDMFormFieldRenderer.class,
			"fieldSetDDMFormFieldTemplateContextContributor"
		).set(
			fieldSetDDMFormFieldRenderer,
			fieldSetDDMFormFieldTemplateContextContributor
		);

		DDMFormField ddmFormField = new DDMFormField();

		ddmFormField.setRepeatable(true);
		ddmFormField.setProperty("title", "Field Set Title");
		ddmFormField.setProperty("rows", new ArrayList<DDMFormLayoutRow>());

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			new DDMFormFieldRenderingContext();

		TestTemplate testTemplate = new TestTemplate();

		fieldSetDDMFormFieldRenderer.populateOptionalContext(
			testTemplate, ddmFormField, ddmFormFieldRenderingContext);

		Assert.assertEquals(3, testTemplate.size());
		Assert.assertTrue(testTemplate.containsKey("repeatable"));
		Assert.assertTrue(testTemplate.containsKey("rows"));
		Assert.assertTrue(testTemplate.containsKey("title"));
	}

	protected FieldSetDDMFormFieldTemplateContextContributor
		fieldSetDDMFormFieldTemplateContextContributor =
			new FieldSetDDMFormFieldTemplateContextContributor();

	private class TestTemplate implements Template {

		@Override
		public void clear() {
			_map.clear();
		}

		@Override
		public boolean containsKey(Object key) {
			return _map.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return _map.containsValue(value);
		}

		@Override
		public void doProcessTemplate(Writer writer) throws Exception {
		}

		@Override
		public Set<Map.Entry<String, Object>> entrySet() {
			return _map.entrySet();
		}

		@Override
		public Object get(Object key) {
			return _map.get(key);
		}

		@Override
		public Object get(String key) {
			return _map.get(key);
		}

		@Override
		public String[] getKeys() {
			String[] keys = new String[_map.size()];

			return _map.keySet().toArray(keys);
		}

		@Override
		public boolean isEmpty() {
			return _map.isEmpty();
		}

		@Override
		public Set<String> keySet() {
			return _map.keySet();
		}

		@Override
		public void prepare(HttpServletRequest request) {
		}

		@Override
		public void processTemplate(Writer writer) throws TemplateException {
		}

		@Override
		public Object put(String key, Object value) {
			return _map.put(key, value);
		}

		@Override
		public void putAll(Map<? extends String, ? extends Object> m) {
			_map.putAll(m);
		}

		@Override
		public Object remove(Object key) {
			return _map.remove(key);
		}

		@Override
		public int size() {
			return _map.size();
		}

		@Override
		public Collection<Object> values() {
			return _map.values();
		}

		private final Map<String, Object> _map = new HashMap<>();

	}

}