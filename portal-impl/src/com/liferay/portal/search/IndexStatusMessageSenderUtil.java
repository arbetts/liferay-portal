package com.liferay.portal.search;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

/**
 * Created by liferay on 12/2/2014.
 */
public class IndexStatusMessageSenderUtil {

	public static void sendStatusMessage(
		String portletId, int totalPortlets, int currentPortlet) {

		_indexStatusMessageSender.sendStatusMessage(
			portletId, totalPortlets, currentPortlet);
	}

	public void setIndexStatusMessageSender(
		IndexStatusMessageSender indexStatusMessageSender) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_indexStatusMessageSender = indexStatusMessageSender;
	}

	private static IndexStatusMessageSender _indexStatusMessageSender;
}
