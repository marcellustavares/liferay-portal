/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.taglib.ui;

import com.liferay.taglib.util.IncludeTag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * @author Brian Wing Shun Chan
 */
public class JournalContentSearchTag extends IncludeTag {

	@Override
	protected String getEndPage() {
		return _END_PAGE;
	}

	@Override
	protected String getStartPage() {
		return _START_PAGE;
	}

	public boolean isShowListed() {
		return _showListed;
	}

	public void setShowListed(boolean showListed) {
		_showListed = showListed;
	}

	public String getTargetPortletId() {
		return _targetPortletId;
	}

	public void setTargetPortletId(String targetPortletId) {
		_targetPortletId = targetPortletId;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	@Override
	protected void cleanUp() {
		_showListed = true;
		_targetPortletId = null;
		_type = null;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-ui:journal-content-search:showListed", 
			String.valueOf(_showListed));
		request.setAttribute(
			"liferay-ui:journal-content-search:targetPortletId",
			_targetPortletId);
		request.setAttribute("liferay-ui:journal-content-search:type", _type);
	}

	@Override
	protected int processEndTag() throws Exception {
		JspWriter jspWriter = pageContext.getOut();

		jspWriter.write("</form>");

		return EVAL_PAGE;
	}

	private static final String _END_PAGE =
		"/html/taglib/ui/journal_content_search/end.jsp";

	private static final String _START_PAGE =
		"/html/taglib/ui/journal_content_search/start.jsp";

	private String _type;
	private boolean _showListed;
	private String _targetPortletId;

}