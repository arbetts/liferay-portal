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

package com.liferay.portal.service;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ResourcePermissionTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.ResourceAction;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.UserPersonalSite;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.util.PortalUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.testng.Assert;

/**
 * @author Andrew Betts
 */
public class GroupLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		_resourceAction = ResourceActionLocalServiceUtil.getResourceAction(
			Group.class.getName(), ActionKeys.VIEW);

		_resourcePermission = ResourcePermissionTestUtil.addResourcePermission(
			_resourceAction.getBitwiseValue(), Group.class.getName(),
			String.valueOf(_group.getGroupId()), _role.getRoleId(),
			ResourceConstants.SCOPE_GROUP);
	}

	@Test
	public void testDoSearchRolePermissions() throws Exception {
		long companyId = TestPropsValues.getCompanyId();

		long[] classNameIds = new long[] {
			PortalUtil.getClassNameId(Company.class),
			PortalUtil.getClassNameId(Group.class),
			PortalUtil.getClassNameId(Organization.class),
			PortalUtil.getClassNameId(UserPersonalSite.class)
		};

		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		List rolePermissions = new ArrayList();

		rolePermissions.add(_resourcePermission.getName());
		rolePermissions.add(new Integer(_resourcePermission.getScope()));
		rolePermissions.add(_resourceAction.getActionId());
		rolePermissions.add(new Long(_role.getRoleId()));

		groupParams.put("rolePermissions", rolePermissions);

		List<Group> rolePermissionsGroups = GroupLocalServiceUtil.search(
			companyId, classNameIds, null, null, groupParams, true,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Assert.assertEquals(rolePermissionsGroups.size(), 1);

		Group group = rolePermissionsGroups.get(0);

		Assert.assertTrue(group.equals(_group));
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private ResourceAction _resourceAction;

	@DeleteAfterTestRun
	private ResourcePermission _resourcePermission;

	@DeleteAfterTestRun
	private Role _role;

}