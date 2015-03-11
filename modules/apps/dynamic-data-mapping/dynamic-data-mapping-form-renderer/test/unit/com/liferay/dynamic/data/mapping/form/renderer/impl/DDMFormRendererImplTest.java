package com.liferay.dynamic.data.mapping.form.renderer.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.junit.Test;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

public class DDMFormRendererImplTest {

	@Test
	public void testCreateDDMFormFieldRenderingContextStackWithNoValues() {
		DDMForm ddmForm = new DDMForm();
		
		
		DDMFormField contactDDMFormField = new DDMFormField("contact", "separator");
		
		DDMFormField nameDDMFormField = new DDMFormField("name", "separator");
		
		DDMFormField firstNameDDMFormField = new DDMFormField("FirstName", "text");
		DDMFormField lastNameDDMFormField = new DDMFormField("LastName", "text");
		
		DDMFormField phoneDDMFormField = new DDMFormField("Phone", "text");
		
		DDMFormField extDDMFormField = new DDMFormField("Ext", "text");

		phoneDDMFormField.addNestedDDMFormField(extDDMFormField);
		
		nameDDMFormField.addNestedDDMFormField(firstNameDDMFormField);
		nameDDMFormField.addNestedDDMFormField(lastNameDDMFormField);
		
		
		contactDDMFormField.addNestedDDMFormField(nameDDMFormField);
		contactDDMFormField.addNestedDDMFormField(phoneDDMFormField);
		
		ddmForm.addDDMFormField(contactDDMFormField);

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap = Collections.emptyMap();
		
		Stack<DDMFormFieldRenderingContext> ddmFormFieldRenderingContextStack = new Stack<DDMFormFieldRenderingContext>();
		
		//_ddmFormRendererImpl.populateDDMFormFieldRenderingContextStack(ddmForm.getDDMFormFields(), ddmFormFieldValuesMap, null, ddmFormFieldRenderingContextStack);
		
//		while (!ddmFormFieldRenderingContextStack.isEmpty()) {
//		//	System.out.println(ddmFormFieldRenderingContextStack.pop().getDDMFormField().getName());
//		}
	}
	
	@Test
	public void testCreateDDMFormFieldRenderingContextStackWithValues() {
		DDMForm ddmForm = new DDMForm();
		
		DDMFormField contactDDMFormField = new DDMFormField("contact", "separator");
		
		contactDDMFormField.setRepeatable(true);
		
		DDMFormField nameDDMFormField = new DDMFormField("name", "separator");
		
		DDMFormField firstNameDDMFormField = new DDMFormField("firstName", "text");
		
		firstNameDDMFormField.setDataType("string");
		
		DDMFormField lastNameDDMFormField = new DDMFormField("lastName", "text");
		
		lastNameDDMFormField.setDataType("string");
		
		DDMFormField phoneDDMFormField = new DDMFormField("phone", "text");
		
		phoneDDMFormField.setDataType("string");
		phoneDDMFormField.setRepeatable(true);
		
		DDMFormField extDDMFormField = new DDMFormField("ext", "text");

		extDDMFormField.setDataType("string");
		
		phoneDDMFormField.addNestedDDMFormField(extDDMFormField);
		
		nameDDMFormField.addNestedDDMFormField(firstNameDDMFormField);
		nameDDMFormField.addNestedDDMFormField(lastNameDDMFormField);
		
		
		contactDDMFormField.addNestedDDMFormField(nameDDMFormField);
		contactDDMFormField.addNestedDDMFormField(phoneDDMFormField);
		
		ddmForm.addDDMFormField(contactDDMFormField);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);
		
		DDMFormFieldValue firstNameDDMFormFieldValue = createDDMFormFieldValue("firstName", new UnlocalizedValue("Joe"));
		DDMFormFieldValue lastNameDDMFormFieldValue = createDDMFormFieldValue("lastName", new UnlocalizedValue("Bloggs"));
		
		DDMFormFieldValue nameDDMFormFieldValue = createDDMFormFieldValue("name", null);
		
		nameDDMFormFieldValue.addNestedDDMFormFieldValue(firstNameDDMFormFieldValue);
		nameDDMFormFieldValue.addNestedDDMFormFieldValue(lastNameDDMFormFieldValue);
		
		DDMFormFieldValue phone1DDMFormFieldValue = createDDMFormFieldValue("phone", new UnlocalizedValue("Phone 1"));
		DDMFormFieldValue ext1DDMFormFieldValue = createDDMFormFieldValue("ext", new UnlocalizedValue("Ext 1"));
		
		phone1DDMFormFieldValue.addNestedDDMFormFieldValue(ext1DDMFormFieldValue);
		
		DDMFormFieldValue phone2DDMFormFieldValue = createDDMFormFieldValue("phone", new UnlocalizedValue("Phone 2"));
		DDMFormFieldValue ext2DDMFormFieldValue = createDDMFormFieldValue("ext", new UnlocalizedValue("Ext 2"));
		
		phone2DDMFormFieldValue.addNestedDDMFormFieldValue(ext2DDMFormFieldValue);
		
		DDMFormFieldValue contactDDMFormFieldValue = createDDMFormFieldValue("contact", null);
		
		contactDDMFormFieldValue.addNestedDDMFormFieldValue(nameDDMFormFieldValue);
		contactDDMFormFieldValue.addNestedDDMFormFieldValue(phone1DDMFormFieldValue);
		contactDDMFormFieldValue.addNestedDDMFormFieldValue(phone2DDMFormFieldValue);
		
		ddmFormValues.addDDMFormFieldValue(contactDDMFormFieldValue);
		
		
		Stack<DDMFormFieldRenderingContext> ddmFormFieldRenderingContextStack = new Stack<DDMFormFieldRenderingContext>();
		
//		_ddmFormRendererImpl.populateDDMFormFieldRenderingContextStack(ddmForm.getDDMFormFields(), ddmFormValues.getDDMFormFieldValuesMap(), null, ddmFormFieldRenderingContextStack);
//		
//		while (!ddmFormFieldRenderingContextStack.isEmpty()) {
//			System.out.println(ddmFormFieldRenderingContextStack.pop().getDDMFormField().getName());
//		}
	}
	
	protected DDMFormFieldValue createDDMFormFieldValue(
		String instanceId, String name, Value value) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setInstanceId(instanceId);
		ddmFormFieldValue.setName(name);
		ddmFormFieldValue.setValue(value);

		return ddmFormFieldValue;
	}

	protected DDMFormFieldValue createDDMFormFieldValue(
		String name, Value value) {

		return createDDMFormFieldValue(StringUtil.randomString(), name, value);
	}

	private DDMFormRendererImpl _ddmFormRendererImpl = new DDMFormRendererImpl();
}
