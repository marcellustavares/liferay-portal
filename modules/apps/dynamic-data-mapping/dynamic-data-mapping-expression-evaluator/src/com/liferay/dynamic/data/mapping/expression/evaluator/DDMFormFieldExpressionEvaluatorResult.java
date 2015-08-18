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

package com.liferay.dynamic.data.mapping.expression.evaluator;

import com.liferay.portal.kernel.json.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class DDMFormFieldExpressionEvaluatorResult {

	public DDMFormFieldExpressionEvaluatorResult(
		String name, String instanceId) {

		_name = name;
		_instanceId = instanceId;
	}

	public String getInstanceId() {
		return _instanceId;
	}

	public String getName() {
		return _name;
	}

	public List<DDMFormFieldExpressionEvaluatorResult>
		getNestedDDMFormFieldExpressionEvaluatorResults() {

		return _nestedDDMFormFieldExpressionEvaluatorResults;
	}

	public boolean isValid() {
		return _valid;
	}

	public boolean isVisible() {
		return _visible;
	}

	public void setNestedDDMFormFieldExpressionEvaluatorResults(
		List<DDMFormFieldExpressionEvaluatorResult>
			nestedDDMFormFieldExpressionEvaluatorResults) {

		_nestedDDMFormFieldExpressionEvaluatorResults =
			nestedDDMFormFieldExpressionEvaluatorResults;
	}

	public void setValid(boolean valid) {
		_valid = valid;
	}

	public void setVisible(boolean visible) {
		_visible = visible;
	}

	@JSON
	private final String _instanceId;

	@JSON
	private final String _name;

	@JSON(name = "nestedFields")
	private List<DDMFormFieldExpressionEvaluatorResult>
		_nestedDDMFormFieldExpressionEvaluatorResults = new ArrayList<>();

	@JSON
	private boolean _valid;

	@JSON
	private boolean _visible;

}