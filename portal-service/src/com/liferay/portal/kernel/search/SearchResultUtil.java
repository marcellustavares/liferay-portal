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
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.messageboards.model.MBMessage;

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
			final String entryClassName = GetterUtil.getString(
				document.get(Field.ENTRY_CLASS_NAME));
			final long entryClassPK = GetterUtil.getLong(
				document.get(Field.ENTRY_CLASS_PK));

			try {
				final SearchResultKey key;
				final SearchResultContributor contributor;

				if (entryClassName.equals(DLFileEntry.class.getName()) ||
					entryClassName.equals(MBMessage.class.getName())) {

					final long overrideClassPK = GetterUtil.getLong(
						document.get(Field.CLASS_PK));
					final long overrideClassNameId = GetterUtil.getLong(
						document.get(Field.CLASS_NAME_ID));

					if ((overrideClassPK > 0) && (overrideClassNameId > 0)) {
						key = new SearchResultKey(
							PortalUtil.getClassName(overrideClassNameId),
							overrideClassPK);
						contributor = contributorRegistry.getInstance(
							entryClassName, entryClassPK, locale, portletURL,
							portletRequest, portletResponse);
					}
					else {
						key = new SearchResultKey(entryClassName, entryClassPK);
						contributor = null;
					}
				}
				else {
					key = new SearchResultKey(entryClassName, entryClassPK);
					contributor = null;
				}

				SearchResult searchResult = searchResults.get(key);

				if (searchResult == null) {
					searchResult = new SearchResult(key);
					searchResults.put(key, searchResult);
				}

				if (contributor != null) {
					contributor.contributeTo(searchResult, document);
				}

				if (entryClassName.equals(JournalArticle.class.getName())) {
					String version = document.get(Field.VERSION);

					searchResult.addVersion(version);
				}

				if (contributor == null) {
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

	private static Log _log = LogFactoryUtil.getLog(SearchResultUtil.class);

}