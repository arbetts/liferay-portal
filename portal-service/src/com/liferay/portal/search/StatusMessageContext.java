package com.liferay.portal.search;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liferay on 1/9/2015.
 */
public class StatusMessageContext {

	public StatusMessageContext(String messageType) {
		this(messageType, new HashMap<String, Serializable>());
	}

	public StatusMessageContext(
		String messageType, Map<String, Serializable> contextMap) {

		_messageType = messageType;
		_contextMap = contextMap;
	}

	public String getMessageType() {
		return _messageType;
	}

	public Map<String, Serializable> getContextMap() {
		return _contextMap;
	}

	public void put(String key, Serializable value) {
		_contextMap.put(key, value);
	}

	private String _messageType;
	private Map<String, Serializable> _contextMap;
}
