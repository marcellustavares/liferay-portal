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

package com.liferay.dynamic.data.lists.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 *
 * @author Lino Alves
 *
 */

@Meta.OCD(
		id = "com.liferay.dynamic.data.lists.configuration.DDLServiceConfiguration",
		name= "Dynamic Data Lists Configuration"
	)
public interface DDLServiceConfiguration {

	/**
	 * Set the default display view.
	 *
	 */
	@Meta.AD(
			deflt = "list",
			name= "Default display view",
			required = false
	)
	public String defaultDisplayView();

	/**
	 * Set the storage type that will be used to store the dynamic data lists
	 * records.
	 *
	 */
	@Meta.AD(
			deflt = "json", 
			name="Storage type used to store dynamic data lists records",
			required = false
	)
	public String storageType();

	/**
	 * Set the list of supported display views.
	 *
	 */
	@Meta.AD(
			deflt = "descriptive | list",
			name= "Supported dislay view",
			required = false
	)
	public String[] displayViews();

}
