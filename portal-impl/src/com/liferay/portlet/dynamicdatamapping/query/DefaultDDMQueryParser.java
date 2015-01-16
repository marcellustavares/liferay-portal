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

package com.liferay.portlet.dynamicdatamapping.query;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portlet.dynamicdatamapping.model.Value;

import java.io.IOException;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * @author Adolfo Pérez
 */
public class DefaultDDMQueryParser implements DDMQueryParser {

	public DefaultDDMQueryParser(DDMQueryTokenizer tokenizer) {
		_tokenizer = tokenizer;
	}

	@Override
	public DDMQuery parse() throws DDMQuerySyntaxErrorException, IOException {
		DDMQueryMatcher ddmQueryMatcher = _parse();

		return new DDMQueryImpl(ddmQueryMatcher);
	}

	private String _expectToken(
			DDMQueryToken token, DDMQueryTokenType expectedTokenType)
		throws DDMQuerySyntaxErrorException {

		if (token.getTokenType() != expectedTokenType) {
			throw new DDMQuerySyntaxErrorException(
				String.format(
					"found token %s with value %s; expected token of type %s",
					token, token.getContent(), expectedTokenType));
		}

		return token.getContent();
	}

	private String _expectToken(DDMQueryTokenType expectedTokenType)
		throws DDMQuerySyntaxErrorException, IOException {

		DDMQueryToken token = _nextToken();

		if (token == null) {
			throw new DDMQuerySyntaxErrorException(
				String.format(
					"premature EOF found; expected token of type %s",
					expectedTokenType));
		}

		return _expectToken(token, expectedTokenType);
	}

	private DDMQueryToken _nextToken() throws IOException {
		DDMQueryToken ddmQueryToken = _tokenizer.nextToken();

		if (_firstToken && (ddmQueryToken == DDMQueryToken.CHILD_OF)) {
			_firstToken = false;

			return _tokenizer.nextToken();
		}

		return ddmQueryToken;
	}

	private DDMQueryMatcher _parse()
		throws DDMQuerySyntaxErrorException, IOException {

		DDMQueryToken token = _nextToken();

		if (token == null) {
			return new IdentityDDMQueryMatcher();
		}
		else if (token == DDMQueryToken.CHILD_OF) {
			return new MoveToChildrenDDMQueryMatcher(_parse());
		}
		else if (token == DDMQueryToken.STAR) {
			return new AnyDescendantDDMQueryMatcher(_parse());
		}
		else if (token.getTokenType() == DDMQueryTokenType.IDENTIFIER) {
			return new FieldNameDDMQueryMatcher(
				token.getContent(), _parseFieldExpression());
		}
		else {
			throw new DDMQuerySyntaxErrorException(
				String.format(
					"Unexpected token %s with value %s found",
					token.getTokenType(), token.getContent()));
		}
	}

	private DDMQueryMatcher _parseFieldExpression()
		throws DDMQuerySyntaxErrorException, IOException {

		DDMQueryToken token = _nextToken();

		if (token == null) {
			return new IdentityDDMQueryMatcher();
		}

		if (token == DDMQueryToken.CHILD_OF) {
			return new MoveToChildrenDDMQueryMatcher(_parse());
		}

		_expectToken(token, DDMQueryTokenType.OPEN_BRACKET);

		String attributeName = _expectToken(DDMQueryTokenType.IDENTIFIER);

		if (!ArrayUtil.contains(_SUPPORTED_ATTRIBUTE_NAMES, "value")) {
			throw new DDMQuerySyntaxErrorException(
				String.format(
					"unsupported attribute name '%s'; expected one of %s",
					attributeName,
					Arrays.toString(_SUPPORTED_ATTRIBUTE_NAMES)));
		}

		_expectToken(DDMQueryTokenType.EQUAL);

		final String expectedAttributeValue = _expectToken(
			DDMQueryTokenType.STRING);

		_expectToken(DDMQueryTokenType.CLOSE_BRACKET);

		PredicateFilter<Value> filter = new PredicateFilter<Value>() {

			@Override
			public boolean filter(Value value) {
				Map<Locale, String> values = value.getValues();

				for (Map.Entry<Locale, String> entry : values.entrySet()) {
					if (entry.getValue().equals(expectedAttributeValue)) {
						return true;
					}
				}

				return false;
			}

		};

		return new ValueAttributeDDMQueryMatcher(filter, _parse());
	}

	private static final String[] _SUPPORTED_ATTRIBUTE_NAMES = { "value" };

	private boolean _firstToken = true;
	private final DDMQueryTokenizer _tokenizer;

}