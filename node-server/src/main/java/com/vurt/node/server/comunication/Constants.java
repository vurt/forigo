package com.vurt.node.server.comunication;

/**
 * 消息中间件常量
 * @author Vurt
 *
 */
public interface Constants {
	/**
	 * 用于心跳通讯
	 */
	public static final String CHANNEL_HEARTBEAT="heartbeat";
	
	/**
	 * 文件分发Exchange
	 */
	public static final String EXCHANGE_FILE="fileDistributor";
	
	
	/**
	 * 未分组节点的severity字符串
	 */
	public static final String EMPTY_GROUP_SEVERITY="~~emptygroup";
	
	/**
	 * 所有节点的severity字符串
	 */
	public static final String ALL_GROUP_SEVERITY="~~allgroups";
}
