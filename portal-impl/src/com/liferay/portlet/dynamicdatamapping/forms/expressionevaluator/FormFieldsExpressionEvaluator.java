
package com.liferay.portlet.dynamicdatamapping.forms.expressionevaluator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.commons.compiler.CompileException;

import com.liferay.portal.kernel.expressionevaluator.ExpressionEvaluator;
import com.liferay.portal.kernel.expressionevaluator.ExpressionEvaluatorImpl;
import com.liferay.portal.kernel.expressionevaluator.model.ExpressionVariable;
import com.liferay.portal.kernel.expressionevaluator.model.VariableDependencies;
import com.liferay.portlet.dynamicdatamapping.forms.FlatFieldRepresentation;
import com.liferay.portlet.dynamicdatamapping.forms.FormFieldValue;

public class FormFieldsExpressionEvaluator implements ExpressionEvaluator {

	public FormFieldsExpressionEvaluator(Locale locale,
		FlatFieldRepresentation fields) {

		_fields = fields;
		_fieldsRepetable = new HashMap<String, String>();
		_locale = locale;
		_expressionEvaluator = new ExpressionEvaluatorImpl(
			_createVariablesMap());
	}

	@Override
	public Boolean evaluateBooleanExpression(String expression)
		throws CompileException, InvocationTargetException {

		return _expressionEvaluator
			.evaluateBooleanExpression(_rewriteExpression(expression));
	}

	@Override
	public Double evaluateDoubleExpression(String expression)
		throws CompileException, InvocationTargetException {

		return _expressionEvaluator
			.evaluateDoubleExpression(_rewriteExpression(expression));
	}

	@Override
	public Object evaluateExpression(String expression, Class<?> returnedType)
		throws CompileException, InvocationTargetException {

		return _expressionEvaluator
			.evaluateExpression(_rewriteExpression(expression), returnedType);
	}

	@Override
	public Float evaluateFloatExpression(String expression)
		throws CompileException, InvocationTargetException {

		return _expressionEvaluator
			.evaluateFloatExpression(_rewriteExpression(expression));
	}

	@Override
	public Integer evaluateIntegerExpression(String expression)
		throws CompileException, InvocationTargetException {

		return _expressionEvaluator
			.evaluateIntegerExpression(_rewriteExpression(expression));
	}

	@Override
	public Long evaluateLongExpression(String expression)
		throws CompileException, InvocationTargetException {

		return _expressionEvaluator
			.evaluateLongExpression(_rewriteExpression(expression));
	}

	@Override
	public String evaluateStringExpression(String expression)
		throws CompileException, InvocationTargetException {

		return _expressionEvaluator
			.evaluateStringExpression(_rewriteExpression(expression));
	}

	@Override
	public Map<String, VariableDependencies> getDependenciesMap() {

		return _expressionEvaluator.getDependenciesMap();
	}

	private ExpressionVariable _buildExpressionVariable(
		String variableName, FormFieldValue formFieldValue) {

		ExpressionVariable variable = new ExpressionVariable();

		variable.setCalculatedValue(formFieldValue.getCalculatedValue()
			.getValue(_locale));
		variable.setDataType(formFieldValue.getDataType());
		variable.setName(variableName);
		variable.setValueExpression(formFieldValue.getValueExpression()
			.getValue(_locale));

		return variable;
	}

	private Map<String, ExpressionVariable> _createVariablesMap() {

		HashMap<String, ExpressionVariable> variables =
			new HashMap<String, ExpressionVariable>();

		for (String fieldName : _fields.getFieldsNames()) {
			List<FormFieldValue> formFieldList = _fields.getFieldValue(
				fieldName);

			if (formFieldList.size() > 1) {

				for (int i = 0; i < formFieldList.size(); i++) {
					FormFieldValue formFieldValue = formFieldList.get(i);
					String variableName = getIndexedFieldName(fieldName, i);
					variables.put(variableName,
						_buildExpressionVariable(variableName, formFieldValue));
				}

				_fieldsRepetable.put(fieldName, _expandFieldRepeatable(
					fieldName, formFieldList.size()));
			}
			else {
				variables.put(fieldName,
					_buildExpressionVariable(fieldName, formFieldList.get(0)));
			}
		}

		_rewriteExpressions(variables);

		return variables;
	}

	private String _expandFieldRepeatable(String fieldName,
		int quantityOfValues) {

		StringBuilder expandandField = new StringBuilder();
		expandandField.append(getIndexedFieldName(fieldName, 0));

		for (int i = 1; i < quantityOfValues; i++) {
			expandandField.append(", ");
			expandandField.append(getIndexedFieldName(fieldName, i));
		}

		return expandandField.toString();
	}

	private List<String> _extractExpressionRepeatableFields(String expression) {

		List<String> repeatableFields = new ArrayList<String>();

		if (expression == null) {
			return repeatableFields;
		}

		Matcher matcher = FIELD_NAME_REGEX_PATTERN.matcher(expression);

		while (matcher.find()) {
			String fieldName = matcher.group(1);
			if (_fieldsRepetable.containsKey(fieldName)) {
				repeatableFields.add(fieldName);
			}
		}

		return repeatableFields;
	}

	private String _rewriteAggregateFunctions(String expression,
		String dependency) {

		String rewritedExpression;
		String regex = REGEX_PREFIX_FIELD_IN_AGGREGATE_FUNCTION
			+ dependency
			+ REGEX_SUFFIX_FIELD_IN_AGGREGATE_FUNCTION;

		rewritedExpression = expression.replaceAll(regex,
			_fieldsRepetable.get(dependency));
		return rewritedExpression;
	}

	private String _rewriteExpression(String expression) {

		List<String> variableDependencies =
			_extractExpressionRepeatableFields(expression);

		String rewritedExpression = expression;

		for (String dependency : variableDependencies) {
			if (_fieldsRepetable.containsKey(dependency)) {
				rewritedExpression = rewritedExpression.replaceAll(dependency,
					_fieldsRepetable.get(dependency));
			}
		}

		return rewritedExpression;
	}

	private String _rewriteExpression(String expression, String variableName) {

		List<String> variableRepeatableDependencies =
			_extractExpressionRepeatableFields(expression);

		String rewritedExpression = expression;

		String fieldName =
			variableName.indexOf("_") > -1 ? variableName.replaceAll("_\\d+",
				"") : variableName;

		if (_fieldsRepetable.containsKey(fieldName)) {
			for (String dependency : variableRepeatableDependencies) {
				rewritedExpression =
					_rewriteAggregateFunctions(expression, dependency);
				rewritedExpression = _rewriteNestedReferences(expression,
					dependency, variableName);
			}
		}
		else {
			for (String dependency : variableRepeatableDependencies) {
				_validateUseOfRepeatableField(expression, dependency);
				rewritedExpression = expression.replaceAll(dependency,
					_fieldsRepetable.get(dependency));
			}
		}

		return rewritedExpression;
	}

	private void _rewriteExpressions(Map<String, ExpressionVariable> variables) {

		for (ExpressionVariable variable : variables.values()) {

			variable.setValueExpression(_rewriteExpression(variable
				.getValueExpression(), variable.getName()));
		}

	}

	private String _rewriteNestedReferences(String expression,
		String dependency, String variableName) {

		Matcher matcher = VARIABLE_INDEX_REGEX_PATTERN.matcher(variableName);

		if (matcher.find()) {
			String index = matcher.group(0);
			expression.replaceAll(dependency, dependency + index);
		}

		return expression;
	}

	private void _validateUseOfRepeatableField(String expression,
		String fieldName) {

		String regex = REGEX_PREFIX_FIELD_NOT_IN_AGGREGATE_FUNCTION
			+ fieldName
			+ REGEX_SUFFIX_FIELD_NOT_IN_AGGREGATE_FUNCTION;

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(expression);

		if (matcher.find()) {
			throw new IllegalArgumentException("The field " + fieldName +
				" is repeatable and cannot be used without an aggregate " +
				"function.");
		}
	}

	private String getIndexedFieldName(String fieldName, int index) {

		return fieldName + "_" + index;
	}

	private static final Pattern FIELD_NAME_REGEX_PATTERN = Pattern.compile(
		"\\b([a-zA-Z]+[\\w\\._]+)(?!\\()\\b", Pattern.MULTILINE);

	private static final String REGEX_PREFIX_FIELD_IN_AGGREGATE_FUNCTION =
		"(?<=[a-zA-Z\\.]+)(?<=\\w+)(?<=\\s*\\(\\s*)(";

	private static final String REGEX_PREFIX_FIELD_NOT_IN_AGGREGATE_FUNCTION =
		"(?<![a-zA-Z\\.]+)(?<!\\w+)(?<!\\s*\\(\\s*)(";

	private static final String REGEX_SUFFIX_FIELD_IN_AGGREGATE_FUNCTION =
		")(?=\\s*\\))";

	private static final String REGEX_SUFFIX_FIELD_NOT_IN_AGGREGATE_FUNCTION =
		")(?!\\s*\\))";

	private static final Pattern VARIABLE_INDEX_REGEX_PATTERN = Pattern
		.compile("(_\\d+)");

	private final ExpressionEvaluatorImpl _expressionEvaluator;
	private final FlatFieldRepresentation _fields;
	private final Map<String, String> _fieldsRepetable;
	private final Locale _locale;

}
