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

package com.liferay.portal.kernel.bean;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class BeanPropertiesUtil {

	public static void copyProperties(Object source, Object target) {
		getBeanProperties().copyProperties(source, target);
	}

	public static void copyProperties(
		Object source, Object target, Class<?> editable) {

		getBeanProperties().copyProperties(source, target, editable);
	}

	public static void copyProperties(
		Object source, Object target, String[] ignoreProperties) {

		getBeanProperties().copyProperties(source, target, ignoreProperties);
	}

	public static <T> T deepCopyProperties(Class<T> toClazz, Object from)
		throws Exception {

		T to = toClazz.newInstance();

		BeanInfo fromBean = Introspector.getBeanInfo(from.getClass());
		BeanInfo toBean = Introspector.getBeanInfo(toClazz);

		PropertyDescriptor[] fromProperties = fromBean.getPropertyDescriptors();
		PropertyDescriptor[] toProperties = toBean.getPropertyDescriptors();

		for (PropertyDescriptor toProperty : toProperties) {
			Class<?> toPropertyType = toProperty.getPropertyType();

			for (PropertyDescriptor fromProperty : fromProperties) {
				if (!toProperty.getName().equals("class") &&
					toProperty.getName().equals(fromProperty.getName())) {

					Class<?> fromPropertyType = fromProperty.getPropertyType();

					if ((toPropertyType != null) &&
						(fromPropertyType != null) &&
						(toProperty.getWriteMethod() != null) &&
						(fromProperty.getReadMethod() != null)) {

						if (isCollectionType(
								toPropertyType, fromPropertyType)) {

							copyCollection(fromProperty, toProperty, from, to);
						}
						else if (isMapType(toPropertyType, fromPropertyType)) {
							copyMap(fromProperty, toProperty, from, to);
						}
						else if (isSimpleCopy(
									toPropertyType, fromPropertyType)) {

							toProperty.getWriteMethod().invoke(
								to, fromProperty.getReadMethod().invoke(from));
						}
						else {
							Object value =
								fromProperty.getReadMethod().invoke(from);

							Object newObject = deepCopyProperties(
								toPropertyType, value);

							toProperty.getWriteMethod().invoke(to, newObject);
						}
					}

					break;
				}
			}
		}

		return to;
	}

	public static BeanProperties getBeanProperties() {
		PortalRuntimePermission.checkGetBeanProperty(BeanPropertiesUtil.class);

		return _beanProperties;
	}

	public static boolean getBoolean(Object bean, String param) {
		return getBeanProperties().getBoolean(bean, param);
	}

	public static boolean getBoolean(
		Object bean, String param, boolean defaultValue) {

		return getBeanProperties().getBoolean(bean, param, defaultValue);
	}

	public static boolean getBooleanSilent(Object bean, String param) {
		return getBeanProperties().getBooleanSilent(bean, param);
	}

	public static boolean getBooleanSilent(
		Object bean, String param, boolean defaultValue) {

		return getBeanProperties().getBooleanSilent(bean, param, defaultValue);
	}

	public static byte getByte(Object bean, String param) {
		return getBeanProperties().getByte(bean, param);
	}

	public static byte getByte(Object bean, String param, byte defaultValue) {
		return getBeanProperties().getByte(bean, param, defaultValue);
	}

	public static byte getByteSilent(Object bean, String param) {
		return getBeanProperties().getByteSilent(bean, param);
	}

	public static byte getByteSilent(
		Object bean, String param, byte defaultValue) {

		return getBeanProperties().getByteSilent(bean, param, defaultValue);
	}

	public static double getDouble(Object bean, String param) {
		return getBeanProperties().getDouble(bean, param);
	}

	public static double getDouble(
		Object bean, String param, double defaultValue) {

		return getBeanProperties().getDouble(bean, param, defaultValue);
	}

	public static double getDoubleSilent(Object bean, String param) {
		return getBeanProperties().getDoubleSilent(bean, param);
	}

	public static double getDoubleSilent(
		Object bean, String param, double defaultValue) {

		return getBeanProperties().getDoubleSilent(bean, param, defaultValue);
	}

	public static float getFloat(Object bean, String param) {
		return getBeanProperties().getFloat(bean, param);
	}

	public static float getFloat(
		Object bean, String param, float defaultValue) {

		return getBeanProperties().getFloat(bean, param, defaultValue);
	}

	public static float getFloatSilent(Object bean, String param) {
		return getBeanProperties().getFloatSilent(bean, param);
	}

	public static float getFloatSilent(
		Object bean, String param, float defaultValue) {

		return getBeanProperties().getFloatSilent(bean, param, defaultValue);
	}

	public static int getInteger(Object bean, String param) {
		return getBeanProperties().getInteger(bean, param);
	}

	public static int getInteger(Object bean, String param, int defaultValue) {
		return getBeanProperties().getInteger(bean, param, defaultValue);
	}

	public static int getIntegerSilent(Object bean, String param) {
		return getBeanProperties().getIntegerSilent(bean, param);
	}

	public static int getIntegerSilent(
		Object bean, String param, int defaultValue) {

		return getBeanProperties().getIntegerSilent(bean, param, defaultValue);
	}

	public static long getLong(Object bean, String param) {
		return getBeanProperties().getLong(bean, param);
	}

	public static long getLong(Object bean, String param, long defaultValue) {
		return getBeanProperties().getLong(bean, param, defaultValue);
	}

	public static long getLongSilent(Object bean, String param) {
		return getBeanProperties().getLongSilent(bean, param);
	}

	public static long getLongSilent(
		Object bean, String param, long defaultValue) {

		return getBeanProperties().getLongSilent(bean, param, defaultValue);
	}

	public static Object getObject(Object bean, String param) {
		return getBeanProperties().getObject(bean, param);
	}

	public static Object getObject(
		Object bean, String param, Object defaultValue) {

		return getBeanProperties().getObject(bean, param, defaultValue);
	}

	public static Object getObjectSilent(Object bean, String param) {
		return getBeanProperties().getObjectSilent(bean, param);
	}

	public static Object getObjectSilent(
		Object bean, String param, Object defaultValue) {

		return getBeanProperties().getObjectSilent(bean, param, defaultValue);
	}

	public static Class<?> getObjectType(Object bean, String param) {
		return getBeanProperties().getObjectType(bean, param);
	}

	public static Class<?> getObjectType(
		Object bean, String param, Class<?> defaultValue) {

		return getBeanProperties().getObjectType(bean, param, defaultValue);
	}

	public static Class<?> getObjectTypeSilent(Object bean, String param) {
		return getBeanProperties().getObjectType(bean, param);
	}

	public static Class<?> getObjectTypeSilent(
		Object bean, String param, Class<?> defaultValue) {

		return getBeanProperties().getObjectType(bean, param, defaultValue);
	}

	public static short getShort(Object bean, String param) {
		return getBeanProperties().getShort(bean, param);
	}

	public static short getShort(
		Object bean, String param, short defaultValue) {

		return getBeanProperties().getShort(bean, param, defaultValue);
	}

	public static short getShortSilent(Object bean, String param) {
		return getBeanProperties().getShortSilent(bean, param);
	}

	public static short getShortSilent(
		Object bean, String param, short defaultValue) {

		return getBeanProperties().getShortSilent(bean, param, defaultValue);
	}

	public static String getString(Object bean, String param) {
		return getBeanProperties().getString(bean, param);
	}

	public static String getString(
		Object bean, String param, String defaultValue) {

		return getBeanProperties().getString(bean, param, defaultValue);
	}

	public static String getStringSilent(Object bean, String param) {
		return getBeanProperties().getStringSilent(bean, param);
	}

	public static String getStringSilent(
		Object bean, String param, String defaultValue) {

		return getBeanProperties().getStringSilent(bean, param, defaultValue);
	}

	public static void setProperties(Object bean, HttpServletRequest request) {
		getBeanProperties().setProperties(bean, request);
	}

	public static void setProperty(Object bean, String param, Object value) {
		getBeanProperties().setProperty(bean, param, value);
	}

	public static void setPropertySilent(
		Object bean, String param, Object value) {

		getBeanProperties().setPropertySilent(bean, param, value);
	}

	public void setBeanProperties(BeanProperties beanProperties) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_beanProperties = beanProperties;
	}

	private static Collection<Object> copyCollection(
			Class<?> toClazz, Collection<?> from)
		throws Exception {

		Collection<Object> to = null;

		if (from == null) {
			return null;
		}

		if (from instanceof Set<?>) {
			to = new LinkedHashSet<>();
		}
		else if (from instanceof List<?>) {
			to = new ArrayList<>();
		}

		if (!from.isEmpty()) {
			for (Object obj : from) {
				if (isSimpleCopy(toClazz, obj.getClass())) {
					to.add(obj);
				}
				else {
					to.add(deepCopyProperties(toClazz, obj));
				}
			}
		}

		return to;
	}

	private static void copyCollection(
			PropertyDescriptor fromProperty, PropertyDescriptor toProperty,
			Object from, Object to)
		throws Exception {

		Object value = fromProperty.getReadMethod().invoke(from);

		if (value != null) {
			Method toMethod = toProperty.getWriteMethod();

			Type[] types = toMethod.getGenericParameterTypes();

			ParameterizedType pType = (ParameterizedType)types[0];

			Class<?> clazz = (Class<?>)pType.getActualTypeArguments()[0];

			toProperty.getWriteMethod().invoke(
				to, copyCollection(clazz, (Collection<?>)value));
		}
	}

	private static void copyMap(
			PropertyDescriptor fromProperty, PropertyDescriptor toProperty,
			Object from, Object to)
		throws Exception {

		Object value = fromProperty.getReadMethod().invoke(from);

		if (value != null) {
			Method toMethod = toProperty.getWriteMethod();

			Type[] types = toMethod.getGenericParameterTypes();

			ParameterizedType pType = (ParameterizedType)types[0];

			Class<?> keyClazz = (Class<?>)pType.getActualTypeArguments()[0];
			Class<?> valueClazz = (Class<?>)pType.getActualTypeArguments()[1];

			Map<?, ?> fromMap = (Map<?, ?>)value;
			Map<Object, Object> toMap = new HashMap<>();

			for (Object key : fromMap.keySet()) {
				Object toKey = null;
				Object toValue = null;

				Object mapValue = fromMap.get(key);

				if (isSimpleCopy(keyClazz, key.getClass())) {
					toKey = key;
				}
				else {
					toKey = deepCopyProperties(keyClazz, key);
				}

				if (isSimpleCopy(valueClazz, mapValue.getClass())) {
					toValue = mapValue;
				}
				else {
					toValue = deepCopyProperties(valueClazz, mapValue);
				}

				toMap.put(toKey, toValue);
			}

			toProperty.getWriteMethod().invoke(to, toMap);
		}
	}

	private static boolean isCollectionType(
		Class<?> toClazz, Class<?> fromClazz) {

		return ((toClazz.equals(Set.class) || toClazz.equals(List.class)) &&
			(fromClazz.equals(Set.class) || fromClazz.equals(List.class)));
	}

	private static boolean isMapType(Class<?> toClazz, Class<?> fromClazz) {
		return toClazz.equals(Map.class) && fromClazz.equals(Map.class);
	}

	private static boolean isSimpleCopy(Class<?> toClazz, Class<?> fromClazz) {
		return toClazz.isAssignableFrom(fromClazz) &&
			(fromClazz.isPrimitive() || fromClazz.equals(String.class) ||
			fromClazz.equals(Long.class) || fromClazz.equals(Integer.class) ||
			fromClazz.equals(Double.class) || fromClazz.equals(Float.class) ||
			fromClazz.equals(Boolean.class) || fromClazz.equals(Locale.class));
	}

	private static BeanProperties _beanProperties;

}