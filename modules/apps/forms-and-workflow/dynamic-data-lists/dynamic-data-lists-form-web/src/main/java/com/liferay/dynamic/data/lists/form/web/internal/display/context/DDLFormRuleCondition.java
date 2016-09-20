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

package com.liferay.dynamic.data.lists.form.web.internal.display.context;

import java.util.List;
import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class DDLFormRuleCondition {

	public DDLFormRuleCondition() {
	}

	public DDLFormRuleCondition(String operator, List<Operand> operands) {
		_operator = operator;
		_operands = operands;
	}

	public List<Operand> getOperands() {
		return _operands;
	}

	public void setOperands(List<Operand> operands) {
		_operands = operands;
	}

	public String getOperator() {
		return _operator;
	}

	public void setOperator(String operator) {
		_operator = operator;
	}

	@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof DDLFormRuleCondition)) {
				return false;
			}

			DDLFormRuleCondition ddlFormRuleCondition = (DDLFormRuleCondition)obj;

			if (Objects.equals(_operator, ddlFormRuleCondition._operator) &&
				Objects.equals(_operands, ddlFormRuleCondition._operands)) {

				return true;
			}

			return false;
		}


	private String _operator;
	private List<Operand> _operands;

	public static class Operand {

		public Operand() {

		}
		public Operand(String type, String value) {
			_type = type;
			_value = value;
		}

		public void setValue(String value) {
			_value = value;
		}


		public void setType(String type) {
			_type = type;
		}


		public String getType() {
			return _type;
		}

		public String getValue() {
			return _value;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Operand)) {
				return false;
			}

			Operand operand = (Operand)obj;

			if (Objects.equals(_type, operand._type) &&
				Objects.equals(_value, operand._value)) {

				return true;
			}

			return false;
		}

		private String _type;
		private String _value;

	}


}
