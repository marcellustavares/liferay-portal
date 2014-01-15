
package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portlet.dynamicdatamapping.storage.query.ComparisonOperator;
import com.liferay.portlet.dynamicdatamapping.storage.query.Condition;
import com.liferay.portlet.dynamicdatamapping.storage.query.ConditionFactoryUtil;
class ExpectedResult {

	ExpectedResult(ComparisonOperator comparisonOperator, String fieldName,
		Object _valueForTest, int _resultCount)
	{

		this._resultCount = _resultCount;

		switch (comparisonOperator) {
			case EXCLUDES:
				break;
			case GREATER_THAN:
				_condition = ConditionFactoryUtil
					.getConditionFactory().gt(fieldName, _valueForTest);
				break;
			case GREATER_THAN_OR_EQUAL_TO:
				_condition = ConditionFactoryUtil
					.getConditionFactory().gte(fieldName, _valueForTest);
				break;
			case IN:
				_condition = ConditionFactoryUtil
					.getConditionFactory().in(fieldName, _valueForTest);
				break;
			case INCLUDES:
				break;
			case JOIN:
				break;
			case LESS_THAN:
				_condition = ConditionFactoryUtil
					.getConditionFactory().lt(fieldName, _valueForTest);
				break;
			case LESS_THAN_OR_EQUAL_TO:
				_condition = ConditionFactoryUtil
					.getConditionFactory().lte(fieldName, _valueForTest);
				break;
			case LIKE:
				_condition = ConditionFactoryUtil
					.getConditionFactory().like(fieldName, _valueForTest);
				break;
			case NOT_EQUALS:
				_condition = ConditionFactoryUtil
					.getConditionFactory().ne(fieldName, _valueForTest);
				break;
			case NOT_IN:
				_condition = ConditionFactoryUtil
					.getConditionFactory().notIn(fieldName, _valueForTest);
				break;
			default:
				_condition = ConditionFactoryUtil
					.getConditionFactory().eq(fieldName, _valueForTest);
				break;
		}
	}

	Condition getCondition() {

		return _condition;
	}

	int getResultCount() {

		return _resultCount;
	}

	private Condition _condition;
	private int _resultCount;

}