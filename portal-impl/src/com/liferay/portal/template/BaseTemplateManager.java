/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.template;

import com.liferay.portal.kernel.security.pacl.NotPrivileged;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateResource;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author Raymond Augé
 */
public abstract class BaseTemplateManager extends BaseMultiTemplateManager {

	@NotPrivileged
	@Override
	public Template getTemplate(
		TemplateResource templateResource, boolean restricted) {

		return super.getTemplates(Collections.singletonList(templateResource),
			null, restricted);
	}

	@NotPrivileged
	@Override
	public Template getTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted) {

		return super.getTemplates(Collections.singletonList(templateResource),
			errorTemplateResource, restricted);
	}


	public Template getTemplates(
		List<TemplateResource> templateResources, boolean restricted) {

		throw new UnsupportedOperationException(
			"Template type does not support multi templates.");
	}

	public Template getTemplates(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted) {

		throw new UnsupportedOperationException(
			"Template type does not support multi templates.");
	}

	protected abstract Template doGetTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted,
		Map<String, Object> helperUtilities, boolean privileged);

	protected Template doGetTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted,
		Map<String, Object> helperUtilities, boolean privileged) {

		if (templateResources == null || templateResources.isEmpty()) {
			throw new IllegalArgumentException(
				"TemplateResource parameter is empty");
		}

		return doGetTemplate(templateResources.get(0),
			errorTemplateResource, restricted, helperUtilities, privileged);
	}

}