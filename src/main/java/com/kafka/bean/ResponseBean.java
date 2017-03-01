package com.kafka.bean;

import org.apache.kafka.common.record.TimestampType;

import com.kafka.rpc.Request;

import lombok.Data;

@Data
public class ResponseBean {
	private String topic;
	private int partition;
	private long offset;
	private long timestamp;
	private TimestampType timestampType;
	private long checksum;
	private int serializedKeySize;
	private int serializedValueSize;
	private String key;
	private Request value;
}
