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
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoader;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Miroslav Ligas
 */
public abstract class BaseMultiTemplateManager implements TemplateManager {


	@Override
	public void addContextObjects(
		Map<String, Object> contextObjects,
		Map<String, Object> newContextObjects) {

		for (String variableName : newContextObjects.keySet()) {
			if (contextObjects.containsKey(variableName)) {
				continue;
			}

			Object object = newContextObjects.get(variableName);

			if (object instanceof Class) {
				addStaticClassSupport(
					contextObjects, variableName, (Class<?>)object);
			}
			else {
				contextObjects.put(variableName, object);
			}
		}
	}

	@Override
	public void addStaticClassSupport(
		Map<String, Object> contextObjects, String variableName,
		Class<?> variableClass) {
	}

	@Override
	public void addTaglibApplication(
		Map<String, Object> contextObjects, String applicationName,
		ServletContext servletContext) {
	}

	@Override
	public void addTaglibFactory(
		Map<String, Object> contextObjects, String taglibLiferayHash,
		ServletContext servletContext) {
	}

	@Override
	public void addTaglibRequest(
		Map<String, Object> contextObjects, String applicationName,
		HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void addTaglibTheme(
		Map<String, Object> contextObjects, String themeName,
		HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public String[] getRestrictedVariables() {
		return new String[0];
	}

	@NotPrivileged
	@Override
	public Template getTemplate(
		TemplateResource templateResource, boolean restricted) {

		return getTemplates(Collections.singletonList(templateResource),
			null, restricted);
	}

	@NotPrivileged
	@Override
	public Template getTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted) {

		return getTemplates(Collections.singletonList(templateResource),
			errorTemplateResource, restricted);
	}


		@NotPrivileged
	@Override
	public Template getTemplates(
		List<TemplateResource> templateResources, boolean restricted) {

		return getTemplates(templateResources, null, restricted);
	}

	@NotPrivileged
	@Override
	public Template getTemplates(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted) {

		TemplateControlContext templateControlContext =
			templateContextHelper.getTemplateControlContext();

		AccessControlContext accessControlContext =
			templateControlContext.getAccessControlContext();

		ClassLoader classLoader = templateControlContext.getClassLoader();

		if (accessControlContext == null) {
			Map<String, Object> helperUtilities =
				templateContextHelper.getHelperUtilities(
					classLoader, restricted);

			return doGetTemplate(
				templateResources, errorTemplateResource, restricted,
				helperUtilities, false);
		}

		Map<String, Object> helperUtilities = AccessController.doPrivileged(
			new DoGetHelperUtilitiesPrivilegedAction(
				templateContextHelper, classLoader, restricted),
			accessControlContext);

		Template template = AccessController.doPrivileged(
			new DoGetTemplatePrivilegedAction(
				templateResources, errorTemplateResource, restricted,
				helperUtilities));

		return new PrivilegedTemplateWrapper(accessControlContext, template);
	}

	public void setTemplateContextHelper(
		TemplateContextHelper templateContextHelper) {

		this.templateContextHelper = templateContextHelper;
	}

	public void setTemplateResourceLoader(
		TemplateResourceLoader templateResourceLoader) {

		this.templateResourceLoader = templateResourceLoader;
	}

	protected abstract Template doGetTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted,
		Map<String, Object> helperUtilities, boolean privileged);

	protected TemplateContextHelper templateContextHelper;
	protected TemplateResourceLoader templateResourceLoader;

	private class DoGetHelperUtilitiesPrivilegedAction
		implements PrivilegedAction<Map<String, Object>> {

		public DoGetHelperUtilitiesPrivilegedAction(
			TemplateContextHelper templateContextHelper,
			ClassLoader classLoader, boolean restricted) {

			_templateContextHelper = templateContextHelper;
			_classLoader = classLoader;
			_restricted = restricted;
		}

		@Override
		public Map<String, Object> run() {
			return _templateContextHelper.getHelperUtilities(
				_classLoader, _restricted);
		}

		private final ClassLoader _classLoader;
		private boolean _restricted;
		private final TemplateContextHelper _templateContextHelper;

	}

	private class DoGetTemplatePrivilegedAction
		implements PrivilegedAction<Template> {

		public DoGetTemplatePrivilegedAction(
			List<TemplateResource> templateResources,
			TemplateResource errorTemplateResource, boolean restricted,
			Map<String, Object> helperUtilities) {

			_templateResources = templateResources;
			_errorTemplateResource = errorTemplateResource;
			_restricted = restricted;
			_helperUtilities = helperUtilities;
		}

		@Override
		public Template run() {
			return doGetTemplate(
				_templateResources, _errorTemplateResource, _restricted,
				_helperUtilities, true);
		}

		private final TemplateResource _errorTemplateResource;
		private final Map<String, Object> _helperUtilities;
		private boolean _restricted;
		private List<TemplateResource> _templateResources;

	}

}
