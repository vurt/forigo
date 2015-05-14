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
@Entity(id = "entity:com.vurt.node.server.bean.ApplicationVersion", table = "TD_APP_VERSION", ds = "mainds")
public class ApplicationVersion implements Serializable {
	private static final long serialVersionUID = 1139803686535168L;
	/**
	 *版本id
	 */
	@Column(id = "id", type = ColumnType.increment, datatype = "int")
	private java.lang.Integer id;

	/**
	 *版本描述
	 */
	@Column(id = "desc", datatype = "string2000")
	private java.lang.String desc;

	/**
	 *版本号
	 */
	@Column(id = "version", datatype = "string128")
	private java.lang.String version;

	/**
	 *pom文件路径
	 */
	@Column(id = "pom", datatype = "string256")
	private java.lang.String pom;

	/**
	 * 设置版本id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * 获取版本id
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * 设置版本描述
	 */
	public void setDesc(java.lang.String desc) {
		this.desc = desc;
	}

	/**
	 * 获取版本描述
	 */
	public java.lang.String getDesc() {
		return desc;
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

	/**
	 * 设置pom文件路径
	 */
	public void setPom(java.lang.String pom) {
		this.pom = pom;
	}

	/**
	 * 获取pom文件路径
	 */
	public java.lang.String getPom() {
		return pom;
	}
}
