package com.vurt.node.server.bean;

import java.io.Serializable;

import com.chinacreator.c2.annotation.Column;
import com.chinacreator.c2.annotation.ColumnType;
import com.chinacreator.c2.annotation.Entity;
import com.chinacreator.c2.annotation.SortType;

/**
 * 文件同步信息
 * @author 
 * @generated
 */
@Entity(id = "entity:com.vurt.node.server.bean.FileSyncInfo", table = "TD_FILE_SYNC_INFO")
public class FileSyncInfo implements Serializable {
	private static final long serialVersionUID = 883835140112384L;
	/**
	 *id
	 */
	@Column(id = "id_", type = ColumnType.uuid, datatype = "char22")
	private java.lang.String id;

	/**
	 *分组名称
	 */
	@Column(id = "group_", datatype = "string64", sort = SortType.asc)
	private java.lang.String group;

	/**
	 *应用文件路径
	 */
	@Column(id = "app", datatype = "string512")
	private java.lang.String app;

	/**
	 *资源目录路径
	 */
	@Column(id = "res", datatype = "string512")
	private java.lang.String res;

	/**
	 *应用文件最后修改时间
	 */
	@Column(id = "app_lastmodify", datatype = "timestamp")
	private java.sql.Timestamp appLastmodify;

	/**
	 *资源目录最后修改时间
	 */
	@Column(id = "res_lastmodify", datatype = "timestamp")
	private java.sql.Timestamp resLastmodify;

	/**
	 * 设置id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 获取id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * 设置分组名称
	 */
	public void setGroup(java.lang.String group) {
		this.group = group;
	}

	/**
	 * 获取分组名称
	 */
	public java.lang.String getGroup() {
		return group;
	}

	/**
	 * 设置应用文件路径
	 */
	public void setApp(java.lang.String app) {
		this.app = app;
	}

	/**
	 * 获取应用文件路径
	 */
	public java.lang.String getApp() {
		return app;
	}

	/**
	 * 设置资源目录路径
	 */
	public void setRes(java.lang.String res) {
		this.res = res;
	}

	/**
	 * 获取资源目录路径
	 */
	public java.lang.String getRes() {
		return res;
	}

	/**
	 * 设置应用文件最后修改时间
	 */
	public void setAppLastmodify(java.sql.Timestamp appLastmodify) {
		this.appLastmodify = appLastmodify;
	}

	/**
	 * 获取应用文件最后修改时间
	 */
	public java.sql.Timestamp getAppLastmodify() {
		return appLastmodify;
	}

	/**
	 * 设置资源目录最后修改时间
	 */
	public void setResLastmodify(java.sql.Timestamp resLastmodify) {
		this.resLastmodify = resLastmodify;
	}

	/**
	 * 获取资源目录最后修改时间
	 */
	public java.sql.Timestamp getResLastmodify() {
		return resLastmodify;
	}
}
