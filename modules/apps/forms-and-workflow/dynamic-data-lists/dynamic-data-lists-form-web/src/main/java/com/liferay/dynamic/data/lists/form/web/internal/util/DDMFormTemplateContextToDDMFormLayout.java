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

import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, service = DDMFormTemplateContextToDDMFormLayout.class
)
public class DDMFormTemplateContextToDDMFormLayout {

	public DDMFormLayout deserialize(String serializedDDMFormTemplateContext)
		throws PortalException {

		DDMFormLayout ddmFormLayout = new DDMFormLayout();

		ddmFormLayout.setDefaultLocale(LocaleThreadLocal.getSiteDefaultLocale());

		JSONObject jsonObject = jsonFactory.createJSONObject(
			serializedDDMFormTemplateContext);

		String paginationMode = jsonObject.getString(
			"paginationMode", DDMFormLayout.WIZARD_MODE);

		ddmFormLayout.setPaginationMode(paginationMode);

		DDMFormTemplateJSONContextVisitor ddmFormTemplateContextVisitor =
			new DDMFormTemplateJSONContextVisitor(jsonObject.getJSONArray("pages"));

		List<DDMFormLayoutRow> ddmFormLayoutRows = new ArrayList<>();
		List<DDMFormLayoutColumn> ddmFormLayoutColumns = new ArrayList<>();
		List<String> fieldNames = new ArrayList<>();

		ddmFormTemplateContextVisitor.onVisitPage(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject pageJSONObject) {
					DDMFormLayoutPage ddmFormLayoutPage =
						new DDMFormLayoutPage();

					ddmFormLayoutPage.setDescription(
						deserializeLocalizedValue(
							pageJSONObject.getJSONObject("description")));

					ddmFormLayoutPage.setTitle(
						deserializeLocalizedValue(
							pageJSONObject.getJSONObject("title")));

					ddmFormLayoutPage.setDDMFormLayoutRows(new ArrayList<DDMFormLayoutRow>(ddmFormLayoutRows));

					ddmFormLayout.addDDMFormLayoutPage(ddmFormLayoutPage);

					ddmFormLayoutRows.clear();
				}

			});

		ddmFormTemplateContextVisitor.onVisitRow(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject rowJSONObject) {
					DDMFormLayoutRow ddmFormLayoutRow = new DDMFormLayoutRow();

					ddmFormLayoutRow.setDDMFormLayoutColumns(
						new ArrayList<DDMFormLayoutColumn>(ddmFormLayoutColumns));

					ddmFormLayoutRows.add(ddmFormLayoutRow);

					ddmFormLayoutColumns.clear();
				}

			});

		ddmFormTemplateContextVisitor.onVisitColumn(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject columnJSONObject) {
					DDMFormLayoutColumn ddmFormLayoutColumn =
						new DDMFormLayoutColumn();

					ddmFormLayoutColumn.setSize(
						columnJSONObject.getInt("size"));
					ddmFormLayoutColumn.setDDMFormFieldNames(new ArrayList<String>(fieldNames));

					ddmFormLayoutColumns.add(ddmFormLayoutColumn);

					fieldNames.clear();
				}

			});

		ddmFormTemplateContextVisitor.onVisitField(
			new Consumer<JSONObject>() {

				@Override
				public void accept(JSONObject fieldJSONObject) {
					fieldNames.add(fieldJSONObject.getString("fieldName"));
				}

			});

		ddmFormTemplateContextVisitor.visit();

		return ddmFormLayout;
	}

	protected LocalizedValue deserializeLocalizedValue(JSONObject jsonObject) {
		LocalizedValue value = new LocalizedValue(
			LocaleThreadLocal.getSiteDefaultLocale());

		Iterator<String> itr = jsonObject.keys();

		while (itr.hasNext()) {
			String languageId = itr.next();

			value.addString(
				LocaleUtil.fromLanguageId(languageId),
				jsonObject.getString(languageId));
		}

		return value;
	}

	@Reference
	protected JSONFactory jsonFactory;

}