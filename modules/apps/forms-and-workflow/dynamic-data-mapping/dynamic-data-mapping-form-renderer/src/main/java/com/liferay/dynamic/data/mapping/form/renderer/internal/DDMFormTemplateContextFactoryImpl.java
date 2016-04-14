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

package com.liferay.dynamic.data.mapping.form.renderer.internal;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluator;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingException;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.AggregateResourceBundle;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true)
public class DDMFormTemplateContextFactoryImpl
	implements DDMFormTemplateContextFactory {

	@Override
	public JSONObject create(
		DDMForm ddmForm, DDMFormRenderingContext ddmFormRenderingContext) {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		jsonObject.put("containerId", ddmFormRenderingContext.getContainerId());
		//jsonObject.put("dataProviderURL", getDDMDataProviderServletURL());
		jsonObject.put("evaluatorURL", getDDMFormEvaluatorServletURL());

		DDMFormLayout ddmFormLayout = _ddm.getDefaultDDMFormLayout(ddmForm);

		JSONArray pages = getPages(
			ddmForm, ddmFormLayout, ddmFormRenderingContext);

		jsonObject.put("pages", pages);
		jsonObject.put(
			"portletNamespace", ddmFormRenderingContext.getPortletNamespace());
		jsonObject.put("readOnly", ddmFormRenderingContext.isReadOnly());

//		JSONArray readOnlyFieldsJSONArray = getReadOnlyFieldsJSONArray(ddmForm);
//
//		jsonObject.put("readOnlyFields", readOnlyFieldsJSONArray);

		Locale locale = ddmFormRenderingContext.getLocale();

		ResourceBundle resourceBundle = getResourceBundle(locale);

		jsonObject.put(
			"requiredFieldsWarningMessageHTML",
			getRequiredFieldsWarningMessageHTML(resourceBundle));
		jsonObject.put(
			"showRequiredFieldsWarning",
			ddmFormRenderingContext.isShowRequiredFieldsWarning());

		boolean showSubmitButton = ddmFormRenderingContext.isShowSubmitButton();

		if (ddmFormRenderingContext.isReadOnly()) {
			showSubmitButton = false;
		}

		jsonObject.put("showSubmitButton", showSubmitButton);
		jsonObject.put("strings", getLanguageStringsMap(resourceBundle));

		String submitLabel = GetterUtil.getString(
			ddmFormRenderingContext.getSubmitLabel(),
			LanguageUtil.get(locale, "submit"));

		jsonObject.put("submitLabel", submitLabel);
		jsonObject.put(
			"templateNamespace", getTemplateNamespace(ddmFormLayout));

		return jsonObject;
	}

	protected void collectResourceBundles(
		Class<?> clazz, List<ResourceBundle> resourceBundles, Locale locale) {

		for (Class<?> interfaceClass : clazz.getInterfaces()) {
			collectResourceBundles(interfaceClass, resourceBundles, locale);
		}

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, clazz.getClassLoader());

		if (resourceBundle != null) {
			resourceBundles.add(resourceBundle);
		}
	}

	protected String getDDMDataProviderServletURL() {
		String servletContextPath = getServletContextPath(
			_ddmDataProviderServlet);

		return servletContextPath.concat(
			"/dynamic-data-mapping-data-provider/");
	}

	protected String getDDMFormEvaluatorServletURL() {
		String servletContextPath = getServletContextPath(
			_ddmFormEvaluatorServlet);

		return servletContextPath.concat(
			"/dynamic-data-mapping-form-evaluator/");
	}

	protected Map<String, String> getLanguageStringsMap(
		ResourceBundle resourceBundle) {

		Map<String, String> stringsMap = new HashMap<>();

		stringsMap.put("next", LanguageUtil.get(resourceBundle, "next"));
		stringsMap.put(
			"previous", LanguageUtil.get(resourceBundle, "previous"));

		return stringsMap;
	}

	protected JSONArray getPages(
		DDMForm ddmForm, DDMFormLayout ddmFormLayout,
		DDMFormRenderingContext ddmFormRenderingContext) {

		try {
			Map<String, JSONArray> templateContextDDMFormFieldsMap =
				getTemplateContextDDMFormFieldsMap(
					ddmForm, ddmFormRenderingContext);

			DDMFormLayoutTransformer ddmFormLayoutTransformer =
				new DDMFormLayoutTransformer(
					ddmForm, ddmFormLayout, templateContextDDMFormFieldsMap,
					ddmFormRenderingContext.isShowRequiredFieldsWarning(),
					ddmFormRenderingContext.getLocale(), _jsonFactory);

			return ddmFormLayoutTransformer.getPages();
		}
		catch (DDMFormRenderingException ddmfre) {
			ddmfre.printStackTrace();
		}

		return _jsonFactory.createJSONArray();
	}

	protected JSONArray getReadOnlyFieldsJSONArray(DDMForm ddmForm) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		for (DDMFormField ddmFormField : ddmFormFields) {
			if (ddmFormField.isReadOnly()) {
				jsonArray.put(ddmFormField.getName());
			}
		}

		return jsonArray;
	}

	protected String getRequiredFieldsWarningMessageHTML(
		ResourceBundle resourceBundle) {

		StringBundler sb = new StringBundler(3);

		sb.append("<label class=\"required-warning\">");
		sb.append(
			LanguageUtil.format(
				resourceBundle, "all-fields-marked-with-x-are-required",
				"<i class=\"icon-asterisk text-warning\"></i>", false));
		sb.append("</label>");

		return sb.toString();
	}

	protected ResourceBundle getResourceBundle(Locale locale) {
		List<ResourceBundle> resourceBundles = new ArrayList<>();

		ResourceBundle portalResourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, PortalClassLoaderUtil.getClassLoader());

		resourceBundles.add(portalResourceBundle);

		collectResourceBundles(getClass(), resourceBundles, locale);

		ResourceBundle[] resourceBundlesArray = resourceBundles.toArray(
			new ResourceBundle[resourceBundles.size()]);

		return new AggregateResourceBundle(resourceBundlesArray);
	}

	protected String getServletContextPath(Servlet servlet) {
		ServletConfig servletConfig = servlet.getServletConfig();

		ServletContext servletContext = servletConfig.getServletContext();

		return servletContext.getContextPath();
	}

	protected Map<String, JSONArray> getTemplateContextDDMFormFieldsMap(
			DDMForm ddmForm, DDMFormRenderingContext ddmFormRenderingContext)
		throws DDMFormRenderingException {

		DDMFormRendererHelper ddmFormRendererHelper = new DDMFormRendererHelper(
			ddmForm, ddmFormRenderingContext);

		ddmFormRendererHelper.setDDMFormFieldTypeServicesTracker(
			_ddmFormFieldTypeServicesTracker);
		ddmFormRendererHelper.setDDMFormEvaluator(_ddmFormEvaluator);
		ddmFormRendererHelper.setJSONFactory(_jsonFactory);

		return ddmFormRendererHelper.getTemplateContextDDMFormFieldsMap();
	}

	protected String getTemplateNamespace(DDMFormLayout ddmFormLayout) {
		String paginationMode = ddmFormLayout.getPaginationMode();

		if (Validator.equals(paginationMode, DDMFormLayout.SINGLE_PAGE_MODE)) {
			return "ddm.simple_form";
		}
		else if (Validator.equals(paginationMode, DDMFormLayout.TABBED_MODE)) {
			return "ddm.tabbed_form";
		}

		return "ddm.paginated_form";
	}

	@Reference
	private DDM _ddm;

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=DDMDataProviderServlet)"
	)
	private Servlet _ddmDataProviderServlet;

	@Reference
	private DDMFormEvaluator _ddmFormEvaluator;

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=DDMFormEvaluatorServlet)"
	)
	private Servlet _ddmFormEvaluatorServlet;

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private JSONFactory _jsonFactory;

}