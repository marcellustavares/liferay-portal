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

package com.liferay.taglib.ddm.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * @author Bruno Basto
 * @generated
 */
public abstract class BaseHTMLFieldTag extends com.liferay.taglib.util.IncludeTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		return super.doStartTag();
	}

	public long getClassNameId() {
		return _classNameId;
	}

	public long getClassPK() {
		return _classPK;
	}

	public com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue getDdmFormFieldValue() {
		return _ddmFormFieldValue;
	}

	public java.lang.String getDdmFormValuesNamespace() {
		return _ddmFormValuesNamespace;
	}

	public boolean getReadOnly() {
		return _readOnly;
	}

	public boolean getRepeatable() {
		return _repeatable;
	}

	public java.util.Locale getRequestedLocale() {
		return _requestedLocale;
	}

	public boolean getShowEmptyFieldLabel() {
		return _showEmptyFieldLabel;
	}

	public void setClassNameId(long classNameId) {
		_classNameId = classNameId;

		setScopedAttribute("classNameId", classNameId);
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;

		setScopedAttribute("classPK", classPK);
	}

	public void setDdmFormFieldValue(com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue ddmFormFieldValue) {
		_ddmFormFieldValue = ddmFormFieldValue;

		setScopedAttribute("ddmFormFieldValue", ddmFormFieldValue);
	}

	public void setDdmFormValuesNamespace(java.lang.String ddmFormValuesNamespace) {
		_ddmFormValuesNamespace = ddmFormValuesNamespace;

		setScopedAttribute("ddmFormValuesNamespace", ddmFormValuesNamespace);
	}

	public void setReadOnly(boolean readOnly) {
		_readOnly = readOnly;

		setScopedAttribute("readOnly", readOnly);
	}

	public void setRepeatable(boolean repeatable) {
		_repeatable = repeatable;

		setScopedAttribute("repeatable", repeatable);
	}

	public void setRequestedLocale(java.util.Locale requestedLocale) {
		_requestedLocale = requestedLocale;

		setScopedAttribute("requestedLocale", requestedLocale);
	}

	public void setShowEmptyFieldLabel(boolean showEmptyFieldLabel) {
		_showEmptyFieldLabel = showEmptyFieldLabel;

		setScopedAttribute("showEmptyFieldLabel", showEmptyFieldLabel);
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_classNameId = 0;
		_classPK = 0;
		_ddmFormFieldValue = null;
		_ddmFormValuesNamespace = null;
		_readOnly = false;
		_repeatable = true;
		_requestedLocale = null;
		_showEmptyFieldLabel = true;
	}

	@Override
	protected String getEndPage() {
		return _END_PAGE;
	}

	@Override
	protected String getStartPage() {
		return _START_PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		setNamespacedAttribute(request, "classNameId", _classNameId);
		setNamespacedAttribute(request, "classPK", _classPK);
		setNamespacedAttribute(request, "ddmFormFieldValue", _ddmFormFieldValue);
		setNamespacedAttribute(request, "ddmFormValuesNamespace", _ddmFormValuesNamespace);
		setNamespacedAttribute(request, "readOnly", _readOnly);
		setNamespacedAttribute(request, "repeatable", _repeatable);
		setNamespacedAttribute(request, "requestedLocale", _requestedLocale);
		setNamespacedAttribute(request, "showEmptyFieldLabel", _showEmptyFieldLabel);
	}

	protected static final String _ATTRIBUTE_NAMESPACE = "ddm:html-field:";

	private static final String _END_PAGE =
		"/html/taglib/ddm/html_field/end.jsp";

	private static final String _START_PAGE =
		"/html/taglib/ddm/html_field/start.jsp";

	private long _classNameId = 0;
	private long _classPK = 0;
	private com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue _ddmFormFieldValue = null;
	private java.lang.String _ddmFormValuesNamespace = null;
	private boolean _readOnly = false;
	private boolean _repeatable = true;
	private java.util.Locale _requestedLocale = null;
	private boolean _showEmptyFieldLabel = true;

}