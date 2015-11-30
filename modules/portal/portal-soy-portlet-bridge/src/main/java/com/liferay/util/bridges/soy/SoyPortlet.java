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

package com.liferay.util.bridges.soy;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.Writer;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

/**
 * @author Miroslav Ligas
 */
public class SoyPortlet extends MVCPortlet {

	@Override
	public void init() throws PortletException {
		super.init();

		try {
			_template = getTemplate();
		}
		catch (TemplateException te) {

			// TODO

		}
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		renderRequest.setAttribute(WebKeys.TEMPLATE, _template);

		super.render(renderRequest, renderResponse);
	}

	protected void addLocalTemplateResources(
		Bundle bundle, List<TemplateResource> templateResources) {

		for (URL url : getSoyResourceURLs(bundle)) {
			templateResources.add(new URLTemplateResource(url.getPath(), url));
		}
	}

	protected void addRequiredTemplateResources(
			Bundle bundle, List<TemplateResource> templateResources)
		throws TemplateException {

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		for (BundleWire bundleWire : bundleWiring.getRequiredWires("soy")) {
			BundleRevision providerBundleRevision = bundleWire.getProvider();

			List<String> templateIds = getProviderTemplateIds(
				providerBundleRevision.getBundle());

			templateResources.addAll(
				TemplateResourceLoaderUtil.getTemplateResources(
					TemplateConstants.LANG_TYPE_SOY, templateIds));
		}
	}

	protected void doInclude(
		PortletRequest portletRequest, PortletResponse portletResponse,
		Template template) {
	}

	protected List<String> getProviderTemplateIds(Bundle providerBundle) {
		BundleWire bundleWire = providerBundle.adapt(BundleWire.class);

		BundleCapability bundleCapability = bundleWire.getCapability();

		List<String> templateIds = new ArrayList<>();

		for (URL url : getSoyResourceURLs(providerBundle)) {
			String templateId = getTemplateId(bundleCapability, url);

			templateIds.add(templateId);
		}

		return templateIds;
	}

	protected List<URL> getSoyResourceURLs(Bundle bundle) {
		Enumeration<URL> urls = bundle.findEntries("META-INF/", "*.soy", true);

		if (urls == null) {
			return Collections.emptyList();
		}

		return Collections.list(urls);
	}

	protected Template getTemplate() throws TemplateException {
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());

		List<TemplateResource> templateResources = new ArrayList<>();

		addLocalTemplateResources(bundle, templateResources);
		addRequiredTemplateResources(bundle, templateResources);

		return TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_SOY, templateResources, false);
	}

	protected String getTemplateId(BundleCapability bundleCapability, URL url) {
		Map<String, Object> attributes = bundleCapability.getAttributes();

		StringBundler sb = new StringBundler(5);

		sb.append(attributes.get("type"));
		sb.append(StringPool.DOUBLE_UNDERLINE);
		sb.append(attributes.get("version"));
		sb.append(StringPool.DOUBLE_UNDERLINE);
		sb.append(url.getPath());

		return sb.toString();
	}

	@Override
	protected void include(
			String namespace, PortletRequest portletRequest,
			PortletResponse portletResponse, String lifecycle)
		throws IOException, PortletException {

		try {
			_template.put(TemplateConstants.NAMESPACE, namespace);

			doInclude(portletRequest, portletResponse, _template);

			Writer writer = null;

			if (portletResponse instanceof MimeResponse) {
				MimeResponse mimeResponse = (MimeResponse)portletResponse;

				writer = UnsyncPrintWriterPool.borrow(mimeResponse.getWriter());
			}
			else {
				writer = new UnsyncStringWriter();
			}

			_template.processTemplate(writer);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}

		if (clearRequestParameters) {
			if (lifecycle.equals(PortletRequest.RENDER_PHASE)) {
				portletResponse.setProperty("clear-request-parameters", "true");
			}
		}
	}

	private Template _template;

}