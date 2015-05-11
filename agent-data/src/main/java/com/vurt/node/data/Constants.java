package com.vurt.node.data;

/**
 * 节点代理的所有常量
 */
public interface Constants {

    /**
     * 节点信息：id
     */
    String NODE_ID = "node.id";

    /**
     * 节点信息：经纬度
     */
    String NODE_POSITION = "node.position";

    /**
     * 节点信息：地址
     */
    String NODE_ADDRESS = "node.address";

    /**
     * 消息渠道：心跳
     */
    String MQ_CHANNEL_HEARTBEAT = "heartbeat";

    /**
     * 文件分发Exchange
     */
    String MQ_EXCHANGE_APPLICATION = "application";
}
