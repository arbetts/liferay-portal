<%@ page
		import="com.liferay.portal.search.backgroundtask.IndexBackgroundTaskExecutor" %>
<%@ page
		import="com.liferay.portlet.backgroundtask.util.comparator.BackgroundTaskComparatorFactoryUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ProgressTracker" %>
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

<%@ include file="/html/portlet/admin/init.jsp" %>

<%
Runtime runtime = Runtime.getRuntime();

numberFormat = NumberFormat.getInstance(locale);

long totalMemory = runtime.totalMemory();
long usedMemory = totalMemory - runtime.freeMemory();
long backgroundTaskId = 0;


String	orderByCol =  "create-date";
String 	orderByType =  "desc";

OrderByComparator<BackgroundTask> orderByComparator =
	BackgroundTaskComparatorFactoryUtil.getBackgroundTaskOrderByComparator(
		orderByCol, orderByType);

List<BackgroundTask> backgroundTasks =
	BackgroundTaskLocalServiceUtil.getBackgroundTasks(
		themeDisplay.getCompanyGroupId(),
		IndexBackgroundTaskExecutor.class.getName(), QueryUtil.ALL_POS,
		QueryUtil.ALL_POS, orderByComparator);

	boolean inProgress = false;
if (!backgroundTasks.isEmpty()) {
	BackgroundTask backgroundTask = backgroundTasks.get(0);

	inProgress = backgroundTask.isInProgress();
	backgroundTaskId = backgroundTask.getBackgroundTaskId();

}
%>

<div>
	<portlet:resourceURL var="totalMemoryChartURL">
		<portlet:param name="struts_action" value="/admin_server/view_chart" />
		<portlet:param name="type" value="total" />
		<portlet:param name="totalMemory" value="<%= String.valueOf(totalMemory) %>" />
		<portlet:param name="usedMemory" value="<%= String.valueOf(usedMemory) %>" />
	</portlet:resourceURL>

	<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="memory-used-vs-total-memory" />" src="<%= totalMemoryChartURL %>" />

	<portlet:resourceURL var="maxMemoryChartURL">
		<portlet:param name="struts_action" value="/admin_server/view_chart" />
		<portlet:param name="type" value="max" />
		<portlet:param name="maxMemory" value="<%= String.valueOf(runtime.maxMemory()) %>" />
		<portlet:param name="usedMemory" value="<%= String.valueOf(usedMemory) %>" />
	</portlet:resourceURL>

	<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="memory-used-vs-max-memory" />" src="<%= maxMemoryChartURL %>" />
</div>

<br />

<table class="lfr-table">
<tr>
	<td>
		<liferay-ui:message key="used-memory" />:
	</td>
	<td>
		<%= numberFormat.format(usedMemory) %> <liferay-ui:message key="bytes" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="total-memory" />:
	</td>
	<td>
		<%= numberFormat.format(runtime.totalMemory()) %> <liferay-ui:message key="bytes" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="maximum-memory" />:
	</td>
	<td>
		<%= numberFormat.format(runtime.maxMemory()) %> <liferay-ui:message key="bytes" />
	</td>
</tr>
</table>

<br />

<liferay-ui:panel-container extended="<%= true %>" id="adminServerAdministrationActionsPanelContainer" persistState="<%= true %>">
	<liferay-ui:panel collapsible="<%= true %>" extended="<%= true %>" id="adminServerAdministrationActionsPanel" persistState="<%= true %>" title="actions">
		<table class="table table-condensed table-hover">
		<tr>
			<td>
				<liferay-ui:message key="run-the-garbage-collector-to-free-up-memory" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="gc" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-content-cached-by-this-vm" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="cacheSingle" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-content-cached-across-the-cluster" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="cacheMulti" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-the-database-cache" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="cacheDb" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clear-the-direct-servlet-cache" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="cacheServlet" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="reindex-all-search-indexes" />
				<div id="myProgressBar"></div>
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="reindex" value="execute" />
				<aui:button id="myButton" value="Myexecute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="reindex-all-spell-check-indexes" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="reindexDictionaries" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="reset-preview-and-thumbnail-files-for-documents-and-media-portlet" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="dlPreviews" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="generate-thread-dump" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="threadDump" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="verify-database-tables-of-all-plugins" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="verifyPluginTables" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="verify-membership-policies" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="verifyMembershipPolicies" value="execute" />
			</td>
		</tr>
		<tr>
			<td>
				<liferay-ui:message key="clean-up-permissions" /> <liferay-ui:icon-help message="clean-up-permissions-help" />
			</td>
			<td>
				<aui:button cssClass="save-server-button" data-cmd="cleanUpPermissions" value="execute" />
			</td>
		</tr>
		</table>
	</liferay-ui:panel>
</liferay-ui:panel-container>

<aui:script use="aui-progressbar">

	var myProgressBar;

	var isInProgress = <%=inProgress%>;

	function _createProgressBar() {

		myProgressBar = new Y.ProgressBar(
			{
				boundingBox: '#myProgressBar',
				max: 100,
				min: 0,
				on: {
					complete: function(e) {
						this.set('label', '<liferay-ui:message key="complete" />');
					},
					valueChange: function(e) {
						this.set('label', e.newVal + '%');
					}
				},
			}
		).render();
	}

	var checkBackgroundTaskStatus = function() {
		Liferay.Service(
			'/backgroundtask/get-background-task-status-json',
			{
				backgroundTaskId: '<%= backgroundTaskId %>',
			},
			function(json) {
				var total = json.total;
				var current = json.current;
				var portlet = json.portlet;

				if (current == total) {
					isInProgress = false;
				}

				var value = parseInt(current / total * 100);

				myProgressBar.value(value);

				checkBackgroundTaskStatus();
			}
		);
	};

	function _submitForm() {
	// call form submit, disable button
	}

	A.one("#myButton").on('click', function(event){

		_submitForm();

		_createProgressBar();

	});

	if (isInProgress) {

	_disableButton();

	_createProgressBar();

	}

</aui:script>