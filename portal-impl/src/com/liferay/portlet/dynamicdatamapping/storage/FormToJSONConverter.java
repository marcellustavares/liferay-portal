package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portlet.dynamicdatamapping.forms.Form;
import com.liferay.portlet.dynamicdatamapping.forms.FormField;
import com.liferay.portlet.dynamicdatamapping.forms.FormPage;
import com.liferay.portlet.dynamicdatamapping.forms.FormSection;
import com.liferay.portlet.dynamicdatamapping.forms.SectionLayout;

import java.util.List;
public class FormToJSONConverter {
	public String convert(Form layout) {
		JSONObject root = JSONFactoryUtil.createJSONObject();
		root.put("pages", convert(layout.getPages()));
		return root.toString();
	}

	protected JSONObject convert(FormField field) {
		_stringBuilder.append("{}");
		JSONObject fieldJSON = JSONFactoryUtil.createJSONObject();
		return fieldJSON;
	}

	protected JSONObject convert(FormPage page) {
		JSONObject pageJSON = JSONFactoryUtil.createJSONObject();
		pageJSON.put("sections", convert(page.getSections()));
		return pageJSON;
	}

	protected JSONObject convert(FormSection section) {
		JSONObject sectionJSON = JSONFactoryUtil.createJSONObject();
		sectionJSON.put("layout", convert(section.getLayout()));
		sectionJSON.put("fields", convert(section.getFields()));
		return sectionJSON;
	}

	protected <T> JSONArray convert(List<T> list) {
		JSONArray array = JSONFactoryUtil.createJSONArray();

		for (T item : list) {
			array.put(convert(item));
		}

		return array;
	}

	protected JSONObject convert(Object obj) {
		throw new IllegalArgumentException(
			"Unrecognized type: " + obj.getClass().getName());
	}

	protected JSONObject convert(SectionLayout sectionLayout) {
		JSONObject layoutJSON = JSONFactoryUtil.createJSONObject();
		return layoutJSON;
	}

	private StringBuilder _stringBuilder;
}