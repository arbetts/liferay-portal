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

package com.liferay.shopping.model.listener;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.Group;
import com.liferay.shopping.service.ShoppingCartLocalService;
import com.liferay.shopping.service.ShoppingCategoryLocalService;
import com.liferay.shopping.service.ShoppingCouponLocalService;
import com.liferay.shopping.service.ShoppingOrderLocalService;

/**
 * @author Peter Fellwock
 */
public class GroupModelListener extends BaseModelListener<Group> {

	@Override
	public void onAfterRemove(Group group) throws ModelListenerException {
		try {
			_shoppingCartLocalService.deleteGroupCarts(group.getGroupId());

			_shoppingCategoryLocalService.deleteCategories(group.getGroupId());

			_shoppingCouponLocalService.deleteCoupons(group.getGroupId());

			_shoppingOrderLocalService.deleteOrders(group.getGroupId());
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@BeanReference(type = ShoppingCartLocalService.class)
	private ShoppingCartLocalService _shoppingCartLocalService;

	@BeanReference(type = ShoppingCategoryLocalService.class)
	private ShoppingCategoryLocalService _shoppingCategoryLocalService;

	@BeanReference(type = ShoppingCouponLocalService.class)
	private ShoppingCouponLocalService _shoppingCouponLocalService;

	@BeanReference(type = ShoppingOrderLocalService.class)
	private ShoppingOrderLocalService _shoppingOrderLocalService;

}