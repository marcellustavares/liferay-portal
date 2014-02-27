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

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.doReturn;

import com.liferay.portal.kernel.util.FastDateFormatFactory;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistry;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;

import java.util.List;

import javax.portlet.PortletURL;

import org.junit.Before;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author André de Oliveira
 */
public abstract class BaseSearchResultUtilTestCase {

	@Before
	public void setUp() {
		doSetup();
	}

	List<SearchResult> getSearchResults(Hits hits) {

		return SearchResultUtil.getSearchResults(hits, null, portletURL);
	}

	DocumentImpl newDocument(String entryClassName) {

		DocumentImpl doc = new DocumentImpl();
		doc.add(
			new Field(Field.ENTRY_CLASS_PK, String.valueOf(ENTRY_CLASS_PK)));
		doc.add(new Field(Field.ENTRY_CLASS_NAME, entryClassName));
		return doc;
	}

	Hits newHits(Document... docs) {

		Hits hits = new HitsImpl();
		hits.setDocs(docs);
		return hits;
	}

	void searchSingleDocument(DocumentImpl doc) {

		List<SearchResult> searchResults = getSearchResults(newHits(doc));

		assertThat("one hit, one result", searchResults, hasSize(1));

		result = searchResults.get(0);
	}

	void setAlternates(
		DocumentImpl doc, long alternateClassPK, long alternateClassNameId) {

		doc.add(new Field(Field.CLASS_PK, String.valueOf(alternateClassPK)));
		doc.add(
			new Field(
				Field.CLASS_NAME_ID, String.valueOf(alternateClassNameId)));
	}

	protected void doSetup() {
		MockitoAnnotations.initMocks(this);

		PropsUtil.setProps(props);

		new FastDateFormatFactoryUtil().setFastDateFormatFactory(
			fastDateFormatFactory);

		new PortalUtil().setPortal(portal);

		new AssetRendererFactoryRegistryUtil().setAssetRendererFactoryRegistry(
			assetRendererFactoryRegistry);

		new IndexerRegistryUtil().setIndexerRegistry(indexerRegistry);

		doReturn(
			ALTERNATE_CLASS_NAME
		).when(
			portal
		).getClassName(
			ALTERNATE_CLASS_NAME_ID
		);
	}

	static final String ALTERNATE_CLASS_NAME = "com.liferay.Foo";
	static final long ALTERNATE_CLASS_NAME_ID = 42;
	static final long ALTERNATE_CLASS_PK = 142857;
	static final long ENTRY_CLASS_PK = 12321;

	static final String SUMMARY_TITLE = "S.R. Wars";
	static final String SUMMARY_CONTENT =
		"A long time ago, in a galaxy far, far away...";

	@Mock
	AssetRendererFactoryRegistry assetRendererFactoryRegistry;

	@Mock
	AssetRendererFactory assetRendererFactory;

	@Mock
	AssetRenderer assetRenderer;

	@Mock
	FastDateFormatFactory fastDateFormatFactory;

	@Mock
	IndexerRegistry indexerRegistry;

	@Mock
	Indexer indexer;

	@Mock
	Portal portal;

	@Mock
	PortletURL portletURL;

	@Mock
	Props props;

	SearchResult result;

}