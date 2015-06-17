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

package com.liferay.dynamic.data.mapping.storage.query;

import org.osgi.service.component.annotations.Component;

import com.liferay.dynamic.data.mapping.storage.query.ComparisonOperator;
import com.liferay.dynamic.data.mapping.storage.query.Condition;
import com.liferay.dynamic.data.mapping.storage.query.ConditionFactory;
import com.liferay.dynamic.data.mapping.storage.query.Junction;
import com.liferay.dynamic.data.mapping.storage.query.LogicalOperator;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = ConditionFactory.class)
public class ConditionFactoryImpl implements ConditionFactory {

	@Override
	public Junction conjunction() {
		return new JunctionImpl(LogicalOperator.AND);
	}

	@Override
	public Junction disjunction() {
		return new JunctionImpl(LogicalOperator.OR);
	}

	@Override
	public Condition eq(String name, Object value) {
		return new FieldConditionImpl(name, value, ComparisonOperator.EQUALS);
	}

	@Override
	public Condition gt(String name, Object value) {
		return new FieldConditionImpl(
			name, value, ComparisonOperator.GREATER_THAN);
	}

	@Override
	public Condition gte(String name, Object value) {
		return new FieldConditionImpl(
			name, value, ComparisonOperator.GREATER_THAN_OR_EQUAL_TO);
	}

	@Override
	public Condition in(String name, Object value) {
		return new FieldConditionImpl(name, value, ComparisonOperator.IN);
	}

	@Override
	public Condition like(String name, Object value) {
		return new FieldConditionImpl(name, value, ComparisonOperator.LIKE);
	}

	@Override
	public Condition lt(String name, Object value) {
		return new FieldConditionImpl(
			name, value, ComparisonOperator.LESS_THAN);
	}

	@Override
	public Condition lte(String name, Object value) {
		return new FieldConditionImpl(
			name, value, ComparisonOperator.LESS_THAN_OR_EQUAL_TO);
	}

	@Override
	public Condition ne(String name, Object value) {
		return new FieldConditionImpl(
			name, value, ComparisonOperator.NOT_EQUALS);
	}

	@Override
	public Condition notIn(String name, Object value) {
		return new FieldConditionImpl(name, value, ComparisonOperator.NOT_IN);
	}

}