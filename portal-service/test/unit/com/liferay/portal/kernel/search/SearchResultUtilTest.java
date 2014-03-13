/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.theInstance;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import static org.junit.Assert.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletURL;

import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class SearchResultUtilTest extends BaseSearchResultUtilTestCase {

	@Test
	public void testBlankDocument() {

		searchBlankDocument();

		assertThat(
			"should return a null summary," +
			" as no Indexer or AssetRenderer were defined", result.getSummary(),
			nullValue()
		);

		assertAllUnrelatedDetailsAreEmpty();
	}

	@Test
	public void testNoHits() {

		Hits hits = newHits();

		List<SearchResult> searchResults = getSearchResults(hits);

		assertThat("no hits, no results", searchResults, empty());
	}

	@Test
	public void testSummaryFromAssetRenderer() throws Exception {

		doReturn(
			assetRendererFactory
		).when(
			assetRendererFactoryRegistry
		).getAssetRendererFactoryByClassName(
			anyString()
		);

		doReturn(
			assetRenderer
		).when(
			assetRendererFactory
		).getAssetRenderer(
			anyLong()
		);

		doReturn(
			SUMMARY_TITLE
		).when(
			assetRenderer
		).getTitle(
			(Locale)any()
		);

		doReturn(
			SUMMARY_CONTENT
		).when(
			assetRenderer
		).getSearchSummary(
			(Locale)any()
		);

		searchBlankDocument();

		Summary summary = result.getSummary();
		assertThat(summary.getTitle(), is(SUMMARY_TITLE));
		assertThat(summary.getContent(), is(SUMMARY_CONTENT));
		assertThat(summary.getMaxContentLength(), is(200));
		assertThat(summary.getPortletURL(), theInstance(portletURL));

		assertAllUnrelatedDetailsAreEmpty();
	}

	@Test
	public void testSummaryFromIndexer() throws Exception {

		Summary summary = new Summary(
			null, SUMMARY_TITLE, SUMMARY_CONTENT, null);

		doReturn(
			indexer
		).when(
			indexerRegistry
		).getIndexer(
			anyString()
		);

		doReturn(
			summary
		).when(
			indexer
		).getSummary(
			(Document)any(), (Locale)any(), anyString(), (PortletURL)any()
		);

		searchBlankDocument();

		assertThat(result.getSummary(), theInstance(summary));

		assertAllUnrelatedDetailsAreEmpty();
	}

	void assertAllUnrelatedDetailsAreEmpty() {

		assertThat(result.getClassName(), isEmptyString());
		assertThat(result.getClassPK(), is(0L));
		assertThat(result.getFileEntryTuples(), empty());
		assertThat(result.getMBMessages(), empty());
		assertThat(result.getVersions(), empty());
	}

	void searchBlankDocument() {

		searchSingleDocument(new DocumentImpl());
	}

}