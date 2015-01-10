package com.liferay.portal.search;

import com.liferay.portal.kernel.messaging.Message;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by liferay on 12/2/2014.
 */
public interface StatusMessageSender {

	public void sendStatusMessage(StatusMessageContext statusMessageContext);

	public Message createStatusMessage(
		StatusMessageContext statusMessageContext);

}
