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

package com.liferay.dynamic.data.mapping.render;

import com.liferay.dynamic.data.mapping.storage.Field;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Pablo Carvalho
 */
public class DDMFormFieldRenderingContext {

	public Object getAttribute(String key) {
		return _attributes.get(key);
	}

	public Map<String, Object> getAttributes() {
		return _attributes;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getNestedDDMFormFieldsTemplateContext()}
	 */
	@Deprecated
	public String getChildElementsHTML() {
		return StringPool.BLANK;
	}

	public Fields getFields() {
		return _fields;
	}

	public HttpServletRequest getHttpServletRequest() {
		return _httpServletRequest;
	}

	public HttpServletResponse getHttpServletResponse() {
		return _httpServletResponse;
	}

	public String getLabel() {
		return MapUtil.getString(_attributes, "label");
	}

	public Locale getLocale() {
		return _locale;
	}

	public String getMode() {
		return _mode;
	}

	public String getName() {
		return MapUtil.getString(_attributes, "name");
	}

	public String getNamespace() {
		return _namespace;
	}

	public JSONArray getNestedDDMFormFieldsTemplateContext() {
		return (JSONArray)_attributes.get("nestedDDMFormFieldsTemplateContext");
	}

	public String getPortletNamespace() {
		return _portletNamespace;
	}

	public String getTemplateNamespace() {
		return MapUtil.getString(_attributes, "templateNamespace");
	}

	public String getTip() {
		return MapUtil.getString(_attributes, "tip");
	}

	public String getValidationErrorMessage() {
		return MapUtil.getString(_attributes, "validationErrorMessage");
	}

	public String getValue() {
		return MapUtil.getString(_attributes, "value");
	}

	public boolean isReadOnly() {
		return MapUtil.getBoolean(_attributes, "readOnly");
	}

	public boolean isRepeatable() {
		return MapUtil.getBoolean(_attributes, "repeatable");
	}

	public boolean isRequired() {
		return MapUtil.getBoolean(_attributes, "required");
	}

	public boolean isShowEmptyFieldLabel() {
		return MapUtil.getBoolean(_attributes, "showEmptyFieldLabel");
	}

	public boolean isValid() {
		return MapUtil.getBoolean(_attributes, "valid");
	}

	public boolean isVisible() {
		return MapUtil.getBoolean(_attributes, "visible");
	}

	public void setAttribute(String key, Object value) {
		_attributes.put(key, value);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #setNestedDDMFormFieldsTemplateContext(JSONArray)}
	 */
	@Deprecated
	public void setChildElementsHTML(String childElementsHTML) {
	}

	public void setField(Field field) {
		_fields = new Fields();

		_fields.put(field);
	}

	public void setFields(Fields fields) {
		_fields = fields;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		_httpServletRequest = httpServletRequest;
	}

	public void setHttpServletResponse(
		HttpServletResponse httpServletResponse) {

		_httpServletResponse = httpServletResponse;
	}

	public void setLabel(String label) {
		_attributes.put("label", label);
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	public void setMode(String mode) {
		_mode = mode;
	}

	public void setName(String name) {
		_attributes.put("name", name);
	}

	public void setNamespace(String namespace) {
		_namespace = namespace;
	}

	public void setNestedDDMFormFieldsTemplateContext(
		JSONArray nestedDDMFormFieldsTemplateContext) {

		_attributes.put(
			"nestedDDMFormFieldsTemplateContext",
			nestedDDMFormFieldsTemplateContext);
	}

	public void setPortletNamespace(String portletNamespace) {
		_portletNamespace = portletNamespace;
	}

	public void setReadOnly(boolean readOnly) {
		_attributes.put("readOnly", readOnly);
	}

	public void setRepeatable(boolean repeatable) {
		_attributes.put("repeatable", repeatable);
	}

	public void setRequired(boolean required) {
		_attributes.put("required", required);
	}

	public void setShowEmptyFieldLabel(boolean showEmptyFieldLabel) {
		_attributes.put("showEmptyFieldLabel", showEmptyFieldLabel);
	}

	public void setTemplateNamespace(String templateNamespace) {
		_attributes.put("templateNamespace", templateNamespace);
	}

	public void setTip(String tip) {
		_attributes.put("tip", tip);
	}

	public void setValid(boolean valid) {
		_attributes.put("valid", valid);
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		_attributes.put("validationErrorMessage", validationErrorMessage);
	}

	public void setValue(String value) {
		_attributes.put("value", value);
	}

	public void setVisible(boolean visible) {
		_attributes.put("visible", visible);
	}

	private final Map<String, Object> _attributes = new HashMap<>();
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private Locale _locale;
	private String _portletNamespace;
	private Fields _fields;
	private String _mode;
	private String _namespace;

}