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

package com.liferay.portal.kernel.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Transactional;

/**
 * Provides the remote service interface for UserGroupRole. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see UserGroupRoleServiceUtil
 * @see com.liferay.portal.service.base.UserGroupRoleServiceBaseImpl
 * @see com.liferay.portal.service.impl.UserGroupRoleServiceImpl
 * @generated
 */
@AccessControlled
@JSONWebService
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface UserGroupRoleService extends BaseService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link UserGroupRoleServiceUtil} to access the user group role remote service. Add custom service methods to {@link com.liferay.portal.service.impl.UserGroupRoleServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public void addUserGroupRoles(long userId, long groupId, long[] roleIds)
		throws PortalException;

	public void addUserGroupRoles(long[] userIds, long groupId, long roleId)
		throws PortalException;

	public void deleteUserGroupRoles(long userId, long groupId, long[] roleIds)
		throws PortalException;

	public void deleteUserGroupRoles(long[] userIds, long groupId, long roleId)
		throws PortalException;

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public java.lang.String getOSGiServiceIdentifier();

	public void updateUserGroupRoles(long userId, long groupId,
		long[] addedRoleIds, long[] deletedRoleIds) throws PortalException;
}