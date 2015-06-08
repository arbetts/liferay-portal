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

package com.liferay.portal.search;

import com.liferay.portal.NoSuchResourceException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.security.permission.ResourceBlockIdsBag;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockPermissionLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen Chiang
 * @author Bruno Farache
 * @author Raymond Augé
 * @author Amos Fong
 */
public class SearchPermissionCheckerImpl implements SearchPermissionChecker {

	@Override
	public void addPermissionFields(long companyId, Document document) {
		try {
			long groupId = GetterUtil.getLong(document.get(Field.GROUP_ID));

			String className = document.get(Field.ENTRY_CLASS_NAME);

			boolean relatedEntry = GetterUtil.getBoolean(
				document.get(Field.RELATED_ENTRY));

			if (relatedEntry) {
				long classNameId = GetterUtil.getLong(
					document.get(Field.CLASS_NAME_ID));

				className = PortalUtil.getClassName(classNameId);
			}

			if (Validator.isNull(className)) {
				return;
			}

			String classPK = document.get(Field.ROOT_ENTRY_CLASS_PK);

			if (Validator.isNull(classPK)) {
				classPK = document.get(Field.ENTRY_CLASS_PK);
			}

			if (relatedEntry) {
				classPK = document.get(Field.CLASS_PK);
			}

			if (Validator.isNull(classPK)) {
				return;
			}

			Indexer indexer = IndexerRegistryUtil.getIndexer(className);

			if (!indexer.isPermissionAware()) {
				return;
			}

			doAddPermissionFields_6(
				companyId, groupId, className, classPK, document);
		}
		catch (NoSuchResourceException nsre) {
			if (_log.isDebugEnabled()) {
				_log.debug(nsre, nsre);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	@Override
	public BooleanFilter getPermissionBooleanFilter(
		long companyId, long[] groupIds, long userId, String className,
		BooleanFilter booleanFilter, SearchContext searchContext) {

		try {
			booleanFilter = doGetPermissionBooleanFilter(
				companyId, groupIds, userId, className, booleanFilter,
				searchContext);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return booleanFilter;
	}

	@Override
	public void updatePermissionFields(
		String resourceName, String resourceClassPK) {

		try {
			Indexer indexer = IndexerRegistryUtil.getIndexer(resourceName);

			if (indexer != null) {
				indexer.reindex(
					resourceName, GetterUtil.getLong(resourceClassPK));
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void doAddPermissionFields_6(
			long companyId, long groupId, String className, String classPK,
			Document doc)
		throws Exception {

		Group group = null;

		if (groupId > 0) {
			group = GroupLocalServiceUtil.getGroup(groupId);
		}

		List<Role> roles = ListUtil.copy(
			ResourceActionsUtil.getRoles(companyId, group, className, null));

		if (groupId > 0) {
			List<Role> teamRoles = RoleLocalServiceUtil.getTeamRoles(groupId);

			roles.addAll(teamRoles);
		}

		long[] roleIdsArray = new long[roles.size()];

		for (int i = 0; i < roleIdsArray.length; i++) {
			Role role = roles.get(i);

			roleIdsArray[i] = role.getRoleId();
		}

		boolean[] hasResourcePermissions = null;

		if (ResourceBlockLocalServiceUtil.isSupported(className)) {
			ResourceBlockIdsBag resourceBlockIdsBag =
				ResourceBlockLocalServiceUtil.getResourceBlockIdsBag(
					companyId, groupId, className, roleIdsArray);

			long actionId = ResourceBlockLocalServiceUtil.getActionId(
				className, ActionKeys.VIEW);

			List<Long> resourceBlockIds =
				resourceBlockIdsBag.getResourceBlockIds(actionId);

			hasResourcePermissions = new boolean[roleIdsArray.length];

			for (long resourceBlockId : resourceBlockIds) {
				for (int i = 0; i < roleIdsArray.length; i++) {
					int count =
						ResourceBlockPermissionLocalServiceUtil.
							getResourceBlockPermissionsCount(
								resourceBlockId, roleIdsArray[i]);

					hasResourcePermissions[i] = (count > 0);
				}
			}
		}
		else {
			hasResourcePermissions =
				ResourcePermissionLocalServiceUtil.hasResourcePermissions(
					companyId, className, ResourceConstants.SCOPE_INDIVIDUAL,
					classPK, roleIdsArray, ActionKeys.VIEW);
		}

		List<Long> roleIds = new ArrayList<>();
		List<String> groupRoleIds = new ArrayList<>();

		for (int i = 0; i < hasResourcePermissions.length; i++) {
			if (!hasResourcePermissions[i]) {
				continue;
			}

			Role role = roles.get(i);

			if ((role.getType() == RoleConstants.TYPE_ORGANIZATION) ||
				(role.getType() == RoleConstants.TYPE_SITE)) {

				groupRoleIds.add(groupId + StringPool.DASH + role.getRoleId());
			}
			else {
				roleIds.add(role.getRoleId());
			}
		}

		doc.addKeyword(
			Field.ROLE_ID, roleIds.toArray(new Long[roleIds.size()]));
		doc.addKeyword(
			Field.GROUP_ROLE_ID,
			groupRoleIds.toArray(new String[groupRoleIds.size()]));
	}

	protected BooleanFilter doGetPermissionBooleanFilter(
			long companyId, long[] groupIds, long userId, String className,
			BooleanFilter booleanFilter, SearchContext searchContext)
		throws Exception {

		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(className);

		if (!indexer.isPermissionAware()) {
			return booleanFilter;
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker.getUserId() != userId) {
			User user = UserLocalServiceUtil.getUser(userId);

			permissionChecker = PermissionCheckerFactoryUtil.create(user);
		}

		return permissionChecker.getPermissionBooleanFilter(
			companyId, groupIds, className, booleanFilter, searchContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchPermissionCheckerImpl.class);

}