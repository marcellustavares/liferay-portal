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

package com.liferay.asset.categories.admin.web.internal.exportimport.data.handler;

import com.liferay.asset.kernel.model.AssetCategoryConstants;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portlet.asset.util.AssetVocabularySettingsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Praxedes
 */
public class AssetVocabularySettingsImportHelper
	extends AssetVocabularySettingsHelper {

	public AssetVocabularySettingsImportHelper(
		String propertiesString,
		JSONObject classNameClassNameIdMapperJSONObject,
		ClassNameLocalService classNameLocalService) {

		super(propertiesString);

		_classNameClassNameIdMapperJSONObject =
			classNameClassNameIdMapperJSONObject;
		_classNameLocalService = classNameLocalService;
	}

	public String getUpdatedSettings() {
		updateSettings();

		return super.toString();
	}

	protected boolean existClassName(long oldClassNameId) {
		String oldClassName = _classNameClassNameIdMapperJSONObject.getString(
			String.valueOf(oldClassNameId));

		if (_classNameLocalService.fetchClassName(oldClassName) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	protected void fillClassNameIdsAndClassTypePKs(
		String[] classNameIdsAndClassTypePKs, boolean required,
		List<Long> newClassNameIds, List<Long> newClassTypePKs,
		List<Boolean> newRequireds) {

		for (String classNameIdAndClassTypePK : classNameIdsAndClassTypePKs) {
			long classNameId = getClassNameId(classNameIdAndClassTypePK);
			long classTypePK = getClassTypePK(classNameIdAndClassTypePK);

			if (classNameId != AssetCategoryConstants.ALL_CLASS_NAME_ID) {
				if (!existClassName(classNameId)) {
					continue;
				}

				classNameId = getNewClassNameId(classNameId);
			}

			newClassNameIds.add(classNameId);
			newClassTypePKs.add(classTypePK);
			newRequireds.add(required);
		}
	}

	protected long getNewClassNameId(long oldClassNameId) {
		if (oldClassNameId == AssetCategoryConstants.ALL_CLASS_NAME_ID) {
			return AssetCategoryConstants.ALL_CLASS_NAME_ID;
		}

		String oldClassName = _classNameClassNameIdMapperJSONObject.getString(
			String.valueOf(oldClassNameId));

		return _classNameLocalService.getClassNameId(oldClassName);
	}

	protected void updateSettings() {
		List<Long> newClassNameIds = new ArrayList<>();
		List<Long> newClassTypePKs = new ArrayList<>();
		List<Boolean> newRequireds = new ArrayList<>();

		fillClassNameIdsAndClassTypePKs(
			getClassNameIdsAndClassTypePKs(), false, newClassNameIds,
			newClassTypePKs, newRequireds);

		fillClassNameIdsAndClassTypePKs(
			getRequiredClassNameIdsAndClassTypePKs(), true, newClassNameIds,
			newClassTypePKs, newRequireds);

		long[] newClassNameIdsArray = ArrayUtil.toArray(
			newClassNameIds.toArray(new Long[newClassNameIds.size()]));

		long[] newClassTypePKsArray = ArrayUtil.toArray(
			newClassTypePKs.toArray(new Long[newClassTypePKs.size()]));

		boolean[] newRequiredsArray = ArrayUtil.toArray(
			newRequireds.toArray(new Boolean[newRequireds.size()]));

		setClassNameIdsAndClassTypePKs(
			newClassNameIdsArray, newClassTypePKsArray, newRequiredsArray);
	}

	private final JSONObject _classNameClassNameIdMapperJSONObject;
	private final ClassNameLocalService _classNameLocalService;

}