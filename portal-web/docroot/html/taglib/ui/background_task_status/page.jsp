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

<%@ include file="/html/taglib/ui/background_task_status/init.jsp" %>

BT <%= backgroundTask %>
BTD <%= backgroundTaskDisplay %>

<strong class="background-task-status-<%= BackgroundTaskConstants.getStatusLabel(backgroundTask.getStatus()) %> <%= BackgroundTaskConstants.getStatusCssClass(backgroundTask.getStatus()) %> label">
	<liferay-ui:message key="<%= backgroundTask.getStatusLabel() %>" />
</strong>

<c:if test="<%= backgroundTask.isInProgress() %>">
	<c:if test="<%= backgroundTaskDisplay.hasBackgroundTaskStatus() %>">
		<div class="active progress progress-striped">
		<c:choose>
			<c:when test="<%= backgroundTaskDisplay.hasPercentage() %>">

				<%
				int percentage = backgroundTaskDisplay.getPercentage();
				%>

				<div class="progress-bar" style="width: <%= percentage %>%;">
					<%= percentage + StringPool.PERCENT %>
				</div>
			</c:when>
			<c:otherwise>
				<div class="progress-bar" style="width: 100%;">
			</c:otherwise>
		</c:choose>
		</div>

		<c:if test="<%= backgroundTaskDisplay.hasMessage() %>">
			<div class="progress-current-item">
				<liferay-ui:message key="<%= backgroundTaskDisplay.getMessage(locale) %>" localizeKey="<%= false %>" />
			</div>
		</c:if>
	</c:if>
</c:if>

<c:if test="<%= Validator.isNotNull(backgroundTask.getStatusMessage()) %>">

	<%
	long[] expandedBackgroundTaskIds = StringUtil.split(GetterUtil.getString(SessionClicks.get(request, "com.liferay.exportimport.web_backgroundTaskIds", null)), 0L);
	%>

	<a class="details-link toggler-header-<%= ArrayUtil.contains(expandedBackgroundTaskIds, backgroundTask.getBackgroundTaskId()) ? "expanded" : "collapsed" %>" data-persist-id="<%= backgroundTask.getBackgroundTaskId() %>" href="#"><liferay-ui:message key="details" /></a>

	<div class="background-task-status-message toggler-content-<%= ArrayUtil.contains(expandedBackgroundTaskIds, backgroundTask.getBackgroundTaskId()) ? "expanded" : "collapsed" %>">
	
		<c:choose>
			<c:when test="<%= !backgroundTaskDisplay.hasDetails() %>">
				<div class="alert <%= backgroundTask.getStatus() == BackgroundTaskConstants.STATUS_FAILED ? "alert-danger" : StringPool.BLANK %> publish-error">
					<liferay-ui:message arguments="<%= backgroundTask.getStatusMessage() %>" key="unable-to-execute-process-x" translateArguments="<%= false %>" />
				</div>
			</c:when>
			<c:otherwise>
				<%= backgroundTaskDisplay.getDetailsBody(locale) %>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>