package com.vurt.node.server.bean;

import java.io.Serializable;

import com.chinacreator.c2.annotation.Column;
import com.chinacreator.c2.annotation.ColumnType;
import com.chinacreator.c2.annotation.Entity;
import com.chinacreator.c2.annotation.SortType;

/**
 * 系统元数据
 * @author 
 * @generated
 */
@Entity(id = "entity:com.vurt.node.server.bean.Metadata", table = "td_metadata", ds = "mainds")
public class Metadata implements Serializable {
	private static final long serialVersionUID = 1143815877214208L;
	/**
	 *元数据id
	 */
	@Column(id = "id", type = ColumnType.increment, datatype = "int")
	private java.lang.Integer id;

	/**
	 *settings文件路径
	 */
	@Column(id = "settings_xml", datatype = "string256")
	private java.lang.String settingsXml;

	/**
	 *查看配置文件的url
	 */
	@Column(id = "url", datatype = "string256")
	private java.lang.String url;

	/**
	 *创建时间
	 */
	@Column(id = "createAt", datatype = "timestamp", sort = SortType.desc)
	private java.sql.Timestamp createat;

	/**
	 * 设置元数据id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * 获取元数据id
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * 设置settings文件路径
	 */
	public void setSettingsXml(java.lang.String settingsXml) {
		this.settingsXml = settingsXml;
	}

	/**
	 * 获取settings文件路径
	 */
	public java.lang.String getSettingsXml() {
		return settingsXml;
	}

	/**
	 * 设置查看配置文件的url
	 */
	public void setUrl(java.lang.String url) {
		this.url = url;
	}

	/**
	 * 获取查看配置文件的url
	 */
	public java.lang.String getUrl() {
		return url;
	}

	/**
	 * 设置创建时间
	 */
	public void setCreateat(java.sql.Timestamp createat) {
		this.createat = createat;
	}

	/**
	 * 获取创建时间
	 */
	public java.sql.Timestamp getCreateat() {
		return createat;
	}
}
