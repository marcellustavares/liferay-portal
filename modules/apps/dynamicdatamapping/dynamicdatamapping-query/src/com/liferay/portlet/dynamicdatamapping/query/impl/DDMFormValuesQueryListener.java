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

package com.liferay.portlet.dynamicdatamapping.query.impl;

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueMatchesAllMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueMatchesAnyMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueNameMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueTypeMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueValueMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormValuesFilter;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormValuesFilterImpl;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryBaseListener;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.AttributeTypeContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.AttributeValueContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.FieldSelectorContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.FieldSelectorExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.LocaleExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.PredicateAndExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.PredicateEqualityExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.PredicateOrExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.SelectorExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.StepTypeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Marcellus Tavares
 * @author Pablo Carvalho
 */
public class DDMFormValuesQueryListener extends DDMFormValuesQueryBaseListener {

	@Override
	public void enterAttributeType(AttributeTypeContext ctx) {
		_matchers.push(new DDMFormFieldValueTypeMatcher());
	}

	@Override
	public void enterAttributeValue(AttributeValueContext ctx) {
		_matchers.push(new DDMFormFieldValueValueMatcher());
	}

	@Override
	public void enterFieldSelectorExpression(
		FieldSelectorExpressionContext ctx) {

		_matchers.push(new DDMFormFieldValueMatchesAllMatcher());
	}

	@Override
	public void enterPredicateAndExpression(PredicateAndExpressionContext ctx) {
		_matchers.push(new DDMFormFieldValueMatchesAllMatcher());
	}

	@Override
	public void enterPredicateOrExpression(PredicateOrExpressionContext ctx) {
		_matchers.push(new DDMFormFieldValueMatchesAnyMatcher());
	}

	@Override
	public void enterSelectorExpression(SelectorExpressionContext ctx) {
		_ddmFormValuesFilters.add(new DDMFormValuesFilterImpl());
	}

	@Override
	public void exitFieldSelector(FieldSelectorContext ctx) {
		String text = ctx.getText();

		if (!text.equals(StringPool.STAR)) {
			DDMFormFieldValueNameMatcher nameMatcher =
				new DDMFormFieldValueNameMatcher();

			nameMatcher.setExpectedName(text);

			DDMFormFieldValueMatchesAllMatcher previousMatcher =
				(DDMFormFieldValueMatchesAllMatcher) _matchers.peek();

			previousMatcher.addDDMFormFieldValueMatcher(nameMatcher);
		}
	}

	@Override
	public void exitFieldSelectorExpression(
		FieldSelectorExpressionContext ctx) {

		DDMFormFieldValueMatcher fieldValueMatcher = _matchers.pop();

		DDMFormValuesFilter lastFilter = _ddmFormValuesFilters.get(
			_ddmFormValuesFilters.size() - 1);

		lastFilter.setDDMFormFieldValueMatcher(fieldValueMatcher);
	}

	@Override
	public void exitLocaleExpression(LocaleExpressionContext ctx) {
		String languageId = StringUtil.unquote(ctx.STRING_LITERAL().getText());

		DDMFormFieldValueValueMatcher lastMatcher =
			(DDMFormFieldValueValueMatcher) _matchers.peek();

		lastMatcher.setLocale(LocaleUtil.fromLanguageId(languageId));
	}

	@Override
	public void exitPredicateAndExpression(PredicateAndExpressionContext ctx) {
		DDMFormFieldValueMatcher andMatcher = _matchers.pop();

		DDMFormFieldValueMatchesAnyMatcher orMatcher =
			(DDMFormFieldValueMatchesAnyMatcher) _matchers.peek();

		orMatcher.addDDMFormFieldValueMatcher(andMatcher);
	}

	@Override
	public void exitPredicateEqualityExpression(
		PredicateEqualityExpressionContext ctx) {

		DDMFormFieldValueMatcher lastMatcher = _matchers.pop();

		String text = StringUtil.unquote(ctx.STRING_LITERAL().getText());

		if (lastMatcher instanceof DDMFormFieldValueTypeMatcher) {
			DDMFormFieldValueTypeMatcher typeMatcher =
				(DDMFormFieldValueTypeMatcher)lastMatcher;

			typeMatcher.setExpectedType(text);
		}
		else {
			DDMFormFieldValueValueMatcher valueMatcher =
				(DDMFormFieldValueValueMatcher)lastMatcher;

			valueMatcher.setExpectedValue(text);
		}

		DDMFormFieldValueMatchesAllMatcher andMatcher =
			(DDMFormFieldValueMatchesAllMatcher) _matchers.peek();

		andMatcher.addDDMFormFieldValueMatcher(lastMatcher);
	}

	@Override
	public void exitPredicateOrExpression(PredicateOrExpressionContext ctx) {
		DDMFormFieldValueMatcher orMatcher = _matchers.pop();

		DDMFormFieldValueMatchesAllMatcher previousMatcher =
			(DDMFormFieldValueMatchesAllMatcher) _matchers.peek();

		previousMatcher.addDDMFormFieldValueMatcher(orMatcher);
	}

	@Override
	public void exitStepType(StepTypeContext ctx) {
		String text = ctx.getText();

		DDMFormValuesFilter lastFilter = _ddmFormValuesFilters.get(
			_ddmFormValuesFilters.size() - 1);

		if (text.equals(StringPool.SLASH)) {
			lastFilter.setGreedy(false);
		}
		else {
			lastFilter.setGreedy(true);
		}
	}

	public List<DDMFormValuesFilter> getDDMFormValuesFilters() {
		return _ddmFormValuesFilters;
	}

	public List<DDMFormValuesFilter> _ddmFormValuesFilters = new ArrayList<>();
	public Stack<DDMFormFieldValueMatcher> _matchers = new Stack<>();

}