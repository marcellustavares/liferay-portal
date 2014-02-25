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

package com.liferay.portlet.blogs.util;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import com.liferay.portal.kernel.security.pacl.permission.PortalSocketPermission;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.DoesNothing;

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
@PrepareForTest( {
	MBMessageLocalServiceUtil.class, PortalSocketPermission.class
})
public class LinkbackConsumerUtilTest {

	@Before
	public void setUp() throws Exception {
		_prepareMocks();
	}

	@Test
	public void testAddVerify() throws Exception {

		long messageId = 42;
		String url = "__url__";

		doReturn("__URLtoString__").when(_mockHttp).URLtoString(url);

		LinkbackConsumerUtil.addNewTrackback(messageId, url, "__entryUrl__");
		LinkbackConsumerUtil.verifyNewTrackbacks();

		// verify that expected API was indeed called

		verify(_mockMBMessageLocalService).deleteDiscussionMessage(messageId);
	}

	@Test
	public void testTrackbackToItself() throws Exception {

		long messageId = 42;
		String url = "__url__";

		doReturn("__URLtoString_contains_entryUrl__").when(
			_mockHttp).URLtoString(url);

		LinkbackConsumerUtil.addNewTrackback(messageId, url, "entryUrl");
		LinkbackConsumerUtil.verifyNewTrackbacks();

		// verify that unwanted API was not called spuriously

		verifyZeroInteractions(_mockMBMessageLocalService);
	}

	private void _prepareMocks() throws Exception {

		MockitoAnnotations.initMocks(this);

		mockStatic(MBMessageLocalServiceUtil.class, new CallsRealMethods());
		stub(method(MBMessageLocalServiceUtil.class, "getService")).toReturn(
			_mockMBMessageLocalService);

		mockStatic(PortalSocketPermission.class, new DoesNothing());

		new HttpUtil().setHttp(_mockHttp);
	}

	@Mock
	private Http _mockHttp;

	@Mock
	private MBMessageLocalService _mockMBMessageLocalService;

}