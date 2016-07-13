/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.model.Value;

import java.util.Locale;
import java.util.Optional;

/**
 * @author Marcellus Tavares
 */
public class ValueStringMapper {

	public static Optional<String> apply(
		Optional<Value> valueOptional, Locale locale) {

		if (!valueOptional.isPresent()) {
			return Optional.empty();
		}

		Value value = valueOptional.get();

		return Optional.of(value.getString(locale));
	}

}
