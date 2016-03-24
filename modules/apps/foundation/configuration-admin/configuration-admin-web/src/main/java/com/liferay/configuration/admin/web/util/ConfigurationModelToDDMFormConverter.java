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
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.FieldConstants;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Kamesh Sampath
 * @author Raymond Augé
 * @author Marcellus Tavares
 */
public class ConfigurationModelToDDMFormConverter {

	public ConfigurationModelToDDMFormConverter(
		ConfigurationModel configurationModel, Locale locale,
		ResourceBundle resourceBundle) {

		_configurationModel = configurationModel;
		_locale = locale;
		_resourceBundle = resourceBundle;
	}

	public DDMForm getDDMForm() {
		DDMForm ddmForm = new DDMForm();

		ddmForm.addAvailableLocale(_locale);
		ddmForm.setDefaultLocale(_locale);

		addRequiredDDMFormFields(ddmForm);
		addOptionalDDMFormFields(ddmForm);

		return ddmForm;
	}

	protected void addDDMFormFields(
		AttributeDefinition[] attributeDefinitions, DDMForm ddmForm,
		boolean required) {

		if (attributeDefinitions == null) {
			return;
		}

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String type = getDDMFormFieldType(attributeDefinition);

			DDMFormField ddmFormField = new DDMFormField(
				attributeDefinition.getID(), type);

			String dataType = getDDMFormFieldDataType(
				attributeDefinition);

			ddmFormField.setDataType(dataType);

			LocalizedValue label = new LocalizedValue(_locale);

			label.addString(_locale, translate(attributeDefinition.getName()));

			ddmFormField.setLabel(label);

			setDDMFormFieldOptions(attributeDefinition, ddmFormField);
			setDDMFormFieldPredefinedValue(ddmFormField);

			LocalizedValue tip = new LocalizedValue(_locale);

			tip.addString(
				_locale, translate(attributeDefinition.getDescription()));

			ddmFormField.setTip(tip);

			ddmFormField.setLocalizable(true);

			if (!DDMFormFieldType.CHECKBOX.equals(ddmFormField.getType())) {
				ddmFormField.setRequired(required);
			}

			ddmFormField.setShowLabel(true);

			if (attributeDefinition.getCardinality() != 0) {
				ddmFormField.setRepeatable(true);
			}

			if (Validator.equals(dataType, FieldConstants.STRING)) {
				ddmFormField.setProperty("displayStyle", "multiline");
			}

			ddmForm.addDDMFormField(ddmFormField);
		}
	}

	protected void addOptionalDDMFormFields(DDMForm ddmForm) {
		AttributeDefinition[] optionalAttributeDefinitions =
			_configurationModel.getAttributeDefinitions(
				ObjectClassDefinition.OPTIONAL);

		addDDMFormFields(optionalAttributeDefinitions, ddmForm, false);
	}

	protected void addRequiredDDMFormFields(DDMForm ddmForm) {
		AttributeDefinition[] requiredAttributeDefinitions =
			_configurationModel.getAttributeDefinitions(
				ObjectClassDefinition.REQUIRED);

		addDDMFormFields(requiredAttributeDefinitions, ddmForm, true);
	}

	protected String getDDMFormFieldDataType(
		AttributeDefinition attributeDefinition) {

		int type = attributeDefinition.getType();

		if (type == AttributeDefinition.BOOLEAN) {
			return FieldConstants.BOOLEAN;
		}
		else if (type == AttributeDefinition.DOUBLE) {
			return FieldConstants.DOUBLE;
		}
		else if (type == AttributeDefinition.FLOAT) {
			return FieldConstants.FLOAT;
		}
		else if (type == AttributeDefinition.INTEGER) {
			return FieldConstants.INTEGER;
		}
		else if (type == AttributeDefinition.LONG) {
			return FieldConstants.LONG;
		}
		else if (type == AttributeDefinition.SHORT) {
			return FieldConstants.SHORT;
		}

		return FieldConstants.STRING;
	}

	protected String getDDMFormFieldPredefinedValue(String dataType) {
		if (dataType.equals(FieldConstants.BOOLEAN)) {
			return "false";
		}
		else if (dataType.equals(FieldConstants.DOUBLE) ||
				 dataType.equals(FieldConstants.FLOAT)) {

			return "0.0";
		}
		else if (dataType.equals(FieldConstants.INTEGER) ||
				 dataType.equals(FieldConstants.LONG) ||
				 dataType.equals(FieldConstants.SHORT)) {

			return "0";
		}

		return StringPool.BLANK;
	}

	protected String getDDMFormFieldType(
		AttributeDefinition attributeDefinition) {

		int type = attributeDefinition.getType();

		if (type == AttributeDefinition.BOOLEAN) {
			String[] optionLabels = attributeDefinition.getOptionLabels();

			if (ArrayUtil.isEmpty(optionLabels)) {
				return DDMFormFieldType.CHECKBOX;
			}

			return DDMFormFieldType.RADIO;
		}

		if (ArrayUtil.isNotEmpty(attributeDefinition.getOptionLabels()) ||
			ArrayUtil.isNotEmpty(attributeDefinition.getOptionValues())) {

			return DDMFormFieldType.SELECT;
		}

		return DDMFormFieldType.TEXT;
	}

	protected void setDDMFormFieldOptions(
		AttributeDefinition attributeDefinition, DDMFormField ddmFormField) {

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		String[] optionLabels = attributeDefinition.getOptionLabels();
		String[] optionValues = attributeDefinition.getOptionValues();

		if ((optionLabels != null) && (optionValues != null)) {
			for (int i = 0; i < optionLabels.length; i++) {
				ddmFormFieldOptions.addOptionLabel(
					optionValues[i], _locale, translate(optionLabels[i]));
			}
		}

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);
	}

	protected void setDDMFormFieldPredefinedValue(DDMFormField ddmFormField) {
		String dataType = ddmFormField.getDataType();

		String predefinedValueString = getDDMFormFieldPredefinedValue(dataType);

		String type = ddmFormField.getType();

		if (type.equals(DDMFormFieldType.SELECT)) {
			predefinedValueString = "[\"" + predefinedValueString + "\"]";
		}

		LocalizedValue predefinedValue = new LocalizedValue(_locale);

		predefinedValue.addString(_locale, translate(predefinedValueString));

		ddmFormField.setPredefinedValue(predefinedValue);
	}

	protected String translate(String key) {
		if ((_resourceBundle == null) || (key == null)) {
			return key;
		}

		String value = ResourceBundleUtil.getString(_resourceBundle, key);

		if (value == null) {
			return key;
		}

		return value;
	}

	private final ConfigurationModel _configurationModel;
	private final Locale _locale;
	private final ResourceBundle _resourceBundle;

}