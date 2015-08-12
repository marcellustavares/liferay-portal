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

package com.liferay.dynamic.data.mapping.service.permission;

import com.liferay.dynamic.data.mapping.constants.DDMActionKeys;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.util.DDMStructurePermissionSupport;
import com.liferay.dynamic.data.mapping.util.DDMTemplatePermissionSupport;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTrackerCustomizer;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = DDMPermissionSupportTracker.class)
public class DDMPermissionSupportTracker {

	public DDMStructurePermissionSupportWrapper
			getDDMStructurePermissionSupportWrapper(long classNameId)
		throws PortalException {

		String className = PortalUtil.getClassName(classNameId);

		if (!_ddmStructurePermissionSupportWrapperMap.containsKey(className)) {
			throw new PortalException(
				"The model does not support DDMStructure permission checking " +
					className);
		}

		return _ddmStructurePermissionSupportWrapperMap.get(className);
	}

	public DDMTemplatePermissionSupportWrapper
			getDDMTemplatePermissionSupportWrapper(long resourceClassNameId)
		throws PortalException {

		String className = PortalUtil.getClassName(resourceClassNameId);

		if (!_ddmTemplatePermissionSupportWrapperMap.containsKey(className)) {
			throw new PortalException(
				"The model does not support DDMTemplate permission checking " +
					className);
		}

		return _ddmTemplatePermissionSupportWrapperMap.get(className);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_structurePermissionSuportServiceTrackerMap =
			ServiceTrackerCollections.singleValueMap(
				DDMStructurePermissionSupport.class, "model.class.name",
				new DDMStructurePermissionSupportServiceTrackerCustomizer());

		_structurePermissionSuportServiceTrackerMap.open();

		_templatePermissionSupportServiceTrackerMap =
			ServiceTrackerCollections.singleValueMap(
				DDMTemplatePermissionSupport.class, "model.class.name",
				new DDMTemplatePermissionSupportServiceTrackerCustomizer());

		_templatePermissionSupportServiceTrackerMap.open();
	}

	protected DDMStructurePermissionSupportWrapper
		createDDMStructurePermissionSupportWrapper(
			String modelClassName,
			DDMStructurePermissionSupport ddmStructurePermissionSupport,
			Map<String, Object> properties) {

		DDMStructurePermissionSupportWrapper structurePermissionSupportWrapper =
			new DDMStructurePermissionSupportWrapper(
				modelClassName, ddmStructurePermissionSupport);

		String addActionId = MapUtil.getString(
			properties, "add.structure.action.id", DDMActionKeys.ADD_STRUCTURE);

		structurePermissionSupportWrapper.setAddActionId(addActionId);

		boolean defaultModelResourceName = MapUtil.getBoolean(
			properties, "default.model.resource.name");

		structurePermissionSupportWrapper.setDefaultModelResourceName(
			defaultModelResourceName);

		return structurePermissionSupportWrapper;
	}

	protected DDMTemplatePermissionSupportWrapper
		createDDMTemplatePermissionSupportWrapper(
			String modelClassName,
			DDMTemplatePermissionSupport ddmTemplatePermissionSupport,
			Map<String, Object> properties) {

		DDMTemplatePermissionSupportWrapper templatePermissionSupportWrapper =
			new DDMTemplatePermissionSupportWrapper(
				modelClassName, ddmTemplatePermissionSupport);

		String addActionId = MapUtil.getString(
			properties, "add.template.action.id", DDMActionKeys.ADD_TEMPLATE);

		templatePermissionSupportWrapper.setAddActionId(addActionId);

		boolean defaultModelResourceName = MapUtil.getBoolean(
			properties, "default.model.resource.name");

		templatePermissionSupportWrapper.setDefaultModelResourceName(
			defaultModelResourceName);

		return templatePermissionSupportWrapper;
	}

	@Deactivate
	protected void deactivate() {
		_structurePermissionSuportServiceTrackerMap.close();

		_templatePermissionSupportServiceTrackerMap.close();
	}

	protected static class DDMStructurePermissionSupportWrapper {

		public DDMStructurePermissionSupportWrapper(
			String modelClassName,
			DDMStructurePermissionSupport ddmStructurePermissionSupport) {

			_modelClassName = modelClassName;
			_ddmStructurePermissionSupport = ddmStructurePermissionSupport;
		}

		public String getAddActionId() {
			return _addActionId;
		}

		public String getModelResourceName() {
			if (_defaultModelResourceName) {
				return DDMStructure.class.getName();
			}

			StringBundler sb = new StringBundler(3);

			sb.append(_modelClassName);
			sb.append(ResourceActionsUtil.getCompositeModelNameSeparator());
			sb.append(DDMStructure.class.getName());

			return sb.toString();
		}

		public String getPortletResourceName() {
			return _ddmStructurePermissionSupport.getResourceName();
		}

		public void setAddActionId(String addActionId) {
			_addActionId = addActionId;
		}

		public void setDefaultModelResourceName(
			boolean defaultModelResourceName) {

			_defaultModelResourceName = defaultModelResourceName;
		}

		private String _addActionId;
		private final DDMStructurePermissionSupport
			_ddmStructurePermissionSupport;
		private boolean _defaultModelResourceName;
		private String _modelClassName;

	}

	protected class DDMTemplatePermissionSupportWrapper {

		public DDMTemplatePermissionSupportWrapper(
			String modelClassName,
			DDMTemplatePermissionSupport ddmTemplatePermissionSupport) {

			_modelClassName = modelClassName;
			_ddmTemplatePermissionSupport = ddmTemplatePermissionSupport;
		}

		public String getAddActionId() {
			return _addActionId;
		}

		public String getModelResourceName() {
			if (_defaultModelResourceName) {
				return DDMTemplate.class.getName();
			}

			StringBundler sb = new StringBundler(3);

			sb.append(_modelClassName);
			sb.append(ResourceActionsUtil.getCompositeModelNameSeparator());
			sb.append(DDMTemplate.class.getName());

			return sb.toString();
		}

		public String getPortletResourceName(long classNameId) {
			return _ddmTemplatePermissionSupport.getResourceName(classNameId);
		}

		public void setAddActionId(String addActionId) {
			_addActionId = addActionId;
		}

		public void setDefaultModelResourceName(
			boolean defaultModelResourceName) {

			_defaultModelResourceName = defaultModelResourceName;
		}

		private String _addActionId;
		private final DDMTemplatePermissionSupport
			_ddmTemplatePermissionSupport;
		private boolean _defaultModelResourceName;
		private String _modelClassName;

	}

	private final ConcurrentMap<String, DDMStructurePermissionSupportWrapper>
		_ddmStructurePermissionSupportWrapperMap = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, DDMTemplatePermissionSupportWrapper>
		_ddmTemplatePermissionSupportWrapperMap = new ConcurrentHashMap<>();
	private ServiceTrackerMap<String, DDMStructurePermissionSupport>
		_structurePermissionSuportServiceTrackerMap;
	private ServiceTrackerMap<String, DDMTemplatePermissionSupport>
		_templatePermissionSupportServiceTrackerMap;

	private class DDMStructurePermissionSupportServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<DDMStructurePermissionSupport, DDMStructurePermissionSupport> {

		@Override
		public DDMStructurePermissionSupport addingService(
			ServiceReference<DDMStructurePermissionSupport> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			DDMStructurePermissionSupport ddmStructurePermissionSupport =
				registry.getService(serviceReference);

			Map<String, Object> properties = serviceReference.getProperties();

			String modelClassName = MapUtil.getString(
				properties, "model.class.name");

			DDMStructurePermissionSupportWrapper
				structurePermissionSupportWrapper =
					createDDMStructurePermissionSupportWrapper(
						modelClassName, ddmStructurePermissionSupport,
						properties);

			_ddmStructurePermissionSupportWrapperMap.putIfAbsent(
				modelClassName, structurePermissionSupportWrapper);

			return ddmStructurePermissionSupport;
		}

		@Override
		public void modifiedService(
			ServiceReference<DDMStructurePermissionSupport> serviceReference,
			DDMStructurePermissionSupport ddmStructurePermissionSupport) {
		}

		@Override
		public void removedService(
			ServiceReference<DDMStructurePermissionSupport> serviceReference,
			DDMStructurePermissionSupport ddmStructurePermissionSupport) {

			_ddmStructurePermissionSupportWrapperMap.remove(
				(String)serviceReference.getProperty("model.class.name"));

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

	}

	private class DDMTemplatePermissionSupportServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<DDMTemplatePermissionSupport, DDMTemplatePermissionSupport> {

		@Override
		public DDMTemplatePermissionSupport addingService(
			ServiceReference<DDMTemplatePermissionSupport> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			DDMTemplatePermissionSupport ddmTemplatePermissionSupport =
				registry.getService(serviceReference);

			Map<String, Object> properties = serviceReference.getProperties();

			String modelClassName = MapUtil.getString(
				properties, "model.class.name");

			DDMTemplatePermissionSupportWrapper
				ddmTemplatePermissionSupportWrapper =
					createDDMTemplatePermissionSupportWrapper(
						modelClassName, ddmTemplatePermissionSupport,
						properties);

			_ddmTemplatePermissionSupportWrapperMap.putIfAbsent(
				modelClassName, ddmTemplatePermissionSupportWrapper);

			return ddmTemplatePermissionSupport;
		}

		@Override
		public void modifiedService(
			ServiceReference<DDMTemplatePermissionSupport> serviceReference,
			DDMTemplatePermissionSupport DDMTemplatePermissionSupport) {
		}

		@Override
		public void removedService(
			ServiceReference<DDMTemplatePermissionSupport> serviceReference,
			DDMTemplatePermissionSupport DDMTemplatePermissionSupport) {

			_ddmTemplatePermissionSupportWrapperMap.remove(
				(String)serviceReference.getProperty("model.class.name"));

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

	}

}