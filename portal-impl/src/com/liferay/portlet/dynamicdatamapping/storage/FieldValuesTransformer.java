/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.kernel.util.Transformer;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class FieldValuesTransformer implements Transformer<List<Serializable>> {

	public FieldValuesTransformer(String type) {
		_type = type;
	}

	public List<Serializable> transform(Object object) {
		List<Serializable> serializables = new ArrayList<Serializable>();

		if (object instanceof String[]) {
			String[] values = (String[])object;

			for (String value : values) {
				Serializable serializable = FieldConstants.getSerializable(
					_type, value);

				serializables.add(serializable);
			}
		}
		else {
			Serializable serializable = FieldConstants.getSerializable(
				_type, String.valueOf(object));

			serializables.add(serializable);
		}

		return serializables;
	}

	private String _type;

}