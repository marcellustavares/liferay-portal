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
 */
public class SearchResultUtil {

	public static List<SearchResult> getSearchResults(
		Hits hits, Locale locale, PortletURL portletURL) {

		return getSearchResults(hits, locale, portletURL, null, null);
	}

	public static List<SearchResult> getSearchResults(
		Hits hits, Locale locale, PortletURL portletURL,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		SearchResultContributorRegistry contributorRegistry =
			new SearchResultContributorRegistryImpl();

		Map<SearchResultKey, SearchResult> searchResults =
			new HashMap<SearchResultKey, SearchResult>();

		for (Document document : hits.getDocs()) {
			String entryClassName = GetterUtil.getString(
				document.get(Field.ENTRY_CLASS_NAME));
			long entryClassPK = GetterUtil.getLong(
				document.get(Field.ENTRY_CLASS_PK));

			try {
				SearchResultKey key = null;
				SearchResultContributor contributor = null;

				SearchResultContributorFactory factory =
					contributorRegistry.getFactory(entryClassName);

				if (factory != null) {
					boolean useContributor = true;

					if (factory.requiresKeyInDocument()) {
						SearchResultKey keyInDocument = getSearchResultKey(
							document);

						if (keyInDocument == null) {
							useContributor = false;
						}
						else {
							key = keyInDocument;
						}
					}

					if (useContributor) {
						contributor = factory.getInstance(
							entryClassPK, locale, portletURL, portletRequest,
							portletResponse);
					}
				}

				if (key == null) {
					key = new SearchResultKey(entryClassName, entryClassPK);
				}

				SearchResult searchResult = searchResults.get(key);

				if (searchResult == null) {
					searchResult = new SearchResult(key);
					searchResults.put(key, searchResult);
				}

				if (contributor != null) {
					contributor.contributeTo(searchResult, document);
				}

				if ((contributor == null) ||
					contributor.prefersSummaryOfDocument()) {

					Summary summary = SearchResultSummaryFactory.getSummary(
						document, searchResult.getClassName(),
						searchResult.getClassPK(), locale, portletURL,
						portletRequest, portletResponse);

					searchResult.setSummary(summary);
				}
				else {
					if (searchResult.getSummary() == null) {
						Summary summary = SearchResultSummaryFactory.getSummary(
							searchResult.getClassName(),
							searchResult.getClassPK(), locale, portletURL);

						searchResult.setSummary(summary);
					}
				}
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Search index is stale and contains entry {" +
							entryClassPK + "}");
				}
			}
		}

		return new ArrayList<SearchResult>(searchResults.values());
	}

	protected static SearchResultKey getSearchResultKey(Document document) {
		long classPK = GetterUtil.getLong(document.get(Field.CLASS_PK));
		long classNameId = GetterUtil.getLong(
			document.get(Field.CLASS_NAME_ID));

		if ((classPK > 0) && (classNameId > 0)) {
			return new SearchResultKey(
				PortalUtil.getClassName(classNameId), classPK);
		}

		return null;
	}

	private static Log _log = LogFactoryUtil.getLog(SearchResultUtil.class);

}