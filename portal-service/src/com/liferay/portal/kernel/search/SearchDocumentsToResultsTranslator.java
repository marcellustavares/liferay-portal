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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.util.PortalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

/**
 * @author Eudaldo Alonso
 * @author André de Oliveira
 */
public class SearchDocumentsToResultsTranslator {

	public SearchDocumentsToResultsTranslator(
		Locale locale, PortletURL portletURL, PortletRequest portletRequest,
		PortletResponse portletResponse,
		SearchResultContributorRegistry contributorRegistry) {

		_locale = locale;
		_portletURL = portletURL;
		_portletRequest = portletRequest;
		_portletResponse = portletResponse;

		_searchResults = new HashMap<SearchResultKey, SearchResult>();
		_contributorRegistry = contributorRegistry;
	}

	public List<SearchResult> translate(Document[] docs) {
		for (Document document : docs) {
			try {
				add(document);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					long entryClassPK = GetterUtil.getLong(
						document.get(Field.ENTRY_CLASS_PK));
					_log.warn(
						"Search index is stale and contains entry {" +
							entryClassPK + "}");
				}
			}
		}

		return getSearchResultList();
	}

	protected void add(Document document)
		throws PortalException, SystemException {

		String entryClassName = GetterUtil.getString(
			document.get(Field.ENTRY_CLASS_NAME));
		long entryClassPK = GetterUtil.getLong(
			document.get(Field.ENTRY_CLASS_PK));

		SearchResultKey key = null;
		SearchResultContributor contributor = null;

		SearchResultContributorFactory factory =
			_contributorRegistry.getFactory(entryClassName);

		if (factory != null) {
			boolean useContributor = true;

			if (factory.requiresKeyInDocument()) {
				SearchResultKey keyInDocument = getSearchResultKey(document);

				if (keyInDocument == null) {
					useContributor = false;
				}
				else {
					key = keyInDocument;
				}
			}

			if (useContributor) {
				contributor = factory.getInstance(
					entryClassPK, _locale, _portletURL, _portletRequest,
					_portletResponse);
			}
		}

		if (key == null) {
			key = new SearchResultKey(entryClassName, entryClassPK);
		}

		SearchResult searchResult = _searchResults.get(key);

		if (searchResult == null) {
			searchResult = new SearchResult(key);
			_searchResults.put(key, searchResult);
		}

		if (contributor != null) {
			contributor.contributeTo(searchResult, document);
		}

		if ((contributor == null) || contributor.prefersSummaryOfDocument()) {
			Summary summary = SearchResultSummaryFactory.getSummary(
				document, searchResult.getClassName(),
				searchResult.getClassPK(), _locale, _portletURL,
				_portletRequest, _portletResponse);

			searchResult.setSummary(summary);
		}
		else {
			if (searchResult.getSummary() == null) {
				Summary summary = SearchResultSummaryFactory.getSummary(
					searchResult.getClassName(), searchResult.getClassPK(),
					_locale, _portletURL);

				searchResult.setSummary(summary);
			}
		}
	}

	protected SearchResultKey getSearchResultKey(Document document) {
		long classPK = GetterUtil.getLong(document.get(Field.CLASS_PK));
		long classNameId = GetterUtil.getLong(
			document.get(Field.CLASS_NAME_ID));

		if ((classPK > 0) && (classNameId > 0)) {
			return new SearchResultKey(
				PortalUtil.getClassName(classNameId), classPK);
		}

		return null;
	}

	protected List<SearchResult> getSearchResultList() {
		return new ArrayList<SearchResult>(_searchResults.values());
	}

	private static Log _log = LogFactoryUtil.getLog(
		SearchDocumentsToResultsTranslator.class);

	private SearchResultContributorRegistry _contributorRegistry;
	private Locale _locale;
	private PortletRequest _portletRequest;
	private PortletResponse _portletResponse;
	private PortletURL _portletURL;
	private Map<SearchResultKey, SearchResult> _searchResults;

}