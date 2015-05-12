package com.vurt.node.agent.command;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;
import com.vurt.node.agent.application.ApplicationOperator;
import com.vurt.node.agent.application.ExecuteThread;
import com.vurt.node.agent.util.ChannelHolder;
import com.vurt.node.agent.util.ConfigManager;
import com.vurt.node.data.Constants;
import com.vurt.node.data.Command;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Model;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Vurt on 15/5/12.
 */
public class CommandThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(CommandThread.class);

    private Channel channel;

    private String queueName;

    private String nodeId;

    private QueueingConsumer consumer;

    private boolean running;

    public CommandThread() {
        try {
            channel = ChannelHolder.createChannel();
            channel.exchangeDeclare(Constants.MQ_EXCHANGE_APPLICATION, "topic");

            queueName = channel.queueDeclare().getQueue();

            nodeId = ConfigManager.getInstance().getConfig(Constants.NODE_ID);

            //初始连接时，先把节点分到_none_分组
            //服务端在接到初始心跳之后，会通过group._node_.{nodeid}的exchanged将当前节点的真实分组发送回来，并将应该部署的最新应用版本也一起发送回来
            channel.queueBind(queueName, Constants.MQ_EXCHANGE_APPLICATION, "group." + Constants.NONE_GROUP_NAME + "." + nodeId);

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);

            running = true;
        } catch (IOException e) {
            logger.error("与服务器的通讯通道建立失败！", e);
        }
    }

    @Override
    public void run() {
        while (running) {
            QueueingConsumer.Delivery delivery;
            try {
                delivery = consumer.nextDelivery();
                //收到的第一条消息一定是带有group信息的GroupApplicationAssignment
                Command cmd = JSON.parseObject(delivery.getBody(), Command.class);
                if (StringUtils.isNotEmpty(cmd.getGroup()) && !"_none_".equals(cmd.getGroup())) {
                    //修改分组
                    channel.queueUnbind(queueName, Constants.MQ_EXCHANGE_APPLICATION, "group." + Constants.NONE_GROUP_NAME + "." + nodeId);
                    channel.queueBind(queueName, Constants.MQ_EXCHANGE_APPLICATION, "group." + cmd.getGroup() + "." + nodeId);
                    //TODO 有没有必要？
                    consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, false, consumer);
                }
                if (cmd.getApplication() != null) {
                    Model newModel = cmd.getApplication();
                    Model currModel = ApplicationOperator.getApplicationModel();
                    boolean hasChange = false;
                    if (currModel == null) {
                        ApplicationOperator.writeApplicationModel(cmd.getApplication());
                        hasChange = true;
                    } else {
                        if (ApplicationOperator.equals(currModel, newModel)) {
                            //坐标没变，作为没变化处理
                        } else {
                            ApplicationOperator.writeApplicationModel(newModel);
                            hasChange = true;
                        }
                    }
                    if (hasChange) {
                        //重启应用
                        ExecuteThread thread = ExecuteThread.getCurrThread();
                        if (thread != null) {
                            thread.stopApplication();
                        }
                        thread = new ExecuteThread();
                        thread.start();
                    }
                }
            } catch (ShutdownSignalException e) {
                e.printStackTrace();
            } catch (ConsumerCancelledException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        running = false;
        Connection connection = channel.getConnection();
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            System.err.println("关闭服务器清理Channel时发生错误");
            e.fillInStackTrace();
        }
    }
}
