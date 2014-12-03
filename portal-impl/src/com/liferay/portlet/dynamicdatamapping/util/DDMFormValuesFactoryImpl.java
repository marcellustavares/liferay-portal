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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.UnlocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesFactoryImpl implements DDMFormValuesFactory {

	@Override
	public DDMFormValues create(
		DDMForm ddmForm, HttpServletRequest httpServletRequest) {

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		String defaulLanguageId = ParamUtil.getString(
			httpServletRequest, "defaultLanguageId");

		Locale defaultLocale = LocaleUtil.fromLanguageId(defaulLanguageId);

		Set<Locale> availableLocales = getAvailableLocales(httpServletRequest);

		ddmFormValues.setAvailableLocales(availableLocales);

		ddmFormValues.setDefaultLocale(defaultLocale);

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		Map<String, DDMFormFieldValue> ddmFormFieldValuesMap =
			createDDMFormFieldValuesMap(ddmFormFieldsMap, httpServletRequest);

		List<DDMFormFieldValue> ddmFormFieldValues =
			getSortedRootDDMFormFieldValues(
				ddmFormFieldValuesMap, ddmForm.getDDMFormFields());

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		return ddmFormValues;
	}

	@Override
	public DDMFormValues create(
		DDMForm ddmForm, PortletRequest portletRequest) {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		return create(ddmForm, httpServletRequest);
	}

	@Override
	public DDMFormValues create(
		DDMForm ddmForm, ServiceContext serviceContext) {

		HttpServletRequest httpServletRequest = serviceContext.getRequest();

		if (httpServletRequest != null) {
			return create(ddmForm, httpServletRequest);
		}

		return null;
	}

	protected DDMFormFieldValue createDDMFormFieldValue(
		Map<String, DDMFormField> ddmFormFieldsMap,
		String qualifiedFieldParameterName,
		HttpServletRequest httpServletRequest) {

		String fieldQualifiedName = StringUtil.extractLast(
			qualifiedFieldParameterName, StringPool.DOUBLE_UNDERLINE);

		if (fieldQualifiedName == null) {
			fieldQualifiedName = qualifiedFieldParameterName;
		}

		String[] qualifiedFieldParameterNameParts = StringUtil.split(
			fieldQualifiedName, StringPool.UNDERLINE);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		String fieldName = qualifiedFieldParameterNameParts[0];

		ddmFormFieldValue.setName(fieldName);
		ddmFormFieldValue.setInstanceId(qualifiedFieldParameterNameParts[2]);

		DDMFormField ddmFormField = ddmFormFieldsMap.get(fieldName);

		if (Validator.isNull(ddmFormField.getDataType())) {
			return ddmFormFieldValue;
		}

		if (ddmFormField.isLocalizable()) {
			Locale defaultLocale = getDefaultLocale(httpServletRequest);

			ddmFormFieldValue.setValue(new LocalizedValue(defaultLocale));
		}
		else {
			ddmFormFieldValue.setValue(new UnlocalizedValue(null));
		}

		setDDMFormFieldValueValues(
			ddmFormFieldValue, qualifiedFieldParameterName, httpServletRequest);

		return ddmFormFieldValue;
	}

	protected Map<String, DDMFormFieldValue> createDDMFormFieldValuesMap(
		Map<String, DDMFormField> ddmFormFieldsMap,
		HttpServletRequest httpServletRequest) {

		Map<String, DDMFormFieldValue> ddmFormFieldValuesMap =
			new HashMap<String, DDMFormFieldValue>();

		Map<String, String[]> parameterMap =
			httpServletRequest.getParameterMap();

		for (String parameterName : parameterMap.keySet()) {
			if (!parameterName.contains(DDMImpl.INSTANCE_SEPARATOR)) {
				continue;
			}

			String[] qualifiedFieldParameterNameParts = StringUtil.split(
				parameterName, StringPool.DOUBLE_UNDERLINE);

			for (int i = 0; i < qualifiedFieldParameterNameParts.length; i++) {
				String qualifiedFieldParameterName =
					getQualifiedFieldParameterName(
						qualifiedFieldParameterNameParts, i);

				if (ddmFormFieldValuesMap.containsKey(
						qualifiedFieldParameterName)) {

					continue;
				}

				DDMFormFieldValue ddmFormFieldValue = createDDMFormFieldValue(
					ddmFormFieldsMap, qualifiedFieldParameterName,
					httpServletRequest);

				ddmFormFieldValuesMap.put(
					qualifiedFieldParameterName, ddmFormFieldValue);
			}
		}

		return ddmFormFieldValuesMap;
	}

	protected Set<Locale> getAvailableLocales(
		HttpServletRequest httpServletRequest) {

		String[] availableLanguageIds = ParamUtil.getParameterValues(
			httpServletRequest, "availableLanguageIds");

		return getAvailableLocales(availableLanguageIds);
	}

	protected Set<Locale> getAvailableLocales(String[] availableLanguageIds) {
		Set<Locale> availableLocales = new HashSet<Locale>();

		for (String availableLanguageId : availableLanguageIds) {
			Locale availableLocale = LocaleUtil.fromLanguageId(
				availableLanguageId);

			availableLocales.add(availableLocale);
		}

		return availableLocales;
	}

	protected Locale getDefaultLocale(HttpServletRequest httpServletRequest) {
		String defaultLanguageId = ParamUtil.getString(
			httpServletRequest, "defaultLanguageId");

		return LocaleUtil.fromLanguageId(defaultLanguageId);
	}

	protected Set<String> getKeys(
		Map<String, DDMFormFieldValue> ddmFormFieldValuesMap,
		DDMFormField ddmFormField) {

		Set<String> fileteredKeys = new HashSet<String>();

		for (Map.Entry<String, DDMFormFieldValue> entry :
				ddmFormFieldValuesMap.entrySet()) {

			String key = entry.getKey();

			DDMFormFieldValue value = entry.getValue();

			if (value.getName().equals(ddmFormField.getName())) {
				fileteredKeys.add(key);
			}
		}

		return fileteredKeys;
	}

	protected Set<String> getKeys(
		Map<String, DDMFormFieldValue> ddmFormFieldValuesMap, String parentPath,
		DDMFormField ddmFormField) {

		Set<String> fileteredKeys = new HashSet<String>();

		parentPath += "__" + ddmFormField.getName();

		for (Map.Entry<String, DDMFormFieldValue> entry :
				ddmFormFieldValuesMap.entrySet()) {

			String key = entry.getKey();

			DDMFormFieldValue value = entry.getValue();

			if (key.startsWith(parentPath) &&
				value.getName().equals(ddmFormField.getName())) {

				fileteredKeys.add(key);
			}
		}

		return fileteredKeys;
	}

	protected DDMFormFieldValue getParentDDMFormFieldValue(
		Map<String, DDMFormFieldValue> ddmFormFieldValuesMap,
		String qualifiedFieldParameterName) {

		int pos = qualifiedFieldParameterName.lastIndexOf(
			StringPool.DOUBLE_UNDERLINE);

		if (pos == -1) {
			return null;
		}

		String parentQualifiedFieldParameterName =
			qualifiedFieldParameterName.substring(0, pos);

		return ddmFormFieldValuesMap.get(parentQualifiedFieldParameterName);
	}

	protected String getQualifiedFieldParameterName(
		String[] qualifiedFieldParameterNameParts, int offset) {

		StringBundler sb = new StringBundler(2 * (offset + 1) - 1);

		for (int i = 0; i <= offset; i++) {
			if (i != 0) {
				sb.append(StringPool.DOUBLE_UNDERLINE);
			}

			if ((i + 1) < qualifiedFieldParameterNameParts.length) {
				sb.append(qualifiedFieldParameterNameParts[i]);
			}
			else {
				String[] parts = StringUtil.split(
					qualifiedFieldParameterNameParts[i], StringPool.UNDERLINE);

				sb.append(
					parts[0] + "_" + parts[1] + "_" + parts[2] + "_" +
					parts[3]);
			}
		}

		return sb.toString();
	}

	protected List<DDMFormFieldValue> getSortedRootDDMFormFieldValues(
		Map<String, DDMFormFieldValue> ddmFormFieldValuesMap,
		List<DDMFormField> ddmFormFields) {

		List<DDMFormFieldValue> ddmFormFieldValues =
			new ArrayList<DDMFormFieldValue>();

		int i = 0;

		for (DDMFormField ddmFormField : ddmFormFields) {
			Set<String> keys = getKeys(ddmFormFieldValuesMap, ddmFormField);

			for (String key : keys) {
				DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValuesMap.get(
					key);

				int pos = i + getIndex(key);

				setNested(
					ddmFormFieldValuesMap,
					ddmFormField.getNestedDDMFormFields(), ddmFormFieldValue,
					key, pos);

				setAtIndex(ddmFormFieldValues, pos, ddmFormFieldValue);
			}

			i = ddmFormFieldValues.size();
		}

		return ddmFormFieldValues;
	}

	protected void setAtIndex(
		List<DDMFormFieldValue> ddmFormFieldValues, int index,
		DDMFormFieldValue ddmFormFieldValue) {

		if (ddmFormFieldValues.size() < index + 1) {
			for (int i = ddmFormFieldValues.size(); i <= index; i++) {
				ddmFormFieldValues.add(null);
			}
		}

		ddmFormFieldValues.set(index, ddmFormFieldValue);
	}

	protected void setDDMFormFieldValueValues(
		DDMFormFieldValue ddmFormFieldValue, String qualifiedFieldParameterName,
		HttpServletRequest httpServletRequest) {

		String[] availableLanguageIds = ParamUtil.getParameterValues(
			httpServletRequest, "availableLanguageIds");

		for (String availableLanguageId : availableLanguageIds) {
			String parameterValue = httpServletRequest.getParameter(
				qualifiedFieldParameterName.concat("_").concat(
					availableLanguageId));

			if (Validator.isNull(parameterValue)) {
				continue;
			}

			Value value = ddmFormFieldValue.getValue();

			value.addString(
				LocaleUtil.fromLanguageId(availableLanguageId), parameterValue);
		}
	}

	protected void setNested(
		Map<String, DDMFormFieldValue> ddmFormFieldValuesMap,
		List<DDMFormField> nestedDDMFormFields,
		DDMFormFieldValue parentDDMFormFieldValue, String parentPath, int pos) {

		int i = 0;

		for (DDMFormField nestedDDMFormField : nestedDDMFormFields) {
			Set<String> keys = getKeys(
				ddmFormFieldValuesMap, parentPath, nestedDDMFormField);

			for (String key : keys) {
				DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValuesMap.get(
					key);

				int pos1 = i + getIndex(key);

				setNested(
					ddmFormFieldValuesMap,
					nestedDDMFormField.getNestedDDMFormFields(),
					ddmFormFieldValue, key, pos1);

				setAtIndex(
					parentDDMFormFieldValue.getNestedDDMFormFieldValues(), pos1,
					ddmFormFieldValue);
			}

			i = parentDDMFormFieldValue.getNestedDDMFormFieldValues().size();
		}
	}

	private int getIndex(String qualifiedFieldParameterName) {
		String metadata = StringUtil.extractLast(
			qualifiedFieldParameterName, DDMImpl.INSTANCE_SEPARATOR);

		String[] metadataParts = StringUtil.split(
			metadata, StringPool.UNDERLINE);

		return GetterUtil.getInteger(metadataParts[1]);
	}

}