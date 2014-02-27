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
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.theInstance;

import static org.junit.Assert.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppLocalService;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletURL;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Note for Eclipse users:
 * -XX:-UseSplitVerifier is needed to run this test under Java 7.
 * (https://groups.google.com/d/msg/powermock/vngllLwhv70/UluqE0wTO-IJ)
 *
 * @author André de Oliveira
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( {
	DLAppLocalServiceUtil.class
})
public class SearchResultUtilDLFileEntryTest
	extends BaseSearchResultUtilTestCase {

	@Override
	public void setUp() {
		super.setUp();

		doSetupDLApp();
	}

	@Test
	public void testDLFileEntry() throws Exception {
		long fileEntryId = 404;
		Summary summaryFromIndexer = new Summary(
			null, "FileEntry Title", "FileEntry Content", null);

		doReturn(
			fileEntry
		).when(
			dlAppLocalService
		).getFileEntry(
			ENTRY_CLASS_PK
		);

		doReturn(
			fileEntryId
		).when(
			fileEntry
		).getFileEntryId();

		doReturn(
			indexer
		).when(
			indexerRegistry
		).getIndexer(
			DLFILEENTRY_CLASS_NAME
		);

		doReturn(
			null
		).when(
			indexerRegistry
		).getIndexer(
			ALTERNATE_CLASS_NAME
		);

		doReturn(
			summaryFromIndexer
		).when(
			indexer
		).getSummary(
			(Document)any(), (Locale)any(), anyString(), (PortletURL)any()
		);

		doReturn(
			assetRendererFactory
		).when(
			assetRendererFactoryRegistry
		).getAssetRendererFactoryByClassName(
			ALTERNATE_CLASS_NAME
		);

		doReturn(
			null
		).when(
			assetRendererFactoryRegistry
		).getAssetRendererFactoryByClassName(
			DLFILEENTRY_CLASS_NAME
		);

		doReturn(
			assetRenderer
		).when(
			assetRendererFactory
		).getAssetRenderer(
			ALTERNATE_CLASS_PK
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

		searchSingleDocument(newDocumentDLFileEntryWithAlternates());

		assertThat(
			"must match alternate className in Document", result.getClassName(),
			is(ALTERNATE_CLASS_NAME));
		assertThat(
			"must match alternate classPK in Document", result.getClassPK(),
			is(ALTERNATE_CLASS_PK));
		assertThat(result.getMBMessages(), empty());
		assertThat(result.getVersions(), empty());

		Summary summaryFromResult = result.getSummary();
		assertThat(
			"the SearchResult's summary must not be the same " +
				"as the Indexer's one", summaryFromResult,
			not(theInstance(summaryFromIndexer)));
		assertThat(summaryFromResult.getTitle(), is(SUMMARY_TITLE));
		assertThat(summaryFromResult.getContent(), is(SUMMARY_CONTENT));

		List<Tuple> tuples = result.getFileEntryTuples();
		assertThat("must add FileEntry tuple", tuples, hasSize(1));

		Tuple tuple = tuples.get(0);
		FileEntry fileEntryFromTuple = (FileEntry)tuple.getObject(0);
		Summary summaryFromTuple = (Summary)tuple.getObject(1);
		assertThat(fileEntryFromTuple, theInstance(fileEntry));
		assertThat(
			"the FileEntry tuple's summary must be the same " +
				"as the Indexer's one", summaryFromTuple,
			theInstance(summaryFromIndexer));
		assertThat(summaryFromTuple.getTitle(), is("FileEntry Title"));
		assertThat(summaryFromTuple.getContent(), is("FileEntry Content"));
	}

	@Test
	public void testDLFileEntryMissingAlternateClassPKAndName()
		throws PortalException, SystemException {

		searchSingleDocument(newDocumentDLFileEntry());

		assertThat(
			"must match className in Document", result.getClassName(),
			is(DLFILEENTRY_CLASS_NAME));
		assertThat(
			"must match classPK in Document", result.getClassPK(),
			is(ENTRY_CLASS_PK));
		assertThat(result.getFileEntryTuples(), empty());
		assertThat(result.getVersions(), empty());
		assertThat(result.getMBMessages(), empty());
		assertThat(result.getSummary(), nullValue());

		assertThat(
			"Must not add any FileEntry to the result. " +
				"Indeed, the DLAppLocalService should not even be invoked " +
				"when there isn't an alternate Class Name or PK.",
			result.getFileEntryTuples(), empty());

		verifyZeroInteractions(
			dlAppLocalService
		);
	}

	@Test
	public void testDLFileEntryMissingFromService()
		throws PortalException, SystemException {

		doReturn(
			null
		).when(
			dlAppLocalService
		).getFileEntry(
			ENTRY_CLASS_PK
		);

		searchSingleDocument(newDocumentDLFileEntryWithAlternates());

		assertThat(
			"must match alternate className in Document", result.getClassName(),
			is(ALTERNATE_CLASS_NAME));
		assertThat(
			"must match alternate classPK in Document", result.getClassPK(),
			is(ALTERNATE_CLASS_PK));
		assertThat(result.getMBMessages(), empty());
		assertThat(result.getVersions(), empty());

		assertThat(
			"This test documents observed behavior: " +
				"If the Document contains an alternate Class Name and PK, " +
				"but DLAppLocalService cannot find the given file entry, " +
				"no failure occurs. " +
				"We still get an entry at the search results, " +
				"which will use the alternate Class Name and PK, " +
				"but the file entry will be missing.",
			result.getFileEntryTuples(), empty());

		assertThat(
			"There was neither a MBMessage nor a FileEntry to work with, " +
				"therefore Indexer and AssetRenderer are both tried " +
				"but ultimately the summary ends empty.", result.getSummary(),
			nullValue());

		verify(
			dlAppLocalService
		).getFileEntry(
			ENTRY_CLASS_PK
		);

		verify(
			indexerRegistry
		).getIndexer(
			ALTERNATE_CLASS_NAME
		);

		verify(
			assetRendererFactoryRegistry
		).getAssetRendererFactoryByClassName(
			ALTERNATE_CLASS_NAME
		);
	}

	@Test
	public void testDLFileEntryWithDefectiveIndexer() throws Exception {

		doReturn(
			indexer
		).when(
			indexerRegistry
		).getIndexer(
			anyString()
		);

		doThrow(
			IllegalArgumentException.class
		).when(
			indexer
		).getSummary(
			(Document)any(), (Locale)any(), anyString(), (PortletURL)any()
		);

		doReturn(
			fileEntry
		).when(
			dlAppLocalService
		).getFileEntry(
			ENTRY_CLASS_PK
		);

		String snippet = "This is a snippet";

		DocumentImpl doc = newDocumentDLFileEntryWithAlternates();
		doc.add(new Field(Field.SNIPPET, snippet));

		searchSingleDocument(doc);

		assertThat(
			"must match alternate className in Document", result.getClassName(),
			is(ALTERNATE_CLASS_NAME));
		assertThat(
			"must match alternate classPK in Document", result.getClassPK(),
			is(ALTERNATE_CLASS_PK));
		assertThat(result.getMBMessages(), empty());
		assertThat(result.getVersions(), empty());

		assertThat(
			"This test documents observed behavior: " +
				"If a FileEntry is found but the Indexer throws " +
				"an exception on getSummary, the exception is discarded. " +
				"We still get an entry at the search results, " +
				"but BOTH the entry's FileEntry and summary will be missing.",
			result.getSummary(), nullValue());

		assertThat(
			"no file entry tuples even though a FileEntry was found",
			result.getFileEntryTuples(), empty());

		verify(
			dlAppLocalService
		).getFileEntry(
			ENTRY_CLASS_PK
		);

		verify(
			indexerRegistry
		).getIndexer(
			DLFILEENTRY_CLASS_NAME
		);

		verify(
			indexer
		).getSummary(
			doc, null, snippet, portletURL
		);
	}

	DocumentImpl newDocumentDLFileEntry() {

		return newDocument(DLFILEENTRY_CLASS_NAME);
	}

	DocumentImpl newDocumentDLFileEntryWithAlternates() {

		DocumentImpl doc = newDocumentDLFileEntry();
		setAlternates(doc, ALTERNATE_CLASS_PK, ALTERNATE_CLASS_NAME_ID);
		return doc;
	}

	protected void doSetupDLApp() {
		mockStatic(DLAppLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(DLAppLocalServiceUtil.class, "getService")
		).toReturn(
			dlAppLocalService
		);
	}

	static final String DLFILEENTRY_CLASS_NAME = DLFileEntry.class.getName();

	@Mock
	DLAppLocalService dlAppLocalService;

	@Mock
	FileEntry fileEntry;

}