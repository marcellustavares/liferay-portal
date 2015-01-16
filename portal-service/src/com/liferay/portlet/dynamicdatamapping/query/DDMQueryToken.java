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

import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Adolfo Pérez
 */
public class DDMQueryToken {

	public static final DDMQueryToken CHILD_OF = new DDMQueryToken(
		DDMQueryTokenType.CHILD_OF, StringPool.SLASH);

	public static final DDMQueryToken CLOSE_BRACKET = new DDMQueryToken(
		DDMQueryTokenType.CLOSE_BRACKET, StringPool.CLOSE_BRACKET);

	public static final DDMQueryToken COMMA = new DDMQueryToken(
		DDMQueryTokenType.COMMA, StringPool.COMMA);

	public static final DDMQueryToken EQUAL = new DDMQueryToken(
		DDMQueryTokenType.EQUAL, StringPool.EQUAL);

	public static final DDMQueryToken OPEN_BRACKET = new DDMQueryToken(
		DDMQueryTokenType.OPEN_BRACKET, StringPool.OPEN_BRACKET);

	public static final DDMQueryToken STAR = new DDMQueryToken(
		DDMQueryTokenType.STAR, StringPool.STAR);

	public static DDMQueryToken createIdentifier(String s) {
		return new DDMQueryToken(DDMQueryTokenType.IDENTIFIER, s);
	}

	public static DDMQueryToken createString(String s) {
		return new DDMQueryToken(DDMQueryTokenType.STRING, s);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DDMQueryToken)) {
			return false;
		}

		DDMQueryToken ddmQueryToken = (DDMQueryToken)obj;

		return _ddmQueryTokenType.equals(ddmQueryToken._ddmQueryTokenType) &&
			_content.equals(ddmQueryToken._content);
	}

	public String getContent() {
		return _content;
	}

	public DDMQueryTokenType getTokenType() {
		return _ddmQueryTokenType;
	}

	@Override
	public int hashCode() {
		int hash = 31 + _ddmQueryTokenType.hashCode();

		return hash * 31 + _content.hashCode();
	}

	protected DDMQueryToken(
		DDMQueryTokenType ddmQueryTokenType, String content) {

		_ddmQueryTokenType = ddmQueryTokenType;
		_content = content;
	}

	private final String _content;
	private final DDMQueryTokenType _ddmQueryTokenType;

}