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
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.test.LayoutTestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Julio Camarero
 * @author Roberto Díaz
 * @author Sergio González
 */
@Sync(cleanTransaction = true)
public class GroupServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testAddPermissionsCustomRole() throws Exception {
		Group group = GroupTestUtil.addGroup();

		User user = UserTestUtil.addUser(null, group.getGroupId());

		givePermissionToManageSubsites(user, group);

		testGroup(
			user, group, null, null, true, false, false, false, true, true,
			true);
	}

	@Test
	public void testAddPermissionsCustomRoleInSubsite() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		User user = UserTestUtil.addUser(null, group11.getGroupId());

		givePermissionToManageSubsites(user, group11);

		testGroup(
			user, group1, group11, null, true, false, false, false, false, true,
			true);
	}

	@Test
	public void testAddPermissionsRegularUser() throws Exception {
		Group group = GroupTestUtil.addGroup();

		User user = UserTestUtil.addUser(null, group.getGroupId());

		testGroup(
			user, group, null, null, true, false, false, false, false, false,
			false);
	}

	@Test
	public void testAddPermissionsSiteAdmin() throws Exception {
		Group group = GroupTestUtil.addGroup();

		User user = UserTestUtil.addUser(null, group.getGroupId());

		giveSiteAdminRole(user, group);

		testGroup(
			user, group, null, null, true, false, true, false, true, true,
			true);
	}

	@Test
	public void testAddPermissionsSubsiteAdmin() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		User user = UserTestUtil.addUser(null, group11.getGroupId());

		giveSiteAdminRole(user, group11);

		testGroup(
			user, group1, group11, null, true, false, false, true, false, true,
			true);
	}

	@Test
	public void testGetUserSitesGroups() throws Exception {
		Organization parentOrganization = OrganizationTestUtil.addOrganization(
			true);

		Group parentOrganizationGroup = parentOrganization.getGroup();

		LayoutTestUtil.addLayout(parentOrganizationGroup);

		Organization organization = OrganizationTestUtil.addOrganization(
			parentOrganization.getOrganizationId(),
			RandomTestUtil.randomString(), false);

		_organizations.add(organization);
		_organizations.add(parentOrganization);

		UserLocalServiceUtil.addOrganizationUsers(
			organization.getOrganizationId(),
			new long[] {TestPropsValues.getUserId()});

		try {
			List<Group> groups = GroupServiceUtil.getUserSitesGroups(
				TestPropsValues.getUserId(), null, false, QueryUtil.ALL_POS);

			Assert.assertTrue(groups.contains(parentOrganizationGroup));
		}
		finally {
			UserLocalServiceUtil.unsetOrganizationUsers(
				organization.getOrganizationId(),
				new long[] {TestPropsValues.getUserId()});
		}
	}

	@Test
	public void testUpdatePermissionsCustomRole() throws Exception {
		Group group = GroupTestUtil.addGroup();

		User user = UserTestUtil.addUser(null, group.getGroupId());

		givePermissionToManageSubsites(user, group);

		testGroup(
			user, group, null, null, false, true, false, false, true, true,
			true);
	}

	@Test
	public void testUpdatePermissionsCustomRoleInSubsite() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		User user = UserTestUtil.addUser(null, group11.getGroupId());

		givePermissionToManageSubsites(user, group11);

		testGroup(
			user, group1, group11, null, false, true, false, false, false, true,
			true);
	}

	@Test
	public void testUpdatePermissionsRegularUser() throws Exception {
		Group group = GroupTestUtil.addGroup();

		User user = UserTestUtil.addUser(null, group.getGroupId());

		testGroup(
			user, group, null, null, false, true, false, false, false, false,
			false);
	}

	@Test
	public void testUpdatePermissionsSiteAdmin() throws Exception {
		Group group = GroupTestUtil.addGroup();

		User user = UserTestUtil.addUser(null, group.getGroupId());

		giveSiteAdminRole(user, group);

		testGroup(
			user, group, null, null, false, true, true, false, true, true,
			true);
	}

	@Test
	public void testUpdatePermissionsSubsiteAdmin() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		User user = UserTestUtil.addUser(null, group11.getGroupId());

		giveSiteAdminRole(user, group11);

		testGroup(
			user, group1, group11, null, false, true, false, true, false, true,
			true);
	}

	protected Locale getLocale() {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		return themeDisplay.getLocale();
	}

	protected void givePermissionToManageSubsites(User user, Group group)
		throws Exception {

		Role role = RoleTestUtil.addRole(
			"Subsites Admin", RoleConstants.TYPE_SITE, Group.class.getName(),
			ResourceConstants.SCOPE_GROUP_TEMPLATE,
			String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
			ActionKeys.MANAGE_SUBGROUPS);

		long[] roleIds = new long[] {role.getRoleId()};

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			user.getUserId(), group.getGroupId(), roleIds);
	}

	protected void giveSiteAdminRole(User user, Group group) throws Exception {
		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(), RoleConstants.SITE_ADMINISTRATOR);

		long[] roleIds = new long[] {role.getRoleId()};

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			user.getUserId(), group.getGroupId(), roleIds);
	}

	protected void testGroup(
			User user, Group group1, Group group11, Group group111,
			boolean addGroup, boolean updateGroup, boolean hasManageSite1,
			boolean hasManageSite11, boolean hasManageSubsitePermisionOnGroup1,
			boolean hasManageSubsitePermisionOnGroup11,
			boolean hasManageSubsitePermisionOnGroup111)
		throws Exception {

		if (group1 == null) {
			group1 = GroupTestUtil.addGroup();
		}

		if (group11 == null) {
			group11 = GroupTestUtil.addGroup(group1.getGroupId());
		}

		if (group111 == null) {
			group111 = GroupTestUtil.addGroup(group11.getGroupId());
		}

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionThreadLocal.setPermissionChecker(permissionChecker);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group1.getGroupId(), user.getUserId());

		if (addGroup) {
			try {
				GroupTestUtil.addGroup(
					GroupConstants.DEFAULT_PARENT_GROUP_ID, serviceContext);

				Assert.fail(
					"The user should not be able to add top level sites");
			}
			catch (PrincipalException pe) {
			}

			try {
				GroupTestUtil.addGroup(group1.getGroupId(), serviceContext);

				if (!hasManageSubsitePermisionOnGroup1 && !hasManageSite1) {
					Assert.fail("The user should not be able to add this site");
				}
			}
			catch (PrincipalException pe) {
				if (hasManageSubsitePermisionOnGroup1 || hasManageSite1) {
					Assert.fail("The user should be able to add this site");
				}
			}

			try {
				GroupTestUtil.addGroup(group11.getGroupId(), serviceContext);

				if (!hasManageSubsitePermisionOnGroup11 && !hasManageSite1) {
					Assert.fail("The user should not be able to add this site");
				}
			}
			catch (PrincipalException pe) {
				if (hasManageSubsitePermisionOnGroup11 || hasManageSite1) {
					Assert.fail("The user should be able to add this site");
				}
			}

			try {
				GroupTestUtil.addGroup(group111.getGroupId(), serviceContext);

				if (!hasManageSubsitePermisionOnGroup111 && !hasManageSite1) {
					Assert.fail("The user should not be able to add this site");
				}
			}
			catch (PrincipalException pe) {
				if (hasManageSubsitePermisionOnGroup111 || hasManageSite1) {
					Assert.fail("The user should be able to add this site");
				}
			}
		}

		if (updateGroup) {
			try {
				GroupServiceUtil.updateGroup(group1.getGroupId(), "");

				if (!hasManageSite1) {
					Assert.fail(
						"The user should not be able to update this site");
				}
			}
			catch (PrincipalException pe) {
				if (hasManageSite1) {
					Assert.fail("The user should be able to update this site");
				}
			}

			try {
				GroupServiceUtil.updateGroup(group11.getGroupId(), "");

				if (!hasManageSubsitePermisionOnGroup1 && !hasManageSite1 &&
					!hasManageSite11) {

					Assert.fail(
						"The user should not be able to update this site");
				}
			}
			catch (PrincipalException pe) {
				if (hasManageSubsitePermisionOnGroup1 || hasManageSite1 ||
					hasManageSite11) {

					Assert.fail("The user should be able to update this site");
				}
			}

			try {
				GroupServiceUtil.updateGroup(group111.getGroupId(), "");

				if (!hasManageSubsitePermisionOnGroup11 && !hasManageSite1) {
					Assert.fail(
						"The user should not be able to update this site");
				}
			}
			catch (PrincipalException pe) {
				if (hasManageSubsitePermisionOnGroup1 || hasManageSite1) {
					Assert.fail("The user should be able to update this site");
				}
			}
		}
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private final List<Organization> _organizations = new ArrayList<>();

}