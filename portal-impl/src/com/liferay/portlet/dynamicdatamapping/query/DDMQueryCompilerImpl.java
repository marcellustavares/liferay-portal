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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Adolfo Pérez
 */
public class DDMQueryCompilerImpl implements DDMQueryCompiler {

	@Override
	public DDMQuery compile(String ddmQuery)
		throws DDMQuerySyntaxErrorException, IOException {

		Reader reader = new StringReader(ddmQuery);
		DDMQueryTokenizer tokenizer = new DefaultDDMQueryTokenizer(reader);
		DDMQueryParser parser = new DefaultDDMQueryParser(tokenizer);

		return parser.parse();
	}

}