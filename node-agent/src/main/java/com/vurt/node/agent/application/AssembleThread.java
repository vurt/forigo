package com.vurt.node.agent.application;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.vurt.node.data.Constants;
import com.vurt.node.agent.util.ChannelHolder;
import com.vurt.node.agent.util.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 应用组装线程
 * <p>在启动时拉取当前节点应用的最新状态，检查与当前节点内部状态是否一致，不一致则更新</p>
 * <p>启动后接收MQ的应用状态修改广播，接到广播数据后按请求变更应用状态</p>
 */
public class AssembleThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(AssembleThread.class);

    private Channel channel;

    private QueueingConsumer consumer;

    public AssembleThread() {
        setDaemon(false);
        try {
            channel = ChannelHolder.createChannel();
            channel.exchangeDeclare(Constants.MQ_EXCHANGE_APPLICATION, "topic");
            String queueName = channel.queueDeclare().getQueue();

            String node = ConfigManager.getInstance().getConfig(Constants.NODE_ID);
            channel.queueBind(queueName, Constants.MQ_EXCHANGE_APPLICATION, "app.node." + node);

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);

        } catch (IOException e) {
            logger.error("与服务器的通讯通道建立失败！", e);
        }
    }

    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            QueueingConsumer.Delivery delivery;
            try {
                delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");
            } catch (ShutdownSignalException e) {
                e.printStackTrace();
            } catch (ConsumerCancelledException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止应用组装线程
     */
    public void stopAssembleThread() {
        this.running = false;
    }
}
