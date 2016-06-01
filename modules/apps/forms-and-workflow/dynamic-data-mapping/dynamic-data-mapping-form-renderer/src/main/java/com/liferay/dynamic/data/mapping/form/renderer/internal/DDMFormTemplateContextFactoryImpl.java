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
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
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
		DDMForm ddmForm, DDMFormLayout ddmFormLayout,
		DDMFormRenderingContext ddmFormRenderingContext) {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		String containerId = ddmFormRenderingContext.getContainerId();

		if (Validator.isNull(containerId)) {
			containerId = StringUtil.randomId();
		}

		jsonObject.put("containerId", containerId);

		String evaluatorURL = ddmFormRenderingContext.getEvaluatorURL();

		if (Validator.isNotNull(evaluatorURL)) {
			jsonObject.put("evaluatorURL", evaluatorURL);
		}

		JSONArray pages = getPages(
			ddmForm, ddmFormLayout, ddmFormRenderingContext);

		jsonObject.put("pages", pages);
		jsonObject.put(
			"portletNamespace", ddmFormRenderingContext.getPortletNamespace());
		jsonObject.put("readOnly", ddmFormRenderingContext.isReadOnly());

//		JSONArray readOnlyFieldsJSONArray = getReadOnlyFieldsJSONArray(ddmForm);
//
//		jsonObject.put("readOnlyFields", readOnlyFieldsJSONArray);

//		DDMFormValues ddmFormValues =
//			ddmFormRenderingContext.getDDMFormValues();
//
//		if (ddmFormValues != null) {
//			removeStaleDDMFormFieldValues(
//				ddmForm.getDDMFormFieldsMap(true),
//				ddmFormValues.getDDMFormFieldValues());
//		}

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

	@Override
	public JSONObject create(
		DDMForm ddmForm, DDMFormRenderingContext ddmFormRenderingContext) {

		return create(
			ddmForm, _ddm.getDefaultDDMFormLayout(ddmForm),
			ddmFormRenderingContext);
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
				getDDMFormFieldsTemplateContextMap(
					ddmForm, ddmFormRenderingContext);

			DDMFormPagesTemplateContextHelper ddmFormPagesTemplateContextHelper =
				new DDMFormPagesTemplateContextHelper(
					ddmForm, ddmFormLayout, templateContextDDMFormFieldsMap,
					ddmFormRenderingContext.isShowRequiredFieldsWarning(),
					ddmFormRenderingContext.getLocale(), _jsonFactory);

			return ddmFormPagesTemplateContextHelper.getPagesTemplateContext();
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

	protected void removeStaleDDMFormFieldValues(
			Map<String, DDMFormField> ddmFormFieldsMap,
			List<DDMFormFieldValue> ddmFormFieldValues) {

			Iterator<DDMFormFieldValue> iterator =
				ddmFormFieldValues.iterator();

			while (iterator.hasNext()) {
				DDMFormFieldValue ddmFormFieldValue = iterator.next();

				if (!ddmFormFieldsMap.containsKey(
						ddmFormFieldValue.getName())) {

					iterator.remove();
				}

				removeStaleDDMFormFieldValues(
					ddmFormFieldsMap,
					ddmFormFieldValue.getNestedDDMFormFieldValues());
			}
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

	protected Map<String, JSONArray> getDDMFormFieldsTemplateContextMap(
			DDMForm ddmForm, DDMFormRenderingContext ddmFormRenderingContext)
		throws DDMFormRenderingException {

		DDMFormFieldsTemplateContextHelper ddmFormFieldsTemplateContextHelper =
			new DDMFormFieldsTemplateContextHelper(
				ddmForm, ddmFormRenderingContext);

		ddmFormFieldsTemplateContextHelper.setDDMFormFieldTypeServicesTracker(
			_ddmFormFieldTypeServicesTracker);
		ddmFormFieldsTemplateContextHelper.setDDMFormEvaluator(
			_ddmFormEvaluator);
		ddmFormFieldsTemplateContextHelper.setJSONFactory(_jsonFactory);

		return ddmFormFieldsTemplateContextHelper.
			getDDMFormFieldsTemplateContextMap();
	}

	protected String getTemplateNamespace(DDMFormLayout ddmFormLayout) {
		String paginationMode = ddmFormLayout.getPaginationMode();

		if (Objects.equals(paginationMode, DDMFormLayout.SETTINGS_MODE)) {
			return "ddm.settings_form";
		}
		else if (Objects.equals(paginationMode, DDMFormLayout.SINGLE_PAGE_MODE)) {
			return "ddm.simple_form";
		}
		else if (Objects.equals(paginationMode, DDMFormLayout.TABBED_MODE)) {
			return "ddm.tabbed_form";
		}

		return "ddm.paginated_form";
	}

	@Reference
	private DDM _ddm;

	@Reference
	private DDMFormEvaluator _ddmFormEvaluator;

	@Reference(
		target = "(osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.form.evaluator.internal.servlet.DDMFormEvaluatorServlet)"
	)
	private Servlet _ddmFormEvaluatorServlet;

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private JSONFactory _jsonFactory;

}