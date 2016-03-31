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

package com.liferay.dynamic.data.mapping.model;

import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pablo Carvalho
 * @author Marcellus Tavares
 */
public class DDMFormField implements Serializable {

	public DDMFormField() {
	}

	public DDMFormField(DDMFormField ddmFormField) {
		if (ddmFormField._properties != null) {
			_properties = new LinkedHashMap<>(ddmFormField._properties);
		}

		setDDMFormFieldOptions(
			new DDMFormFieldOptions(ddmFormField.getDDMFormFieldOptions()));

		DDMFormFieldValidation ddmFormFieldValidation =
			ddmFormField.getDDMFormFieldValidation();

		if (ddmFormFieldValidation != null) {
			setDDMFormFieldValidation(ddmFormFieldValidation);
		}

		setLabel(new LocalizedValue(ddmFormField.getLabel()));
		setPredefinedValue(
			new LocalizedValue(ddmFormField.getPredefinedValue()));
		setStyle(new LocalizedValue(ddmFormField.getStyle()));
		setTip(new LocalizedValue(ddmFormField.getTip()));

		for (DDMFormField nestedDDMFormField :
				ddmFormField._nestedDDMFormFields) {

			addNestedDDMFormField(nestedDDMFormField);
		}
	}

	public DDMFormField(String name, String type) {
		setName(name);
		setType(type);

		setDDMFormFieldOptions(new DDMFormFieldOptions());
		setLabel(new LocalizedValue());
		setPredefinedValue(new LocalizedValue());
		setStyle(new LocalizedValue());
		setTip(new LocalizedValue());
	}

	public void addNestedDDMFormField(DDMFormField nestedDDMFormField) {
		nestedDDMFormField.setDDMForm(_ddmForm);

		_nestedDDMFormFields.add(nestedDDMFormField);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMFormField)) {
			return false;
		}

		DDMFormField ddmFormField = (DDMFormField)obj;

		if (!Validator.equals(_dataType, ddmFormField._dataType) ||
			!Validator.equals(
				_ddmFormFieldOptions, ddmFormField._ddmFormFieldOptions) ||
			!Validator.equals(
				_ddmFormFieldValidation,
				ddmFormField._ddmFormFieldValidation) ||
			!Validator.equals(
				_fieldNamespace, ddmFormField._fieldNamespace) ||
			!Validator.equals(_indexType, ddmFormField._indexType) ||
			!Validator.equals(_label, ddmFormField._label) ||
			!Validator.equals(_localizable, ddmFormField._localizable) ||
			!Validator.equals(_multiple, ddmFormField._multiple) ||
			!Validator.equals(_name, ddmFormField._name) ||
			Validator.equals(
				_nestedDDMFormFields, ddmFormField._nestedDDMFormFields) ||
			!Validator.equals(
				_predefinedValue, ddmFormField._predefinedValue) ||
			!Validator.equals(_properties, ddmFormField._properties) ||
			!Validator.equals(_readOnly, ddmFormField._readOnly) ||
			!Validator.equals(_repeatable, ddmFormField._repeatable) ||
			!Validator.equals(_required, ddmFormField._required) ||
			!Validator.equals(_showLabel, ddmFormField._showLabel) ||
			!Validator.equals(_style, ddmFormField._style) ||
			!Validator.equals(_tip, ddmFormField._tip) ||
			!Validator.equals(_type, ddmFormField._type) ||
			!Validator.equals(
				_visibilityExpression, ddmFormField._visibilityExpression)) {

			return false;
		}

		return true;
	}

	public String getDataType() {
		return _dataType;
	}

	public DDMForm getDDMForm() {
		return _ddmForm;
	}

	public DDMFormFieldOptions getDDMFormFieldOptions() {
		return _ddmFormFieldOptions;
	}

	public DDMFormFieldValidation getDDMFormFieldValidation() {
		return _ddmFormFieldValidation;
	}

	public String getFieldNamespace() {
		return _fieldNamespace;
	}

	public String getIndexType() {
		return _indexType;
	}

	public LocalizedValue getLabel() {
		return _label;
	}

	public String getName() {
		return _name;
	}

	public List<DDMFormField> getNestedDDMFormFields() {
		return _nestedDDMFormFields;
	}

	public Map<String, DDMFormField> getNestedDDMFormFieldsMap() {
		Map<String, DDMFormField> nestedDDMFormFieldsMap =
			new LinkedHashMap<>();

		for (DDMFormField nestedDDMFormField : _nestedDDMFormFields) {
			nestedDDMFormFieldsMap.put(
				nestedDDMFormField.getName(), nestedDDMFormField);

			nestedDDMFormFieldsMap.putAll(
				nestedDDMFormField.getNestedDDMFormFieldsMap());
		}

		return nestedDDMFormFieldsMap;
	}

	public LocalizedValue getPredefinedValue() {
		return _predefinedValue;
	}

	public Map<String, Object> getProperties() {
		if (_properties != null) {
			return _properties;
		}

		_properties = new LinkedHashMap<>();

		return _properties;
	}

	public Object getProperty(String name) {
		if (_properties != null) {
			return _properties.get(name);
		}

		return null;
	}

	public LocalizedValue getStyle() {
		return _style;
	}

	public LocalizedValue getTip() {
		return _tip;
	}

	public String getType() {
		return _type;
	}

	public String getVisibilityExpression() {
		return _visibilityExpression;
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _properties);

		hash = HashUtil.hash(hash, _dataType);
		hash = HashUtil.hash(hash, _ddmFormFieldOptions);
		hash = HashUtil.hash(hash, _ddmFormFieldValidation);
		hash = HashUtil.hash(hash, _fieldNamespace);
		hash = HashUtil.hash(hash, _indexType);
		hash = HashUtil.hash(hash, _label);
		hash = HashUtil.hash(hash, _localizable);
		hash = HashUtil.hash(hash, _multiple);
		hash = HashUtil.hash(hash, _name);
		hash = HashUtil.hash(hash, _predefinedValue);
		hash = HashUtil.hash(hash, _readOnly);
		hash = HashUtil.hash(hash, _repeatable);
		hash = HashUtil.hash(hash, _required);
		hash = HashUtil.hash(hash, _showLabel);
		hash = HashUtil.hash(hash, _style);
		hash = HashUtil.hash(hash, _tip);
		hash = HashUtil.hash(hash, _type);
		hash = HashUtil.hash(hash, _visibilityExpression);

		return HashUtil.hash(hash, _nestedDDMFormFields);
	}

	public boolean isLocalizable() {
		return _localizable;
	}

	public boolean isMultiple() {
		return _multiple;
	}

	public boolean isReadOnly() {
		return _readOnly;
	}

	public boolean isRepeatable() {
		return _repeatable;
	}

	public boolean isRequired() {
		return _required;
	}

	public boolean isShowLabel() {
		return _showLabel;
	}

	public boolean isTransient() {
		if (Validator.isNull(_dataType)) {
			return true;
		}

		return false;
	}

	public void setDataType(String dataType) {
		_dataType = dataType;
	}

	public void setDDMForm(DDMForm ddmForm) {
		for (DDMFormField nestedDDMFormField : _nestedDDMFormFields) {
			nestedDDMFormField.setDDMForm(ddmForm);
		}

		_ddmForm = ddmForm;
	}

	public void setDDMFormFieldOptions(
		DDMFormFieldOptions ddmFormFieldOptions) {

		_ddmFormFieldOptions = ddmFormFieldOptions;
	}

	public void setDDMFormFieldValidation(
		DDMFormFieldValidation ddmFormFieldValidation) {

		_ddmFormFieldValidation = ddmFormFieldValidation;
	}

	public void setFieldNamespace(String fieldNamespace) {
		_fieldNamespace = fieldNamespace;
	}

	public void setIndexType(String indexType) {
		_indexType = indexType;
	}

	public void setLabel(LocalizedValue label) {
		_label = label;
	}

	public void setLocalizable(boolean localizable) {
		_localizable = localizable;
	}

	public void setMultiple(boolean multiple) {
		_multiple = multiple;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setNestedDDMFormFields(List<DDMFormField> nestedDDMFormFields) {
		_nestedDDMFormFields = nestedDDMFormFields;
	}

	public void setPredefinedValue(LocalizedValue predefinedValue) {
		_predefinedValue = predefinedValue;
	}

	public void setProperty(String name, Object value) {
		if (_properties == null) {
			_properties = new LinkedHashMap<>();
		}

		_properties.put(name, value);
	}

	public void setReadOnly(boolean readOnly) {
		_readOnly = readOnly;
	}

	public void setRepeatable(boolean repeatable) {
		_repeatable = repeatable;
	}

	public void setRequired(boolean required) {
		_required = required;
	}

	public void setShowLabel(boolean showLabel) {
		_showLabel = showLabel;
	}

	public void setStyle(LocalizedValue style) {
		_style = style;
	}

	public void setTip(LocalizedValue tip) {
		_tip = tip;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setVisibilityExpression(String visibilityExpression) {
		_visibilityExpression = visibilityExpression;
	}

	private String _dataType;
	private DDMForm _ddmForm;
	private DDMFormFieldOptions _ddmFormFieldOptions;
	private DDMFormFieldValidation _ddmFormFieldValidation;
	private String _fieldNamespace;
	private String _indexType;
	private LocalizedValue _label;
	private boolean _localizable;
	private boolean _multiple;
	private String _name;
	private List<DDMFormField> _nestedDDMFormFields = new ArrayList<>();
	private LocalizedValue _predefinedValue;
	private Map<String, Object> _properties;
	private boolean _readOnly;
	private boolean _repeatable;
	private boolean _required;
	private boolean _showLabel;
	private LocalizedValue _style;
	private LocalizedValue _tip;
	private String _type;
	private String _visibilityExpression;

}