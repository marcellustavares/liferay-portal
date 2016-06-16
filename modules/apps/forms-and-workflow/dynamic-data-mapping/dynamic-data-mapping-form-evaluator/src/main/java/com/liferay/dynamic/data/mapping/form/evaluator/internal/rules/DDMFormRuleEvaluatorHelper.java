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

package com.liferay.dynamic.data.mapping.form.evaluator.internal.rules;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.VariableDependencies;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRule;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldRuleType;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Leonardo Barros
 */
public class DDMFormRuleEvaluatorHelper {

	public DDMFormRuleEvaluatorHelper(
		DDMExpressionFactory ddmExpressionFactory, DDMForm ddmForm) {

		_ddmExpressionFactory = ddmExpressionFactory;
		_ddmForm = ddmForm;
	}

	public DDMFormRuleEvaluatorGraph createDDMFormRuleEvaluatorGraph()
		throws Exception {

		Set<DDMFormRuleEvaluatorNode> nodes = createDDMFormRuleEvaluatorNodes();

		createEdges(nodes);

		DDMFormRuleEvaluatorGraph ddmFormRuleEvaluatorGraph =
			new DDMFormRuleEvaluatorGraph(nodes);

		return ddmFormRuleEvaluatorGraph;
	}

	protected Set<DDMFormRuleEvaluatorNode> createDataProviderEdges(
		String expression) {

		Set<DDMFormRuleEvaluatorNode> edges = new HashSet<>();

		for (String patternStr : _DATA_PROVIDER_FUNCTIONS_PATTERN) {
			edges.addAll(
				createEdges(
					expression, patternStr,
					DDMFormFieldRuleType.DATA_PROVIDER));
		}

		return edges;
	}

	protected DDMFormRuleEvaluatorNode createDDMFormRuleEvaluatorNode(
		DDMFormField ddmFormField, DDMFormFieldRule ddmFormFieldRule) {

		DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode =
			new DDMFormRuleEvaluatorNode(
				ddmFormFieldRule.getExpression(),
				ddmFormFieldRule.getDDMFormFieldRuleType(),
				ddmFormField.getName(), StringPool.BLANK);

		return ddmFormRuleEvaluatorNode;
	}

	protected Set<DDMFormRuleEvaluatorNode>
		createDDMFormRuleEvaluatorNodeEdges(String expression) {

		Set<DDMFormRuleEvaluatorNode> edges = new HashSet<>();

		edges.addAll(createDataProviderEdges(expression));
		edges.addAll(createReadOnlyEdges(expression));
		edges.addAll(createValidationEdges(expression));
		edges.addAll(createValueEdges(expression));
		edges.addAll(createVisibilityEdges(expression));

		return edges;
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

	protected void createEdges(
			List<DDMFormRuleEvaluatorNode> flatNodes,
			Set<DDMFormRuleEvaluatorNode> groupedNodes,
			DDMFormRuleEvaluatorNode node)
		throws Exception {

		String expression = node.getExpression();

		boolean hasDependencies = hasDependencies(expression);

		if (!hasDependencies) {
			return;
		}

		groupedNodes.remove(node);

		Set<DDMFormRuleEvaluatorNode> edges =
			createDDMFormRuleEvaluatorNodeEdges(expression);

		for (DDMFormRuleEvaluatorNode edge : edges) {
			if (node.equals(edge)) {
				continue;
			}

			if (flatNodes.contains(edge)) {
				int indexOf = flatNodes.indexOf(edge);
				DDMFormRuleEvaluatorNode foundNode = flatNodes.get(indexOf);
				node.getEdges().add(foundNode);
				groupedNodes.remove(foundNode);
			}
			else {
				node.getEdges().add(edge);
				flatNodes.add(edge);
				groupedNodes.add(edge);
			}
		}
	}

	protected void createEdges(Set<DDMFormRuleEvaluatorNode> nodes)
		throws Exception {

		Set<DDMFormRuleEvaluatorNode> groupedNodes = new HashSet<>(nodes);
		List<DDMFormRuleEvaluatorNode> flatNodes = new ArrayList<>(nodes);

		for (DDMFormRuleEvaluatorNode node : nodes) {
			createEdges(flatNodes, groupedNodes, node);
		}
	}

	protected Set<DDMFormRuleEvaluatorNode> createEdges(
		String expression, String patternStr,
		DDMFormFieldRuleType ddmFormFieldRuleType) {

		Set<DDMFormRuleEvaluatorNode> edges = new HashSet<>();

		Pattern pattern = Pattern.compile(patternStr);

		Matcher matcher = pattern.matcher(expression);

		while (matcher.find()) {
			Set<String> ddmFormFieldNames = extractDDMFormFieldName(
				matcher.group(1));

			for (String ddmFormFieldName : ddmFormFieldNames) {
				DDMFormRuleEvaluatorNode ddmFormRuleEvaluatorNode =
					new DDMFormRuleEvaluatorNode(
						StringPool.BLANK, ddmFormFieldRuleType,
						ddmFormFieldName, StringPool.BLANK);

				edges.add(ddmFormRuleEvaluatorNode);
			}
		}

		return edges;
	}

	protected Set<DDMFormRuleEvaluatorNode> createReadOnlyEdges(
		String expression) {

		return createEdges(
			expression, _READONLY_PATTERN, DDMFormFieldRuleType.READ_ONLY);
	}

	protected Set<DDMFormRuleEvaluatorNode> createValidationEdges(
		String expression) {

		Set<DDMFormRuleEvaluatorNode> edges = new HashSet<>();

		for (String patternStr : _VALIDATION_FUNCTIONS_PATTERN) {
			edges.addAll(
				createEdges(
					expression, patternStr, DDMFormFieldRuleType.VALIDATION));
		}

		return edges;
	}

	protected Set<DDMFormRuleEvaluatorNode> createValueEdges(
		String expression) {

		Set<DDMFormRuleEvaluatorNode> edges = new HashSet<>();

		for (String patternStr : _VALUE_FUNCTIONS_PATTERN) {
			edges.addAll(
				createEdges(
					expression, patternStr, DDMFormFieldRuleType.VALUE));
		}

		return edges;
	}

	protected Set<DDMFormRuleEvaluatorNode> createVisibilityEdges(
		String expression) {

		return createEdges(
			expression, _VISIBILITY_PATTERN, DDMFormFieldRuleType.VISIBILITY);
	}

	protected Set<String> extractDDMFormFieldName(String innerExpression) {
		Set<String> ddmFormFieldNames = new HashSet<>();

		Map<String, DDMFormField> ddmFormFieldsMap = getDDMFormFieldsMap();

		Pattern pattern = Pattern.compile(_VARIABLE_PATTERN);

		Matcher matcher = pattern.matcher(innerExpression);

		while (matcher.find()) {
			String variableName = null;

			for (int i = 1; i <= matcher.groupCount(); i++) {
				variableName = matcher.group(i);

				if (ddmFormFieldsMap.containsKey(variableName)) {
					ddmFormFieldNames.add(variableName);
				}
			}
		}

		return ddmFormFieldNames;
	}

	protected Map<String, DDMFormField> getDDMFormFieldsMap() {
		return _ddmForm.getDDMFormFieldsMap(true);
	}

	protected boolean hasDependencies(String expression) throws Exception {
		if (Validator.isNull(expression)) {
			return false;
		}

		DDMExpression<Boolean> ddmExpression =
			_ddmExpressionFactory.createBooleanDDMExpression(expression);

		Map<String, VariableDependencies> dependenciesMap =
			ddmExpression.getVariableDependenciesMap();

		Map<String, DDMFormField> ddmFormFieldsMap = getDDMFormFieldsMap();

		for (String ddmFormFieldName : ddmFormFieldsMap.keySet()) {
			if (dependenciesMap.containsKey(ddmFormFieldName)) {
				return true;
			}
		}

		return false;
	}

	private static final String _BETWEEN_PATTERN =
		"((between)\\((\\w+),(\\w+),(\\w+)\\))";

	private static final String _CALL_PATTERN =
		"((call)\\((\\d+),\"(.*)\",\"(.*)\"\\))";

	private static final String _CONTAINS_PATTERN =
		"((contains)\\((\\w+),\"(\\w+)\"\\))";

	private static final String _EQUALS_PATTERN =
		"((equals)\\((\\w+),\"(\\w+)\"\\))";

	private static final String _IS_EMAIL_ADDRESS_PATTERN =
		"((isEmailAddress)\\((\\w+)\\))";

	private static final String _IS_URL_PATTERN = "((isURL)\\((\\w+)\\))";

	private static final String _READONLY_PATTERN =
		"((isReadOnly)\\((\\w+)\\))";

	private static final String _VARIABLE_PATTERN =
		"\\b([a-zA-Z]+[\\w_]*)(?!\\()\\b";

	private static final String _VISIBILITY_PATTERN =
		"((isVisible)\\((\\w+)\\))";

	private final String[] _DATA_PROVIDER_FUNCTIONS_PATTERN =
		new String[] {_CALL_PATTERN};

	private final String[] _VALIDATION_FUNCTIONS_PATTERN =
		new String[] {_IS_EMAIL_ADDRESS_PATTERN, _IS_URL_PATTERN};

	private final String[] _VALUE_FUNCTIONS_PATTERN = new String[] {
		_BETWEEN_PATTERN, _CONTAINS_PATTERN, _EQUALS_PATTERN, _VARIABLE_PATTERN
	};

	private final DDMExpressionFactory _ddmExpressionFactory;
	private final DDMForm _ddmForm;

}