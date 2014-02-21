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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import static org.junit.Assert.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.liferay.portlet.journal.model.JournalArticle;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletURL;

import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class SearchResultUtilJournalArticleTest
	extends BaseSearchResultUtilTestCase {

	@Test
	public void testJournalArticle() {
		searchSingleDocument(newDocumentJournalArticleWithVersion());

		assertSearchResultWithVersion();

		assertThat(
			"should return a null summary, as no Indexer or AssetRenderer " +
			"were defined", result.getSummary(), nullValue());
	}

	@Test
	public void testJournalArticleWithDefectiveIndexer() throws Exception {

		doReturn(mockIndexer).when(mockIndexerRegistry).getIndexer(anyString());
		doThrow(IllegalArgumentException.class).when(mockIndexer).getSummary(
			(Document)any(), (Locale)any(), anyString(), (PortletURL)any());

		DocumentImpl doc = newDocumentJournalArticleWithVersion();

		searchSingleDocument(doc);

		assertSearchResultWithVersion();

		assertThat(
			"This test documents observed behavior: " +
			"If a JournalArticle is found but the Indexer throws " +
			"an exception on getSummary, the exception is discarded. " +
			"We still get an entry at the search results, " +
			"and versions will be set, but the summary will be missing.",
			result.getSummary(), nullValue());

		// verify APIs were indeed called

		verify(mockIndexerRegistry).getIndexer(JOURNALARTICLE_CLASS_NAME);
		verify(mockIndexer).getSummary(doc, null, "", mockPortletURL);
	}

	void assertSearchResultWithVersion() {

		assertThat(
			"must match className in Document", result.getClassName(),
			is(JOURNALARTICLE_CLASS_NAME));
		assertThat(result.getClassPK(), is(ENTRY_CLASS_PK));

		assertThat(result.getFileEntryTuples(), empty());
		assertThat(result.getMBMessages(), empty());

		List<String> versions = result.getVersions();
		assertThat("must add Journal Article version", versions, hasSize(1));
		assertThat(
			"must match version in Document", versions.get(0),
			is(DOCUMENT_VERSION));
	}

	DocumentImpl newDocumentJournalArticleWithVersion() {

		DocumentImpl doc = newDocument(JOURNALARTICLE_CLASS_NAME);
		doc.add(new Field(Field.VERSION, DOCUMENT_VERSION));
		return doc;
	}

	static final String DOCUMENT_VERSION = "42";
	static final String JOURNALARTICLE_CLASS_NAME =
		JournalArticle.class.getName();

}