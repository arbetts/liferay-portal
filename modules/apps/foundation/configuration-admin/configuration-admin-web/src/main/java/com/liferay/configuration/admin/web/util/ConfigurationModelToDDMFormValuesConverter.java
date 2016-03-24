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

package com.liferay.configuration.admin.web.util;

import com.liferay.configuration.admin.web.model.ConfigurationModel;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.osgi.service.cm.Configuration;
import org.osgi.service.metatype.AttributeDefinition;

/**
 * @author Marcellus Tavares
 */
public class ConfigurationModelToDDMFormValuesConverter {

	public ConfigurationModelToDDMFormValuesConverter(
		Locale locale, ResourceBundle resourceBundle) {

		_locale = locale;
		_resourceBundle = resourceBundle;
	}

	public DDMFormValues getDDMFormValues(
		ConfigurationModel configurationModel, DDMForm ddmForm) {

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.addAvailableLocale(_locale);
		ddmFormValues.setDefaultLocale(_locale);

		addDDMFormFieldValues(configurationModel, ddmFormValues);

		return ddmFormValues;
	}

	protected void addDDMFormFieldValues(
		ConfigurationModel configurationModel, DDMFormValues ddmFormValues) {

		DDMForm ddmForm = ddmFormValues.getDDMForm();

		AttributeDefinition[] attributeDefinitions =
			configurationModel.getAttributeDefinitions(ConfigurationModel.ALL);

		if (attributeDefinitions == null) {
			return;
		}

		Configuration configuration = configurationModel.getConfiguration();

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String[] values = null;

			if (configuration != null) {
				values = AttributeDefinitionUtil.getProperty(
					attributeDefinition, configuration);
			}
			else {
				values = AttributeDefinitionUtil.getDefaultValue(
					attributeDefinition);
			}

			for (String value : values) {
				DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

				ddmFormFieldValue.setName(attributeDefinition.getID());
				ddmFormFieldValue.setInstanceId(StringUtil.randomString());

				setDDMFormFieldValueLocalizedValue(
					value, ddmFormFieldValue, ddmForm);

				ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
			}
		}
	}

	protected void setDDMFormFieldValueLocalizedValue(
		String value, DDMFormFieldValue ddmFormFieldValue, DDMForm ddmForm) {

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(false);

		DDMFormField ddmFormField = ddmFormFieldsMap.get(
			ddmFormFieldValue.getName());

		String type = ddmFormField.getType();

		if (type.equals(DDMFormFieldType.SELECT)) {
			value = "[\"" + value + "\"]";
		}

		LocalizedValue localizedValue = new LocalizedValue(_locale);

		if ((_resourceBundle != null) && (value != null)) {
			String resourceBundleValue = null;

			resourceBundleValue = ResourceBundleUtil.getString(_resourceBundle, value);

			if (resourceBundleValue != null) {
				value = resourceBundleValue;
			}
		}

		localizedValue.addString(_locale, value);

		ddmFormFieldValue.setValue(localizedValue);
	}

	private final Locale _locale;
	private final ResourceBundle _resourceBundle;

}