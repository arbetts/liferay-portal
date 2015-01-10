package com.liferay.portal.search;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskThreadLocal;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSender;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by liferay on 12/2/2014.
 */
public class BaseStatusMessageSender implements StatusMessageSender {

	public void sendStatusMessage(
		StatusMessageContext statusMessageContext) {

		if (!BackgroundTaskThreadLocal.hasBackgroundTask()) {
			return;
		}

		Message message = createStatusMessage(statusMessageContext);

		_singleDestinationMessageSender.send(message);
	}

	public Message createStatusMessage(
		StatusMessageContext statusMessageContext) {

		return doCreateMessage(statusMessageContext);
	}

	protected Message doCreateMessage(
		StatusMessageContext statusMessageContext) {

		Map<String, Serializable> contextMap =
			statusMessageContext.getContextMap();

		Message message = new Message();

		message.put(
			"backgroundTaskId",
			BackgroundTaskThreadLocal.getBackgroundTaskId());

		message.put("messageType", statusMessageContext.getMessageType());

		Set<String> keys = contextMap.keySet();

		for (String key : keys) {
			message.put(key, contextMap.get(key));
		}

		return message;
	}

	public void setSingleDestinationMessageSender(
		SingleDestinationMessageSender singleDestinationMessageSender) {

		_singleDestinationMessageSender = singleDestinationMessageSender;
	}

	private SingleDestinationMessageSender _singleDestinationMessageSender;
}
