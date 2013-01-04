package com.bd.spring.mvc.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitMessageConsumer {
	private static Logger logger = LoggerFactory.getLogger(RabbitMessageConsumer.class);

	private final static String QUEUE_NAME = "simple_queue";

	public static void consumeRabbitMQ() throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		logger.debug(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			logger.debug(" [x] Received '" + message + "'");
			Thread.sleep(1000);
		}
	}

	public static void main(String[] argv) throws Exception {
		consumeRabbitMQ();
	}

}
