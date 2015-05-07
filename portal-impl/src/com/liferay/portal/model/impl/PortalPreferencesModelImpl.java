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

package com.liferay.portal.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.PortalPreferences;
import com.liferay.portal.model.PortalPreferencesModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the PortalPreferences service. Represents a row in the &quot;PortalPreferences&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link PortalPreferencesModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link PortalPreferencesImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PortalPreferencesImpl
 * @see PortalPreferences
 * @see PortalPreferencesModel
 * @generated
 */
@ProviderType
public class PortalPreferencesModelImpl extends BaseModelImpl<PortalPreferences>
	implements PortalPreferencesModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a portal preferences model instance should use the {@link PortalPreferences} interface instead.
	 */
	public static final String TABLE_NAME = "PortalPreferences";
	public static final Object[][] TABLE_COLUMNS = {
			{ "mvccVersion", Types.BIGINT },
			{ "portalPreferencesId", Types.BIGINT },
			{ "ownerId", Types.BIGINT },
			{ "ownerType", Types.INTEGER },
			{ "preferences", Types.CLOB }
		};
	public static final String TABLE_SQL_CREATE = "create table PortalPreferences (mvccVersion LONG default 0,portalPreferencesId LONG not null primary key,ownerId LONG,ownerType INTEGER,preferences TEXT null)";
	public static final String TABLE_SQL_DROP = "drop table PortalPreferences";
	public static final String ORDER_BY_JPQL = " ORDER BY portalPreferences.portalPreferencesId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY PortalPreferences.portalPreferencesId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portal.model.PortalPreferences"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portal.model.PortalPreferences"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.model.PortalPreferences"),
			true);
	public static final long OWNERID_COLUMN_BITMASK = 1L;
	public static final long OWNERTYPE_COLUMN_BITMASK = 2L;
	public static final long PORTALPREFERENCESID_COLUMN_BITMASK = 4L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portal.model.PortalPreferences"));

	public PortalPreferencesModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _portalPreferencesId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setPortalPreferencesId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _portalPreferencesId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return PortalPreferences.class;
	}

	@Override
	public String getModelClassName() {
		return PortalPreferences.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("portalPreferencesId", getPortalPreferencesId());
		attributes.put("ownerId", getOwnerId());
		attributes.put("ownerType", getOwnerType());
		attributes.put("preferences", getPreferences());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long portalPreferencesId = (Long)attributes.get("portalPreferencesId");

		if (portalPreferencesId != null) {
			setPortalPreferencesId(portalPreferencesId);
		}

		Long ownerId = (Long)attributes.get("ownerId");

		if (ownerId != null) {
			setOwnerId(ownerId);
		}

		Integer ownerType = (Integer)attributes.get("ownerType");

		if (ownerType != null) {
			setOwnerType(ownerType);
		}

		String preferences = (String)attributes.get("preferences");

		if (preferences != null) {
			setPreferences(preferences);
		}
	}

	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	@Override
	public long getPortalPreferencesId() {
		return _portalPreferencesId;
	}

	@Override
	public void setPortalPreferencesId(long portalPreferencesId) {
		_portalPreferencesId = portalPreferencesId;
	}

	@Override
	public long getOwnerId() {
		return _ownerId;
	}

	@Override
	public void setOwnerId(long ownerId) {
		_columnBitmask |= OWNERID_COLUMN_BITMASK;

		if (!_setOriginalOwnerId) {
			_setOriginalOwnerId = true;

			_originalOwnerId = _ownerId;
		}

		_ownerId = ownerId;
	}

	public long getOriginalOwnerId() {
		return _originalOwnerId;
	}

	@Override
	public int getOwnerType() {
		return _ownerType;
	}

	@Override
	public void setOwnerType(int ownerType) {
		_columnBitmask |= OWNERTYPE_COLUMN_BITMASK;

		if (!_setOriginalOwnerType) {
			_setOriginalOwnerType = true;

			_originalOwnerType = _ownerType;
		}

		_ownerType = ownerType;
	}

	public int getOriginalOwnerType() {
		return _originalOwnerType;
	}

	@Override
	public String getPreferences() {
		if (_preferences == null) {
			return StringPool.BLANK;
		}
		else {
			return _preferences;
		}
	}

	@Override
	public void setPreferences(String preferences) {
		_preferences = preferences;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(0,
			PortalPreferences.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public PortalPreferences toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (PortalPreferences)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		PortalPreferencesImpl portalPreferencesImpl = new PortalPreferencesImpl();

		portalPreferencesImpl.setMvccVersion(getMvccVersion());
		portalPreferencesImpl.setPortalPreferencesId(getPortalPreferencesId());
		portalPreferencesImpl.setOwnerId(getOwnerId());
		portalPreferencesImpl.setOwnerType(getOwnerType());
		portalPreferencesImpl.setPreferences(getPreferences());

		portalPreferencesImpl.resetOriginalValues();

		return portalPreferencesImpl;
	}

	@Override
	public int compareTo(PortalPreferences portalPreferences) {
		long primaryKey = portalPreferences.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PortalPreferences)) {
			return false;
		}

		PortalPreferences portalPreferences = (PortalPreferences)obj;

		long primaryKey = portalPreferences.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		PortalPreferencesModelImpl portalPreferencesModelImpl = this;

		portalPreferencesModelImpl._originalOwnerId = portalPreferencesModelImpl._ownerId;

		portalPreferencesModelImpl._setOriginalOwnerId = false;

		portalPreferencesModelImpl._originalOwnerType = portalPreferencesModelImpl._ownerType;

		portalPreferencesModelImpl._setOriginalOwnerType = false;

		portalPreferencesModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<PortalPreferences> toCacheModel() {
		PortalPreferencesCacheModel portalPreferencesCacheModel = new PortalPreferencesCacheModel();

		portalPreferencesCacheModel.mvccVersion = getMvccVersion();

		portalPreferencesCacheModel.portalPreferencesId = getPortalPreferencesId();

		portalPreferencesCacheModel.ownerId = getOwnerId();

		portalPreferencesCacheModel.ownerType = getOwnerType();

		portalPreferencesCacheModel.preferences = getPreferences();

		String preferences = portalPreferencesCacheModel.preferences;

		if ((preferences != null) && (preferences.length() == 0)) {
			portalPreferencesCacheModel.preferences = null;
		}

		return portalPreferencesCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(11);

		sb.append("{mvccVersion=");
		sb.append(getMvccVersion());
		sb.append(", portalPreferencesId=");
		sb.append(getPortalPreferencesId());
		sb.append(", ownerId=");
		sb.append(getOwnerId());
		sb.append(", ownerType=");
		sb.append(getOwnerType());
		sb.append(", preferences=");
		sb.append(getPreferences());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(19);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.model.PortalPreferences");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>mvccVersion</column-name><column-value><![CDATA[");
		sb.append(getMvccVersion());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>portalPreferencesId</column-name><column-value><![CDATA[");
		sb.append(getPortalPreferencesId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>ownerId</column-name><column-value><![CDATA[");
		sb.append(getOwnerId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>ownerType</column-name><column-value><![CDATA[");
		sb.append(getOwnerType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>preferences</column-name><column-value><![CDATA[");
		sb.append(getPreferences());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = PortalPreferences.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			PortalPreferences.class
		};
	private long _mvccVersion;
	private long _portalPreferencesId;
	private long _ownerId;
	private long _originalOwnerId;
	private boolean _setOriginalOwnerId;
	private int _ownerType;
	private int _originalOwnerType;
	private boolean _setOriginalOwnerType;
	private String _preferences;
	private long _columnBitmask;
	private PortalPreferences _escapedModel;
}