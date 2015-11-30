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

package com.liferay.portal.template.soy;

import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.SingleVMPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoader;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.StringPool;

import java.net.URL;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miroslav Ligas
 */
@Component(
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	service = {SoyTemplateResourceLoader.class, TemplateResourceLoader.class}
)
public class SoyTemplateResourceLoader implements TemplateResourceLoader {

	@Override
	public void clearCache() {
		//TODO
	}

	@Override
	public void clearCache(String templateId) {
		//TODO
	}

	@Override
	public void destroy() {
		//TODO
	}

	@Override
	public String getName() {
		return _NAME;
	}

	@Override
	public TemplateResource getTemplateResource(String templateId) {
		String[] split = templateId.split(StringPool.DOUBLE_DASH);
		URL resource = null;

		if (split.length > 1) {
			long bundleId = Integer.parseInt(split[0]);
			String filePath = split[1];

			Bundle bundle = _context.getBundle(bundleId);
			resource = bundle.getResource(filePath);
		}

		if (resource == null) {
			_log.error("Unable to load resource " + templateId);
			return null;
		}

		return new URLTemplateResource(templateId, resource);
	}

	@Override
	public boolean hasTemplateResource(String templateId) {
		TemplateResource templateResource = getTemplateResource(templateId);
		return templateResource != null;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
	}

	@Reference(unbind = "-")
	protected void setMultiVMPool(MultiVMPool multiVMPool) {
		//TODO
	}

	@Reference(unbind = "-")
	protected void setSingleVMPool(SingleVMPool singleVMPool) {
		//TODO
	}

	private static final String _NAME = TemplateConstants.LANG_TYPE_SOY;

	private static final Log _log = LogFactoryUtil.getLog(
		SoyTemplateResourceLoader.class);

	private BundleContext _context;

}