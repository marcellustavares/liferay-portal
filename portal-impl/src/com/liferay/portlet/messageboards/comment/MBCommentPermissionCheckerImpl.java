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

package com.liferay.portlet.messageboards.comment;

import com.liferay.portal.kernel.comment.Comment;
import com.liferay.portal.kernel.comment.CommentPermissionChecker;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.messageboards.service.permission.MBDiscussionPermission;

/**
 * @author André de Oliveira
 */
public class MBCommentPermissionCheckerImpl
	implements CommentPermissionChecker {

	@Override
	public boolean canAdd() {
		return MBDiscussionPermission.contains(
			_permissionChecker, _companyId, _scopeGroupId, _permissionClassName,
			_permissionClassPK, _userId, ActionKeys.ADD_DISCUSSION);
	}

	@Override
	public boolean canDelete(Comment comment) throws PortalException {

		return MBDiscussionPermission.contains(
			_permissionChecker, _companyId, _scopeGroupId, _permissionClassName,
			_permissionClassPK, comment.getMessageId(), comment.getUserId(),
			ActionKeys.DELETE_DISCUSSION);
	}

	@Override
	public boolean canEdit(Comment comment) throws PortalException {
		return MBDiscussionPermission.contains(
			_permissionChecker, _companyId, _scopeGroupId, _permissionClassName,
			_permissionClassPK, comment.getMessageId(), comment.getUserId(),
			ActionKeys.UPDATE_DISCUSSION);
	}

	@Override
	public boolean canView() {
		return MBDiscussionPermission.contains(
			_permissionChecker, _companyId, _scopeGroupId, _permissionClassName,
			_permissionClassPK, _userId, ActionKeys.VIEW);
	}

	MBCommentPermissionCheckerImpl(
		long userId, long scopeGroupId, PermissionChecker permissionChecker,
		long companyId, String permissionClassName, long permissionClassPK) {

		_companyId = companyId;
		_permissionChecker = permissionChecker;
		_permissionClassName = permissionClassName;
		_permissionClassPK = permissionClassPK;
		_scopeGroupId = scopeGroupId;
		_userId = userId;
	}

	private final long _companyId;
	private final PermissionChecker _permissionChecker;
	private final String _permissionClassName;
	private final long _permissionClassPK;
	private final long _scopeGroupId;
	private final long _userId;

}