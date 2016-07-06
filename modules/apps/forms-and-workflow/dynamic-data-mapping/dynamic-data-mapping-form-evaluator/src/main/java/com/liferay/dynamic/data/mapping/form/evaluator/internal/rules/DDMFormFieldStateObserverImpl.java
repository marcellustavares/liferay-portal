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

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldStateObserver;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldStateObserverImpl
	implements DDMFormFieldStateObserver {

	public DDMFormFieldStateObserverImpl(
		Map<String, Map<String, DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults) {

		_ddmFormFieldEvaluationResults = ddmFormFieldEvaluationResults;
	}

	@Override
	public Object getDDMFormFieldValue(String ddmFormFieldName) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("No field's instance was found");
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		return ddmFormFieldEvaluationResult.getValue();
	}

	@Override
	public boolean isReadOnly(String ddmFormFieldName) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("No field's instance was found");
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		return ddmFormFieldEvaluationResult.isReadOnly();
	}

	@Override
	public boolean isValid(String ddmFormFieldName) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("No field's instance was found");
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		return ddmFormFieldEvaluationResult.isValid();
	}

	@Override
	public boolean isVisible(String ddmFormFieldName) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("No field's instance was found");
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		return ddmFormFieldEvaluationResult.isVisible();
	}

	@Override
	public void setReadOnly(String ddmFormFieldName, boolean value) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("No field's instance was found");
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		ddmFormFieldEvaluationResult.setReadOnly(value);
	}

	@Override
	public void setValid(String ddmFormFieldName, boolean value) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("No field's instance was found");
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		ddmFormFieldEvaluationResult.setValid(value);
	}

	@Override
	public void setVisible(String ddmFormFieldName, boolean value) {
		if (!_ddmFormFieldEvaluationResults.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap =
				_ddmFormFieldEvaluationResults.get(ddmFormFieldName);

		Collection<DDMFormFieldEvaluationResult> values =
			ddmFormFieldEvaluationResultMap.values();

		Iterator<DDMFormFieldEvaluationResult> iterator = values.iterator();

		if (!iterator.hasNext()) {
			throw new IllegalArgumentException("No field's instance was found");
		}

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			iterator.next();

		ddmFormFieldEvaluationResult.setVisible(value);
	}

	private final Map<String, Map<String, DDMFormFieldEvaluationResult>>
		_ddmFormFieldEvaluationResults;

}