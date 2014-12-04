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

package com.liferay.portal.notifications;

import com.liferay.portal.json.JSONObjectImpl;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.notifications.Channel;
import com.liferay.portal.kernel.notifications.ChannelException;
import com.liferay.portal.kernel.notifications.ChannelListener;
import com.liferay.portal.kernel.notifications.NotificationEvent;
import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.test.AggregateTestRule;
import com.liferay.portal.kernel.test.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.NewEnv;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.UserNotificationEvent;
import com.liferay.portal.test.AdviseWith;
import com.liferay.portal.test.AspectJNewEnvTestRule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by liferay on 12/3/2014.
 */
public class ChannelImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			CodeCoverageAssertor.INSTANCE, AspectJNewEnvTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_channel = new ChannelImpl();

		_channelListener = new TestChannelListener();
		_channel.registerChannelListener(_channelListener);

		_throwUNELSUException = new AtomicBoolean();
		_throwJSONFUException = new AtomicBoolean();

		_notificationEvents = ReflectionTestUtil.getFieldValue(
			_channel, "_notificationEvents");
		_unconfirmedNotificationEvents = ReflectionTestUtil.getFieldValue(
			_channel, "_unconfirmedNotificationEvents");
	}

	@Test
	public void testClone() {
		Channel channel = _channel.clone(
			CompanyConstants.SYSTEM, _channel.getUserId());

		Assert.assertNotSame(_channel, channel);
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testConfirmDelivery0() throws Exception {
		Stack<String> uuidStack = new Stack<String>();

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			uuidStack.push(notificationEvent.getUuid());

			_unconfirmedNotificationEvents.put(
				uuidStack.peek(), notificationEvent);
		}

		_channel.confirmDelivery(uuidStack.pop());

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());

		_channel.confirmDelivery(uuidStack.pop(), true);

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());

		_throwUNELSUException.set(true);

		try {
			_channel.confirmDelivery(uuidStack.pop());

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableUserNotificationEventConfirmationAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testConfirmDelivery1() throws Exception {
		Stack<String> uuidStack = new Stack<String>();

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			uuidStack.push(notificationEvent.getUuid());

			_unconfirmedNotificationEvents.put(
				uuidStack.peek(), notificationEvent);
		}

		_channel.confirmDelivery(uuidStack.pop());

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(adviceClasses =
		{UserNotificationEventLocalServiceUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testDeleteUserNotificiationEvent()
		throws ChannelException {
		Stack<String> uuidStack = new Stack<String>();

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			uuidStack.push(notificationEvent.getUuid());

			_unconfirmedNotificationEvents.put(
				uuidStack.peek(), notificationEvent);
		}

		_channel.deleteUserNotificiationEvent(uuidStack.pop());

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());

		_throwUNELSUException.set(true);

		try {
			_channel.deleteUserNotificiationEvent(uuidStack.pop());

			Assert.fail();
		}
		catch (ChannelException e) {
		}

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());
	}

	@Test
	public void testFlush() throws Exception {
		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			_notificationEvents.add(notificationEvent);
		}

		_channel.flush();

		Assert.assertEquals(0, _notificationEvents.size());
	}

	@Test
	public void testFlushBeforeTimestamp() throws Exception {
		long timestamp = 0;

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			timestamp = notificationEvent.getTimestamp();

			_notificationEvents.add(notificationEvent);
		}

		_channel.flush(timestamp);

		Assert.assertEquals(1, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testGetNotifications() throws Exception {
		NotificationEvent expiredNotificationEvent = _createNotificationEvent();

		expiredNotificationEvent.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent);
		_notificationEvents.add(_createNotificationEvent());

		NotificationEvent expiredNotificationEvent1 = _createNotificationEvent();

		expiredNotificationEvent1.setDeliverBy(1);

		_unconfirmedNotificationEvents.put(
			expiredNotificationEvent1.getUuid(), expiredNotificationEvent1);

		NotificationEvent unconfirmedNotificaitonEvent = _createNotificationEvent();

		_unconfirmedNotificationEvents.put(
			unconfirmedNotificaitonEvent.getUuid(), unconfirmedNotificaitonEvent);

		List<NotificationEvent> notificationEvents = _channel.getNotificationEvents();

		Assert.assertEquals(2, notificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class,
			JSONFactoryUtilAdvice.class,
			NotificationEventFactoryUtilAdvice.class
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testInit() throws Exception {
		_channel.init();

		Assert.assertEquals(2, _unconfirmedNotificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			EnableUserNotificationEventConfirmationAdvice.class,
			UserNotificationEventLocalServiceUtilAdvice.class,
		})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCleanUp() throws Exception {
		NotificationEvent expiredNotificationEvent = _createNotificationEvent();

		expiredNotificationEvent.setDeliverBy(1);

		_notificationEvents.add(expiredNotificationEvent);
		_notificationEvents.add(_createNotificationEvent());

		NotificationEvent expiredNotificationEvent1 = _createNotificationEvent();

		expiredNotificationEvent1.setDeliverBy(1);

		_unconfirmedNotificationEvents.put(
			expiredNotificationEvent1.getUuid(), expiredNotificationEvent1);

		NotificationEvent unconfirmedNotificaitonEvent = _createNotificationEvent();

		_unconfirmedNotificationEvents.put(
			unconfirmedNotificaitonEvent.getUuid(), unconfirmedNotificaitonEvent);

		_channel.cleanUp();

		Assert.assertEquals(1, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(1, _notificationEvents.size());
	}

	@Test
	public void testRemoveTransientNotificationEvents() {
		Assert.assertEquals(0, _notificationEvents.size());

		Set<NotificationEvent> notificationEventSet =
			new HashSet<NotificationEvent>(3);

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			_notificationEvents.add(notificationEvent);

			notificationEventSet.add(notificationEvent);
		}

		_channel.removeTransientNotificationEvents(notificationEventSet);

		Assert.assertEquals(0, _notificationEvents.size());
	}

	@Test
	public void testRemoveTransientNotificationEventsByUuid() {
		Assert.assertEquals(0, _notificationEvents.size());

		Set<String> notificationEventSet = new HashSet<String>(3);

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			_notificationEvents.add(notificationEvent);

			notificationEventSet.add(notificationEvent.getUuid());
		}

		Assert.assertEquals(3, _notificationEvents.size());

		_channel.removeTransientNotificationEventsByUuid(notificationEventSet);

		Assert.assertEquals(0, _notificationEvents.size());
	}

	@Test
	public void testSendNotificationEvent() {
		Assert.assertNotNull(_createNotificationEvent().getDeliverBy());
	}

	@AdviseWith(
		adviceClasses = {EnableUserNotificationEventConfirmationAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStoreNotificationEvent0() throws Exception {
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			notificationEvent.setDeliveryRequired(0);

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(3, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());
	}

	@AdviseWith(
		adviceClasses = {
			DisableUserNotificationEventConfirmationAdvice.class,
			TenMaxNotificationEventsAdvice.class
	})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStoreNotificationEvent1() throws Exception {
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());

		for (int i = 0; i < 10; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			notificationEvent.setDeliveryRequired(0);

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(10, _notificationEvents.size());

		NotificationEvent notificationEvent = _createNotificationEvent();

		notificationEvent.setDeliveryRequired(0);

		_channel.storeNotificationEvent(
			notificationEvent, System.currentTimeMillis());

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(10, _notificationEvents.size());
	}

	@Test
	public void testStorNotificationEventExpired() throws ChannelException {
		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());

		for (int i = 0; i < 3; i++) {
			NotificationEvent notificationEvent = _createNotificationEvent();

			notificationEvent.setDeliverBy(System.currentTimeMillis());

			_channel.storeNotificationEvent(
				notificationEvent, System.currentTimeMillis());
		}

		Assert.assertEquals(0, _unconfirmedNotificationEvents.size());
		Assert.assertEquals(0, _notificationEvents.size());
	}

	@Aspect
	public static class DisableUserNotificationEventConfirmationAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED)")
		public Object disableUserNotificationEventConfirmation(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.FALSE});
		}

	}

	@Aspect
	public static class EnableUserNotificationEventConfirmationAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"USER_NOTIFICATION_EVENT_CONFIRMATION_ENABLED)")
		public Object enableUserNotificationEventConfirmation(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.TRUE});
		}
	}

	@Aspect
	public static class JSONFactoryUtilAdvice {

		@Around(
			"execution(" +
				"public static com.liferay.portal.kernel.json.JSONObject " +
				"com.liferay.portal.kernel.json.JSONFactoryUtil." +
				"createJSONObject(String)) && args(json)"
		)
		public JSONObject createJSONObject(String json) throws JSONException {
			if (_throwJSONFUException.get()) {
				throw new JSONException();
			}

			return null;
		}
	}

	@Aspect
	public static class NotificationEventFactoryUtilAdvice {

		@Around(
			"execution(" +
				"public static com.liferay.portal.kernel.notifications.NotificationEvent " +
				"com.liferay.portal.kernel.notifications.NotificationEventFactoryUtil." +
				"createNotificationEvent(long, String, com.liferay.portal.kernel.json.JSONObject)) && " +
			"args(timestamp, type, payloadJSONObject)")
		public NotificationEvent createNotificationEvent(
			long timestamp, String type, JSONObject payloadJSONObject) {

			return new NotificationEvent(timestamp, type, payloadJSONObject);
		}
	}


	@Aspect
	public static class TenMaxNotificationEventsAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"NOTIFICATIONS_MAX_EVENTS)")
		public Object disableUserNotificationEventConfirmation(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {10});
		}

	}

	@Aspect
	public static class UserNotificationEventLocalServiceUtilAdvice {

		@Around(
			"execution(" +
				"public static com.liferay.portal.model.UserNotificationEvent " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"addUserNotificationEvent(long, NotificationEvent)) && " +
			"args(userId, notificationEvent)")
		public UserNotificationEvent addUserNotificationEvent(
			long userId, NotificationEvent notificationEvent) {

			if (_throwUNELSUException.get()) {
				throw new RuntimeException();
			}

			return _createUserNotificationEventProxy(notificationEvent);
		}

		@Around(
			"execution(" +
				"public static void " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"deleteUserNotificationEvent(String, long)) && " +
			"args(uuid, companyId)")
		public void deleteUserNotificationEvent(String uuid, long companyId) {
			if (_throwUNELSUException.get()) {
				throw new RuntimeException();
			}
		}

		@Around(
			"execution(" +
				"public static void " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"deleteUserNotificationEvents(java.util.Collection<String>, long)) && " +
			"args(uuids, companyId)")
		public void deleteUserNotificationEvents(
			Collection<String> uuids, long companyId) {

			if (_throwUNELSUException.get()) {
				throw new RuntimeException();
			}
		}

		@Around(
			"execution(" +
				"public static java.util.List<com.liferay.portal.model.UserNotificationEvent> " +
				"com.liferay.portal.service.UserNotificationEventLocalServiceUtil." +
				"getDeliveredUserNotificationEvents(long, boolean)) && " +
			"args(userId, delivered)")
		public List<UserNotificationEvent> getDeliveredUserNotificationEvents(
			long userId, boolean delivered) {

			if (_throwUNELSUException.get()) {
				throw new RuntimeException();
			}

			List<UserNotificationEvent> events =
				new ArrayList<UserNotificationEvent>();

			for (int i = 0; i < 3; i++) {
				events.add(_createUserNotificationEventProxy());
			}

			NotificationEvent expiredNotificationEvent = _createNotificationEvent();

			expiredNotificationEvent.setDeliverBy(1);

			events.add(_createUserNotificationEventProxy(expiredNotificationEvent));

			return events;
		}

		@Around(
			"execution(" +
				"public static com.liferay.portal.model.UserNotificationEvent" +
				" com.liferay.portal.service." +
					"UserNotificationEventLocalServiceUtil." +
				"updateUserNotificationEvent(String, long, boolean)) && " +
			"args(uuid, companyId, archive)")
		public UserNotificationEvent updateUserNotificationEvent(
			String uuid, long companyId, boolean archive) {

			if (_throwUNELSUException.get()) {
				throw new RuntimeException();
			}

			return _createUserNotificationEventProxy();
		}

	}

	private static NotificationEvent _createNotificationEvent() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		NotificationEvent notificationEvent = new NotificationEvent(
			System.currentTimeMillis(), ChannelImplTest.class.getName(),
			new JSONObjectImpl());

		UUID uuid = new UUID(
			SecureRandomUtil.nextLong(), SecureRandomUtil.nextLong());

		notificationEvent.setUuid(uuid.toString());
		notificationEvent.setArchived(false);

		return notificationEvent;
	}

	private static UserNotificationEvent _createUserNotificationEventProxy() {
		return (UserNotificationEvent)ProxyUtil.newProxyInstance(
			UserNotificationEvent.class.getClassLoader(),
			new Class<?>[]{UserNotificationEvent.class},
			new TestUserNotificationInvocationHandler(
				_createNotificationEvent()));
	}

	private static UserNotificationEvent _createUserNotificationEventProxy(
		NotificationEvent notificationEvent) {

		return (UserNotificationEvent)ProxyUtil.newProxyInstance(
			UserNotificationEvent.class.getClassLoader(),
			new Class<?>[]{UserNotificationEvent.class},
			new TestUserNotificationInvocationHandler(notificationEvent));
	}

	private static AtomicBoolean _throwUNELSUException;
	private static AtomicBoolean _throwJSONFUException;

	private Channel _channel;
	private ChannelListener _channelListener;
	private TreeSet<NotificationEvent> _notificationEvents;
	private Map<String, NotificationEvent> _unconfirmedNotificationEvents;

	private static class TestUserNotificationInvocationHandler
		implements InvocationHandler {

		public TestUserNotificationInvocationHandler(
			NotificationEvent notificationEvent) {

			_notificationEvent = notificationEvent;
		}

		@Override
		public Object invoke(
			Object proxy, Method method, Object[] args) throws Throwable {

			String methodName = method.getName();

			if (methodName.equals("getDeliverBy")) {
				return _notificationEvent.getDeliverBy();
			}
			else if (methodName.equals("getPayload")) {
				return _notificationEvent.getPayload().toString();
			}
			else if (methodName.equals("getTimestamp")) {
				return _notificationEvent.getTimestamp();
			}
			else if (methodName.equals("getType")) {
				return _notificationEvent.getType();
			}
			else if (methodName.equals("getUuid")) {
				return _notificationEvent.getUuid();
			}

			throw new UnsupportedOperationException();
		}

		private NotificationEvent _notificationEvent;

	}

	private class TestChannelListener implements ChannelListener {

		public TestChannelListener() {
			_notificationEventsCount = 0;
		}

		@Override
		public void channelListenerRemoved(long channelId) {
		}

		public int getNotificationEventsCount() {
			return _notificationEventsCount;
		}

		@Override
		public void notificationEventsAvailable(long channelId) {
			_notificationEventsCount++;
		}

		private int _notificationEventsCount;

	}

}