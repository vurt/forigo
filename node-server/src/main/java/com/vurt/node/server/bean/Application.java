package com.vurt.node.server.bean;

import java.io.Serializable;
import java.util.Collection;

import com.chinacreator.c2.annotation.Children;
import com.chinacreator.c2.annotation.Column;
import com.chinacreator.c2.annotation.ColumnType;
import com.chinacreator.c2.annotation.Entity;

/**
 * 应用
 * @author 
 * @generated
 */
@Entity(id = "entity:com.vurt.node.server.bean.Application", table = "TD_APP")
public class Application implements Serializable {
	private static final long serialVersionUID = 876574877876224L;
	/**
	 *应用id
	 */
	@Column(id = "id_", type = ColumnType.uuid, datatype = "string64")
	private java.lang.String id;

	/**
	 *应用名称
	 */
	@Column(id = "name", datatype = "string128")
	private java.lang.String name;

	/**
	 *应用详细描述
	 */
	@Column(id = "desc", datatype = "string1024")
	private java.lang.String desc;

	/**
	 *应用版本
	 */
	@Children(id = "versions", fk = "id_:app_id")
	private Collection<ApplicationVersions> versions;

	/**
	 * 设置应用id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 获取应用id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * 设置应用名称
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 获取应用名称
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * 设置应用详细描述
	 */
	public void setDesc(java.lang.String desc) {
		this.desc = desc;
	}

	/**
	 * 获取应用详细描述
	 */
	public java.lang.String getDesc() {
		return desc;
	}

	/**
	 * 设置应用版本
	 */
	public void setVersions(Collection<ApplicationVersions> versions) {
		this.versions = versions;
	}

	/**
	 * 获取应用版本
	 */
	public Collection<ApplicationVersions> getVersions() {
		return versions;
	}
}
