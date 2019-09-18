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

package com.liferay.segments.experiment.web.internal.servlet.taglib;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.segments.constants.SegmentsWebKeys;
import com.liferay.segments.experiment.web.internal.constants.SegmentsExperimentWebKeys;
import com.liferay.segments.experiment.web.internal.util.SegmentsExperimentUtil;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.model.SegmentsExperiment;
import com.liferay.segments.service.SegmentsExperienceLocalService;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 */
@Component(immediate = true, service = DynamicInclude.class)
public class SegmentsExperimentAnalyticsTopHeadJSPDynamicInclude
	implements DynamicInclude {

	@Override
	public void include(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String key)
		throws IOException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!SegmentsExperimentUtil.isAnalyticsEnabled(
				themeDisplay.getCompanyId())) {

			return;
		}

		long[] segmentsExperienceIds = GetterUtil.getLongValues(
			httpServletRequest.getAttribute(
				SegmentsWebKeys.SEGMENTS_EXPERIENCE_IDS));

		String segmentsExperimentSegmentsExperienceKey =
			SegmentsExperienceConstants.KEY_DEFAULT;

		if (segmentsExperienceIds.length > 0) {
			SegmentsExperience segmentsExperience =
				_segmentsExperienceLocalService.fetchSegmentsExperience(
					segmentsExperienceIds[0]);

			if (segmentsExperience != null) {
				segmentsExperimentSegmentsExperienceKey =
					segmentsExperience.getSegmentsExperienceKey();
			}
		}

		String experienceId = segmentsExperimentSegmentsExperienceKey;

		String experimentId = StringPool.BLANK;
		String variantId = StringPool.BLANK;

		SegmentsExperiment segmentsExperiment =
			(SegmentsExperiment)httpServletRequest.getAttribute(
				SegmentsExperimentWebKeys.SEGMENTS_EXPERIMENT);

		if (segmentsExperiment != null) {
			experienceId = segmentsExperiment.getSegmentsExperienceKey();
			experimentId =
				"request.context.experimentId = \'" +
					segmentsExperiment.getSegmentsExperimentKey() + "\';";
			variantId =
				"request.context.variantId = \'" +
					segmentsExperimentSegmentsExperienceKey + "\';";
		}

		experienceId =
			"request.context.experienceId = \'" + experienceId + "\';";

		Map<String, String> replaceMap = new HashMap<>();

		replaceMap.put("experienceId", experienceId);
		replaceMap.put("experimentId", experimentId);
		replaceMap.put("variantId", variantId);

		StringBundler sb = StringUtil.replaceToStringBundler(
			_ANALYTICS_TMPL_CONTENT, "${", "}", replaceMap);

		sb.writeTo(httpServletResponse.getWriter());
	}

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register(
			"/html/common/themes/top_js.jspf#analytics");
	}

	private static final String _ANALYTICS_TMPL_CONTENT = StringUtil.read(
		SegmentsExperimentAnalyticsTopHeadJSPDynamicInclude.class,
		"analytics.tmpl");

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

}