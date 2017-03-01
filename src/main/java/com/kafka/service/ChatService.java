package com.kafka.service;

public interface ChatService {
	
	boolean createCompany(String companyName) throws Exception;

	void sendMessage(String companyName, String userName, String toUserName) throws Exception;
}
