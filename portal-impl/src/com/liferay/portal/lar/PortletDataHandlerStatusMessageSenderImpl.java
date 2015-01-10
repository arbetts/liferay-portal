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

package com.liferay.portal.lar;

import com.liferay.portal.kernel.lar.ManifestSummary;
import com.liferay.portal.kernel.lar.PortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataHandlerStatusMessageSender;
import com.liferay.portal.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerRegistryUtil;
import com.liferay.portal.kernel.util.LongWrapper;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.search.BaseStatusMessageSender;
import com.liferay.portal.search.StatusMessageContext;
import com.liferay.portal.service.PortletLocalServiceUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 */
public class PortletDataHandlerStatusMessageSenderImpl
	extends BaseStatusMessageSender
	implements PortletDataHandlerStatusMessageSender {

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #sendStatusMessage(String,
	 *             String[], ManifestSummary)}
	 */
	@Deprecated
	@Override
	public void sendStatusMessage(
		String messageType, ManifestSummary manifestSummary) {

		sendStatusMessage(messageType, (String[])null, manifestSummary);
	}

	@Override
	public void sendStatusMessage(
		String messageType, String portletId, ManifestSummary manifestSummary) {

		StatusMessageContext statusMessageContext = createStatusMessageContext(
			messageType, manifestSummary);

		statusMessageContext.put("portletId", portletId);

		Portlet portlet = PortletLocalServiceUtil.getPortletById(portletId);

		if (portlet != null) {
			PortletDataHandler portletDataHandler =
				portlet.getPortletDataHandlerInstance();

			long portletModelAdditionCountersTotal =
				portletDataHandler.getExportModelCount(manifestSummary);

			if (portletModelAdditionCountersTotal < 0) {
				portletModelAdditionCountersTotal = 0;
			}

			statusMessageContext.put(
				"portletModelAdditionCountersTotal",
				portletModelAdditionCountersTotal);
		}

		sendStatusMessage(statusMessageContext);
	}

	@Override
	public void sendStatusMessage(
		String messageType, String[] portletIds,
		ManifestSummary manifestSummary) {

		StatusMessageContext statusMessageContext = createStatusMessageContext(
			messageType, manifestSummary);

		statusMessageContext.put("portletIds", portletIds);

		sendStatusMessage(statusMessageContext);
	}

	@Override
	public <T extends StagedModel> void sendStatusMessage(
		String messageType, T stagedModel, ManifestSummary manifestSummary) {

		StatusMessageContext statusMessageContext = createStatusMessageContext(
			messageType, manifestSummary);

		StagedModelDataHandler<T> stagedModelDataHandler =
			(StagedModelDataHandler<T>)
				StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(
					stagedModel.getModelClassName());

		statusMessageContext.put(
			"stagedModelName",
			stagedModelDataHandler.getDisplayName(stagedModel));

		statusMessageContext.put(
			"stagedModelType",
			String.valueOf(stagedModel.getStagedModelType()));
		statusMessageContext.put("uuid", stagedModel.getUuid());

		sendStatusMessage(statusMessageContext);
	}

	protected StatusMessageContext createStatusMessageContext(
		String messageType, ManifestSummary manifestSummary) {

		Map<String, LongWrapper> modelAdditionCounters =
			manifestSummary.getModelAdditionCounters();

		Map<String, Serializable> contextMap = new HashMap<String, Serializable>();

		contextMap.put(
			"modelAdditionCounters",
			new HashMap<String, LongWrapper>(modelAdditionCounters));

		Map<String, LongWrapper> modelDeletionCounters =
			manifestSummary.getModelDeletionCounters();

		contextMap.put(
			"modelDeletionCounters",
			new HashMap<String, LongWrapper>(modelDeletionCounters));

		return new StatusMessageContext(messageType, contextMap);
	}

}