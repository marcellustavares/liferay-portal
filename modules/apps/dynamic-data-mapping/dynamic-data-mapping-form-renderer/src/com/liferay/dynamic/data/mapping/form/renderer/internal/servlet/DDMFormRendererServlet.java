package com.liferay.dynamic.data.mapping.form.renderer.internal.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;

@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/ddm-form-renderer-servlet",
		"osgi.http.whiteboard.servlet.name=Dynamic Data Mapping Form Renderer Servlet",
		"osgi.http.whiteboard.servlet.pattern=/ddm-form-renderer-servlet/*"
	},
	service = Servlet.class
)
public class DDMFormRendererServlet extends HttpServlet {

	@Override
	protected void doGet(
			HttpServletRequest req, HttpServletResponse resp)
			
		throws ServletException, IOException {
		
		try {
		long recordSetId = ParamUtil.getLong(req, "recordSetId");
		
		StringBundler sb = new StringBundler();
		
		sb.append("<html><head>");
		sb.append("<link class=\"lfr-css-file\" href=\"/o/frontend-theme-classic-web/classic/css/aui.css?browserId=other&themeId=classic&languageId=en_US&b=7000&t=14\" rel=\"stylesheet\" type=\"text/css\" />");
		sb.append("</head>");
		sb.append("<body style=\"padding:20px;\">");
		
		DDLRecordSet ddlRecordSet = DDLRecordSetLocalServiceUtil.getRecordSet(recordSetId);
		
		DDMStructure ddmStructure = ddlRecordSet.getDDMStructure();
		
		DDMFormRenderingContext ddmFormRenderingContext = new DDMFormRenderingContext();
		
		ddmFormRenderingContext.setHttpServletRequest(req);
		ddmFormRenderingContext.setHttpServletResponse(resp);
		ddmFormRenderingContext.setLocale(LocaleUtil.US);;
		ddmFormRenderingContext.setPortletNamespace("");
		
		sb.append(_ddmFormRenderer.render(ddmStructure.getDDMForm(), ddmStructure.getDDMFormLayout(), ddmFormRenderingContext));
		sb.append("</body>");
		sb.append("</html>");
		
		
		
		resp.setContentType(ContentTypes.TEXT_HTML);
		
		ServletResponseUtil.write(resp, sb.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	@Reference
	protected void setDDMFormRenderer(DDMFormRenderer ddmFormRenderer) {
		_ddmFormRenderer = ddmFormRenderer;
	}
	
	private DDMFormRenderer _ddmFormRenderer;
}
