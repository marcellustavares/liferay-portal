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

package com.liferay.dynamic.data.lists.form.web.internal.util;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = DDMFormTemplateContextToDDMForm.class)
public class DDMFormTemplateContextToDDMForm {

	public DDMForm deserialize(String serializedDDMFormTemplateContext)
		throws PortalException {

		DDMForm ddmForm = new DDMForm();

		JSONObject jsonObject = jsonFactory.createJSONObject(
			serializedDDMFormTemplateContext);

		JSONArray jsonArray = jsonObject.getJSONArray("availableLanguageIds");

		for (int i = 0; i < jsonArray.length(); i++) {
			ddmForm.addAvailableLocale(
				LocaleUtil.fromLanguageId(jsonArray.getString(i)));
		}

		Locale defaultLocale = LocaleUtil.fromLanguageId(
			jsonObject.getString("defaultLanguageId"));

		ddmForm.setDefaultLocale(defaultLocale);

		DDMFormTemplateContextVisitor ddmFormTemplateContextVisitor =
			new DDMFormTemplateContextVisitor(jsonObject.getJSONArray("pages"));

		ddmFormTemplateContextVisitor.onVisitField(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject fieldJSONObject) {
					JSONObject jsonObject = fieldJSONObject.getJSONObject(
						"settingsContext");

					DDMFormTemplateContextVisitor
						settingsTemplateContextVisitor =
							new DDMFormTemplateContextVisitor(
								jsonObject.getJSONArray("pages"));

					DDMFormField ddmFormField = new DDMFormField();

					settingsTemplateContextVisitor.onVisitField(
						new Consumer<JSONObject>() {

							@Override
							public void accept(JSONObject fieldJSONObject) {
								try {
									Object deserializedDDMFormFieldProperty =
										deserializeDDMFormFieldProperty(
											jsonObject.getString("value"),
											jsonObject.getBoolean(
												"localizable"),
											jsonObject.getString("dataType"),
											jsonObject.getString("type"));

									ddmFormField.setProperty(
										jsonObject.getString("fieldName"),
										deserializedDDMFormFieldProperty);
								}
								catch (PortalException pe) {
									pe.printStackTrace();
								}
							}

							protected void addOptionValueLabels(
								JSONObject jsonObject,
								DDMFormFieldOptions ddmFormFieldOptions,
								String optionValue) {

								Iterator<String> itr = jsonObject.keys();

								while (itr.hasNext()) {
									String languageId = itr.next();

									ddmFormFieldOptions.addOptionLabel(
										optionValue,
										LocaleUtil.fromLanguageId(languageId),
										jsonObject.getString(languageId));
								}
							}

							protected DDMFormFieldOptions
									deserializeDDMFormFieldOptions(
										String serializedDDMFormFieldProperty)
								throws PortalException {

								if (Validator.isNull(
										serializedDDMFormFieldProperty)) {

									return new DDMFormFieldOptions();
								}

								JSONArray jsonArray =
									jsonFactory.createJSONArray(
										serializedDDMFormFieldProperty);

								return getDDMFormFieldOptions(jsonArray);
							}

							protected Object deserializeDDMFormFieldProperty(
									String serializedDDMFormFieldProperty,
									boolean localizable, String dataType,
									String type)
								throws PortalException {

								if (localizable) {
									return deserializeLocalizedValue(
										serializedDDMFormFieldProperty);
								}

								if (Objects.equals(dataType, "boolean")) {
									return Boolean.valueOf(
										serializedDDMFormFieldProperty);
								}
								else if (Objects.equals(
											dataType, "ddm-options")) {

									return deserializeDDMFormFieldOptions(
										serializedDDMFormFieldProperty);
								}
								else if (Objects.equals(type, "validation")) {
									return deserializeDDMFormFieldValidation(
										serializedDDMFormFieldProperty);
								}
								else {
									return serializedDDMFormFieldProperty;
								}
							}

							protected DDMFormFieldValidation
									deserializeDDMFormFieldValidation(
										String serializedDDMFormFieldProperty)
								throws PortalException {

								DDMFormFieldValidation ddmFormFieldValidation =
									new DDMFormFieldValidation();

								if (Validator.isNull(
										serializedDDMFormFieldProperty)) {

									return ddmFormFieldValidation;
								}

								JSONObject jsonObject =
									jsonFactory.createJSONObject(
										serializedDDMFormFieldProperty);

								ddmFormFieldValidation.setExpression(
									jsonObject.getString("expression"));
								ddmFormFieldValidation.setErrorMessage(
									jsonObject.getString("errorMessage"));

								return ddmFormFieldValidation;
							}

							protected LocalizedValue deserializeLocalizedValue(
									String serializedDDMFormFieldProperty)
								throws PortalException {

								LocalizedValue localizedValue =
									new LocalizedValue(defaultLocale);

								JSONObject jsonObject =
									jsonFactory.createJSONObject(
										serializedDDMFormFieldProperty);

								Iterator<String> itr = jsonObject.keys();

								while (itr.hasNext()) {
									String languageId = itr.next();

									localizedValue.addString(
										LocaleUtil.fromLanguageId(languageId),
										jsonObject.getString(languageId));
								}

								return localizedValue;
							}

							protected DDMFormFieldOptions
								getDDMFormFieldOptions(JSONArray jsonArray) {

								DDMFormFieldOptions ddmFormFieldOptions =
									new DDMFormFieldOptions();

								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject =
										jsonArray.getJSONObject(i);

									String value = jsonObject.getString(
										"value");

									ddmFormFieldOptions.addOption(value);

									addOptionValueLabels(
										jsonObject.getJSONObject("label"),
										ddmFormFieldOptions, value);
								}

								return ddmFormFieldOptions;
							}

						});

					settingsTemplateContextVisitor.visit();

					ddmForm.addDDMFormField(ddmFormField);
				}

			});

		ddmFormTemplateContextVisitor.visit();

		return ddmForm;
	}

	@Reference
	protected JSONFactory jsonFactory;

}