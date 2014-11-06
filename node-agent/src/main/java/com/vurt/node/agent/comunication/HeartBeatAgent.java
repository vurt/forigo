package com.vurt.node.agent.comunication;

import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * Created by Vurt on 14/11/4.
 */
public class HeartBeatAgent {
    private Channel channel;

    public HeartBeatAgent() {
        try {
            this.channel = NodeServerChannelFactory.createChannel();
        } catch (IOException e) {

        }
    }
}
