package com.vurt.node.agent.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.maven.cli.MavenCli;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;

/**
 * 节点服务器Channel工厂
 * Created by Vurt on 14/11/4.
 */
public class ChannelHolder {

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

        // fig_node_agent psdwjl351
        factory.setHost("182.105.146.15");
        factory.setUsername("fig_node_agent");
        factory.setPassword("psdwjl351");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
