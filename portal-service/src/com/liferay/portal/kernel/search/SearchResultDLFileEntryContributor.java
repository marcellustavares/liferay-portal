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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

/**
 * @author Eudaldo Alonso
 * @author André de Oliveira
 */
public class SearchResultDLFileEntryContributor
	implements SearchResultContributor {

	@Override
	public void contributeTo(SearchResult searchResult, Document document)
		throws PortalException, SystemException {

		Summary summary = SearchResultSummaryFactory.getSummary(
			document, DLFileEntry.class.getName(), _fileEntry.getFileEntryId(),
			_locale, _portletURL, _portletRequest, _portletResponse);

		searchResult.addFileEntry(_fileEntry, summary);
	}

	@Override
	public boolean prefersSummaryOfDocument() {
		return false;
	}

	protected SearchResultDLFileEntryContributor(
		FileEntry fileEntry, Locale locale, PortletURL portletURL,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		_fileEntry = fileEntry;
		_locale = locale;
		_portletURL = portletURL;
		_portletRequest = portletRequest;
		_portletResponse = portletResponse;
	}

	private FileEntry _fileEntry;
	private Locale _locale;
	private PortletRequest _portletRequest;
	private PortletResponse _portletResponse;
	private PortletURL _portletURL;

}