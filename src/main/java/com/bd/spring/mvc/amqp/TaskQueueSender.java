package com.bd.spring.mvc.amqp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class TaskQueueSender {

	private static Logger logger = LoggerFactory.getLogger(TaskQueueSender.class);

	private final static String QUEUE_NAME = "task_queue";

	private static String getMessage(String[] strings) {
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

	public static void publishToWorkQueue(String message) throws IOException {
		// get mq connection
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// don't dispatch a new message to a worker until it has processed and acknowledged the previous one
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);

		boolean durable = true;

		// publish to queue
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		// String message = "Hello World from rabbit mq!";
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		logger.debug(" [x] Sent '" + message + "'");

		// close connections
		channel.close();
		connection.close();
	}

	public static void main(String[] argv) throws java.io.IOException {
		publishToWorkQueue(getMessage(argv));
	}

}
