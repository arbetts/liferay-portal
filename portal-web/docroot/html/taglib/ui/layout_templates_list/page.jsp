<%--
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
--%>

<%@ include file="/html/taglib/init.jsp" %>

<%
String layoutTemplateId = (String)request.getAttribute("liferay-ui:layout-templates-list:layoutTemplateId");
String layoutTemplateIdPrefix = (String)request.getAttribute("liferay-ui:layout-templates-list:layoutTemplateIdPrefix");
List<LayoutTemplate> layoutTemplates = (List<LayoutTemplate>)request.getAttribute("liferay-ui:layout-templates-list:layoutTemplates");
%>

<aui:fieldset>
	<aui:row cssClass="lfr-page-layouts">
		<ul>

			<%
			layoutTemplates = PluginUtil.restrictPlugins(layoutTemplates, user);

			for (int i = 0; i < layoutTemplates.size(); i++) {
				LayoutTemplate layoutTemplate = layoutTemplates.get(i);
			%>

				<li class="lfr-layout-template radio">
					<label for="<portlet:namespace /><%= layoutTemplateIdPrefix + "layoutTemplateId" + i %>">
						<aui:input checked="<%= layoutTemplateId.equals(layoutTemplate.getLayoutTemplateId()) %>" id='<%= layoutTemplateIdPrefix + "layoutTemplateId" + i %>' label="" name="layoutTemplateId" type="radio" value="<%= layoutTemplate.getLayoutTemplateId() %>" wrappedField="<%= true %>" />

						<img alt="" class="layout-template-entry modify-link <%= layoutTemplateId.equals(layoutTemplate.getLayoutTemplateId()) ? "layout-selected" : StringPool.BLANK %>" height="25" onclick="document.getElementById('<portlet:namespace /><%= layoutTemplateIdPrefix + "layoutTemplateId" + i %>').checked = true;" src="<%= layoutTemplate.getStaticResourcePath() %><%= HtmlUtil.escapeAttribute(layoutTemplate.getThumbnailPath()) %>" width="25" />

						<span><%= HtmlUtil.escape(layoutTemplate.getName(locale)) %></span>
					</label>
				</li>

			<%
			}
			%>

		</ul>
	</aui:row>
</aui:fieldset>