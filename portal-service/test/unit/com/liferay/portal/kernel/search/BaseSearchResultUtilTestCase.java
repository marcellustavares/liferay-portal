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

import com.liferay.portal.kernel.util.FastDateFormatFactory;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.List;

import javax.portlet.PortletURL;

import org.apache.commons.lang.math.RandomUtils;

import org.junit.Assert;
import org.junit.Before;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author André de Oliveira
 */
public abstract class BaseSearchResultUtilTestCase extends PowerMockito {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		setUpPortal();

		setUpRegistryBeforeOtherRegistries();

		setUpAssetRendererFactoryRegistry();
		setUpIndexerRegistry();
	}

	protected List<SearchResult> getSearchResults(Hits hits) {
		return SearchResultUtil.getSearchResults(hits, null, portletURL);
	}

	protected Document newDocument(String entryClassName) {
		return newDocument(entryClassName, ENTRY_CLASS_PK);
	}

	protected Document newDocument(String entryClassName, long entryClassPk) {
		Document doc = new DocumentImpl();

		doc.add(new Field(Field.ENTRY_CLASS_PK, String.valueOf(entryClassPk)));
		doc.add(new Field(Field.ENTRY_CLASS_NAME, entryClassName));

		return doc;
	}

	protected Document newDocumentWithAlternateKey(String entryClassName) {
		Document doc = newDocument(entryClassName);

		setKeyInDocument(doc);

		return doc;
	}

	protected Hits newHits(Document... docs) {
		Hits hits = new HitsImpl();

		hits.setDocs(docs);

		return hits;
	}

	protected void searchSingleDocument(Document doc) {
		List<SearchResult> searchResults = getSearchResults(newHits(doc));

		Assert.assertEquals("one hit, one result", 1, searchResults.size());

		result = searchResults.get(0);
	}

	protected void setKeyInDocument(Document doc) {
		doc.add(new Field(Field.CLASS_PK, String.valueOf(DOCUMENT_CLASS_PK)));
		doc.add(
			new Field(
				Field.CLASS_NAME_ID, String.valueOf(DOCUMENT_CLASS_NAME_ID)));
	}

	protected void setUpAssetRendererFactoryRegistry() {
		mockStatic(
			AssetRendererFactoryRegistryUtil.class, new CallsRealMethods());
	}

	protected void setUpIndexerRegistry() {
		mockStatic(IndexerRegistryUtil.class, new CallsRealMethods());
	}

	protected void setUpPortal() {
		PropsUtil.setProps(props);

		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			mock(FastDateFormatFactory.class));

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(portal);

		when(
			portal.getClassName(DOCUMENT_CLASS_NAME_ID)
		).thenReturn(
			DOCUMENT_CLASS_NAME
		);
	}

	protected void setUpRegistryBeforeOtherRegistries() {
		Registry registry = mock(Registry.class);

		when(
			registry.setRegistry(registry)
		).thenReturn(
			registry
		);

		when(
			registry.getRegistry()
		).thenReturn(
			registry
		);

		ServiceTracker<Object, Object> serviceTracker = mock(
			ServiceTracker.class);

		when(
			registry.trackServices(
				(Class<Object>)Matchers.any(),
				(ServiceTrackerCustomizer<Object, Object>)Matchers.any())
		).thenReturn(
			serviceTracker
		);

		RegistryUtil.setRegistry(registry);
	}

	protected static final String DOCUMENT_CLASS_NAME =
		"com.liferay.ClassInDocument";

	protected static final long DOCUMENT_CLASS_NAME_ID = RandomUtils.nextLong();

	protected static final long DOCUMENT_CLASS_PK = RandomUtils.nextLong();

	protected static final long ENTRY_CLASS_PK = RandomUtils.nextLong();

	protected static final String SUMMARY_CONTENT =
		"A long time ago, in a galaxy far, far away...";

	protected static final String SUMMARY_TITLE = "S.R. Wars";

	@Mock
	protected AssetRenderer assetRenderer;

	@Mock
	protected AssetRendererFactory assetRendererFactory;

	@Mock
	protected Indexer indexer;

	@Mock
	protected Portal portal;

	@Mock
	protected PortletURL portletURL;

	@Mock
	protected Props props;

	protected SearchResult result;

}