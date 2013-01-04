package com.bd.spring.mvc.amqp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMessageProducer {

	private static Logger logger = LoggerFactory.getLogger(RabbitMessageProducer.class);

	private final static String QUEUE_NAME = "simple_queue";

	public static void publishToRabbitMQ() throws IOException {
		// get mq connection
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// publish to queue
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World from rabbit mq!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		logger.debug(" [x] Sent '" + message + "'");
		
		//close connections
		channel.close();
	    connection.close();
	}

	public static void main(String[] argv) throws java.io.IOException {
		publishToRabbitMQ();
	}

}
