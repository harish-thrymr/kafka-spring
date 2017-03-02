package com.kafka.chat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.kafka.bean.ResponseBean;
import com.kafka.rpc.Request;
import com.kafka.rpc.Response;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;

@SuppressWarnings("unused")
public class Client {

	private final static Logger LOGGER = Logger.getLogger(Client.class);

	private boolean isLoggedIn = false;
	private String userName = "";
	private String topicName = "";
	private int partition;

	private final static String BROKER_LIST = "localhost:9092";
	private final static String PRODUCER_TYPE = "sync";
	private final static String REQUEST_REQUIRED_ACK = "1";
	private final static String ZOOKEEPER_SERVER = "localhost:2181";
	private final static int POLL_TIME_OUT = 100;

	private Producer<String, String> producer;
	private Consumer<String, String> consumer;
	private ConsumerRecords<String, String> consumerRecords;
	private static ConsumerConfig consumerConfig;

	// private Map<String, MessageConsumerImpl> topicListenerThreadMap;

	public Client(String topicName, String userName, int partition) {
		this.topicName = topicName;
		this.userName = userName;
		this.partition = partition;
		initProducer();
		initConsumer(this.topicName);
		login();
		// topicListenerThreadMap = new HashMap<>();
	}

	private Properties getProperties() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", BROKER_LIST);
		properties.put("acks", "all");
		properties.put("batch.size", 16384);
		properties.put("linger.ms", 1);
		properties.put("group.id", "test");
		properties.put("buffer.memory", 33554432);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("partitioner.class", KafkaUserCustomPatitioner.class.getCanonicalName());
		return properties;
	}
	// @formatter:off
	//    private void createConsumerConfig() {
	//        Properties consumerProperties = new Properties();
	//        consumerProperties.put("zookeeper.connect", ZOOKEEPER_SERVER);
	//        consumerProperties.put("group.id", UUID.randomUUID().toString());
	//        consumerProperties.put("zookeeper.session.timeout.ms", "400");
	//        consumerProperties.put("zookeeper.sync.time.ms", "200");
	//        consumerProperties.put("auto.commit.interval.ms", "1000");
	//        consumerProperties.put("auto.offset.reset", "largest");
	//
	//        consumerConfig = new ConsumerConfig(consumerProperties);
	//    }
	// @formatter:on

	private void initProducer() {
		producer = new KafkaProducer<>(getProperties());
	}

	private void initConsumer(String topicName) {
		consumer = new KafkaConsumer<>(getProperties());
		TopicPartition topicPartition = new TopicPartition(topicName, partition);
		TopicPartition topicPartition1 = new TopicPartition(topicName, partition);
		consumer.assign(Arrays.asList(topicPartition, topicPartition1));
		LOGGER.info("dffdf " + new Gson().toJson(consumer.partitionsFor(topicName)));

	}

	private void setConsumerRecords() {
		consumerRecords = consumer.poll(POLL_TIME_OUT);
	}

	private void login() {
		if (isLoggedIn) {
			LOGGER.error("Please logout first");
		} else {
			Request request = new Request();
			request.setCommand("LOGIN");
			request.setUserName(userName);
			sendRequest(toJsonFromRequest(request));
			isLoggedIn = true;
			setConsumerRecords();

		}
	}

	public void join(String command, String parameter) throws Exception {
		System.out.println(userName + "  nickname");
		if (!isLoggedIn)
			LOGGER.error("Please login first");
		else {
			Request request = new Request();
			request.setCommand(command);
			request.setChannelName(parameter);
			request.setUserName(userName);

			Response response = fromJsonToResponse(sendRequest(toJsonFromRequest(request)));

			if (response.getUserName().equals(userName)) {
				// addTopicListener(parameter);
				System.out.println(response.getMessage());
			} else
				System.err.println(response.getMessage());
		}
	}

	public void leave(String command, String parameter) throws Exception {
		if (!isLoggedIn)
			LOGGER.error("Please login first");
		else {
			Request request = new Request();
			request.setCommand(command);
			request.setChannelName(parameter);
			request.setUserName(userName);

			Response response = fromJsonToResponse(sendRequest(toJsonFromRequest(request)));

			if (response.getUserName().equals(userName)) {
				// removeTopicListener(parameter);
				System.out.println(response.getMessage());
			} else
				System.err.println(response.getMessage());
		}
	}

	public void sendMessage(String message) throws Exception {
		if (!isLoggedIn)
			LOGGER.error("Please login first");
		else {
			Request request = new Request();
			request.setCommand("SEND");
			// request.setChannelName(channelName);
			request.setMessage(message);
			request.setUserName(userName);

			sendRequest(toJsonFromRequest(request));

			// if (!response.getUserName().equals(userName)) {
			// System.err.println(response.getMessage());
			// }
		}
	}

	private String sendRequest(String message) {
		String key = UUID.randomUUID().toString();
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, partition, key, message);
		producer.send(record);
		return message;

	}

	private String getResponse(String key) {
		String responseMessage = "";
		loop: while (true) {
			for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
				if (consumerRecord.key().equals(key)) {
					LOGGER.error("Found " + consumerRecord.key() + "     " + consumerRecord.value());
					responseMessage = consumerRecord.value();
					break loop;
				}
			}
		}
		return responseMessage;
	}

	// @formatter:off
	//	private void addTopicListener(String topic) {
	//		MessageConsumerImpl messageConsumerImpl = new MessageConsumerImpl(consumerRecords);
	//		topicListenerThreadMap.put(topic, messageConsumerImpl);
	//
	//	}
	//
	//	private void removeTopicListener(String topic) throws InterruptedException {
	//		topicListenerThreadMap.get(topic).terminate();
	//		topicListenerThreadMap.remove(topic);
	//	}
	// @formatter:on

	public List<ResponseBean> getAllMessages() {
		Function<ConsumerRecord<String, String>, ResponseBean> mapper = cr -> {
			ResponseBean responseBean = new ResponseBean();
			responseBean.setChecksum(cr.checksum());
			responseBean.setKey(cr.key());
			responseBean.setOffset(cr.offset());
			responseBean.setPartition(cr.partition());
			responseBean.setTimestamp(cr.timestamp());
			responseBean.setTopic(cr.topic());
			responseBean.setValue(new Gson().fromJson(cr.value(), Request.class));
			return responseBean;
		};
		Predicate<ResponseBean> predicate = rb -> {
			return !rb.getValue().getUserName().equals(userName);
		};
		return StreamSupport.stream(consumerRecords.spliterator(), false).map(mapper).filter(predicate)
				.collect(Collectors.toList());
	}

	private Request fromJsonToRequest(String json) {
		return new Gson().fromJson(json, Request.class);
	}

	private String toJsonFromRequest(Request request) {
		return new Gson().toJson(request);
	}

	private Response fromJsonToResponse(String json) {
		return new Gson().fromJson(json, Response.class);
	}

	private String toJsonFromResponse(Response request) {
		return new Gson().toJson(request);
	}

	public void shutdown() {
		producer.close();
		consumer.close();
	}
}
