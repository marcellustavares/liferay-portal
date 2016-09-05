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

package com.liferay.dynamic.data.mapping.form.rule.translator.impl;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.form.rule.translator.DDMFormRuleTranslator;
import com.liferay.dynamic.data.mapping.form.rule.translator.DDMFormRuleTranslatorException;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true)
public class DDMFormRuleTranslatorImpl implements DDMFormRuleTranslator {

	@Override
	public String translate(DDMForm ddmForm)
		throws DDMFormRuleTranslatorException {

		DDMFormRuleModelTranslator ddmFormRuleModelTranslator =
			new DDMFormRuleModelTranslator(
				_ddmExpressionFactory, ddmForm, _jsonFactory);

		try {
			return ddmFormRuleModelTranslator.translate();
		}
		catch (DDMFormRuleTranslatorException ddmfrte) {
			_log.error(
				"An error occured when translating form rules.", ddmfrte);

			throw ddmfrte;
		}
		catch (Exception e) {
			_log.error("An error occured when translating form rules.", e);

			throw new DDMFormRuleTranslatorException(
				"An error occured when translating form rules.", e);
		}
	}

	@Override
	public List<DDMFormRule> translate(String rulesJSON)
		throws DDMFormRuleTranslatorException {

		DDMFormRuleJSONTranslator ddmFormRuleJSONTranslator =
			new DDMFormRuleJSONTranslator(rulesJSON, _jsonFactory);

		try {
			return ddmFormRuleJSONTranslator.translate();
		}
		catch (DDMFormRuleTranslatorException ddmfrte) {
			_log.error(
				"An error occured when translating form rules.", ddmfrte);

			throw ddmfrte;
		}
		catch (Exception e) {
			_log.error("An error occured when translating form rules.", e);

			throw new DDMFormRuleTranslatorException(
				"An error occured when translating form rules.", e);
		}
	}

	@Reference(unbind = "-")
	protected void setDDMExpressionFactory(
		DDMExpressionFactory ddmExpressionFactory) {

		_ddmExpressionFactory = ddmExpressionFactory;
	}

	@Reference(unbind = "-")
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMFormRuleTranslatorImpl.class);

	private DDMExpressionFactory _ddmExpressionFactory;
	private JSONFactory _jsonFactory;

}