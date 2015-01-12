package com.liferay.portal.search;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liferay on 12/2/2014.
 */
public class IndexStatusMessageSenderImpl extends BaseStatusMessageSender
	implements IndexStatusMessageSender {

	public void sendStatusMessage(
		String portletId, int totalPortlets, int currentPortlet) {

		Map<String, Serializable> contextMap =
			new HashMap<String, Serializable>();

		contextMap.put("portlet", portletId);
		contextMap.put("total", totalPortlets);
		contextMap.put("current", currentPortlet);

		sendStatusMessage(new StatusMessageContext("reindex", contextMap));
	}

}
