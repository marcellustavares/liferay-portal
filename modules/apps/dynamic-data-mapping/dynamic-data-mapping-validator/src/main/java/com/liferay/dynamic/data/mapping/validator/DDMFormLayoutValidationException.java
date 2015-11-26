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

package com.liferay.dynamic.data.mapping.validator;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;

import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@ProviderType
public class DDMFormLayoutValidationException extends PortalException {

	public DDMFormLayoutValidationException() {
	}

	public DDMFormLayoutValidationException(String msg) {
		super(msg);
	}

	public DDMFormLayoutValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DDMFormLayoutValidationException(Throwable cause) {
		super(cause);
	}

	public static class MustNotDuplicateFieldName
		extends DDMFormLayoutValidationException {

		public MustNotDuplicateFieldName(Set<String> duplicatedFieldNames) {
			super(
				String.format(
					"Field names %s were defined more than once",
						duplicatedFieldNames));

			_duplicatedFieldNames = duplicatedFieldNames;
		}

		public Set<String> getDuplicatedFieldNames() {
			return _duplicatedFieldNames;
		}

		private final Set<String> _duplicatedFieldNames;

	}

}