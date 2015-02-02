package com.liferay.portal.search;

/**
 * Created by liferay on 1/9/2015.
 */
public interface IndexStatusMessageSender {

	public void sendStatusMessage(
		String portletId, int totalPortlets, int currentPortlet);

	public void sendStatusMessage(String indexStatus);

}
