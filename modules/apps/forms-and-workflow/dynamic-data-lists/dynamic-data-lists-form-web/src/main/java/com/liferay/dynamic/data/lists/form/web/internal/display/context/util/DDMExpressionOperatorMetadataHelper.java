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

package com.liferay.dynamic.data.lists.form.web.internal.display.context.util;

import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Rafael Praxedes
 */
public class DDMExpressionOperatorMetadataHelper {

	public DDMExpressionOperatorMetadataHelper(ResourceBundle resourceBundle) {
		_resourceBundle = resourceBundle;

		init();
	}

	public List<DDMExpressionOperatorMetadata>
		getDDMExpressionOperatorMetadataList() {

		return _ddmExpressionFunctionMetadataList;
	}

	public static class DDMExpressionOperatorMetadata {

		public DDMExpressionOperatorMetadata(
			String name, String label, String returnType,
			String[] parameterTypes) {

			_name = name;
			_label = label;
			_returnType = returnType;
			_parameterTypes = parameterTypes;
		}

		public String getLabel() {
			return _label;
		}

		public String getName() {
			return _name;
		}

		public String[] getParameterTypes() {
			return _parameterTypes;
		}

		public String getReturnType() {
			return _returnType;
		}

		private final String _label;
		private final String _name;
		private final String[] _parameterTypes;
		private final String _returnType;

	}

	protected void init() {
		List<DDMExpressionOperatorMetadata> ddmExpressionFunctionMetadataList =
			new ArrayList<>();

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"equals-to", LanguageUtil.get(_resourceBundle, "is-equal-to"),
				_TYPE_BOOLEAN, new String[] {_TYPE_TEXT, _TYPE_TEXT}));

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"not-equals-to",
				LanguageUtil.get(_resourceBundle, "is-not-equal-to"),
				_TYPE_BOOLEAN, new String[] {_TYPE_TEXT, _TYPE_TEXT}));

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"contains", LanguageUtil.get(_resourceBundle, "contains"),
				_TYPE_BOOLEAN, new String[] {_TYPE_TEXT, _TYPE_TEXT}));

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"not-contains",
				LanguageUtil.get(_resourceBundle, "does-not-contain"),
				_TYPE_BOOLEAN, new String[] {_TYPE_TEXT, _TYPE_TEXT}));

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"greater-than",
				LanguageUtil.get(_resourceBundle, "is-greater-than"),
				_TYPE_BOOLEAN, new String[] {_TYPE_NUMBER, _TYPE_NUMBER}));

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"greater-than-equals",
				LanguageUtil.get(
					_resourceBundle, "is-greater-than-or-equal-to"),
				_TYPE_BOOLEAN, new String[] {_TYPE_NUMBER, _TYPE_NUMBER}));

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"less-than", LanguageUtil.get(_resourceBundle, "is-less-than"),
				_TYPE_BOOLEAN, new String[] {_TYPE_NUMBER, _TYPE_NUMBER}));

		ddmExpressionFunctionMetadataList.add(
			new DDMExpressionOperatorMetadata(
				"less-than-equals",
				LanguageUtil.get(_resourceBundle, "is-less-than-or-equal-to"),
				_TYPE_BOOLEAN, new String[] {_TYPE_NUMBER, _TYPE_NUMBER}));

		_ddmExpressionFunctionMetadataList = ddmExpressionFunctionMetadataList;
	}

	private static final String _TYPE_BOOLEAN = "boolean";

	private static final String _TYPE_NUMBER = "number";

	private static final String _TYPE_TEXT = "text";

	private List<DDMExpressionOperatorMetadata>
		_ddmExpressionFunctionMetadataList;
	private final ResourceBundle _resourceBundle;

}