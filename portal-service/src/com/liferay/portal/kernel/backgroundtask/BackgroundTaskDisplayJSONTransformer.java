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

package com.liferay.portal.kernel.backgroundtask;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Locale;

/**
 * @author Andrew Betts
 */
public class BackgroundTaskDisplayJSONTransformer {

	public static void appendJSONObjectToDetailsItems(
		JSONArray detailsItemsJSONArray, String message,
		JSONArray itemsListJSONArray) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("message", message);
		jsonObject.put("itemsList", itemsListJSONArray);

		detailsItemsJSONArray.put(jsonObject);
	}

	public static void appendJSONObjectToItemsList(
		JSONArray itemsListJSONArray, String info, String errorMessage,
		String errorStrongMessage) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("info", info);
		jsonObject.put("errorMessage", errorMessage);
		jsonObject.put("errorStrongMessage", errorStrongMessage);

		itemsListJSONArray.put(jsonObject);
	}

	public static JSONObject createDetailsJSONObject(
		String detailsHeader, JSONArray detailsItemsJSONArray, int status) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("detailsHeader", detailsHeader);
		jsonObject.put("detailsItems", detailsItemsJSONArray);
		jsonObject.put("status", status);

		return jsonObject;
	}

	public static JSONObject translateDetailsJSON(
		Locale locale, JSONObject detailsJSONObject) {

		if (detailsJSONObject == null) {
			return null;
		}

		JSONArray detailsItemsJSONArray = detailsJSONObject.getJSONArray(
			"detailsItems");

		detailsItemsJSONArray = translateDetailsItemsJSONArray(
			locale, detailsItemsJSONArray);

		String detailsHeader = detailsJSONObject.getString("detailsHeader");

		detailsJSONObject.put(
			"detailsHeader", LanguageUtil.get(locale, detailsHeader));
		detailsJSONObject.put("detailsItems", detailsItemsJSONArray);

		return detailsJSONObject;
	}

	protected static JSONArray translateDetailsItemsJSONArray(
		Locale locale, JSONArray detailsItemsJSONArray) {

		if (detailsItemsJSONArray == null) {
			return null;
		}

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (int i = 0; i < detailsItemsJSONArray.length(); i++) {
			JSONObject jsonObject = detailsItemsJSONArray.getJSONObject(i);

			String message = jsonObject.getString("message");

			jsonObject.put("message", LanguageUtil.get(locale, message));

			JSONArray itemsListJSONArray = jsonObject.getJSONArray("itemsList");

			itemsListJSONArray = translateItemsListJSONArray(
				locale, itemsListJSONArray);

			jsonObject.put("itemsList", itemsListJSONArray);

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	protected static JSONArray translateItemsListJSONArray(
		Locale locale, JSONArray itemsListJSONArray) {

		if (itemsListJSONArray == null) {
			return null;
		}

		JSONArray jsonArray =
			JSONFactoryUtil.createJSONArray();

		for (int j = 0; j < itemsListJSONArray.length(); j++) {
			JSONObject jsonObject = itemsListJSONArray.getJSONObject(j);

			String info = jsonObject.getString("info");
			String errorMessage = jsonObject.getString("errorMessage");
			String errorStrongMessage = jsonObject.getString(
				"errorStrongMessage");

			jsonObject.put("info", LanguageUtil.get(locale, info));
			jsonObject.put(
				"errorMessage",
				LanguageUtil.get(locale, errorMessage));
			jsonObject.put(
				"errorStrongMessage",
				LanguageUtil.get(locale, errorStrongMessage));

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

}