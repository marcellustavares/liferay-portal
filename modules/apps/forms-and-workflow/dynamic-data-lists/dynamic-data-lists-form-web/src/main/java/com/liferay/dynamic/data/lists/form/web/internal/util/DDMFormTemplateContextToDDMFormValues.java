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
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, service = DDMFormTemplateContextToDDMFormValues.class
)
public class DDMFormTemplateContextToDDMFormValues {

	public DDMFormValues deserialize(
			DDMForm ddmForm, String serializedDDMFormTemplateContext)
		throws PortalException {

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		Locale siteDefaultLocale = LocaleThreadLocal.getSiteDefaultLocale();

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		ddmFormValues.addAvailableLocale(siteDefaultLocale);
		ddmFormValues.setDefaultLocale(siteDefaultLocale);

		JSONObject jsonObject = jsonFactory.createJSONObject(
			serializedDDMFormTemplateContext);

		DDMFormTemplateContextVisitor ddmFormTemplateContextVisitor =
			new DDMFormTemplateContextVisitor(jsonObject.getJSONArray("pages"));

		ddmFormTemplateContextVisitor.onVisitField(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject fieldJSONObject) {
					DDMFormField ddmFormField = ddmFormFieldsMap.get(
						fieldJSONObject.getString("fieldName"));

					String type = ddmFormField.getType();

					DDMFormFieldValue ddmFormFieldValue =
						new DDMFormFieldValue();

					ddmFormFieldValue.setName(ddmFormField.getName());

					if (ddmFormField.isLocalizable()) {
						Value value = deserializeLocalizedValue(
							fieldJSONObject.getJSONObject("value"));

						ddmFormFieldValue.setValue(value);
					}
					else if (Objects.equals(type, "checkbox")) {
						ddmFormFieldValue.setValue(
							new UnlocalizedValue(
								String.valueOf(
									fieldJSONObject.getBoolean("value"))));
					}
					else {
						ddmFormFieldValue.setValue(
							new UnlocalizedValue(
								fieldJSONObject.getString("value")));
					}

					ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
				}

				protected Value deserializeLocalizedValue(
					JSONObject jsonObject) {

					Value value = new LocalizedValue(siteDefaultLocale);

					Iterator<String> itr = jsonObject.keys();

					while (itr.hasNext()) {
						String languageId = itr.next();

						value.addString(
							LocaleUtil.fromLanguageId(languageId),
							jsonObject.getString(languageId));
					}

					return value;
				}

			});

		ddmFormTemplateContextVisitor.visit();

		return ddmFormValues;
	}

	@Reference
	protected JSONFactory jsonFactory;

}