package com.vurt.node.client.web.controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by Vurt on 14/11/4.
 */
public class Publisher {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("59.63.174.56");
        factory.setUsername("nodeagent");
        factory.setPassword("qweasdzxc");
        factory.setVirtualHost("node");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

    }
}
