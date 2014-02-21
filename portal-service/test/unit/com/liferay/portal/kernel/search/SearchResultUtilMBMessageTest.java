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

import static org.junit.Assert.assertThat;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.theInstance;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Java 7: this test requires -XX:-UseSplitVerifier
 *
 * https://groups.google.com/d/msg/powermock/vngllLwhv70/UluqE0wTO-IJ
 *
 * @author André de Oliveira
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MBMessageLocalServiceUtil.class})
public class SearchResultUtilMBMessageTest extends SearchResultUtilTestBase {

	@Override
	public void setUp() {
		super.setUp();

		/*
		com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil.
			getMessage(long)
		*/
		PowerMockito.mockStatic(
			MBMessageLocalServiceUtil.class, new CallsRealMethods());
		PowerMockito.stub(
			PowerMockito.method(MBMessageLocalServiceUtil.class, "getService")
						).toReturn(mockMBMessageLocalService);
	}

	@Test
	public void testMBMessage() throws PortalException, SystemException {

		doReturn(ALTERNATE_CLASS_NAME).when(mockPortal).getClassName(
			ALTERNATE_CLASS_NAME_ID);
		doReturn(mockMBMessage).when(mockMBMessageLocalService).getMessage(
			ENTRY_CLASS_PK);

		searchSingleDocument(newDocumentMBMessageWithAlternates());

		assertThat(
			"must match alternate className in Document", result.getClassName(),
			is(ALTERNATE_CLASS_NAME));
		assertThat(
			"must match alternate classPK in Document", result.getClassPK(),
			is(ALTERNATE_CLASS_PK));

		List<MBMessage> mbMessages = result.getMBMessages();
		assertThat("must add MBMessage", mbMessages, hasSize(1));
		assertThat(
			"must be the prepared MBMessage", mbMessages.get(0),
			theInstance(mockMBMessage));

		// verify API was not called spuriously

		verifyZeroInteractions(mockIndexerRegistry);

		assertThat(result.getSummary(), nullValue());

		assertAllUnrelatedDetailsAreEmpty();
	}

	@Test
	public void testMBMessageMissingAlternateClassPKAndName()
		throws PortalException, SystemException {

		searchSingleDocument(newDocumentMBMessage());

		assertThat(
			"must match className in Document", result.getClassName(),
			is(MBMESSAGE_CLASS_NAME));
		assertThat(
			"must match classPK in Document", result.getClassPK(),
			is(ENTRY_CLASS_PK));

		assertThat(
			"Must not add any MBMessage to the result. " +
			"Indeed, the MBMessageLocalService should not even be invoked " +
			"when there isn't an alternate Class Name or PK.",
			result.getMBMessages(), empty());

		// verify API was not called spuriously

		verifyZeroInteractions(mockMBMessageLocalService);

		assertThat(result.getSummary(), nullValue());

		assertAllUnrelatedDetailsAreEmpty();
	}

	@Test
	public void testMBMessageMissingFromService()
		throws PortalException, SystemException {

		doReturn(ALTERNATE_CLASS_NAME).when(mockPortal).getClassName(
			ALTERNATE_CLASS_NAME_ID);
		doReturn(null).when(mockMBMessageLocalService).getMessage(
			ENTRY_CLASS_PK);

		searchSingleDocument(newDocumentMBMessageWithAlternates());

		assertThat(
			"must match alternate className in Document", result.getClassName(),
			is(ALTERNATE_CLASS_NAME));
		assertThat(
			"must match alternate classPK in Document", result.getClassPK(),
			is(ALTERNATE_CLASS_PK));

		assertThat(
			"This test documents observed behavior: " +
			"If the Document contains an alternate Class Name and PK, " +
			"but MBMessageLocalService cannot find the given message, " +
			"no failure occurs. " +
			"We still get an entry at the search results, " +
			"which will use the alternate Class Name and PK, " +
			"but the message will be missing.", result.getMBMessages(),
			empty());

		assertThat(
			"There was neither a MBMessage nor a FileEntry to work with, " +
			"therefore Indexer and AssetRenderer are both tried " +
			"but ultimately the summary ends empty.", result.getSummary(),
			nullValue());

		// verify APIs were indeed called

		verify(mockMBMessageLocalService).getMessage(ENTRY_CLASS_PK);
		verify(mockIndexerRegistry).getIndexer(ALTERNATE_CLASS_NAME);
		verify(mockAssetRendererFactoryRegistry).
			getAssetRendererFactoryByClassName(ALTERNATE_CLASS_NAME);

		assertAllUnrelatedDetailsAreEmpty();
	}

	void assertAllUnrelatedDetailsAreEmpty() {

		assertThat(result.getFileEntryTuples(), empty());
		assertThat(result.getVersions(), empty());
	}

	DocumentImpl newDocumentMBMessage() {

		return newDocument(MBMESSAGE_CLASS_NAME);
	}

	DocumentImpl newDocumentMBMessageWithAlternates() {

		DocumentImpl doc = newDocumentMBMessage();
		setAlternates(doc, ALTERNATE_CLASS_PK, ALTERNATE_CLASS_NAME_ID);
		return doc;
	}

	static final String MBMESSAGE_CLASS_NAME = MBMessage.class.getName();

	@Mock
	MBMessage mockMBMessage;

	@Mock
	MBMessageLocalService mockMBMessageLocalService;

}