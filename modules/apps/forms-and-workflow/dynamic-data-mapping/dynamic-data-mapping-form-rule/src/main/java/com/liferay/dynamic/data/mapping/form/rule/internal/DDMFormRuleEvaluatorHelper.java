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
import com.liferay.dynamic.data.mapping.form.rule.DDMFormFieldRuleEvaluationResult;
import com.liferay.dynamic.data.mapping.form.rule.internal.rules.DDMFormFieldBaseRule;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorHelper implements DDMFormRuleEvaluatorContext {

	public DDMFormRuleEvaluatorHelper(
		DDMForm ddmForm, DDMFormValues ddmFormValues, Locale locale,
		DDMDataProviderInstanceService ddmDataProviderInstanceService,
		DDMDataProviderTracker ddmDataProviderTracker,
		DDMExpressionFactory ddmExpressionFactory,
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer) {

		_ddmFormFields = ddmForm.getDDMFormFields();
		_ddmFormValues = ddmFormValues;
		_locale = locale;
		_ddmDataProviderInstanceService = ddmDataProviderInstanceService;
		_ddmDataProviderTracker = ddmDataProviderTracker;
		_ddmExpressionFactory = ddmExpressionFactory;
		_ddmFormValuesJSONDeserializer = ddmFormValuesJSONDeserializer;

		_ddmFormFieldRuleEvaluationResultMap = new HashMap<>();

		_ddmFormFieldRuleHelper = new DDMFormFieldRuleHelper(this);

		_ddmFormRuleEvaluatorTree = new DDMFormRuleEvaluatorTree(
			_ddmFormFieldRuleHelper);
	}

	public Set<DDMFormFieldBaseRule> createRules() throws PortalException {
		Set<DDMFormFieldBaseRule> rules = new TreeSet<>();

		for (DDMFormField ddmFormField :
				_ddmFormFieldRuleHelper.getDDMFormFields()) {

			List<DDMFormFieldRule> ddmFormFieldRules =
				ddmFormField.getDDMFormFieldRules();

			List<DDMFormFieldBaseRule> ddmFormFieldBaseRules =
				new ArrayList<>();

			List<DDMFormFieldRuleType> absentRuleTypes = new ArrayList<>(
				ListUtil.fromArray(DDMFormFieldRuleType.values()));

			for (DDMFormFieldRule ddmFormFieldRule : ddmFormFieldRules) {
				ddmFormFieldBaseRules.addAll(
					createDDMFormFieldRules(ddmFormField, ddmFormFieldRule));

				absentRuleTypes.remove(ddmFormFieldRule.getType());
			}

			if (ddmFormFieldBaseRules != null) {
				rules.addAll(ddmFormFieldBaseRules);
			}

			if (!absentRuleTypes.isEmpty()) {
				ddmFormFieldBaseRules = createDDMFormFieldAdditionalRules(
					ddmFormField, absentRuleTypes);

				rules.addAll(ddmFormFieldBaseRules);
			}
		}

		_ddmFormRuleEvaluatorTree.assignPriorities();

		return rules;
	}

	@Override
	public DDMDataProviderInstanceService getDDMDataProviderInstanceService() {
		return _ddmDataProviderInstanceService;
	}

	@Override
	public DDMDataProviderTracker getDDMDataProviderTracker() {
		return _ddmDataProviderTracker;
	}

	@Override
	public DDMExpressionFactory getDDMExpressionFactory() {
		return _ddmExpressionFactory;
	}

	@Override
	public Map<String, DDMFormFieldRuleEvaluationResult>
		getDDMFormFieldRuleEvaluationMap() {

		return _ddmFormFieldRuleEvaluationResultMap;
	}

	public List<DDMFormFieldRuleEvaluationResult>
		getDDMFormFieldRuleEvaluationResults() {

		return ListUtil.fromCollection(
			_ddmFormFieldRuleEvaluationResultMap.values());
	}

	@Override
	public List<DDMFormField> getDDMFormFields() {
		return _ddmFormFields;
	}

	@Override
	public DDMFormValues getDDMFormValues() {
		return _ddmFormValues;
	}

	@Override
	public DDMFormValuesJSONDeserializer getDDMFormValuesJSONDeserializer() {
		return _ddmFormValuesJSONDeserializer;
	}

	@Override
	public Locale getLocale() {
		return _locale;
	}

	protected List<DDMFormFieldBaseRule> createDDMFormFieldAdditionalRules(
			DDMFormField ddmFormField,
			List<DDMFormFieldRuleType> absentRuleTypes)
		throws PortalException {

		List<DDMFormFieldBaseRule> rules = new ArrayList<>();

		for (DDMFormFieldRuleType ddmFormFieldRuleType : absentRuleTypes) {
			List<DDMFormFieldBaseRule> ddmFormFieldBaseRules =
				createDDMFormFieldRules(
					ddmFormField, new DDMFormFieldRule(
						StringPool.BLANK, ddmFormFieldRuleType));

			rules.addAll(ddmFormFieldBaseRules);
		}

		return rules;
	}

	protected List<DDMFormFieldBaseRule> createDDMFormFieldRules(
			DDMFormField ddmFormField, DDMFormFieldRule ddmFormFieldRule)
		throws PortalException {

		List<DDMFormFieldBaseRule> ddmFormFieldRules = new ArrayList<>();

		DDMFormValues ddmFormValues =
			_ddmFormFieldRuleHelper.getDDMFormValues();

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			ddmFormField.getName());

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			DDMFormFieldBaseRule ddmFormFieldBaseRule =
				_ddmFormFieldRuleHelper.createDDMFormFieldRule(
					ddmFormField, ddmFormFieldValue, ddmFormFieldRule);

			DDMFormRuleEvaluatorTreeNode treeNode =
				new DDMFormRuleEvaluatorTreeNode(ddmFormFieldBaseRule);

			_ddmFormRuleEvaluatorTree.addTreeNode(treeNode);

			ddmFormFieldRules.add(ddmFormFieldBaseRule);
		}

		return ddmFormFieldRules;
	}

	private final DDMDataProviderInstanceService
		_ddmDataProviderInstanceService;
	private final DDMDataProviderTracker _ddmDataProviderTracker;
	private final DDMExpressionFactory _ddmExpressionFactory;
	private final Map<String, DDMFormFieldRuleEvaluationResult>
		_ddmFormFieldRuleEvaluationResultMap;
	private final DDMFormFieldRuleHelper _ddmFormFieldRuleHelper;
	private final List<DDMFormField> _ddmFormFields;
	private final DDMFormRuleEvaluatorTree _ddmFormRuleEvaluatorTree;
	private final DDMFormValues _ddmFormValues;
	private final DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;
	private final Locale _locale;

}