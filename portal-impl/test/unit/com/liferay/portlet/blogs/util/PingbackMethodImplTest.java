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

package com.liferay.portlet.blogs.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.security.pacl.permission.PortalSocketPermission;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.xmlrpc.Fault;
import com.liferay.portal.kernel.xmlrpc.XmlRpc;
import com.liferay.portal.kernel.xmlrpc.XmlRpcConstants;
import com.liferay.portal.kernel.xmlrpc.XmlRpcUtil;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.PortletLocalService;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalService;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * @author André de Oliveira
 */
@PrepareForTest( {
	PropsValues.class, PortalSocketPermission.class,
	PortletLocalServiceUtil.class, BlogsEntryLocalServiceUtil.class,
	UserLocalServiceUtil.class, MBMessageLocalServiceUtil.class
})
@RunWith(PowerMockRunner.class)
public class PingbackMethodImplTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		setUpBlogsEntry();
		setUpHttp();
		setUpLanguage();
		setUpMessageBoards();
		setUpPortal();
		setUpPortlet();
		setUpUser();
		setUpXmlRpc();
	}

	@Test
	public void testDisabledPingbacksAtEntry() {

		when(
			_blogsEntry.isAllowPingbacks()
		).thenReturn(
			false
		);

		execute();

		verifyFault(
			XmlRpcConstants.REQUESTED_METHOD_NOT_FOUND,
			"Pingbacks are disabled");
	}

	@Test
	public void testDisabledPingbacksAtProps() {
		boolean previous = PropsValues.BLOGS_PINGBACK_ENABLED;

		Whitebox.setInternalState(
			PropsValues.class, "BLOGS_PINGBACK_ENABLED", false);

		try {
			execute();

			verifyFault(
				XmlRpcConstants.REQUESTED_METHOD_NOT_FOUND,
				"Pingbacks are disabled");
		}
		finally {
			Whitebox.setInternalState(
				PropsValues.class, "BLOGS_PINGBACK_ENABLED", previous);
		}
	}

	@Test
	public void testEntryIdParam() throws Exception {
		doTestEntryIdParam(null);
	}

	@Test
	public void testEntryIdParamWithNamespace() throws Exception {
		String namespace = "__namespace__.";

		when(
			_portal.getPortletNamespace(PortletKeys.BLOGS)
		).thenReturn(
			namespace
		);

		doTestEntryIdParam(namespace);
	}

	@Test
	public void testErrorAccessingSource() throws Exception {

		Mockito.doThrow(
			IOException.class
		).when(
			_http
		).URLtoString(
			"__sourceUri__"
		);

		execute();

		verifyFault(
			PingbackMethodImpl.SOURCE_URI_DOES_NOT_EXIST,
			"Error accessing source URI");
	}

	@Test
	public void testExcerptShorten() throws Exception {
		int previous = PropsValues.BLOGS_LINKBACK_EXCERPT_LENGTH;

		Whitebox.setInternalState(
			PropsValues.class, "BLOGS_LINKBACK_EXCERPT_LENGTH", 4);

		try {
			whenURLToStringSourceURIThenReturn(
				"<body><a href='http://liferay.com'>12345</a></body>");

			execute();

			verifyExcerpt("1...");
		}
		finally {
			Whitebox.setInternalState(
				PropsValues.class, "BLOGS_LINKBACK_EXCERPT_LENGTH", previous);
		}
	}

	@Test
	public void testExcerptWithParent() throws Exception {
		whenURLToStringSourceURIThenReturn(
			"<body><p>" +
			"Visit <a href='http://liferay.com'>Liferay</a> to learn more" +
			"</p></body>");

		execute();

		verifyExcerpt("Visit Liferay to learn more");
	}

	@Test
	public void testExcerptWithTwoParents() throws Exception {
		int previous = PropsValues.BLOGS_LINKBACK_EXCERPT_LENGTH;

		Whitebox.setInternalState(
			PropsValues.class, "BLOGS_LINKBACK_EXCERPT_LENGTH",
			"Liferay".length() + 11);

		try {
			whenURLToStringSourceURIThenReturn(
				"<body>_____<p>12345<span>67890" +
				"<a href='http://liferay.com'>Liferay</a>" +
				"12345</span>67890</p>_____</body>");

			execute();

			verifyExcerpt("1234567890Lifer...");
		}
		finally {
			Whitebox.setInternalState(
				PropsValues.class, "BLOGS_LINKBACK_EXCERPT_LENGTH", previous);
		}
	}

	@Test
	public void testFriendlyURL() throws Exception {
		long plid = 84L;

		when(
			_portal.getPlidFromFriendlyURL(COMPANY_ID, "/__blogs__")
		).thenReturn(
			plid
		);

		long groupId = 8844L;

		when(
			_portal.getScopeGroupId(plid)
		).thenReturn(
			groupId
		);

		whenFriendlyURLMapperPopulateParamsPut(
			"/__remainder-of-the-friendly-url__", "urlTitle", "__urlTitle__");

		String friendlyURL =
			"http://liferay.com/__blogs__/-/__remainder-of-the-friendly-url__";

		whenURLToStringSourceURIThenReturn(
			"<body><a href='" + friendlyURL + "'>Liferay</a></body>");

		execute(friendlyURL);

		verifySuccess();

		Mockito.verify(
			_blogsEntryLocalService
		).getEntry(
			groupId, "__urlTitle__"
		);
	}

	@Test
	public void testMalformedTarget() throws Exception {

		whenURLToStringSourceURIThenReturn("<a href='MALFORMED' />");

		execute("MALFORMED");

		verifyFault(
			PingbackMethodImpl.TARGET_URI_INVALID, "Error parsing target URI");
	}

	@Test
	public void testMissingTarget() throws Exception {

		whenURLToStringSourceURIThenReturn("");

		execute();

		verifyFault(
			PingbackMethodImpl.SOURCE_URI_INVALID,
			"Could not find target URI in source");
	}

	@Test
	public void testPingbackAlreadyRegistered() throws Exception {
		MBMessage message = Mockito.mock(MBMessage.class);

		when(
			message.getBody()
		).thenReturn(
			"[...] Liferay [...] [url=__sourceUri__]__read_more__[/url]"
		);

		List<MBMessage> messages = Collections.singletonList(message);

		when(
			_mbMessageLocalService.getThreadMessages(
				THREAD_ID, WorkflowConstants.STATUS_APPROVED)
		).thenReturn(
			messages
		);

		execute();

		verifyFault(
			PingbackMethodImpl.PINGBACK_ALREADY_REGISTERED,
			"Pingback previously registered");
	}

	@Test
	public void testSuccess() throws Exception {

		execute();

		verifySuccess();

		Mockito.verify(
			_mbMessageLocalService
		).getDiscussionMessageDisplay(
			USER_ID, GROUP_ID, BlogsEntry.class.getName(), ENTRY_ID,
			WorkflowConstants.STATUS_APPROVED
		);

		Mockito.verify(
			_mbMessageLocalService
		).getThreadMessages(
			THREAD_ID, WorkflowConstants.STATUS_APPROVED
		);

		Mockito.verify(
			_mbMessageLocalService
		).addDiscussionMessage(
			Matchers.eq(USER_ID), Matchers.eq(StringPool.BLANK),
			Matchers.eq(GROUP_ID), Matchers.eq(BlogsEntry.class.getName()),
			Matchers.eq(ENTRY_ID), Matchers.eq(THREAD_ID),
			Matchers.eq(PARENT_MESSAGE_ID), Matchers.eq(StringPool.BLANK),
			Matchers.eq(
				"[...] Liferay [...] [url=__sourceUri__]__read_more__[/url]"),
			(ServiceContext)Matchers.any()
		);
	}

	protected void doTestEntryIdParam(String namespace) throws Exception {

		when(
			_blogsEntryLocalService.getEntry(Matchers.anyLong())
		).thenReturn(
			_blogsEntry
		);

		String name = (namespace == null ? "" : namespace) + "entryId";
		long entryId = 12345;

		whenFriendlyURLMapperPopulateParamsPut(
			"", name, String.valueOf(entryId));

		execute();

		verifySuccess();

		Mockito.verify(
			_blogsEntryLocalService
		).getEntry(
			entryId
		);
	}

	protected void execute() {
		execute("http://liferay.com");
	}

	protected void execute(String targetURI) {
		PingbackMethodImpl method = new PingbackMethodImpl();

		method.setArguments(new Object[]{"__sourceUri__", targetURI});

		method.execute(COMPANY_ID);
	}

	protected void setUpBlogsEntry() throws Exception {

		when(
			_blogsEntry.getEntryId()
		).thenReturn(
			ENTRY_ID
		);

		when(
			_blogsEntry.getGroupId()
		).thenReturn(
			GROUP_ID
		);

		when(
			_blogsEntry.getUrlTitle()
		).thenReturn(
			"__UrlTitle__"
		);

		when(
			_blogsEntry.isAllowPingbacks()
		).thenReturn(
			true
		);

		when(
			_blogsEntryLocalService.getEntry(
				Matchers.anyLong(), Matchers.anyString())
		).thenReturn(
			_blogsEntry
		);

		mockStatic(BlogsEntryLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(BlogsEntryLocalServiceUtil.class, "getService")
		).toReturn(
			_blogsEntryLocalService
		);
	}

	protected void setUpHttp() throws Exception {

		whenURLToStringSourceURIThenReturn(
			"<body><a href='http://liferay.com'>Liferay</a></body>");

		mockStatic(PortalSocketPermission.class, new DoesNothing());

		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(_http);
	}

	protected void setUpLanguage() {

		whenLanguageGetThenReturn("read-more", "__read_more__");

		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);
	}

	protected void setUpMessageBoards() throws Exception {

		when(
			_mbMessageDisplay.getThread()
		).thenReturn(
			_mbThread
		);

		when(
			_mbMessageLocalService.getDiscussionMessageDisplay(
				Matchers.anyLong(), Matchers.anyLong(),
				Matchers.eq(BlogsEntry.class.getName()), Matchers.anyLong(),
				Matchers.eq(WorkflowConstants.STATUS_APPROVED))
		).thenReturn(
			_mbMessageDisplay
		);

		when(
			_mbThread.getRootMessageId()
		).thenReturn(
			PARENT_MESSAGE_ID
		);

		when(
			_mbThread.getThreadId()
		).thenReturn(
			THREAD_ID
		);

		mockStatic(MBMessageLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(MBMessageLocalServiceUtil.class, "getService")
		).toReturn(
			_mbMessageLocalService
		);
	}

	protected void setUpPortal() throws PortalException, SystemException {

		when(
			_portal.getPlidFromFriendlyURL(
				Matchers.eq(COMPANY_ID), Matchers.anyString())
		).thenReturn(
			42L
		);

		when(
			_portal.getScopeGroupId(Matchers.anyLong())
		).thenReturn(
			42L
		);

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(_portal);
	}

	protected void setUpPortlet() throws SystemException {

		Portlet portlet = Mockito.mock(Portlet.class);

		when(
			portlet.getFriendlyURLMapperInstance()
		).thenReturn(
			_friendlyURLMapper
		);

		when(
			portlet.getFriendlyURLMapping()
		).thenReturn(
			"__FriendlyURLMapping__"
		);

		PortletLocalService portletLocalService = Mockito.mock(
			PortletLocalService.class);

		when(
			portletLocalService.getPortletById(PortletKeys.BLOGS)
		).thenReturn(
			portlet
		);

		when(
			portletLocalService.getPortletById(
				Matchers.anyLong(), Matchers.eq(PortletKeys.BLOGS))
		).thenReturn(
			portlet
		);

		mockStatic(PortletLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(PortletLocalServiceUtil.class, "getService")
		).toReturn(
			portletLocalService
		);
	}

	protected void setUpUser() throws PortalException, SystemException {

		UserLocalService userLocalService = Mockito.mock(
			UserLocalService.class);

		when(
			userLocalService.getDefaultUserId(Matchers.anyLong())
		).thenReturn(
			USER_ID
		);

		mockStatic(UserLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(UserLocalServiceUtil.class, "getService")
		).toReturn(
			userLocalService
		);
	}

	protected void setUpXmlRpc() {

		Fault fault = Mockito.mock(Fault.class);

		when(
			_xmlRpc.createFault(Matchers.anyInt(), Matchers.anyString())
		).thenReturn(
			fault
		);

		XmlRpcUtil xmlRpcUtil = new XmlRpcUtil();

		xmlRpcUtil.setXmlRpc(_xmlRpc);
	}

	protected void verifyExcerpt(String excerpt) throws Exception {

		verifySuccess();

		Mockito.verify(
			_mbMessageLocalService
		).addDiscussionMessage(
			Matchers.anyLong(), Matchers.anyString(), Matchers.anyLong(),
			Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(),
			Matchers.anyLong(), Matchers.anyString(),
			Matchers.eq(
				"[...] " + excerpt +
					" [...] [url=__sourceUri__]__read_more__[/url]"),
			(ServiceContext)Matchers.any()
		);
	}

	protected void verifyFault(int code, String description) {

		Mockito.verify(
			_xmlRpc
		).createFault(
			code, description
		);
	}

	protected void verifySuccess() {

		Mockito.verify(
			_xmlRpc
		).createSuccess(
			"Pingback accepted"
		);
	}

	protected void whenFriendlyURLMapperPopulateParamsPut(
		String friendlyURLPath, final String name, final String value) {

		Mockito.doAnswer(
			new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocationOnMock)
					throws Throwable {

					Map<String, String[]> params = (Map<String, String[]>)
						invocationOnMock.getArguments()[1];

					params.put(name, new String[]{value});

					return null;
				}
			}
		).when(
			_friendlyURLMapper
		).populateParams(
			Matchers.eq(friendlyURLPath), Matchers.anyMap(), Matchers.anyMap()
		);
	}

	protected void whenLanguageGetThenReturn(String key, String toBeReturned) {

		when(
			_language.get((Locale)Matchers.any(), Matchers.eq(key))
		).thenReturn(
			toBeReturned
		);
	}

	protected void whenURLToStringSourceURIThenReturn(String toBeReturned)
		throws Exception {

		when(
			_http.URLtoString("__sourceUri__")
		).thenReturn(
			toBeReturned
		);
	}

	private static final long COMPANY_ID = 1L;

	private static final long ENTRY_ID = 99999L;

	private static final long GROUP_ID = 33L;

	private static final long PARENT_MESSAGE_ID = 1337L;

	private static final long THREAD_ID = 7676L;

	private static final long USER_ID = 142857L;

	@Mock
	private BlogsEntry _blogsEntry;

	@Mock
	private BlogsEntryLocalService _blogsEntryLocalService;

	@Mock
	private FriendlyURLMapper _friendlyURLMapper;

	@Mock
	private Http _http;

	@Mock
	private Language _language;

	@Mock
	private MBMessageDisplay _mbMessageDisplay;

	@Mock
	private MBMessageLocalService _mbMessageLocalService;

	@Mock
	private MBThread _mbThread;

	@Mock
	private Portal _portal;

	@Mock
	private XmlRpc _xmlRpc;

}