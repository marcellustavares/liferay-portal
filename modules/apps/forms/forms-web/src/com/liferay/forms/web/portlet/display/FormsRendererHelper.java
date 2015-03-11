package com.liferay.forms.web.portlet.display;

import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
@Component(immediate = true, service = {FormsRendererHelper.class})
public class FormsRendererHelper {

	public String render(long recordSetId, String portletNamespace, Locale locale) {
		try {
			DDLRecordSet recordSet = DDLRecordSetLocalServiceUtil.getRecordSet(
				recordSetId);

			DDMStructure ddmStructure =
				DDMStructureLocalServiceUtil.getDDMStructure(
					recordSet.getDDMStructureId());

			DDMFormRenderingContext ddmFormRenderingContext =
				new DDMFormRenderingContext();

			ddmFormRenderingContext.setPortletNamespace(portletNamespace);
			ddmFormRenderingContext.setLocale(locale);

			return _ddmFormRenderer.render(
				ddmStructure.getDDMForm(), ddmStructure.getDDMFormLayout(),
				ddmFormRenderingContext);
		}
		catch (Exception e) {
			return StringPool.BLANK;
		}
	}

	@Reference(service = DDMFormRenderer.class, unbind = "-")
	protected void setDDMFormRenderer(DDMFormRenderer ddmFormRenderer) {
		_ddmFormRenderer = ddmFormRenderer;
	}

	private static DDMFormRenderer _ddmFormRenderer;

}