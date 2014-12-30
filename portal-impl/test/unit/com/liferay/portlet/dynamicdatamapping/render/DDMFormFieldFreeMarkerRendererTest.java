package com.liferay.portlet.dynamicdatamapping.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.liferay.portlet.dynamicdatamapping.BaseDDMTestCase;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldType;

import org.apache.commons.lang.StringUtils;

import org.junit.Test;
public class DDMFormFieldFreeMarkerRendererTest extends BaseDDMTestCase {
	@Test
	public void testGetFieldnameRoot() {
		String name;
		String pieces[];
		DDMFormField nameDDMFormField = new DDMFormField(
			"Foo", DDMFormFieldType.TEXT);

		DDMFormFieldFreeMarkerRenderer renderer =
			new DDMFormFieldFreeMarkerRenderer();

		// Expected: Foo_INSTANCE_<random>_0_en_US

		name = renderer.getFieldname(nameDDMFormField, null);

		pieces = name.split("_");

		assertEquals("Foo", pieces[0]);
		assertEquals("INSTANCE", pieces[1]);

		assertTrue(StringUtils.isAlphanumeric(pieces[2]));

		assertEquals("0", pieces[3]);
	}

	@Test
	public void testGetFieldnameWithParent() {
		String phone;
		String pieces[];
		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		DDMFormField phoneDDMFormField = new DDMFormField(
			"Phone", DDMFormFieldType.TEXT);

		nameDDMFormField.addNestedDDMFormField(phoneDDMFormField);

		DDMFormFieldFreeMarkerRenderer renderer =
			new DDMFormFieldFreeMarkerRenderer();

		// Expected: Name_INSTANCE_<random>_0__Phone_INSTANCE_<random>_0

		phone = renderer.getFieldname(
			phoneDDMFormField, renderer.getFieldname(nameDDMFormField, null));

		pieces = phone.split("_");

		assertEquals("Name", pieces[0]);
		assertEquals("INSTANCE", pieces[1]);
		assertTrue(StringUtils.isAlphanumeric(pieces[2]));
		assertEquals("0", pieces[3]);
		assertEquals("", pieces[4]);
		assertEquals("Phone", pieces[5]);
		assertEquals("INSTANCE", pieces[6]);
		assertTrue(StringUtils.isAlphanumeric(pieces[7]));
		assertEquals("0", pieces[8]);
	}

	@Test
	public void testGetNestedFormFieldIndexRoot() {
		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		DDMFormFieldFreeMarkerRenderer renderer =
			new DDMFormFieldFreeMarkerRenderer();

		assertEquals(0, renderer.getNestedFormFieldIndex(nameDDMFormField));
	}

	@Test
	public void testGetNestedFormFieldIndexWithOlderSibling() {
		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		DDMFormField phoneDDMFormField = new DDMFormField(
			"Phone", DDMFormFieldType.TEXT);

		DDMFormField emailDDMFormField = new DDMFormField(
			"Email", DDMFormFieldType.TEXT);

		nameDDMFormField.addNestedDDMFormField(phoneDDMFormField);
		nameDDMFormField.addNestedDDMFormField(emailDDMFormField);

		DDMFormFieldFreeMarkerRenderer renderer =
			new DDMFormFieldFreeMarkerRenderer();

		assertEquals(1, renderer.getNestedFormFieldIndex(emailDDMFormField));
	}

	@Test
	public void testGetNestedFormFieldIndexWithParent() {
		DDMFormField nameDDMFormField = new DDMFormField(
			"Name", DDMFormFieldType.TEXT);

		DDMFormField phoneDDMFormField = new DDMFormField(
			"Phone", DDMFormFieldType.TEXT);

		nameDDMFormField.addNestedDDMFormField(phoneDDMFormField);

		DDMFormFieldFreeMarkerRenderer renderer =
			new DDMFormFieldFreeMarkerRenderer();

		assertEquals(0, renderer.getNestedFormFieldIndex(phoneDDMFormField));
	}

}