package com.kafka.chat;

import java.util.Iterator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public class MessageConsumerImpl {

	private final static Logger LOGGER = Logger.getLogger(MessageConsumerImpl.class);

	private ConsumerRecords<String, String> consumerRecords;
	private volatile boolean isRunning = true;

	public MessageConsumerImpl(ConsumerRecords<String, String> consumerRecords) {
		this.consumerRecords = consumerRecords;
	}

	@Async
	public void run() throws InterruptedException {
		Iterator<ConsumerRecord<String, String>> iterator = consumerRecords.iterator();
		Thread.sleep(3000);
		while (isRunning) {
			if (iterator.hasNext())
				LOGGER.info(iterator.next().value());
		}
	}

	public void terminate() {
		isRunning = false;
	}
}