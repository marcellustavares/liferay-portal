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

package com.liferay.portlet.dynamicdatamapping.render;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormField;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormFieldOptions;
import com.liferay.portlet.dynamicdatamapping.model.LocalizedValue;
import com.liferay.portlet.dynamicdatamapping.model.Value;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

import java.util.Locale;

/**
 * @author Bruno Basto
 * @author Marcellus Tavares
 */
public abstract class BaseListDDMFormFieldValueRenderer
	extends BaseDDMFormFieldValueRenderer {

	@Override
	protected ValueAccessor getValueAcessor(Locale locale) {
		return new ValueAccessor(locale) {

			@Override
			public String get(DDMFormFieldValue ddmFormFieldValue) {
				Value value = ddmFormFieldValue.getValue();

				String[] valueArray = StringUtil.split(value.getString(locale));

				StringBundler sb = new StringBundler(valueArray.length * 2);

				for (int i = 0; i < valueArray.length; i++) {
					LocalizedValue label = getDDMFormFieldOptionLabel(
						ddmFormFieldValue, valueArray[i]);

					if (label == null) {
						continue;
					}

					sb.append(label.getString(locale));

					if ((i + 1) < valueArray.length) {
						sb.append(StringPool.COMMA_AND_SPACE);
					}
				}

				return sb.toString();
			}

			protected LocalizedValue getDDMFormFieldOptionLabel(
				DDMFormFieldValue ddmFormFieldValue, String optionValue) {

				DDMFormField ddmFormField = getDDMFormField(ddmFormFieldValue);

				DDMFormFieldOptions ddmFormFieldOptions =
					ddmFormField.getDDMFormFieldOptions();

				return ddmFormFieldOptions.getOptionLabels(optionValue);
			}

		};
	}

}