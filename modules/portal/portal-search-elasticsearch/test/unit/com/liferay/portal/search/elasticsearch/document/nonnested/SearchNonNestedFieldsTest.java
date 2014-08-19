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

package com.liferay.portal.search.elasticsearch.document.nonnested;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.search.elasticsearch.document.BaseElasticsearchTest;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class SearchNonNestedFieldsTest extends BaseElasticsearchTest {

	@Override
	public void doAfterRunTest() {
	}

	@Override
	public void doBeforeRunTest() {
	}

	@Test
	public void testSearch() throws Exception {
		String documentFromElasticSearch = verifyCreation(_EN);

		verifySearch(documentFromElasticSearch, 1, "languageId", _EN);

		documentFromElasticSearch = verifyCreation(_PT);

		verifySearch(documentFromElasticSearch, 1, "languageId", _PT);

		verifySearch(documentFromElasticSearch, 2, "articleId", _ARTICLE_ID);
	}

	protected DocumentImpl createDocument(String locale) {
		DocumentImpl document = createDocumentWithRequiredData();

		document.addKeyword("languageId",locale);
		document.addKeyword("articleId",_ARTICLE_ID);

		return document;
	}

	protected String verifyCreation(String locale) throws IOException {
		DocumentImpl document = createDocument(locale);

		String generatedJsonDocument = generateElasticsearchJson(document);

		assertNotNull(generatedJsonDocument);
		assertNotEquals("", generatedJsonDocument);

		String id = indexJsonDocument(generatedJsonDocument);
		String documentFromElasticSearch = getIndexedJsonDocument(id);
		return documentFromElasticSearch;
	}

	protected void verifySearch(
		String documentFromElasticSearch, int expectedTotalHits, String field,
		String value) {

		Hits hits = search(field, value);

		assertEquals(
			"Wrong total of hits!", expectedTotalHits, hits.getLength());
	}

	private static final String _ARTICLE_ID = "123";

	private static final String _EN = "en";

	private static final String _PT = "pt";

}