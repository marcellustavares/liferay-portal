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
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueAnyFieldNameMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueFieldNameMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValuePredicateMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueTypePredicateMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueValuePredicateMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryBaseListener;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.AttributeTypeContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.AttributeValueContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.FieldSelectorContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.FieldSelectorExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.LocaleExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.PredicateEqualityEpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.SelectorExpressionContext;
import com.liferay.portlet.dynamicdatamapping.query.impl.parser.DDMFormValuesQueryParser.StepTypeContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesQueryListener extends DDMFormValuesQueryBaseListener {

	@Override
	public void exitAttributeType(AttributeTypeContext ctx) {
		predicates.push(new DDMFormFieldValueTypePredicateMatcher());
	}

	@Override
	public void exitAttributeValue(AttributeValueContext ctx) {
		DDMFormFieldValueValuePredicateMatcher
			ddmFormFieldValueValuePredicateMatcher =
				new DDMFormFieldValueValuePredicateMatcher();

		if (languageId != null) {
			ddmFormFieldValueValuePredicateMatcher.setLocale(
				LocaleUtil.fromLanguageId(languageId));

			languageId = null;
		}

		predicates.push(ddmFormFieldValueValuePredicateMatcher);
	}

	@Override
	public void exitFieldSelector(FieldSelectorContext ctx) {
		DDMFormFieldValueMatcher ddmFormFieldValueSelector = null;

		String text = ctx.getText();

		if (text.equals(StringPool.STAR)) {
			ddmFormFieldValueSelector =
				new DDMFormFieldValueAnyFieldNameMatcher();
		}
		else {
			ddmFormFieldValueSelector = new DDMFormFieldValueFieldNameMatcher(
				text);
		}

		selector.push(ddmFormFieldValueSelector);
	}

	@Override
	public void exitFieldSelectorExpression(
		FieldSelectorExpressionContext ctx) {

		DDMFormFieldValueMatcher ddmFormFieldValueSelector = selector.peek();

		if (!predicates.isEmpty()) {
			ddmFormFieldValueSelector.setDDMFormFieldValuePredicateMatcher(
				predicates.pop());
		}
	}

	@Override
	public void exitLocaleExpression(LocaleExpressionContext ctx) {
		languageId = StringUtil.unquote(ctx.getChild(1).getText());
	}

	@Override
	public void exitPredicateEqualityEpression(
		PredicateEqualityEpressionContext ctx) {

		DDMFormFieldValuePredicateMatcher matcher = predicates.peek();

		matcher.setValue(StringUtil.unquote(ctx.getChild(3).getText()));
	}

	@Override
	public void exitSelectorExpression(SelectorExpressionContext ctx) {
		DDMFormFieldValueMatcher ddmFormFieldValueMatcher = selector.peek();

		if (stepType.equals(StringPool.DOUBLE_SLASH)) {
			ddmFormFieldValueMatcher.setGready(true);
		}
//		else {
//			ddmFormFieldValueTreeWalker =
//				new DDMFormFieldValueMultipleStepTreeWalker();
//		}
//
//		ddmFormFieldValueTreeWalker.setDDMFormFieldValueMacther(selector.pop());
//
//		walkers.push(ddmFormFieldValueTreeWalker);
	}

	@Override
	public void exitStepType(StepTypeContext ctx) {
		stepType = ctx.getText();
	}

	public List<DDMFormFieldValueMatcher> getDDMFormFieldValueMatchers() {
		return new LinkedList<>(selector);
	}

	private String languageId;
	private Stack<DDMFormFieldValuePredicateMatcher> predicates = new Stack<>();
	private Stack<DDMFormFieldValueMatcher> selector = new Stack<>();
	private String stepType;
//	private Stack<DDMFormFieldValueTreeWalker> walkers = new Stack<>();

}