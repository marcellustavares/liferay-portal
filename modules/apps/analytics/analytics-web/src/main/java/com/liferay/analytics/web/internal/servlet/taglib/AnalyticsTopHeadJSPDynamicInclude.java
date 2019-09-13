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

package com.liferay.analytics.web.internal.servlet.taglib;

import com.liferay.analytics.client.AnalyticsClientRequestContextContributor;
import com.liferay.analytics.web.internal.constants.AnalyticsWebKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.taglib.BaseJSPDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = DynamicInclude.class)
public class AnalyticsTopHeadJSPDynamicInclude extends BaseJSPDynamicInclude {

	@Override
	public void include(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String key)
		throws IOException {

		Map<String, String> context = new HashMap<>();

		for (AnalyticsClientRequestContextContributor
				analyticsClientRequestContextContributor :
					_analyticsClientRequestContextContributors) {

			analyticsClientRequestContextContributor.contribute(
				context, httpServletRequest);
		}

		System.out.println("context" + context);

		httpServletRequest.setAttribute(
			AnalyticsWebKeys.ANALYTICS_CLIENT_REQUEST_CONTEXT_KEY, context);

		super.include(httpServletRequest, httpServletResponse, key);
	}

	@Override
	public void register(
		DynamicInclude.DynamicIncludeRegistry dynamicIncludeRegistry) {

		dynamicIncludeRegistry.register(
			"/html/common/themes/top_head.jsp#post");
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void addAnalyticsClientRequestContextContributor(
		AnalyticsClientRequestContextContributor
			analyticsClientRequestContextContributor) {

		_analyticsClientRequestContextContributors.add(
			analyticsClientRequestContextContributor);
	}

	@Deactivate
	protected void deactivate() {
		_analyticsClientRequestContextContributors.clear();
	}

	@Override
	protected String getJspPath() {
		return "/dynamic_include/top_head.jsp";
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	protected void removeAnalyticsClientRequestContextContributor(
		AnalyticsClientRequestContextContributor
			analyticsClientRequestContextContributor) {

		_analyticsClientRequestContextContributors.remove(
			analyticsClientRequestContextContributor);
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.analytics.web)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AnalyticsTopHeadJSPDynamicInclude.class);

	private final List<AnalyticsClientRequestContextContributor>
		_analyticsClientRequestContextContributors =
			new CopyOnWriteArrayList<>();

}