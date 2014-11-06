package com.vurt.node.agent.comunication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * 节点服务器Channel工厂
 * Created by Vurt on 14/11/4.
 */
public class NodeServerChannelFactory {

    /**
     * 创建一个新的与服务器通讯的Channel，工厂只负责创建，如需关闭需要手动调用：<br>
     * <li>{@link com.rabbitmq.client.Channel#close()}</li>
     * <li>{@link com.rabbitmq.client.Channel#getConnection()}然后{@link com.rabbitmq.client.Connection#close()}</li>
     *
     * @return 新的Channel
     * @throws IOException 与服务器建立连接失败
     */
    public static Channel createChannel() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("59.63.174.56");
        factory.setUsername("nodeagent");
        factory.setPassword("qweasdzxc");
        factory.setVirtualHost("node");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}