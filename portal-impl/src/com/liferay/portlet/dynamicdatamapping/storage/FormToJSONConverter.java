package com.liferay.portlet.dynamicdatamapping.storage;

import com.liferay.portlet.dynamicdatamapping.forms.Form;
import com.liferay.portlet.dynamicdatamapping.forms.FormField;
import com.liferay.portlet.dynamicdatamapping.forms.FormPage;
import com.liferay.portlet.dynamicdatamapping.forms.FormSection;
import com.liferay.portlet.dynamicdatamapping.forms.SectionLayout;

import java.util.List;
public class FormToJSONConverter {
	public String convert(Form layout) {
		_stringBuilder = new StringBuilder();

		_stringBuilder.append("{\"pages\":");
		convert(layout.getPages());
		_stringBuilder.append('}');

		return _stringBuilder.toString();
	}

	protected void convert(FormField field) {
		_stringBuilder.append("{}");
	}

	protected void convert(FormPage page) {
		_stringBuilder.append("{\"sections\":");
		convert(page.getSections());
		_stringBuilder.append('}');
	}

	protected void convert(FormSection section) {
		_stringBuilder.append("{\"layout\":");
		convert(section.getLayout());
		_stringBuilder.append(",\"fields\":");
		convert(section.getFields());
		_stringBuilder.append('}');
	}

	protected <T> void convert(List<T> list) {
		_stringBuilder.append('[');
		boolean firstItem = true;

		for (T item : list) {
			if (!firstItem) {
				_stringBuilder.append(',');
			}

			convert(item);
			firstItem = false;
		}

		_stringBuilder.append(']');
	}

	protected void convert(Object obj) {
		throw new IllegalArgumentException(
			"Unrecognized type: " + obj.getClass().getName());
	}

	protected void convert(SectionLayout sectionLayout) {
		_stringBuilder.append("{}");
	}

	private StringBuilder _stringBuilder;
}