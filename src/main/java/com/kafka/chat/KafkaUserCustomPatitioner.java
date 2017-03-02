package com.kafka.chat;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.log4j.Logger;

public class KafkaUserCustomPatitioner implements Partitioner {

	private final static Logger LOGGER = Logger.getLogger(KafkaUserCustomPatitioner.class);

	@Override
	public void configure(Map<String, ?> configs) {

	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

		int partition = 0;
		String userName = (String) key;
		// Find the id of current user based on the username
		LOGGER.info("in kindjcnjdncjnjdn");
		LOGGER.info("in kindjcnjdncjnjdn");
		LOGGER.info("in kindjcnjdncjnjdn");
		LOGGER.info("in kindjcnjdncjnjdn");
		LOGGER.info("in kindjcnjdncjnjdn");
		Integer userId = /* userService.findUserId(userName) */1;
		// If the userId not found, default partition is 0
		if (userId != null) {
			partition = userId;
		}
		return partition;
	}

	@Override
	public void close() {

	}

}
