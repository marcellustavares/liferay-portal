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

package com.liferay.dynamic.data.mapping.form.rule;

import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldRuleEvaluationResult {

	public DDMFormFieldRuleEvaluationResult(String name) {
		_name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		else if (obj == null) {
			return false;
		}
		else if (getClass() != obj.getClass()) {
			return false;
		}

		DDMFormFieldRuleEvaluationResult other =
			(DDMFormFieldRuleEvaluationResult)obj;

		if (_name == null) {
			if (other._name != null) {
				return false;
			}
		}

		if (!_name.equals(other._name)) {
			return false;
		}

		return true;
	}

	public String getErrorMessage() {
		return _errorMessage;
	}

	public String getName() {
		return _name;
	}

	public Object getValue() {
		return _value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		return result;
	}

	public boolean isReadOnly() {
		return _readOnly;
	}

	public boolean isValid() {
		return _valid;
	}

	public boolean isVisible() {
		return _visible;
	}

	public void setErrorMessage(String errorMessage) {
		_errorMessage = errorMessage;
	}

	public void setReadOnly(boolean readOnly) {
		_readOnly = readOnly;
	}

	public void setValid(boolean valid) {
		_valid = valid;
	}

	public void setValue(Object value) {
		_value = value;
	}

	public void setVisible(boolean visible) {
		_visible = visible;
	}

	private String _errorMessage = StringPool.BLANK;
	private final String _name;
	private boolean _readOnly;
	private boolean _valid = true;
	private Object _value;
	private boolean _visible = true;

}