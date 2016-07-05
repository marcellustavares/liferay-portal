package com.liferay.dynamic.data.lists.form.web.display.context;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.liferay.dynamic.data.lists.form.web.configuration.DDLFormWebConfiguration;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.service.DDLRecordLocalService;
import com.liferay.dynamic.data.lists.service.DDLRecordSetService;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.servlet.DDMFormEvaluatorServlet;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.values.factory.DDMFormValuesFactory;
import com.liferay.dynamic.data.mapping.io.DDMFormFieldTypesJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.StorageEngine;
import com.liferay.dynamic.data.mapping.util.DDMFormValuesMerger;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowEngineManager;

import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Adam Brandizzi
 */
@PrepareForTest({ParamUtil.class, PortalUtil.class})
@RunWith(PowerMockRunner.class)
public class DDLFormAdminDisplayContextTest {

	@Before
	public void setUp() throws PortalException {
		setUpMocks();
		setUpPortalUtil();
		setUpHttpServletRequest();
	}

	@Test
	public void testDefaultLanguageId() throws PortalException {
		DDLFormAdminDisplayContext displayContext = getDisplayContext();
		Locale locale = getNoDefaultLocale();

		setUpThemeDisplaySiteDefaultLocale(locale);

		String languageId = displayContext.getDefaultLanguageId();

		Assert.assertEquals(LocaleUtil.toLanguageId(locale), languageId);
	}

	@Test
	public void testGetAvailableLanguageIdsJSONArray() throws PortalException {
		DDLFormAdminDisplayContext displayContext = getDisplayContext();
		Locale locale = getNoDefaultLocale();

		setUpThemeDisplaySiteDefaultLocale(locale);

		JSONArray locales = displayContext.getAvailableLanguageIdsJSONArray();

		Assert.assertEquals(1, locales.length());
		Assert.assertEquals(LocaleUtil.toLanguageId(locale), locales.get(0));
	}

	@Test
	public void testGetAvailableLanguageIdsJSONArrayFromRecordSet()
		throws PortalException {

		DDLFormAdminDisplayContext displayContext = getDisplayContext();
		Locale locale = getNoDefaultLocale();

		setUpDDLRecordSet();
		setUpDDMStructureLocales(locale);

		JSONArray languageIds =
			displayContext.getAvailableLanguageIdsJSONArray();

		Assert.assertEquals(1, languageIds.length());
		Assert.assertEquals(
			LocaleUtil.toLanguageId(locale), languageIds.get(0));
	}

	@Test
	public void testGetLanguageIdFromRecordSet() throws PortalException {
		DDLFormAdminDisplayContext displayContext = getDisplayContext();
		Locale locale = getNoDefaultLocale();

		setUpDDLRecordSet();
		setUpDDMStructureLocales(locale);

		String languageId = displayContext.getDefaultLanguageId();

		Assert.assertEquals(LocaleUtil.toLanguageId(locale), languageId);
	}

	protected DDLFormAdminDisplayContext getDisplayContext() {
		return new DDLFormAdminDisplayContext(
			_renderRequest, _renderResponse, _ddlFormWebConfiguration,
			_ddlRecordLocalService, _ddlRecordSetService,
			_ddmDataProviderInstanceLocalService, _ddmFormEvaluatorServlet,
			_ddmFormFieldTypeServicesTracker, _ddmFormFieldTypesJSONSerializer,
			_ddmFormJSONSerializer, _ddmFormLayoutJSONSerializer,
			_ddmFormRenderer, _ddmFormValuesFactory, _ddmFormValuesMerger,
			_ddmStructureLocalService, _jsonFactory, _storageEngine,
			_workflowEngineManager);
	}

	protected Locale getNoDefaultLocale() {
		Locale locale = LocaleUtil.BRAZIL;

		if (locale == LocaleUtil.getDefault()) {
			locale = LocaleUtil.SPAIN;
		}

		return locale;
	}

	protected void setUpDDLRecordSet() throws PortalException {
		long recordSetId = RandomTestUtil.randomLong();

		when(
			_renderRequest.getParameter("recordSetId")
		).thenReturn(
			String.valueOf(recordSetId)
		);

		when(
			_ddlRecordSetService.fetchRecordSet(recordSetId)
		).thenReturn(
			_ddlRecordSet
		);

		when(
			_ddlRecordSet.getDDMStructure()
		).thenReturn(
			_ddmStructure
		);
	}

	protected void setUpDDMStructureLocales(
		Locale defaultLocale, Locale... locales) {

		String languageId = LocaleUtil.toLanguageId(defaultLocale);

		when(
			_ddmStructure.getDefaultLanguageId()
		).thenReturn(
			languageId
		);

		String[] languageIds = new String[] {languageId};

		for (Locale locale : locales) {
			ArrayUtil.append(languageIds, LocaleUtil.toLanguageId(locale));
		}

		when(
			_ddmStructure.getAvailableLanguageIds()
		).thenReturn(
			languageIds
		);
	}

	protected void setUpHttpServletRequest() {
		when(
			_httpServletRequestRequest.getAttribute(WebKeys.THEME_DISPLAY)
		).thenReturn(
			_themeDisplay
		);
	}

	protected void setUpMocks() throws PortalException {
		_httpServletRequestRequest = mock(HttpServletRequest.class);
		_ddlFormWebConfiguration = mock(DDLFormWebConfiguration.class);
		_ddlRecordLocalService = mock(DDLRecordLocalService.class);
		_ddlRecordSet = mock(DDLRecordSet.class);
		_ddlRecordSetService = mock(DDLRecordSetService.class);
		_ddmDataProviderInstanceLocalService = mock(
			DDMDataProviderInstanceLocalService.class);
		_ddmFormEvaluatorServlet = mock(DDMFormEvaluatorServlet.class);
		_ddmFormFieldTypeServicesTracker = mock(
			DDMFormFieldTypeServicesTracker.class);
		_ddmFormFieldTypesJSONSerializer = mock(
			DDMFormFieldTypesJSONSerializer.class);
		_ddmFormJSONSerializer = mock(DDMFormJSONSerializer.class);
		_ddmFormLayoutJSONSerializer = mock(DDMFormLayoutJSONSerializer.class);
		_ddmFormRenderer = mock(DDMFormRenderer.class);
		_ddmFormValuesFactory = mock(DDMFormValuesFactory.class);
		_ddmFormValuesMerger = mock(DDMFormValuesMerger.class);
		_ddmStructure = mock(DDMStructure.class);
		_ddmStructureLocalService = mock(DDMStructureLocalService.class);
		_renderRequest = mock(RenderRequest.class);
		_renderResponse = mock(RenderResponse.class);
		_storageEngine = mock(StorageEngine.class);
		_themeDisplay = mock(ThemeDisplay.class);
		_workflowEngineManager = mock(WorkflowEngineManager.class);
	}

	protected void setUpPortalUtil() {
		mockStatic(PortalUtil.class);
		when(
			PortalUtil.getHttpServletRequest(_renderRequest)
		).thenReturn(
			_httpServletRequestRequest
		);
	}

	protected void setUpThemeDisplaySiteDefaultLocale(Locale locale) {
		when(
			_themeDisplay.getSiteDefaultLocale()
		).thenReturn(
			locale
		);
	}

	private DDLFormWebConfiguration _ddlFormWebConfiguration;
	private DDLRecordLocalService _ddlRecordLocalService;
	private DDLRecordSet _ddlRecordSet;
	private DDLRecordSetService _ddlRecordSetService;
	private DDMDataProviderInstanceLocalService
		_ddmDataProviderInstanceLocalService;
	private DDMFormEvaluatorServlet _ddmFormEvaluatorServlet;
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;
	private DDMFormFieldTypesJSONSerializer _ddmFormFieldTypesJSONSerializer;
	private DDMFormJSONSerializer _ddmFormJSONSerializer;
	private DDMFormLayoutJSONSerializer _ddmFormLayoutJSONSerializer;
	private DDMFormRenderer _ddmFormRenderer;
	private DDMFormValuesFactory _ddmFormValuesFactory;
	private DDMFormValuesMerger _ddmFormValuesMerger;
	private DDMStructure _ddmStructure;
	private DDMStructureLocalService _ddmStructureLocalService;
	private HttpServletRequest _httpServletRequestRequest;
	private final JSONFactory _jsonFactory = new JSONFactoryImpl();
	private RenderRequest _renderRequest;
	private RenderResponse _renderResponse;
	private StorageEngine _storageEngine;
	private ThemeDisplay _themeDisplay;
	private WorkflowEngineManager _workflowEngineManager;

}