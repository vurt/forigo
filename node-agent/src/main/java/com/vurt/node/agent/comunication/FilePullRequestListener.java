package com.vurt.node.agent.comunication;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.vurt.node.agent.NodeAgent;

public class FilePullRequestListener extends Thread {
	private Channel channel;

	private QueueingConsumer consumer;

	public FilePullRequestListener() throws IOException {
		channel = ChannelHolder.createChannel();
		channel.exchangeDeclare(Constants.EXCHANGE_FILE, "direct");
		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, Constants.EXCHANGE_FILE,
				NodeAgent.getGroup());

		consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, false, consumer);
	}

	@Override
	public void run() {
		while (true) {
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
}
