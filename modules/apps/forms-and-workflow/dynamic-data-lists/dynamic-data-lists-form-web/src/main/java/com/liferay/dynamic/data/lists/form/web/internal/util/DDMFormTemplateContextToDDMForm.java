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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.dynamic.data.lists.form.web.internal.converter.DDLFormRuleDeserializer;
import com.liferay.dynamic.data.lists.form.web.internal.converter.DDLFormRuleToDDMFormRuleConverter;
import com.liferay.dynamic.data.lists.form.web.internal.converter.model.DDLFormRule;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.dynamic.data.mapping.model.DDMFormSuccessPageSettings;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = DDMFormTemplateContextToDDMForm.class)
public class DDMFormTemplateContextToDDMForm {

	  @Reference
	    protected DDLFormRuleDeserializer ddlFormRuleDeserializer;
	    @Reference
	    protected DDLFormRuleToDDMFormRuleConverter
	        ddlFormRulesToDDMFormRulesConverter;

	protected List<DDMFormRule> getDDMFormRules(String rules)
        throws PortalException {

		if (Validator.isNull(rules) || Objects.equals("[]", rules)) {
            return Collections.emptyList();
        }

		List<DDLFormRule> ddlFormRules = ddlFormRuleDeserializer.deserialize(
            rules);
        return ddlFormRulesToDDMFormRulesConverter.convert(ddlFormRules);
    }


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


        JSONArray rulesJSONArray = jsonObject.getJSONArray("rules");
        ddmForm.setDDMFormRules(getDDMFormRules(rulesJSONArray.toString()));

//		JSONObject successPageJSONObject = jsonObject.getJSONObject("successPage");


//		DDMFormSuccessPageSettings ddmFormSuccessPageSettings = new DDMFormSuccessPageSettings();
//
//		ddmFormSuccessPageSettings.setBody(successPageJSONObject.getString("body"));
//		ddmFormSuccessPageSettings.setTitle(title);
//		ddmFormSuccessPageSettings.setEnabled(enabled);
//

		DDMFormTemplateJSONContextVisitor ddmFormTemplateContextVisitor =
			new DDMFormTemplateJSONContextVisitor(jsonObject.getJSONArray("pages"));

		ddmFormTemplateContextVisitor.onVisitField(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject fieldJSONObject) {
					JSONObject jsonObject = fieldJSONObject.getJSONObject(
						"settingsContext");

					DDMFormTemplateJSONContextVisitor
						settingsTemplateContextVisitor =
							new DDMFormTemplateJSONContextVisitor(
								jsonObject.getJSONArray("pages"));

					DDMFormField ddmFormField = new DDMFormField();

					settingsTemplateContextVisitor.onVisitField(
						new Consumer<JSONObject>() {

							@Override
							public void accept(JSONObject fieldJSONObject) {
								try {
									boolean localizable =
										fieldJSONObject.getBoolean(
											"localizable");

									String valueProperty = "value";

									if (localizable || fieldJSONObject.getString("type").equals("options")) {
										valueProperty = "localizedValue";
									}

									Object deserializedDDMFormFieldProperty =
										deserializeDDMFormFieldProperty(
											fieldJSONObject.getString(
												valueProperty),
											localizable,
											fieldJSONObject.getString(
												"dataType"),
											fieldJSONObject.getString("type"));

									ddmFormField.setProperty(
										fieldJSONObject.getString("fieldName"),
										deserializedDDMFormFieldProperty);
								}
								catch (PortalException pe) {
									pe.printStackTrace();
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

								JSONObject jsonObject =
									jsonFactory.createJSONObject(
										serializedDDMFormFieldProperty);

								return getDDMFormFieldOptions(jsonObject);
							}

							protected Object deserializeDDMFormFieldProperty(
									String serializedDDMFormFieldProperty,
									boolean localizable, String dataType,
									String type)
								throws PortalException {

								if (Objects.equals(
										dataType, "ddm-options")) {

									return deserializeDDMFormFieldOptions(
										serializedDDMFormFieldProperty);
								}
								else if (Objects.equals(dataType, "boolean")) {
									return Boolean.valueOf(
										serializedDDMFormFieldProperty);
								}
								else if (Objects.equals(type, "validation")) {
									return deserializeDDMFormFieldValidation(
										serializedDDMFormFieldProperty);
								}
								else if (localizable) {
									return deserializeLocalizedValue(
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
								getDDMFormFieldOptions(JSONObject jsonObject) {

								DDMFormFieldOptions ddmFormFieldOptions =
									new DDMFormFieldOptions();

								ddmFormFieldOptions.setDefaultLocale(
									defaultLocale);

								Iterator<String> itr = jsonObject.keys();

								while (itr.hasNext()) {
									String key = itr.next();

									JSONArray jsonArray =
										jsonObject.getJSONArray(key);

									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject2 =
											jsonArray.getJSONObject(i);

										ddmFormFieldOptions.addOptionLabel(
												jsonObject2.getString("value"),
												LocaleUtil.fromLanguageId(key),
												jsonObject2.getString("label"));
									}
								}

								return ddmFormFieldOptions;
							}

						});

					settingsTemplateContextVisitor.visit();

					ddmFormField.setIndexType("keyword");

					ddmForm.addDDMFormField(ddmFormField);
				}

			});

		ddmFormTemplateContextVisitor.visit();

		return ddmForm;
	}

	@Reference
	protected JSONFactory jsonFactory;

}