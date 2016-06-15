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

package com.liferay.configuration.admin.web.util;

import com.liferay.configuration.admin.web.model.ConfigurationModel;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

/**
 * @author Marcellus Tavares
 */
public class DDMFormRendererHelper {

	public DDMFormRendererHelper(
		RenderRequest renderRequest, RenderResponse renderResponse,
		ConfigurationModel configurationModel, DDMFormRenderer ddmFormRenderer,
		ResourceBundleLoaderProvider resourceBundleLoaderProvider) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_configurationModel = configurationModel;
		_ddmFormRenderer = ddmFormRenderer;
		_resourceBundleLoaderProvider = resourceBundleLoaderProvider;
	}

	public String getDDMFormHTML() throws PortletException {
		try {
			DDMForm ddmForm = getDDMForm();

			return _ddmFormRenderer.render(
				ddmForm, createDDMFormRenderingContext(ddmForm));
		}
		catch (DDMFormRenderingException ddmfre) {
			_log.error("Unable to render DDM Form ", ddmfre);

			throw new PortletException(ddmfre);
		}
	}

	protected DDMFormRenderingContext createDDMFormRenderingContext(
		DDMForm ddmForm) {

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();
		
		ResourceURL resourceURL = _renderResponse.createResourceURL();

		resourceURL.setParameter("pid", _configurationModel.getID());
		resourceURL.setResourceID("evaluateConfigurationModelDDMForm");

		ddmFormRenderingContext.setEvaluatorURL(resourceURL.toString());

		ddmFormRenderingContext.setDDMFormValues(getDDMFormValues(ddmForm));
		ddmFormRenderingContext.setHttpServletRequest(
			PortalUtil.getHttpServletRequest(_renderRequest));
		ddmFormRenderingContext.setHttpServletResponse(
			PortalUtil.getHttpServletResponse(_renderResponse));
		ddmFormRenderingContext.setLocale(getLocale());
		ddmFormRenderingContext.setPortletNamespace(
			_renderResponse.getNamespace());

		return ddmFormRenderingContext;
	}

	protected DDMForm getDDMForm() {
		String bundleSymbolicName = _configurationModel.getBundleSymbolicName();

		ResourceBundleLoader resourceBundleLoader =
			_resourceBundleLoaderProvider.getResourceBundleLoader(
				bundleSymbolicName);

		ResourceBundle resourceBundle = resourceBundleLoader.loadResourceBundle(
			LocaleUtil.toLanguageId(getLocale()));

		ConfigurationModelToDDMFormConverter
			configurationModelToDDMFormConverter =
				new ConfigurationModelToDDMFormConverter(
					_configurationModel, getLocale(), resourceBundle);

		return configurationModelToDDMFormConverter.getDDMForm();
	}

	protected DDMFormValues getDDMFormValues(DDMForm ddmForm) {
		String bundleSymbolicName = _configurationModel.getBundleSymbolicName();

		ResourceBundleLoader resourceBundleLoader =
			_resourceBundleLoaderProvider.getResourceBundleLoader(
				bundleSymbolicName);

		ResourceBundle resourceBundle = resourceBundleLoader.loadResourceBundle(
			LocaleUtil.toLanguageId(getLocale()));

		ConfigurationModelToDDMFormValuesConverter
			configurationModelToDDMFormValuesConverter =
				new ConfigurationModelToDDMFormValuesConverter(
					_configurationModel, ddmForm, getLocale(), resourceBundle);

		return configurationModelToDDMFormValuesConverter.getDDMFormValues();
	}

	protected Locale getLocale() {
		ThemeDisplay themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay.getLocale();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormRendererHelper.class);

	private final ConfigurationModel _configurationModel;
	private final DDMFormRenderer _ddmFormRenderer;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final ResourceBundleLoaderProvider _resourceBundleLoaderProvider;

}