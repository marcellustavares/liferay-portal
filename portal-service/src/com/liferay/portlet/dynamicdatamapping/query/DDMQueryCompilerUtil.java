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

/**
 * @author Adolfo Pérez
 */
public class DDMQueryCompilerUtil {

	public static DDMQuery compile(String ddmQuery)
		throws DDMQuerySyntaxErrorException, IOException {

		return getDDMQueryCompiler().compile(ddmQuery);
	}

	public static DDMQueryCompiler getDDMQueryCompiler() {
		return _ddmQueryCompiler;
	}

	public void setDDMQueryCompiler(DDMQueryCompiler ddmQueryCompiler) {
		_ddmQueryCompiler = ddmQueryCompiler;
	}

	private static DDMQueryCompiler _ddmQueryCompiler;

}