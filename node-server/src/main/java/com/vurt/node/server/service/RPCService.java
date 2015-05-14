package com.vurt.node.server.service;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.vurt.node.server.comunication.ChannelHolder;

public class RPCService {
	private static final String RPC_QUEUE_NAME = "rpc_queue";
	
	public RPCService() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		Channel channel = ChannelHolder.createChannel();
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

		System.out.println(" [x] Awaiting RPC requests");

		while (true) {
		    QueueingConsumer.Delivery delivery = consumer.nextDelivery();

		    BasicProperties props = delivery.getProperties();
		    BasicProperties replyProps = new BasicProperties
		                                     .Builder()
		                                     .correlationId(props.getCorrelationId())
		                                     .build();

		    String message = new String(delivery.getBody());
		    int n = Integer.parseInt(message);

		    System.out.println(" [.] fib(" + message + ")");
		    String response = "" + fib(n);

		    channel.basicPublish( "", props.getReplyTo(), replyProps, response.getBytes());

		    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}
	
	private static int fib(int n) {
	    if (n == 0) return 0;
	    if (n == 1) return 1;
	    return fib(n-1) + fib(n-2);
	}
	
	
}
