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

package com.liferay.portlet.dynamicdatamapping.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.storage.Field;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eduardo Lundgren
 * @author Brian Wing Shun Chan
 */
public interface DDMXSD {

	public String getFieldHTML(
			HttpServletRequest request, HttpServletResponse response,
			Element element, Fields fields, String portletNamespace,
			String namespace, String mode, boolean readOnly, Locale locale)
		throws Exception;

	public String getFieldHTMLByName(
			HttpServletRequest request, HttpServletResponse response,
			long classNameId, long classPK, String fieldName, Fields fields,
			String portletNamespace, String namespace, String mode,
			boolean readOnly, Locale locale)
		throws Exception;

	public String getHTML(
			HttpServletRequest request, HttpServletResponse response,
			DDMStructure ddmStructure, Fields fields, String portletNamespace,
			String namespace, boolean readOnly, Locale locale)
		throws Exception;

	public String getHTML(
			HttpServletRequest request, HttpServletResponse response,
			DDMTemplate ddmTemplate, Fields fields, String portletNamespace,
			String namespace, boolean readOnly, Locale locale)
		throws Exception;

	public String getHTML(
			HttpServletRequest request, HttpServletResponse response,
			String xml, Fields fields, String portletNamespace, Locale locale)
		throws Exception;

	public String getHTML(
			HttpServletRequest request, HttpServletResponse response,
			String xml, Fields fields, String portletNamespace,
			String namespace, boolean readOnly, Locale locale)
		throws Exception;

	public String getHTML(
			HttpServletRequest request, HttpServletResponse response,
			String xml, Fields fields, String portletNamespace,
			String namespace, Locale locale)
		throws Exception;

	public String getHTML(
			HttpServletRequest request, HttpServletResponse response,
			String xml, Fields fields, String portletNamespace,
			String namespace, String mode, boolean readOnly, Locale locale)
		throws Exception;

	public String getHTML(
			HttpServletRequest request, HttpServletResponse response,
			String xml, String portletNamespace, Locale locale)
		throws Exception;

	public JSONArray getJSONArray(DDMStructure structure, String xsd)
		throws PortalException;

	public JSONArray getJSONArray(Document document) throws PortalException;

	public JSONArray getJSONArray(Element element) throws PortalException;

	public JSONArray getJSONArray(String xml) throws PortalException;

	public String getSimpleFieldHTML(
			HttpServletRequest request, HttpServletResponse response,
			Element element, Field field, String portletNamespace,
			String namespace, String mode, boolean readOnly, Locale locale)
		throws Exception;

	public String getSimpleFieldHTMLByName(
			HttpServletRequest request, HttpServletResponse response,
			long classNameId, long classPK, Field field,
			String portletNamespace, String namespace, String mode,
			boolean readOnly, Locale locale)
		throws Exception;

	public String getXSD(long classNameId, long classPK) throws PortalException;

}