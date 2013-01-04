package com.bd.spring.mvc.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class TaskWorker {
	private static Logger logger = LoggerFactory.getLogger(TaskWorker.class);

	private final static String QUEUE_NAME = "task_queue";
	
	private static void doWork(String task) throws InterruptedException {
	    for (char ch: task.toCharArray()) {
	        if (ch == '.') Thread.sleep(1000);
	    }
	}

	public static void consumeRabbitMQ() throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		boolean durable = true;
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		logger.debug(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			logger.debug(" [x] Received '" + message + "'");
			 doWork(message);
			 
			 //send back the acknowledgement to sender
			 channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			Thread.sleep(1000);
		}
	}

	public static void main(String[] argv) throws Exception {
		consumeRabbitMQ();
	}

}
