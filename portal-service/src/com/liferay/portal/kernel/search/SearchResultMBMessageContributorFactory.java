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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

/**
 * @author Eudaldo Alonso
 * @author André de Oliveira
 */
public class SearchResultMBMessageContributorFactory
	implements SearchResultContributorFactory {

	@Override
	public SearchResultContributor getInstance(
			long entryClassPK, Locale locale, PortletURL portletURL,
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws PortalException, SystemException {

		MBMessage mbMessage = MBMessageLocalServiceUtil.getMessage(
			entryClassPK);

		if (mbMessage == null) {
			return null;
		}

		return new SearchResultMBMessageContributor(mbMessage);
	}

	@Override
	public boolean requiresKeyInDocument() {
		return true;
	}

}