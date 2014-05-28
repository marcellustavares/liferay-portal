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

package com.liferay.portlet.dynamicdatamapping.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Pablo Carvalho
 */
public class DDMFormXSDSerializerImpl implements DDMFormXSDSerializer {

	@Override
	public DDMForm getDDMForm(String serializedDDMForm) throws Exception {
		DDMForm ddmForm = new DDMForm();

		Document document = SAXReaderUtil.read(serializedDDMForm);

		setFormAvailableLocales(document.getRootElement(), ddmForm);
		setFormDefaultLocale(document.getRootElement(), ddmForm);
		setFormFields(document.getRootElement(), ddmForm);

		return ddmForm;
	}

	protected void addOptionValueLabels(
		Element dynamicElementElement, DDMFormFieldOptions options,
		String optionValue) {

		List<Element> metadataElements = dynamicElementElement.elements(
			"meta-data");

		for (Element metadataElement : metadataElements) {
			String languageId = metadataElement.attributeValue("locale");

			Locale locale = LocaleUtil.fromLanguageId(languageId);

			Element labelElement = fetchMetadataEntry(metadataElement, "label");

			options.addOptionLabel(optionValue, locale, labelElement.getText());
		}
	}

	protected Element fetchMetadataEntry(
		Element parentElement, String entryName) {

		StringBundler sb = new StringBundler(3);

		sb.append("entry[@name=");
		sb.append(HtmlUtil.escapeXPathAttribute(entryName));
		sb.append(StringPool.CLOSE_BRACKET);

		XPath xPathSelector = SAXReaderUtil.createXPath(sb.toString());

		return (Element)xPathSelector.selectSingleNode(parentElement);
	}

	protected List<Locale> getAvailableLocales(Element rootElement) {
		List<Locale> availableLocales = new ArrayList<Locale>();

		String availableLanguageIds = rootElement.attributeValue(
			"available-locales");

		for (String availableLanguageId :
				StringUtil.split(availableLanguageIds)) {

			Locale availableLocale = LocaleUtil.fromLanguageId(
				availableLanguageId);

			availableLocales.add(availableLocale);
		}

		return availableLocales;
	}

	protected DDMFormField getDDMFormField(Element dynamicElementElement) {
		String name = dynamicElementElement.attributeValue("name");
		String type = dynamicElementElement.attributeValue("type");

		DDMFormField ddmFormField = new DDMFormField(name, type);

		setFieldDataType(dynamicElementElement, ddmFormField);
		setFieldIndexType(dynamicElementElement, ddmFormField);
		setFieldMultiple(dynamicElementElement, ddmFormField);
		setFieldRequired(dynamicElementElement, ddmFormField);

		List<Element> metadataElements = dynamicElementElement.elements(
			"meta-data");

		for (Element metadataElement : metadataElements) {
			setFieldMetadata(metadataElement, ddmFormField);
		}

		if (type.equals("radio") || type.equals("select")) {
			setFieldOptions(dynamicElementElement, ddmFormField);
		}
		else {
			setFieldNestedField(dynamicElementElement, ddmFormField);
		}

		return ddmFormField;
	}

	protected DDMFormFieldOptions getDDMFormFieldOptions(
		List<Element> dynamicElementElements) {

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		for (Element dynamicElementElement : dynamicElementElements) {
			String value = dynamicElementElement.attributeValue("value");

			ddmFormFieldOptions.addOption(value);

			addOptionValueLabels(
				dynamicElementElement, ddmFormFieldOptions, value);
		}

		return ddmFormFieldOptions;
	}

	protected List<DDMFormField> getDDMFormFields(Element rootElement) {
		List<DDMFormField> ddmFormFields = new ArrayList<DDMFormField>();

		for (Element dynamicElement : rootElement.elements("dynamic-element")) {
			DDMFormField field = getDDMFormField(dynamicElement);

			ddmFormFields.add(field);
		}

		return ddmFormFields;
	}

	protected Locale getDefaultLocale(Element rootElement) {
		String defaultLanguageId = rootElement.attributeValue("default-locale");

		return LocaleUtil.fromLanguageId(defaultLanguageId);
	}

	protected void setFieldDataType(
		Element dynamicElementElement, DDMFormField ddmFormField) {

		String dataType = dynamicElementElement.attributeValue("dataType");

		ddmFormField.setDataType(dataType);
	}

	protected void setFieldIndexType(
		Element dynamicElementElement, DDMFormField ddmFormField) {

		String indexType = dynamicElementElement.attributeValue("indexType");

		ddmFormField.setIndexType(indexType);
	}

	protected void setFieldMetadata(
		Element metadataElement, DDMFormField ddmFormField) {

		String languageId = metadataElement.attributeValue("locale");

		Locale currentLocale = LocaleUtil.fromLanguageId(languageId);

		Element labelElement = fetchMetadataEntry(metadataElement, "label");

		if (labelElement != null) {
			LocalizedValue fieldLabel = ddmFormField.getLabel();

			fieldLabel.addValue(currentLocale, labelElement.getText());
		}

		Element predefinedValueElement = fetchMetadataEntry(
			metadataElement, "predefinedValue");

		if (predefinedValueElement != null) {
			LocalizedValue fieldPredefinedValue =
				ddmFormField.getPredefinedValue();

			fieldPredefinedValue.addValue(
				currentLocale, predefinedValueElement.getText());
		}

		Element tipElement = fetchMetadataEntry(metadataElement, "tip");

		if (tipElement != null) {
			LocalizedValue fieldTip = ddmFormField.getTip();

			fieldTip.addValue(currentLocale, tipElement.getText());
		}

		Element styleElement = fetchMetadataEntry(metadataElement, "style");

		if (styleElement != null) {
			LocalizedValue fieldStyle = ddmFormField.getStyle();

			fieldStyle.addValue(currentLocale, styleElement.getText());
		}
	}

	protected void setFieldMultiple(
		Element dynamicElementElement, DDMFormField ddmFormField) {

		boolean multiple = GetterUtil.getBoolean(
			dynamicElementElement.attributeValue("multiple"));

		ddmFormField.setMultiple(multiple);
	}

	protected void setFieldNestedField(
		Element dynamicElementElement, DDMFormField ddmFormField) {

		List<DDMFormField> nestedFields = getDDMFormFields(
			dynamicElementElement);

		ddmFormField.setNestedFields(nestedFields);
	}

	protected void setFieldOptions(
		Element dynamicElementElement, DDMFormField ddmFormField) {

		DDMFormFieldOptions options = getDDMFormFieldOptions(
			dynamicElementElement.elements("dynamic-element"));

		ddmFormField.setOptions(options);
	}

	protected void setFieldRequired(
		Element dynamicElementElement, DDMFormField ddmFormField) {

		boolean required = GetterUtil.getBoolean(
			dynamicElementElement.attributeValue("required"));

		ddmFormField.setRequired(required);
	}

	protected void setFormAvailableLocales(
		Element rootElement, DDMForm ddmForm) {

		List<Locale> availableLocales = getAvailableLocales(rootElement);

		ddmForm.setAvailableLocales(availableLocales);
	}

	protected void setFormDefaultLocale(Element rootElement, DDMForm ddmForm) {
		Locale defaultLocale = getDefaultLocale(rootElement);

		ddmForm.setDefaultLocale(defaultLocale);
	}

	protected void setFormFields(Element rootElement, DDMForm ddmForm) {
		List<DDMFormField> ddmFormFields = getDDMFormFields(rootElement);

		ddmForm.setDDMFormFields(ddmFormFields);
	}

}