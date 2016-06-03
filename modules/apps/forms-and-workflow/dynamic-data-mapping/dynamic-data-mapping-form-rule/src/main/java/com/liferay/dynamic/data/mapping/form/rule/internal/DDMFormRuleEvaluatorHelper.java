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

package com.liferay.dynamic.data.mapping.form.rule.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.util.StringPool;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorHelper {

	public DDMFormRuleEvaluatorHelper(
		DDMForm ddmForm, DDMFormValues ddmFormValues, Locale locale,
		DDMDataProviderInstanceService ddmDataProviderInstanceService,
		DDMDataProviderTracker ddmDataProviderTracker,
		DDMExpressionFactory ddmExpressionFactory,
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer) {

		_ddmFormRuleEvaluatorContext = new DDMFormRuleEvaluatorContext(
			ddmDataProviderInstanceService, ddmDataProviderTracker,
			ddmExpressionFactory, ddmForm.getDDMFormFields(), ddmFormValues,
			ddmFormValuesJSONDeserializer, locale);
	}

	public DDMFormRuleEvaluatorGraph createDDMFormRuleEvaluatorGraph()
		throws Exception {

		Set<DDMFormRuleEvaluatorNode> nodes = createDDMFormRuleEvaluatorNodes();

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			new DDMFormRuleEvaluatorGraph(nodes, _ddmFormRuleEvaluatorContext);

		return ddmFormRuleEvaluatorGraph;
	}

	protected DDMFormRuleEvaluatorNode createDDMFormRuleEvaluatorNode(
		DDMFormField ddmFormField, DDMFormFieldRule ddmFormFieldRule) {

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode =
			new DDMFormRuleEvaluatorNode(
				ddmFormField.getName(), StringPool.BLANK,
				ddmFormFieldRule.getType(), ddmFormFieldRule.getExpression());

		return ddmFormRuleEvaluatorNode;
	}

	protected Set<DDMFormRuleEvaluatorNode> createDDMFormRuleEvaluatorNodes() {
		Set<DDMFormRuleEvaluatorNode> nodes = new HashSet<>();

		Map<String, DDMFormField> ddmFormFieldsMap = getDDMFormFieldsMap();

		List<DDMFormFieldRule> ddmFormFieldRules = null;

		for (DDMFormField ddmFormField : ddmFormFieldsMap.values()) {
			ddmFormFieldRules = ddmFormField.getDDMFormFieldRules();

			for (DDMFormFieldRule ddmFormFieldRule : ddmFormFieldRules) {
				nodes.add(
					createDDMFormRuleEvaluatorNode(
						ddmFormField, ddmFormFieldRule));
			}
		}

		return nodes;
	}

	protected List<DDMFormField> getDDMFormFields() {
		return _ddmFormRuleEvaluatorContext.getDDMFormFields();
	}

	protected Map<String, DDMFormField> getDDMFormFieldsMap() {
		List<DDMFormField> ddmFormFields = getDDMFormFields();
		DDMForm ddmForm = ddmFormFields.get(0).getDDMForm();

		return ddmForm.getDDMFormFieldsMap(true);
	}

	private final DDMFormRuleEvaluatorContext _ddmFormRuleEvaluatorContext;

}