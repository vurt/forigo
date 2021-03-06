package com.vurt.node.server.comunication;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.chinacreator.c2.web.init.ServerStartup;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.vurt.node.data.Constants;
import com.vurt.node.data.HeartBeat;
import com.vurt.node.server.bean.Node;
import com.vurt.node.server.service.NodeService;

/**
 * 节点观察者，负责处理节点注册和心跳消息
 * 
 * @author Vurt
 * 
 */
public class HeartBeatObserver extends Thread implements ServerStartup {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HeartBeatObserver.class);
	private Channel channel;
	private QueueingConsumer consumer;

	private NodeService nodeService;

	public HeartBeatObserver() {

	}

	public void close() {
		Connection connection = channel.getConnection();
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			LOGGER.warn("关闭服务器清理Channel时发生错误", e);
		}
	}

	public void startup(ServletContext arg0) {
		try {
			channel = ChannelHolder.createChannel();
			channel.queueDeclare(Constants.MQ_CHANNEL_HEARTBEAT, false, false,
					false, null);
			nodeService = new NodeService();
			consumer = new QueueingConsumer(channel);
			channel.basicConsume(Constants.MQ_CHANNEL_HEARTBEAT, true, consumer);
			this.start();
		} catch (IOException e) {
			LOGGER.error("消息Channel获取失败，应用无法正常工作", e);
		}
	}

	@Override
	public void run() {
		LOGGER.info("节点监控线程已启动");
		while (true) {
			try {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				LOGGER.debug("收到心跳信息:" + new String(delivery.getBody()));
				// 用JSON，懒得搞二进制协议
				HeartBeat heartBeat = JSON.parseObject(delivery.getBody(),
						HeartBeat.class);

				System.out.println("接收到心跳信息:'" + new String(delivery.getBody())
						+ "'");

				if (StringUtils.isEmpty(heartBeat.getId())) {
					LOGGER.error("节点id为空，心跳信息无效:"
							+ new String(delivery.getBody()));
					continue;
				}
				
				Node node = new Node();
				node.setId(heartBeat.getId());
				node.setAppVersion(heartBeat.getApplication());

				if (heartBeat.isFirstHeartBeat()) {
					Node currNode=nodeService.getNode(node.getId());
					if(currNode==null){
						node.setAddress(heartBeat.getAddress());
						node.setPosition(heartBeat.getPosition());
						nodeService.addNode(node);
					}else{
						CommandService.getInstance().initNodeConnection(currNode);
					}
				} else {
					nodeService.updateNode(node);
				}
			} catch (InterruptedException e) {
				LOGGER.error("读取消息时发生错误", e);
			} catch (ShutdownSignalException e) {
				LOGGER.error("读取消息时发生错误", e);
			} catch (ConsumerCancelledException e) {
				LOGGER.error("读取消息时发生错误", e);
			} catch (Exception e) {
				LOGGER.error("可能是心跳信息格式错误，应该不会出现", e);
			}
		}
	}
}
