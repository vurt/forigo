package com.vurt.node.server.bean;

import com.chinacreator.c2.annotation.Column;
import com.chinacreator.c2.annotation.ColumnType;
import com.chinacreator.c2.annotation.Entity;
import com.chinacreator.c2.annotation.SortType;

/**
 * 节点
 * @author 
 * @generated
 */
@Entity(id = "entity:com.vurt.node.server.bean.Node", table = "TD_NODE", cache = false)
public class Node {
	/**
	 *节点id
	 */
	@Column(id = "id_", type = ColumnType.uuid, datatype = "string64", sort = SortType.asc)
	private java.lang.String id;

	/**
	 *节点分组
	 */
	@Column(id = "group_", datatype = "string64")
	private java.lang.String group;

	/**
	 *节点位置(经纬度)
	 */
	@Column(id = "position", datatype = "string256")
	private java.lang.String position;

	/**
	 *节点地址
	 */
	@Column(id = "address", datatype = "string1024")
	private java.lang.String address;

	/**
	 *节点备注
	 */
	@Column(id = "remark", datatype = "string1024")
	private java.lang.String remark;

	/**
	 *上次心跳时间
	 */
	@Column(id = "last_heart_beat", datatype = "timestamp")
	private java.sql.Timestamp lastHeartBeat;

	/**
	 *注册时间
	 */
	@Column(id = "reg_time", datatype = "timestamp")
	private java.sql.Timestamp regTime;

	/**
	 * 设置节点id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 获取节点id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * 设置节点分组
	 */
	public void setGroup(java.lang.String group) {
		this.group = group;
	}

	/**
	 * 获取节点分组
	 */
	public java.lang.String getGroup() {
		return group;
	}

	/**
	 * 设置节点位置(经纬度)
	 */
	public void setPosition(java.lang.String position) {
		this.position = position;
	}

	/**
	 * 获取节点位置(经纬度)
	 */
	public java.lang.String getPosition() {
		return position;
	}

	/**
	 * 设置节点地址
	 */
	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	/**
	 * 获取节点地址
	 */
	public java.lang.String getAddress() {
		return address;
	}

	/**
	 * 设置节点备注
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	/**
	 * 获取节点备注
	 */
	public java.lang.String getRemark() {
		return remark;
	}

	/**
	 * 设置上次心跳时间
	 */
	public void setLastHeartBeat(java.sql.Timestamp lastHeartBeat) {
		this.lastHeartBeat = lastHeartBeat;
	}

	/**
	 * 获取上次心跳时间
	 */
	public java.sql.Timestamp getLastHeartBeat() {
		return lastHeartBeat;
	}

	/**
	 * 设置注册时间
	 */
	public void setRegTime(java.sql.Timestamp regTime) {
		this.regTime = regTime;
	}

	/**
	 * 获取注册时间
	 */
	public java.sql.Timestamp getRegTime() {
		return regTime;
	}
}
