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

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Adolfo Pérez
 */
public class DefaultDDMQueryTokenizer implements DDMQueryTokenizer {

	public static final String IDENTIFIER_DELIMITERS = "*/],=[ ";

	public static final String STRING_DELIMITERS = StringPool.APOSTROPHE;

	public DefaultDDMQueryTokenizer(Reader reader) {
		_reader = reader;
	}

	@Override
	public DDMQueryToken nextToken() throws IOException {
		_skipBlanks();

		int c = _readOne();

		if (c == _EOF) {
			return null;
		}
		else if (c == CharPool.SLASH) {
			return DDMQueryToken.CHILD_OF;
		}
		else if (c == CharPool.CLOSE_BRACKET) {
			return DDMQueryToken.CLOSE_BRACKET;
		}
		else if (c == CharPool.COMMA) {
			return DDMQueryToken.COMMA;
		}
		else if (c == CharPool.EQUAL) {
			return DDMQueryToken.EQUAL;
		}
		else if (c == CharPool.STAR) {
			return DDMQueryToken.STAR;
		}
		else if (c == CharPool.APOSTROPHE) {
			return DDMQueryToken.createString(_readString());
		}
		else if (c == CharPool.OPEN_BRACKET) {
			return DDMQueryToken.OPEN_BRACKET;
		}
		else {
			_bufferedChar = c;

			return DDMQueryToken.createIdentifier(_readIdentifier());
		}
	}

	private String _readDelimitedString(String delimiters, boolean skipLast)
		throws IOException {

		StringBundler sb = new StringBundler();

		int c = _readOne();

		while ((c != _EOF) && (delimiters.indexOf((char)c) == -1)) {
			sb.append((char)c);
			c = _readOne();
		}

		if ((c != _EOF) && !skipLast) {
			_bufferedChar = c;
		}

		return sb.toString();
	}

	private String _readIdentifier() throws IOException {
		return _readDelimitedString(IDENTIFIER_DELIMITERS, false);
	}

	private int _readOne() throws IOException {
		if (_bufferedChar != _NONE) {
			int c = _bufferedChar;
			_bufferedChar = _NONE;

			return c;
		}

		return _reader.read();
	}

	private String _readString() throws IOException {
		return _readDelimitedString(STRING_DELIMITERS, true);
	}

	private void _skipBlanks() throws IOException {
		int c = _readOne();

		while ((c != _EOF) && Character.isWhitespace((char)c)) {
			c = _reader.read();
		}

		if (c != _EOF) {
			_bufferedChar = c;
		}
	}

	private static final int _EOF = -1;

	private static final int _NONE = -1;

	private int _bufferedChar = _EOF;
	private final Reader _reader;

}