package com.liferay.dynamic.data.mapping.form.renderer;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portlet.dynamicdatamapping.storage.DDMFormValues;

public class DDMFormRenderingContext {

	public HttpServletRequest getHttpServletRequest() {
		return _httpServletRequest;
	}

	public Locale getLocale() {
		return _locale;
	}
	
	public HttpServletResponse getHttpServletResponse() {
		return _httpServletResponse;
	}
	
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		_httpServletRequest = httpServletRequest;
	}

	public void setHttpServletResponse(
		HttpServletResponse httpServletResponse) {

		_httpServletResponse = httpServletResponse;
	}
	
	public void setLocale(Locale locale) {
		_locale = locale;
	}
	
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private Locale _locale;
	private DDMFormValues _ddmFormValues = new DDMFormValues(null);
	private String _portletNamespace;
	
	public void setPortletNamespace(String portletNamespace) {
		_portletNamespace = portletNamespace;
	}

	public String getPortletNamespace() {
		return _portletNamespace;
	}

	public DDMFormValues getDDMFormValues() {
		return _ddmFormValues;
	}
	
	public void setDDMFormValues(DDMFormValues ddmFormValues) {
		_ddmFormValues = ddmFormValues;
	}
	
}
