package com.vurt.node.server.bean;

import java.io.Serializable;

import com.chinacreator.c2.annotation.Column;
import com.chinacreator.c2.annotation.ColumnType;
import com.chinacreator.c2.annotation.Entity;

/**
 * 应用版本
 * @author 
 * @generated
 */
@Entity(id = "entity:com.vurt.node.server.bean.ApplicationVersions", table = "TD_APP_VERSION")
public class ApplicationVersions implements Serializable {
	private static final long serialVersionUID = 876577628536832L;
	/**
	 *版本id
	 */
	@Column(id = "id_", type = ColumnType.uuid, datatype = "char22")
	private java.lang.String id;

	/**
	 *应用id
	 */
	@Column(id = "app_id", datatype = "string64")
	private java.lang.String appId;

	/**
	 *版本号
	 */
	@Column(id = "version", datatype = "string128")
	private java.lang.String version;

	/**
	 * 设置版本id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 获取版本id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * 设置应用id
	 */
	public void setAppId(java.lang.String appId) {
		this.appId = appId;
	}

	/**
	 * 获取应用id
	 */
	public java.lang.String getAppId() {
		return appId;
	}

	/**
	 * 设置版本号
	 */
	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	/**
	 * 获取版本号
	 */
	public java.lang.String getVersion() {
		return version;
	}
}
