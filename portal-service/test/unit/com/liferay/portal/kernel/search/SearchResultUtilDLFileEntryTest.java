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

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppLocalService;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author André de Oliveira
 */
@PrepareForTest( {
	AssetRendererFactoryRegistryUtil.class, DLAppLocalServiceUtil.class,
	IndexerRegistryUtil.class
})
@RunWith(PowerMockRunner.class)
public class SearchResultUtilDLFileEntryTest
	extends BaseSearchResultUtilTestCase {

	@Override
	public void setUp() {
		super.setUp();

		setUpDLApp();
	}

	@Test
	public void testDLFileEntryMissingFromService() throws Exception {
		when(
			dlAppLocalService.getFileEntry(ENTRY_CLASS_PK)
		).thenReturn(
			null
		);

		searchSingleDocument(newDocumentDLFileEntryWithAlternateKey());

		Assert.assertEquals(DOCUMENT_CLASS_NAME, result.getClassName());
		Assert.assertEquals(DOCUMENT_CLASS_PK, result.getClassPK());

		Assert.assertThat(result.getMBMessages(), IsEmptyCollection.empty());
		Assert.assertThat(result.getVersions(), IsEmptyCollection.empty());
		Assert.assertThat(
			result.getFileEntryTuples(), IsEmptyCollection.empty());

		Assert.assertNull(
			"Indexer and AssetRenderer are both attempted, no summary returned",
			result.getSummary());

		verifyStatic();

		IndexerRegistryUtil.getIndexer(DOCUMENT_CLASS_NAME);

		verifyStatic();

		AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
			DOCUMENT_CLASS_NAME);

		Mockito.verify(
			dlAppLocalService
		).getFileEntry(
			ENTRY_CLASS_PK
		);
	}

	@Test
	public void testDLFileEntryWithDefectiveIndexer() throws Exception {
		when(
			indexer.getSummary(
				(Document)Matchers.any(), Matchers.anyString(),
				(PortletURL)Matchers.any(), (PortletRequest)Matchers.isNull(),
				(PortletResponse)Matchers.isNull())
		).thenThrow(
			IllegalArgumentException.class
		);

		stub(
			method(IndexerRegistryUtil.class, "getIndexer", String.class)
		).toReturn(
			indexer
		);

		when(
			dlAppLocalService.getFileEntry(ENTRY_CLASS_PK)
		).thenReturn(
			fileEntry
		);

		Document document = newDocumentDLFileEntryWithAlternateKey();

		document.add(new Field(Field.SNIPPET, "__snippet__"));

		searchSingleDocument(document);

		Assert.assertEquals(DOCUMENT_CLASS_NAME, result.getClassName());
		Assert.assertEquals(DOCUMENT_CLASS_PK, result.getClassPK());

		Assert.assertThat(result.getMBMessages(), IsEmptyCollection.empty());
		Assert.assertThat(result.getVersions(), IsEmptyCollection.empty());

		Assert.assertNull(
			"Indexer is attempted, exception is discarded, no summary returned",
			result.getSummary());

		verifyStatic();

		IndexerRegistryUtil.getIndexer(DLFILEENTRY_CLASS_NAME);

		Assert.assertThat(
			"no file entry tuples even though a FileEntry was found",
			result.getFileEntryTuples(), IsEmptyCollection.empty());

		Mockito.verify(
			dlAppLocalService
		).getFileEntry(
			ENTRY_CLASS_PK
		);

		Mockito.verify(
			indexer
		).getSummary(
			document, "__snippet__", portletURL, null, null
		);
	}

	@Test
	public void testDLFileEntryWithKeyInDocument() throws Exception {
		when(
			dlAppLocalService.getFileEntry(ENTRY_CLASS_PK)
		).thenReturn(
			fileEntry
		);

		class IndexerRegistryGetIndexer implements InvocationHandler {

			@Override
			public Indexer invoke(Object proxy, Method method, Object[] args)
				throws Throwable {

				String className = (String)args[0];

				if (DLFILEENTRY_CLASS_NAME.equals(className)) {
					return indexer;
				}

				if (DOCUMENT_CLASS_NAME.equals(className)) {
					return null;
				}

				throw new IllegalArgumentException();
			}
		}

		replace(
			method(IndexerRegistryUtil.class, "getIndexer", String.class)
		).with(
			new IndexerRegistryGetIndexer()
		);

		Summary summary = new Summary(
			null, "FileEntry Title", "FileEntry Content", null);

		when(
			indexer.getSummary(
				(Document)Matchers.any(), Matchers.anyString(),
				(PortletURL)Matchers.any(), (PortletRequest)Matchers.isNull(),
				(PortletResponse)Matchers.isNull())
		).thenReturn(
			summary
		);

		class AssetRendererFactoryRegistryGetAssetRendererFactoryByClassName
			implements InvocationHandler {

			@Override
			public AssetRendererFactory invoke(
				Object proxy, Method method, Object[] args)
			throws Throwable {

				String className = (String)args[0];

				if (DLFILEENTRY_CLASS_NAME.equals(className)) {
					return null;
				}

				if (DOCUMENT_CLASS_NAME.equals(className)) {
					return assetRendererFactory;
				}

				throw new IllegalArgumentException();
			}
		}

		replace(
			method(
				AssetRendererFactoryRegistryUtil.class,
				"getAssetRendererFactoryByClassName", String.class)
		).with(
			new AssetRendererFactoryRegistryGetAssetRendererFactoryByClassName()
		);

		when(
			assetRendererFactory.getAssetRenderer(DOCUMENT_CLASS_PK)
		).thenReturn(
			assetRenderer
		);

		when(
			assetRenderer.getTitle((Locale)Matchers.any())
		).thenReturn(
			SUMMARY_TITLE
		);

		when(
			assetRenderer.getSearchSummary((Locale)Matchers.any())
		).thenReturn(
			SUMMARY_CONTENT
		);

		searchSingleDocument(newDocumentDLFileEntryWithAlternateKey());

		Assert.assertEquals(DOCUMENT_CLASS_NAME, result.getClassName());
		Assert.assertEquals(DOCUMENT_CLASS_PK, result.getClassPK());

		Assert.assertThat(result.getMBMessages(), IsEmptyCollection.empty());
		Assert.assertThat(result.getVersions(), IsEmptyCollection.empty());

		Summary summaryFromResult = result.getSummary();

		Assert.assertNotSame(summary, summaryFromResult);

		Assert.assertEquals(SUMMARY_TITLE, summaryFromResult.getTitle());
		Assert.assertEquals(SUMMARY_CONTENT, summaryFromResult.getContent());

		List<Tuple> tuples = result.getFileEntryTuples();

		Assert.assertEquals(1, tuples.size());

		Tuple tuple = tuples.get(0);

		FileEntry fileEntryFromTuple = (FileEntry)tuple.getObject(0);

		Summary summaryFromTuple = (Summary)tuple.getObject(1);

		Assert.assertSame(fileEntry, fileEntryFromTuple);
		Assert.assertSame(summary, summaryFromTuple);

		Assert.assertEquals("FileEntry Title", summaryFromTuple.getTitle());
		Assert.assertEquals("FileEntry Content", summaryFromTuple.getContent());
	}

	@Test
	public void testDLFileEntryWithoutKeyInDocument() throws Exception {
		searchSingleDocument(newDocumentDLFileEntry());

		Assert.assertEquals(DLFILEENTRY_CLASS_NAME, result.getClassName());
		Assert.assertEquals(ENTRY_CLASS_PK, result.getClassPK());

		Assert.assertThat(
			result.getFileEntryTuples(), IsEmptyCollection.empty());
		Assert.assertThat(result.getVersions(), IsEmptyCollection.empty());
		Assert.assertThat(result.getMBMessages(), IsEmptyCollection.empty());

		Assert.assertNull(result.getSummary());

		Assert.assertThat(
			"DLAppLocalService must not be invoked at all",
			result.getFileEntryTuples(), IsEmptyCollection.empty());

		verifyZeroInteractions(dlAppLocalService);
	}

	protected Document newDocumentDLFileEntry() {
		return newDocument(DLFILEENTRY_CLASS_NAME);
	}

	protected Document newDocumentDLFileEntryWithAlternateKey() {
		return newDocumentWithAlternateKey(DLFILEENTRY_CLASS_NAME);
	}

	protected void setUpDLApp() {
		mockStatic(DLAppLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(DLAppLocalServiceUtil.class, "getService")
		).toReturn(
			dlAppLocalService
		);
	}

	protected static final String DLFILEENTRY_CLASS_NAME =
		DLFileEntry.class.getName();

	@Mock
	protected DLAppLocalService dlAppLocalService;

	@Mock
	protected FileEntry fileEntry;

}