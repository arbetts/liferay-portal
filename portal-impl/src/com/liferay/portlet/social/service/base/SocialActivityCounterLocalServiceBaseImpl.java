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

package com.liferay.portlet.social.service.base;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.service.persistence.ClassNamePersistence;
import com.liferay.portal.service.persistence.GroupFinder;
import com.liferay.portal.service.persistence.GroupPersistence;
import com.liferay.portal.service.persistence.LockFinder;
import com.liferay.portal.service.persistence.LockPersistence;
import com.liferay.portal.service.persistence.UserFinder;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.asset.service.persistence.AssetEntryFinder;
import com.liferay.portlet.asset.service.persistence.AssetEntryPersistence;
import com.liferay.portlet.social.model.SocialActivityCounter;
import com.liferay.portlet.social.service.SocialActivityCounterLocalService;
import com.liferay.portlet.social.service.persistence.SocialActivityCounterFinder;
import com.liferay.portlet.social.service.persistence.SocialActivityCounterPersistence;
import com.liferay.portlet.social.service.persistence.SocialActivityLimitPersistence;
import com.liferay.portlet.social.service.persistence.SocialActivitySettingPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the social activity counter local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portlet.social.service.impl.SocialActivityCounterLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.social.service.impl.SocialActivityCounterLocalServiceImpl
 * @see com.liferay.portlet.social.service.SocialActivityCounterLocalServiceUtil
 * @generated
 */
@ProviderType
public abstract class SocialActivityCounterLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements SocialActivityCounterLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portlet.social.service.SocialActivityCounterLocalServiceUtil} to access the social activity counter local service.
	 */

	/**
	 * Adds the social activity counter to the database. Also notifies the appropriate model listeners.
	 *
	 * @param socialActivityCounter the social activity counter
	 * @return the social activity counter that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SocialActivityCounter addSocialActivityCounter(
		SocialActivityCounter socialActivityCounter) {
		socialActivityCounter.setNew(true);

		return socialActivityCounterPersistence.update(socialActivityCounter);
	}

	/**
	 * Creates a new social activity counter with the primary key. Does not add the social activity counter to the database.
	 *
	 * @param activityCounterId the primary key for the new social activity counter
	 * @return the new social activity counter
	 */
	@Override
	public SocialActivityCounter createSocialActivityCounter(
		long activityCounterId) {
		return socialActivityCounterPersistence.create(activityCounterId);
	}

	/**
	 * Deletes the social activity counter with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param activityCounterId the primary key of the social activity counter
	 * @return the social activity counter that was removed
	 * @throws PortalException if a social activity counter with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SocialActivityCounter deleteSocialActivityCounter(
		long activityCounterId) throws PortalException {
		return socialActivityCounterPersistence.remove(activityCounterId);
	}

	/**
	 * Deletes the social activity counter from the database. Also notifies the appropriate model listeners.
	 *
	 * @param socialActivityCounter the social activity counter
	 * @return the social activity counter that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SocialActivityCounter deleteSocialActivityCounter(
		SocialActivityCounter socialActivityCounter) {
		return socialActivityCounterPersistence.remove(socialActivityCounter);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(SocialActivityCounter.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return socialActivityCounterPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.social.model.impl.SocialActivityCounterModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return socialActivityCounterPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.social.model.impl.SocialActivityCounterModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return socialActivityCounterPersistence.findWithDynamicQuery(dynamicQuery,
			start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return socialActivityCounterPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return socialActivityCounterPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public SocialActivityCounter fetchSocialActivityCounter(
		long activityCounterId) {
		return socialActivityCounterPersistence.fetchByPrimaryKey(activityCounterId);
	}

	/**
	 * Returns the social activity counter with the primary key.
	 *
	 * @param activityCounterId the primary key of the social activity counter
	 * @return the social activity counter
	 * @throws PortalException if a social activity counter with the primary key could not be found
	 */
	@Override
	public SocialActivityCounter getSocialActivityCounter(
		long activityCounterId) throws PortalException {
		return socialActivityCounterPersistence.findByPrimaryKey(activityCounterId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(com.liferay.portlet.social.service.SocialActivityCounterLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(SocialActivityCounter.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("activityCounterId");

		return actionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(com.liferay.portlet.social.service.SocialActivityCounterLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(SocialActivityCounter.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("activityCounterId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return socialActivityCounterLocalService.deleteSocialActivityCounter((SocialActivityCounter)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return socialActivityCounterPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the social activity counters.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.social.model.impl.SocialActivityCounterModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of social activity counters
	 * @param end the upper bound of the range of social activity counters (not inclusive)
	 * @return the range of social activity counters
	 */
	@Override
	public List<SocialActivityCounter> getSocialActivityCounters(int start,
		int end) {
		return socialActivityCounterPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of social activity counters.
	 *
	 * @return the number of social activity counters
	 */
	@Override
	public int getSocialActivityCountersCount() {
		return socialActivityCounterPersistence.countAll();
	}

	/**
	 * Updates the social activity counter in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param socialActivityCounter the social activity counter
	 * @return the social activity counter that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SocialActivityCounter updateSocialActivityCounter(
		SocialActivityCounter socialActivityCounter) {
		return socialActivityCounterPersistence.update(socialActivityCounter);
	}

	/**
	 * Returns the social activity counter local service.
	 *
	 * @return the social activity counter local service
	 */
	public SocialActivityCounterLocalService getSocialActivityCounterLocalService() {
		return socialActivityCounterLocalService;
	}

	/**
	 * Sets the social activity counter local service.
	 *
	 * @param socialActivityCounterLocalService the social activity counter local service
	 */
	public void setSocialActivityCounterLocalService(
		SocialActivityCounterLocalService socialActivityCounterLocalService) {
		this.socialActivityCounterLocalService = socialActivityCounterLocalService;
	}

	/**
	 * Returns the social activity counter persistence.
	 *
	 * @return the social activity counter persistence
	 */
	public SocialActivityCounterPersistence getSocialActivityCounterPersistence() {
		return socialActivityCounterPersistence;
	}

	/**
	 * Sets the social activity counter persistence.
	 *
	 * @param socialActivityCounterPersistence the social activity counter persistence
	 */
	public void setSocialActivityCounterPersistence(
		SocialActivityCounterPersistence socialActivityCounterPersistence) {
		this.socialActivityCounterPersistence = socialActivityCounterPersistence;
	}

	/**
	 * Returns the social activity counter finder.
	 *
	 * @return the social activity counter finder
	 */
	public SocialActivityCounterFinder getSocialActivityCounterFinder() {
		return socialActivityCounterFinder;
	}

	/**
	 * Sets the social activity counter finder.
	 *
	 * @param socialActivityCounterFinder the social activity counter finder
	 */
	public void setSocialActivityCounterFinder(
		SocialActivityCounterFinder socialActivityCounterFinder) {
		this.socialActivityCounterFinder = socialActivityCounterFinder;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name remote service.
	 *
	 * @return the class name remote service
	 */
	public com.liferay.portal.service.ClassNameService getClassNameService() {
		return classNameService;
	}

	/**
	 * Sets the class name remote service.
	 *
	 * @param classNameService the class name remote service
	 */
	public void setClassNameService(
		com.liferay.portal.service.ClassNameService classNameService) {
		this.classNameService = classNameService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the group local service.
	 *
	 * @return the group local service
	 */
	public com.liferay.portal.service.GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}

	/**
	 * Sets the group local service.
	 *
	 * @param groupLocalService the group local service
	 */
	public void setGroupLocalService(
		com.liferay.portal.service.GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	/**
	 * Returns the group remote service.
	 *
	 * @return the group remote service
	 */
	public com.liferay.portal.service.GroupService getGroupService() {
		return groupService;
	}

	/**
	 * Sets the group remote service.
	 *
	 * @param groupService the group remote service
	 */
	public void setGroupService(
		com.liferay.portal.service.GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * Returns the group persistence.
	 *
	 * @return the group persistence
	 */
	public GroupPersistence getGroupPersistence() {
		return groupPersistence;
	}

	/**
	 * Sets the group persistence.
	 *
	 * @param groupPersistence the group persistence
	 */
	public void setGroupPersistence(GroupPersistence groupPersistence) {
		this.groupPersistence = groupPersistence;
	}

	/**
	 * Returns the group finder.
	 *
	 * @return the group finder
	 */
	public GroupFinder getGroupFinder() {
		return groupFinder;
	}

	/**
	 * Sets the group finder.
	 *
	 * @param groupFinder the group finder
	 */
	public void setGroupFinder(GroupFinder groupFinder) {
		this.groupFinder = groupFinder;
	}

	/**
	 * Returns the lock local service.
	 *
	 * @return the lock local service
	 */
	public com.liferay.portal.service.LockLocalService getLockLocalService() {
		return lockLocalService;
	}

	/**
	 * Sets the lock local service.
	 *
	 * @param lockLocalService the lock local service
	 */
	public void setLockLocalService(
		com.liferay.portal.service.LockLocalService lockLocalService) {
		this.lockLocalService = lockLocalService;
	}

	/**
	 * Returns the lock persistence.
	 *
	 * @return the lock persistence
	 */
	public LockPersistence getLockPersistence() {
		return lockPersistence;
	}

	/**
	 * Sets the lock persistence.
	 *
	 * @param lockPersistence the lock persistence
	 */
	public void setLockPersistence(LockPersistence lockPersistence) {
		this.lockPersistence = lockPersistence;
	}

	/**
	 * Returns the lock finder.
	 *
	 * @return the lock finder
	 */
	public LockFinder getLockFinder() {
		return lockFinder;
	}

	/**
	 * Sets the lock finder.
	 *
	 * @param lockFinder the lock finder
	 */
	public void setLockFinder(LockFinder lockFinder) {
		this.lockFinder = lockFinder;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the user finder.
	 *
	 * @return the user finder
	 */
	public UserFinder getUserFinder() {
		return userFinder;
	}

	/**
	 * Sets the user finder.
	 *
	 * @param userFinder the user finder
	 */
	public void setUserFinder(UserFinder userFinder) {
		this.userFinder = userFinder;
	}

	/**
	 * Returns the asset entry local service.
	 *
	 * @return the asset entry local service
	 */
	public com.liferay.portlet.asset.service.AssetEntryLocalService getAssetEntryLocalService() {
		return assetEntryLocalService;
	}

	/**
	 * Sets the asset entry local service.
	 *
	 * @param assetEntryLocalService the asset entry local service
	 */
	public void setAssetEntryLocalService(
		com.liferay.portlet.asset.service.AssetEntryLocalService assetEntryLocalService) {
		this.assetEntryLocalService = assetEntryLocalService;
	}

	/**
	 * Returns the asset entry remote service.
	 *
	 * @return the asset entry remote service
	 */
	public com.liferay.portlet.asset.service.AssetEntryService getAssetEntryService() {
		return assetEntryService;
	}

	/**
	 * Sets the asset entry remote service.
	 *
	 * @param assetEntryService the asset entry remote service
	 */
	public void setAssetEntryService(
		com.liferay.portlet.asset.service.AssetEntryService assetEntryService) {
		this.assetEntryService = assetEntryService;
	}

	/**
	 * Returns the asset entry persistence.
	 *
	 * @return the asset entry persistence
	 */
	public AssetEntryPersistence getAssetEntryPersistence() {
		return assetEntryPersistence;
	}

	/**
	 * Sets the asset entry persistence.
	 *
	 * @param assetEntryPersistence the asset entry persistence
	 */
	public void setAssetEntryPersistence(
		AssetEntryPersistence assetEntryPersistence) {
		this.assetEntryPersistence = assetEntryPersistence;
	}

	/**
	 * Returns the asset entry finder.
	 *
	 * @return the asset entry finder
	 */
	public AssetEntryFinder getAssetEntryFinder() {
		return assetEntryFinder;
	}

	/**
	 * Sets the asset entry finder.
	 *
	 * @param assetEntryFinder the asset entry finder
	 */
	public void setAssetEntryFinder(AssetEntryFinder assetEntryFinder) {
		this.assetEntryFinder = assetEntryFinder;
	}

	/**
	 * Returns the social activity limit local service.
	 *
	 * @return the social activity limit local service
	 */
	public com.liferay.portlet.social.service.SocialActivityLimitLocalService getSocialActivityLimitLocalService() {
		return socialActivityLimitLocalService;
	}

	/**
	 * Sets the social activity limit local service.
	 *
	 * @param socialActivityLimitLocalService the social activity limit local service
	 */
	public void setSocialActivityLimitLocalService(
		com.liferay.portlet.social.service.SocialActivityLimitLocalService socialActivityLimitLocalService) {
		this.socialActivityLimitLocalService = socialActivityLimitLocalService;
	}

	/**
	 * Returns the social activity limit persistence.
	 *
	 * @return the social activity limit persistence
	 */
	public SocialActivityLimitPersistence getSocialActivityLimitPersistence() {
		return socialActivityLimitPersistence;
	}

	/**
	 * Sets the social activity limit persistence.
	 *
	 * @param socialActivityLimitPersistence the social activity limit persistence
	 */
	public void setSocialActivityLimitPersistence(
		SocialActivityLimitPersistence socialActivityLimitPersistence) {
		this.socialActivityLimitPersistence = socialActivityLimitPersistence;
	}

	/**
	 * Returns the social activity setting local service.
	 *
	 * @return the social activity setting local service
	 */
	public com.liferay.portlet.social.service.SocialActivitySettingLocalService getSocialActivitySettingLocalService() {
		return socialActivitySettingLocalService;
	}

	/**
	 * Sets the social activity setting local service.
	 *
	 * @param socialActivitySettingLocalService the social activity setting local service
	 */
	public void setSocialActivitySettingLocalService(
		com.liferay.portlet.social.service.SocialActivitySettingLocalService socialActivitySettingLocalService) {
		this.socialActivitySettingLocalService = socialActivitySettingLocalService;
	}

	/**
	 * Returns the social activity setting remote service.
	 *
	 * @return the social activity setting remote service
	 */
	public com.liferay.portlet.social.service.SocialActivitySettingService getSocialActivitySettingService() {
		return socialActivitySettingService;
	}

	/**
	 * Sets the social activity setting remote service.
	 *
	 * @param socialActivitySettingService the social activity setting remote service
	 */
	public void setSocialActivitySettingService(
		com.liferay.portlet.social.service.SocialActivitySettingService socialActivitySettingService) {
		this.socialActivitySettingService = socialActivitySettingService;
	}

	/**
	 * Returns the social activity setting persistence.
	 *
	 * @return the social activity setting persistence
	 */
	public SocialActivitySettingPersistence getSocialActivitySettingPersistence() {
		return socialActivitySettingPersistence;
	}

	/**
	 * Sets the social activity setting persistence.
	 *
	 * @param socialActivitySettingPersistence the social activity setting persistence
	 */
	public void setSocialActivitySettingPersistence(
		SocialActivitySettingPersistence socialActivitySettingPersistence) {
		this.socialActivitySettingPersistence = socialActivitySettingPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register("com.liferay.portlet.social.model.SocialActivityCounter",
			socialActivityCounterLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.portlet.social.model.SocialActivityCounter");
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	@Override
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	protected Class<?> getModelClass() {
		return SocialActivityCounter.class;
	}

	protected String getModelClassName() {
		return SocialActivityCounter.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = socialActivityCounterPersistence.getDataSource();

			DB db = DBFactoryUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = SocialActivityCounterLocalService.class)
	protected SocialActivityCounterLocalService socialActivityCounterLocalService;
	@BeanReference(type = SocialActivityCounterPersistence.class)
	protected SocialActivityCounterPersistence socialActivityCounterPersistence;
	@BeanReference(type = SocialActivityCounterFinder.class)
	protected SocialActivityCounterFinder socialActivityCounterFinder;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.ClassNameLocalService.class)
	protected com.liferay.portal.service.ClassNameLocalService classNameLocalService;
	@BeanReference(type = com.liferay.portal.service.ClassNameService.class)
	protected com.liferay.portal.service.ClassNameService classNameService;
	@BeanReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@BeanReference(type = com.liferay.portal.service.GroupLocalService.class)
	protected com.liferay.portal.service.GroupLocalService groupLocalService;
	@BeanReference(type = com.liferay.portal.service.GroupService.class)
	protected com.liferay.portal.service.GroupService groupService;
	@BeanReference(type = GroupPersistence.class)
	protected GroupPersistence groupPersistence;
	@BeanReference(type = GroupFinder.class)
	protected GroupFinder groupFinder;
	@BeanReference(type = com.liferay.portal.service.LockLocalService.class)
	protected com.liferay.portal.service.LockLocalService lockLocalService;
	@BeanReference(type = LockPersistence.class)
	protected LockPersistence lockPersistence;
	@BeanReference(type = LockFinder.class)
	protected LockFinder lockFinder;
	@BeanReference(type = com.liferay.portal.service.UserLocalService.class)
	protected com.liferay.portal.service.UserLocalService userLocalService;
	@BeanReference(type = com.liferay.portal.service.UserService.class)
	protected com.liferay.portal.service.UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	@BeanReference(type = UserFinder.class)
	protected UserFinder userFinder;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetEntryLocalService.class)
	protected com.liferay.portlet.asset.service.AssetEntryLocalService assetEntryLocalService;
	@BeanReference(type = com.liferay.portlet.asset.service.AssetEntryService.class)
	protected com.liferay.portlet.asset.service.AssetEntryService assetEntryService;
	@BeanReference(type = AssetEntryPersistence.class)
	protected AssetEntryPersistence assetEntryPersistence;
	@BeanReference(type = AssetEntryFinder.class)
	protected AssetEntryFinder assetEntryFinder;
	@BeanReference(type = com.liferay.portlet.social.service.SocialActivityLimitLocalService.class)
	protected com.liferay.portlet.social.service.SocialActivityLimitLocalService socialActivityLimitLocalService;
	@BeanReference(type = SocialActivityLimitPersistence.class)
	protected SocialActivityLimitPersistence socialActivityLimitPersistence;
	@BeanReference(type = com.liferay.portlet.social.service.SocialActivitySettingLocalService.class)
	protected com.liferay.portlet.social.service.SocialActivitySettingLocalService socialActivitySettingLocalService;
	@BeanReference(type = com.liferay.portlet.social.service.SocialActivitySettingService.class)
	protected com.liferay.portlet.social.service.SocialActivitySettingService socialActivitySettingService;
	@BeanReference(type = SocialActivitySettingPersistence.class)
	protected SocialActivitySettingPersistence socialActivitySettingPersistence;
	@BeanReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry;
	private String _beanIdentifier;
}