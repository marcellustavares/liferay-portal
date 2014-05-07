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

package com.liferay.portlet.dynamicdatamapping.storage;

/**
 * @author Pablo Carvalho
 */
public class FormMetadataJSON {
	public FormMetadataJSON(String layoutJSON, String structureJSON) {
		_layoutJSON = layoutJSON;
		_structureJSON = structureJSON;
	}

	public String getLayoutJSON() {
		return _layoutJSON;
	}

	public String getStructureJSON() {
		return _structureJSON;
	}

	public void setLayoutJSON(String layoutJSON) {
		_layoutJSON = layoutJSON;
	}

	public void setStructureJSON(String structureJSON) {
		_structureJSON = structureJSON;
	}

	private String _layoutJSON;
	private String _structureJSON;
}