package com.kafka.service;

import java.util.List;

import com.kafka.bean.ResponseBean;

public interface ChatService {

	void createCompany(String companyName) throws Exception;

	void sendMessage(String companyName, String userName, String toUserName, String message) throws Exception;

	void createChatUser(String companyName, String chatUserName) throws Exception;

	List<ResponseBean> getAllMessages(String companyName, String chatUserName) throws Exception;
}
