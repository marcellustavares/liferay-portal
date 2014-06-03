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

package com.liferay.portal.comment;

import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.comment.DuplicateCommentException;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.test.RandomTestUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author André de Oliveira
 */
@PrepareForTest({MBMessageLocalServiceUtil.class})
@RunWith(PowerMockRunner.class)
public class CommentManagerImplTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		setUpMessageBoards();
		setUpServiceContext();
	}

	@Test
	public void testAddComment() throws Exception {
		_commentManager.addComment(
			_USER_ID, _GROUP_ID, "__className__", _ENTRY_ID, "__body__",
			_serviceContext);

		Mockito.verify(
			_mbMessageLocalService
		).addDiscussionMessage(
			_USER_ID, StringPool.BLANK, _GROUP_ID, "__className__", _ENTRY_ID,
			_THREAD_ID, _PARENT_MESSAGE_ID, StringPool.BLANK, "__body__",
			_serviceContext
		);

		Mockito.verify(
			_mbMessageLocalService
		).getThreadMessages(
			_THREAD_ID, WorkflowConstants.STATUS_APPROVED
		);
	}

	@Test
	public void testAddCommentWithUsernameAndSubject() throws Exception {
		long mbMessageId = RandomTestUtil.randomLong();

		when(
			_mbMessage.getMessageId()
		).thenReturn(
			mbMessageId
		);

		Assert.assertEquals(
			mbMessageId,
			_commentManager.addComment(
				_USER_ID, _GROUP_ID, "__className__", _ENTRY_ID, "__userName__",
				"__subject__", "__body__", _serviceContextFunction));

		Mockito.verify(
			_mbMessageLocalService
		).addDiscussionMessage(
			_USER_ID, "__userName__", _GROUP_ID, "__className__", _ENTRY_ID,
			_THREAD_ID, _PARENT_MESSAGE_ID, "__subject__", "__body__",
			_serviceContext
		);
	}

	@Test(expected = DuplicateCommentException.class)
	public void testAddDuplicateComment() throws Exception {
		when(
			_mbMessage.getBody()
		).thenReturn(
			"__body__"
		);

		List<MBMessage> messages = Collections.singletonList(_mbMessage);

		when(
			_mbMessageLocalService.getThreadMessages(
				_THREAD_ID, WorkflowConstants.STATUS_APPROVED)
		).thenReturn(
			messages
		);

		_commentManager.addComment(
			_USER_ID, _GROUP_ID, "__className__", _ENTRY_ID, "__body__",
			_serviceContext);

		Assert.fail();
	}

	@Test
	public void testDeleteComment() throws Exception {
		long mbMessageId = RandomTestUtil.randomLong();

		_commentManager.deleteComment(mbMessageId);

		Mockito.verify(
			_mbMessageLocalService
		).deleteDiscussionMessage(
			mbMessageId
		);
	}

	protected void setUpMessageBoards() throws Exception {
		when(
			_mbMessageDisplay.getThread()
		).thenReturn(
			_mbThread
		);

		when(
			_mbMessageLocalService.addDiscussionMessage(
				Matchers.anyLong(), Matchers.anyString(), Matchers.anyLong(),
				Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(),
				Matchers.anyLong(), Matchers.anyString(), Matchers.anyString(),
				(ServiceContext)Matchers.any()
			)
		).thenReturn(
			_mbMessage
		);

		when(
			_mbMessageLocalService.getDiscussionMessageDisplay(
				_USER_ID, _GROUP_ID, "__className__", _ENTRY_ID,
				WorkflowConstants.STATUS_APPROVED)
		).thenReturn(
			_mbMessageDisplay
		);

		mockStatic(MBMessageLocalServiceUtil.class, Mockito.CALLS_REAL_METHODS);

		stub(
			method(MBMessageLocalServiceUtil.class, "getService")
		).toReturn(
			_mbMessageLocalService
		);

		when(
			_mbThread.getRootMessageId()
		).thenReturn(
			_PARENT_MESSAGE_ID
		);

		when(
			_mbThread.getThreadId()
		).thenReturn(
			_THREAD_ID
		);
	}

	protected void setUpServiceContext() {
		when(
			_serviceContextFunction.apply(MBMessage.class.getName())
		).thenReturn(
			_serviceContext
		);
	}

	private static final long _ENTRY_ID = RandomTestUtil.randomLong();

	private static final long _GROUP_ID = RandomTestUtil.randomLong();

	private static final long _PARENT_MESSAGE_ID = RandomTestUtil.randomLong();

	private static final long _THREAD_ID = RandomTestUtil.randomLong();

	private static final long _USER_ID = RandomTestUtil.randomLong();

	private CommentManager _commentManager = new CommentManagerImpl();

	@Mock
	private MBMessage _mbMessage;

	@Mock
	private MBMessageDisplay _mbMessageDisplay;

	@Mock
	private MBMessageLocalService _mbMessageLocalService;

	@Mock
	private MBThread _mbThread;

	private ServiceContext _serviceContext = new ServiceContext();

	@Mock
	private Function<String, ServiceContext> _serviceContextFunction;

}