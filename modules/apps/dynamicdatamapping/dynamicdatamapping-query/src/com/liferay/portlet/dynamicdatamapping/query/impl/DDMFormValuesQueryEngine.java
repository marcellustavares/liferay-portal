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

package com.liferay.portlet.dynamicdatamapping.query.impl;

import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueAnyFieldNameMatcher;
import com.liferay.portlet.dynamicdatamapping.query.impl.model.DDMFormFieldValueMatcher;
import com.liferay.portlet.dynamicdatamapping.storage.DDMFormFieldValue;

/**
 * @author Marcellus Tavares
 */
public class DDMFormValuesQueryEngine {

	public DDMFormValuesQueryEngine(
		DDMFormValuesQueryEngineContext ddmFormValuesQueryEngineContext,
		DDMFormFieldValue ddmFormFieldValue) {

		_ddmFormValuesQueryEngineContext = ddmFormValuesQueryEngineContext;
		_ddmFormFieldValue = ddmFormFieldValue;
		_depth = 0;
	}

	public DDMFormValuesQueryEngine(
		DDMFormValuesQueryEngineContext ddmFormFieldValueEngineContext,
		DDMFormFieldValue ddmFormFieldValue, int depth) {

		_ddmFormValuesQueryEngineContext = ddmFormFieldValueEngineContext;
		_ddmFormFieldValue = ddmFormFieldValue;
		_depth = depth;
	}

	public void run() {
		DDMFormFieldValueMatcher ddmFormFieldValueMatcher =
			_ddmFormValuesQueryEngineContext.getDDMFormFieldValueMatcher(
				_depth);

		if (ddmFormFieldValueMatcher.isGready()) {
			if (ddmFormFieldValueMatcher.match(_ddmFormFieldValue)) {
				if (!_ddmFormValuesQueryEngineContext.isLastDDMFormFieldValueMatcher(_depth)) {
					_ddmFormFieldValueMatchListener.onMatch(_ddmFormFieldValue);

					if (ddmFormFieldValueMatcher instanceof DDMFormFieldValueAnyFieldNameMatcher) {
						for (DDMFormFieldValue ddmFormFieldValue :
								_ddmFormFieldValue.getNestedDDMFormFieldValues()) {

							DDMFormValuesQueryEngine ddmFormFieldValueEngine =
								new DDMFormValuesQueryEngine(
									_ddmFormValuesQueryEngineContext,
									ddmFormFieldValue, _depth);

							ddmFormFieldValueEngine.setDDMFormFieldValueMatchListener(
								_ddmFormFieldValueMatchListener);

							ddmFormFieldValueEngine.run();
						}
					}
				}
				else {
					for (DDMFormFieldValue ddmFormFieldValue :
							_ddmFormFieldValue.getNestedDDMFormFieldValues()) {

						DDMFormValuesQueryEngine ddmFormFieldValueEngine =
							new DDMFormValuesQueryEngine(
								_ddmFormValuesQueryEngineContext,
								ddmFormFieldValue, _depth + 1);

						ddmFormFieldValueEngine.setDDMFormFieldValueMatchListener(
							_ddmFormFieldValueMatchListener);

						ddmFormFieldValueEngine.run();
					}
				}
			}
			else {
				for (DDMFormFieldValue ddmFormFieldValue :
						_ddmFormFieldValue.getNestedDDMFormFieldValues()) {

					DDMFormValuesQueryEngine ddmFormFieldValueEngine =
						new DDMFormValuesQueryEngine(
							_ddmFormValuesQueryEngineContext, ddmFormFieldValue,
							_depth);

					ddmFormFieldValueEngine.setDDMFormFieldValueMatchListener(
						_ddmFormFieldValueMatchListener);

					ddmFormFieldValueEngine.run();
				}
			}
		}
		else {
			if (ddmFormFieldValueMatcher.match(_ddmFormFieldValue)) {
				if (!_ddmFormValuesQueryEngineContext.isLastDDMFormFieldValueMatcher(_depth)) {
					_ddmFormFieldValueMatchListener.onMatch(_ddmFormFieldValue);
				}
				else {
					for (DDMFormFieldValue ddmFormFieldValue :
							_ddmFormFieldValue.getNestedDDMFormFieldValues()) {

						DDMFormValuesQueryEngine ddmFormFieldValueEngine =
							new DDMFormValuesQueryEngine(
								_ddmFormValuesQueryEngineContext,
								ddmFormFieldValue, _depth + 1);

						ddmFormFieldValueEngine.setDDMFormFieldValueMatchListener(
							_ddmFormFieldValueMatchListener);

						ddmFormFieldValueEngine.run();
					}
				}
			}
			else {

				// TODO

			}
		}

	}

	public void setDDMFormFieldValueMatchListener(
		DDMFormFieldValueMatchListener ddmFormFieldValueMatchListener) {

		_ddmFormFieldValueMatchListener = ddmFormFieldValueMatchListener;
	}

	private DDMFormFieldValue _ddmFormFieldValue;
	private DDMFormFieldValueMatchListener _ddmFormFieldValueMatchListener;
	private DDMFormValuesQueryEngineContext _ddmFormValuesQueryEngineContext;
	private int _depth;

}