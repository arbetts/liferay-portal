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

package com.liferay.portal.editor.config;

import com.liferay.portal.kernel.editor.config.EditorConfig;
import com.liferay.portal.kernel.editor.config.EditorConfigContributor;
import com.liferay.portal.kernel.editor.config.EditorConfigFactoryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Sergio González
 */
public class EditorConfigFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@After
	public void tearDown() {
		if (_editorConfigContributorServiceRegistration1 != null) {
			_editorConfigContributorServiceRegistration1.unregister();
		}

		if (_editorConfigContributorServiceRegistration2 != null) {
			_editorConfigContributorServiceRegistration2.unregister();
		}
	}

	@Test
	public void testEditorConfigKeyAndEditorNameOverridesPortletNameAndEditorNameEditorConfig()
		throws Exception {

		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<>();

		properties.put("editor.config.key", _CONFIG_KEY);
		properties.put("editor.name", _EDITOR_NAME);
		properties.put("service.ranking", 1000);

		EditorConfigContributor emoticonsEditorConfigContributor =
			new EmoticonsEditorConfigContributor();

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, emoticonsEditorConfigContributor,
			properties);

		properties = new HashMap<>();

		properties.put("editor.name", _EDITOR_NAME);
		properties.put("javax.portlet.name", _PORTLET_NAME);
		properties.put("service.ranking", 1000);

		EditorConfigContributor textFormatEditorConfigContributor =
			new TextFormatEditorConfigContributor();

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, textFormatEditorConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			EmoticonsEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		JSONObject toolbarsJSONObject = configJSONObject.getJSONObject(
			"toolbars");

		Assert.assertEquals("link", toolbarsJSONObject.getString("button1"));
		Assert.assertEquals("bold", toolbarsJSONObject.getString("button2"));
		Assert.assertEquals(
			"emoticons", toolbarsJSONObject.getString("button3"));
	}

	@Test
	public void testEditorConfigKeyOverridesPortletNameEditorConfig()
		throws Exception {

		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<>();

		properties.put("editor.config.key", _CONFIG_KEY);
		properties.put("service.ranking", 1000);

		EditorConfigContributor emoticonsEditorConfigContributor =
			new EmoticonsEditorConfigContributor();

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, emoticonsEditorConfigContributor,
			properties);

		properties = new HashMap<>();

		properties.put("javax.portlet.name", _PORTLET_NAME);
		properties.put("service.ranking", 1000);

		EditorConfigContributor tablesEditorConfigContributor =
			new TablesEditorConfigContributor();

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, tablesEditorConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			EmoticonsEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		JSONObject toolbarsJSONObject = configJSONObject.getJSONObject(
			"toolbars");

		Assert.assertEquals(
			"emoticons", toolbarsJSONObject.getString("button3"));
	}

	@Test
	public void testGetEditorConfigByEditorName() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<>();

		properties.put("editor.name", _EDITOR_NAME);
		properties.put("service.ranking", 1000);

		EditorConfigContributor textFormatEditorConfigContributor =
			new TextFormatEditorConfigContributor();

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, textFormatEditorConfigContributor,
			properties);

		properties = new HashMap<>();

		properties.put("editor.name", _EDITOR_NAME_2);
		properties.put("service.ranking", 1000);

		EditorConfigContributor ImageEditorConfigContributor =
			new ImageEditorConfigContributor();

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, ImageEditorConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			TextFormatEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME_2,
			new HashMap<String, Object>(), null, null);

		configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			ImageEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));
	}

	@Test
	public void testGetEditorConfigByEditorNameAndServiceRanking()
		throws Exception {

		Registry registry = RegistryUtil.getRegistry();

		EditorConfigContributor textFormatEditorConfigContributor =
			new TextFormatEditorConfigContributor();

		Map<String, Object> properties = new HashMap<>();

		properties.put("editor.name", _EDITOR_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, textFormatEditorConfigContributor,
			properties);

		EditorConfigContributor videoEditorVideoConfigContributor =
			new VideoEditorConfigContributor();

		properties = new HashMap<>();

		properties.put("editor.name", _EDITOR_NAME);
		properties.put("service.ranking", 2000);

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, videoEditorVideoConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			TextFormatEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		JSONObject toolbarsJSONObject = configJSONObject.getJSONObject(
			"toolbars");

		Assert.assertEquals("link", toolbarsJSONObject.getString("button1"));
		Assert.assertEquals("play", toolbarsJSONObject.getString("button2"));
		Assert.assertEquals("stop", toolbarsJSONObject.getString("button3"));
	}

	@Test
	public void testPortletNameAndEditorConfigKeyAndEditorNameOverridesPortletNameAndEditorConfigKeyEditorConfig()
		throws Exception {

		Registry registry = RegistryUtil.getRegistry();

		EditorConfigContributor textFormatEditorConfigContributor =
			new TextFormatEditorConfigContributor();

		Map<String, Object> properties = new HashMap<>();

		properties.put("editor.config.key", _CONFIG_KEY);
		properties.put("editor.name", _EDITOR_NAME);
		properties.put("javax.portlet.name", _PORTLET_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, textFormatEditorConfigContributor,
			properties);

		EditorConfigContributor emoticonsEditorConfigContributor =
			new EmoticonsEditorConfigContributor();

		properties = new HashMap<>();

		properties.put("editor.config.key", _CONFIG_KEY);
		properties.put("javax.portlet.name", _PORTLET_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, emoticonsEditorConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			TextFormatEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		JSONObject toolbarsJSONObject = configJSONObject.getJSONObject(
			"toolbars");

		Assert.assertEquals("link", toolbarsJSONObject.getString("button1"));
		Assert.assertEquals("bold", toolbarsJSONObject.getString("button2"));
		Assert.assertEquals(
			"emoticons", toolbarsJSONObject.getString("button3"));
	}

	@Test
	public void testPortletNameAndEditorConfigKeyOverridesEditorConfigKeyAndEditorNameEditorConfig()
		throws Exception {

		Registry registry = RegistryUtil.getRegistry();

		EditorConfigContributor emoticonsEditorConfigContributor =
			new EmoticonsEditorConfigContributor();

		Map<String, Object> properties = new HashMap<>();

		properties.put("editor.config.key", _CONFIG_KEY);
		properties.put("javax.portlet.name", _PORTLET_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, emoticonsEditorConfigContributor,
			properties);

		EditorConfigContributor textFormatEditorConfigContributor =
			new TextFormatEditorConfigContributor();

		properties = new HashMap<>();

		properties.put("editor.config.key", _CONFIG_KEY);
		properties.put("editor.name", _EDITOR_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, textFormatEditorConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			EmoticonsEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		JSONObject toolbarsJSONObject = configJSONObject.getJSONObject(
			"toolbars");

		Assert.assertEquals("link", toolbarsJSONObject.getString("button1"));
		Assert.assertEquals("bold", toolbarsJSONObject.getString("button2"));
		Assert.assertEquals(
			"emoticons", toolbarsJSONObject.getString("button3"));
	}

	@Test
	public void testPortletNameAndEditorNameOverridesEditorConfigKeyEditorConfig()
		throws Exception {

		Registry registry = RegistryUtil.getRegistry();

		EditorConfigContributor textFormatEditorConfigContributor =
			new TextFormatEditorConfigContributor();

		Map<String, Object> properties = new HashMap<>();

		properties.put("editor.name", _EDITOR_NAME);
		properties.put("javax.portlet.name", _PORTLET_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, textFormatEditorConfigContributor,
			properties);

		EditorConfigContributor emoticonsEditorConfigContributor =
			new EmoticonsEditorConfigContributor();

		properties = new HashMap<>();

		properties.put("editor.config.key", _CONFIG_KEY);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, emoticonsEditorConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			TextFormatEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		JSONObject toolbarsJSONObject = configJSONObject.getJSONObject(
			"toolbars");

		Assert.assertEquals("link", toolbarsJSONObject.getString("button1"));
		Assert.assertEquals("bold", toolbarsJSONObject.getString("button2"));
		Assert.assertEquals(
			"emoticons", toolbarsJSONObject.getString("button3"));
	}

	@Test
	public void testPortletNameOverridesEditorNameEditorConfig()
		throws Exception {

		Registry registry = RegistryUtil.getRegistry();

		EditorConfigContributor tablesEditorConfigContributor =
			new TablesEditorConfigContributor();

		Map<String, Object> properties = new HashMap<>();

		properties.put("javax.portlet.name", _PORTLET_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration1 = registry.registerService(
			EditorConfigContributor.class, tablesEditorConfigContributor,
			properties);

		EditorConfigContributor textFormatEditorConfigContributor =
			new TextFormatEditorConfigContributor();

		properties = new HashMap<>();

		properties.put("editor.name", _EDITOR_NAME);
		properties.put("service.ranking", 1000);

		_editorConfigContributorServiceRegistration2 = registry.registerService(
			EditorConfigContributor.class, textFormatEditorConfigContributor,
			properties);

		EditorConfig editorConfig = EditorConfigFactoryUtil.getEditorConfig(
			_PORTLET_NAME, _CONFIG_KEY, _EDITOR_NAME,
			new HashMap<String, Object>(), null, null);

		JSONObject configJSONObject = editorConfig.getConfigJSONObject();

		Assert.assertEquals(
			TablesEditorConfigContributor.class.getName(),
			configJSONObject.getString("className"));

		JSONObject toolbarsJSONObject = configJSONObject.getJSONObject(
			"toolbars");

		Assert.assertEquals("link", toolbarsJSONObject.getString("button1"));
		Assert.assertEquals("bold", toolbarsJSONObject.getString("button2"));
		Assert.assertEquals(
			"tablesButton", toolbarsJSONObject.getString("button3"));
	}

	private static final String _CONFIG_KEY = "testEditorConfigKey";

	private static final String _EDITOR_NAME = "testEditorName1";

	private static final String _EDITOR_NAME_2 = "testEditorName2";

	private static final String _PORTLET_NAME = "testPortletName";

	private ServiceRegistration<EditorConfigContributor>
		_editorConfigContributorServiceRegistration1;
	private ServiceRegistration<EditorConfigContributor>
		_editorConfigContributorServiceRegistration2;

	private class EmoticonsEditorConfigContributor
		implements EditorConfigContributor {

		@Override
		public void populateConfigJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {

			jsonObject.put(
				"className", EmoticonsEditorConfigContributor.class.getName());

			JSONObject toolbarsJSONObject = jsonObject.getJSONObject(
				"toolbars");

			if (toolbarsJSONObject == null) {
				toolbarsJSONObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("toolbars", toolbarsJSONObject);
			}

			toolbarsJSONObject.put("button3", "emoticons");
		}

		@Override
		public void populateOptionsJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {
		}

	}

	private class ImageEditorConfigContributor
		implements EditorConfigContributor {

		@Override
		public void populateConfigJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {

			jsonObject.put(
				"className", ImageEditorConfigContributor.class.getName());

			JSONObject toolbarsJSONObject = JSONFactoryUtil.createJSONObject();

			toolbarsJSONObject.put("button1", "image");
			toolbarsJSONObject.put("button2", "gif");

			jsonObject.put("toolbars", toolbarsJSONObject);
		}

		@Override
		public void populateOptionsJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {
		}

	}

	private class TablesEditorConfigContributor
		implements EditorConfigContributor {

		@Override
		public void populateConfigJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {

			jsonObject.put(
				"className", TablesEditorConfigContributor.class.getName());

			JSONObject toolbarsJSONObject = jsonObject.getJSONObject(
				"toolbars");

			if (toolbarsJSONObject == null) {
				toolbarsJSONObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("toolbars", toolbarsJSONObject);
			}

			toolbarsJSONObject.put("button3", "tablesButton");
		}

		@Override
		public void populateOptionsJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {
		}

	}

	private class TextFormatEditorConfigContributor
		implements EditorConfigContributor {

		@Override
		public void populateConfigJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {

			jsonObject.put(
				"className", TextFormatEditorConfigContributor.class.getName());

			JSONObject toolbarsJSONObject = jsonObject.getJSONObject(
				"toolbars");

			if (toolbarsJSONObject == null) {
				toolbarsJSONObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("toolbars", toolbarsJSONObject);
			}

			toolbarsJSONObject.put("button1", "link");
			toolbarsJSONObject.put("button2", "bold");

			jsonObject.put("toolbars", toolbarsJSONObject);
		}

		@Override
		public void populateOptionsJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {
		}

	}

	private class VideoEditorConfigContributor
		implements EditorConfigContributor {

		@Override
		public void populateConfigJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {

			JSONObject toolbarsJSONObject = jsonObject.getJSONObject(
				"toolbars");

			if (toolbarsJSONObject == null) {
				toolbarsJSONObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("toolbars", toolbarsJSONObject);
			}

			toolbarsJSONObject.put("button2", "play");
			toolbarsJSONObject.put("button3", "stop");
		}

		@Override
		public void populateOptionsJSONObject(
			JSONObject jsonObject,
			Map<String, Object> inputEditorTaglibAttributes,
			ThemeDisplay themeDisplay,
			LiferayPortletResponse liferayPortletResponse) {
		}

	}

}