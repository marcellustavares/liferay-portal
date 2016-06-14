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

package com.liferay.dynamic.data.mapping.form.evaluator.rules;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.List;
import java.util.Map;

import org.junit.Before;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorBaseTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		setUpLanguageUtil();
	}

	protected void createDDMFormFieldEvaluationResult(
		DDMFormField ddmFormField, DDMFormValues ddmFormValues,
		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResults) {

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			ddmFormField.getName());

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
				new DDMFormFieldEvaluationResult(
					ddmFormField.getName(), ddmFormFieldValue.getInstanceId());

			ddmFormFieldEvaluationResult.setErrorMessage(StringPool.BLANK);
			ddmFormFieldEvaluationResult.setReadOnly(false);
			ddmFormFieldEvaluationResult.setValid(true);
			ddmFormFieldEvaluationResult.setVisible(true);
			ddmFormFieldEvaluationResult.setValue(
				ddmFormFieldValue.getValue().getString(LocaleUtil.US));

			ddmFormFieldEvaluationResults.put(
				ddmFormField.getName(), ddmFormFieldEvaluationResult);
		}
	}

	protected DDMFormValues createDDMFormValues() {
		DDMForm ddmForm = new DDMForm();
		return DDMFormValuesTestUtil.createDDMFormValues(ddmForm);
	}

	protected void setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(_language);
	}

	@Mock
	private Language _language;

}