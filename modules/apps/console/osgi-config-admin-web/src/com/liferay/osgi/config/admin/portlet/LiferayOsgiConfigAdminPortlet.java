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

package com.liferay.osgi.config.admin.portlet;

import com.liferay.osgi.config.admin.portlet.internal.freemarker.OsgiFreeMarkerPortlet;
import com.liferay.osgi.config.admin.util.MetaTypeInfoUtil;
import com.liferay.osgi.config.admin.util.ObjectClassDefinitonsIterator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormFieldRenderingContext;
import com.liferay.portlet.dynamicdatamapping.render.DDMFormRendererUtil;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.portlet.Portlet;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.MetaTypeService;

/**
 * @author Kamesh Sampath
 */
@Component(
				immediate = true,
				property = {
					"com.liferay.portlet.control-panel-entry-category=configuration",
					"com.liferay.portlet.control-panel-entry-weight=11",
					"com.liferay.portlet.display-category=category.hidden",
					"com.liferay.portlet.instanceable=false",
					"javax.portlet.portlet-name=liferay_osgi_config_admin",
					"javax.portlet.init-param.template-path=/",
					"javax.portlet.init-param.view-template=/view.ftl",
					"javax.portlet.resource-bundle=content.Language",
					"javax.portlet.security-role-ref=power-user,user"
				},
				service = Portlet.class)
public class LiferayOsgiConfigAdminPortlet
	extends OsgiFreeMarkerPortlet {

	@Activate
	public void activate(BundleContext context) {
		_context = context;
	}

	@Override
	protected void populateContext(
		String path, PortletRequest portletRequest,
		PortletResponse portletResponse, Template template)
		throws Exception {
		super.populateContext(path, portletRequest, portletResponse, template);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)portletRequest.getAttribute(WebKeys.THEME_DISPLAY);

		if (_log.isDebugEnabled()) {
			_log.debug("Path:" + path);
		}

		if ("/edit_attributes.ftl".equals(path)) {
			String servicePID =
				ParamUtil.getString(portletRequest, "servicePID");
			template.put("servicePID", servicePID);

			if (_log.isDebugEnabled()) {
				_log.debug("Editing Service:" + servicePID);
			}

			Set<Locale> availableLocales = new HashSet<>();
			availableLocales.add(Locale.US);
			availableLocales.add(Locale.UK);
			availableLocales.add(Locale.FRENCH);

			DDMForm ddmForm = MetaTypeInfoUtil.attributeForm(servicePID);

			ddmForm.setAvailableLocales(availableLocales);

			ddmForm.setDefaultLocale(LocaleUtil.getDefault());

			DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
				new DDMFormFieldRenderingContext();

			ddmFormFieldRenderingContext.setHttpServletRequest(
				PortalUtil.getHttpServletRequest(portletRequest));

			ddmFormFieldRenderingContext.setHttpServletResponse(
				PortalUtil.getHttpServletResponse(portletResponse));

			ddmFormFieldRenderingContext.setReadOnly(false);

			ddmFormFieldRenderingContext.setShowEmptyFieldLabel(true);

			ddmFormFieldRenderingContext.setPortletNamespace(
				portletResponse.getNamespace());

			ddmFormFieldRenderingContext.setNamespace(
				"com.liferay.osgi.config.admin.portlet.LiferayOsgiConfigAdmin");

			// ??ddmFormFieldRenderingContext.setMode(null);

			ddmFormFieldRenderingContext.setLocale(themeDisplay.getLocale());

			String editAttributeFormContent =
				DDMFormRendererUtil.render(
					ddmForm, ddmFormFieldRenderingContext);

			if (_log.isDebugEnabled()) {
				_log.debug(editAttributeFormContent);
			}

			template.put("editAttributeForm", ddmForm);
			template.put("editAttributeFormContent", editAttributeFormContent);
		}
		else {
			template.put(
				"ocdIterator", new ObjectClassDefinitonsIterator(
					_context, _metaTypeService));
		}
	}

	@Reference
	protected void setConfigAdminService(
		ConfigurationAdmin configurationAdmin) {
		_configurationAdmin = configurationAdmin;
	}

	@Reference
	protected void setMetaTypeService(MetaTypeService metaTypeService) {
		_metaTypeService = metaTypeService;
	}

	private static Log _log = LogFactoryUtil.getLog(
		LiferayOsgiConfigAdminPortlet.class);

	private ConfigurationAdmin _configurationAdmin;
	private BundleContext _context;
	private MetaTypeService _metaTypeService;

}