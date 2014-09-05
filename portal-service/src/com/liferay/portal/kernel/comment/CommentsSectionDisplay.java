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

package com.liferay.portal.kernel.comment;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portlet.ratings.model.RatingsEntry;
import com.liferay.portlet.ratings.model.RatingsStats;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author André de Oliveira
 */
public interface CommentsSectionDisplay {

	public boolean canViewControls(Comment comment) throws PortalException;

	public boolean canViewDiscussion();

	public boolean canViewRatings(Comment comment) throws PortalException;

	public boolean canViewSearchPaginator();

	public boolean canViewSubscribeUnsubscribe() throws PortalException;

	public boolean canViewThreadedReplies();

	public boolean canViewWorkflowStatus(Comment comment);

	public String getBodyFormatted(Comment comment);

	public List<CommentTreeNodeDisplay> getCommentTreeNodeDisplays();

	public Comment getParentComment(Comment comment) throws PortalException;

	public String getRatingsClassName();

	public RatingsEntry getRatingsEntry(Comment comment);

	public RatingsStats getRatingsStats(Comment comment);

	public long getRootCommentMessageId();

	public SearchContainer getSearchContainer();

	public long getThreadId();

	public boolean hasCommentsToView();

	public boolean hasPermissionToAdd();

	public boolean hasPermissionToDelete(Comment comment)
		throws PortalException;

	public boolean hasPermissionToEdit(Comment comment) throws PortalException;

	public boolean hasWorkflowDefinitionLink();

	public List<Comment> initComments(
		RenderRequest renderRequest, RenderResponse renderResponse);

	public boolean isTopChild(Comment comment) throws PortalException;

	public boolean noCommentsYet();

	public boolean shouldSkipComment(Comment comment);

}