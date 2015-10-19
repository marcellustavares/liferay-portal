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

package com.liferay.xsl.content.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Raymond Augé
 */
@Meta.OCD(
	id = "com.liferay.xsl.content.web.configuration.XSLContentConfiguration",
	localization = "content/Language"
)
public interface XSLContentConfiguration {

	@Meta.AD(deflt = "@portlet_context_url@")
	public String validUrlPrefixes();

	@Meta.AD(deflt = "false")
	public boolean xmlDoctypeDeclarationAllowed();

	@Meta.AD(deflt = "false")
	public boolean xmlExternalGeneralEntitiesAllowed();

	@Meta.AD(deflt = "false")
	public boolean xmlExternalParameterEntitiesAllowed();

	@Meta.AD(deflt = "true")
	public boolean xslSecureProcessingEnabled();

}