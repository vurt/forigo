package com.vurt.node.server.bean;

import java.io.Serializable;

import com.chinacreator.c2.annotation.Column;
import com.chinacreator.c2.annotation.ColumnType;
import com.chinacreator.c2.annotation.Entity;
import com.chinacreator.c2.annotation.SortType;

/**
 * 节点分组
 * @author 
 * @generated
 */
@Entity(id = "entity:com.vurt.node.server.bean.Group", table = "TD_GROUP", ds = "mainds")
public class Group implements Serializable {
	private static final long serialVersionUID = 1139816613609472L;
	/**
	 *分组名
	 */
	@Column(id = "name", type = ColumnType.uuid, datatype = "string128", sort = SortType.asc)
	private java.lang.String name;

	/**
	 *应用版本
	 */
	@Column(id = "version", association = true)
	private ApplicationVersion version;

	/**
	 * 设置分组名
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 获取分组名
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * 设置应用版本
	 */
	public void setVersion(ApplicationVersion version) {
		this.version = version;
	}

	/**
	 * 获取应用版本
	 */
	public ApplicationVersion getVersion() {
		return version;
	}
}
