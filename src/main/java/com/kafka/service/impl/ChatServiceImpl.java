package com.kafka.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kafka.bean.ResponseBean;
import com.kafka.chat.Client;
import com.kafka.entity.Channel;
import com.kafka.entity.ChatUser;
import com.kafka.entity.Company;
import com.kafka.repo.ChannelRepository;
import com.kafka.repo.ChatUserRepository;
import com.kafka.repo.CompanyRepository;
import com.kafka.service.ChatService;
import com.kafka.util.Check;

@Service
public class ChatServiceImpl implements ChatService {

	private final static Logger LOGGER = Logger.getLogger(ChatServiceImpl.class);

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ChatUserRepository chatUserRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Override
	public void createCompany(String companyName) throws Exception {
		Company company = companyRepository.findByName(companyName);
		Check.isNull(company, "Company already exists");
		company = new Company();
		company.setName(companyName);
		companyRepository.save(company);
	}

	@Override
	public void sendMessage(String companyName, String userName, String toUserName, String message) throws Exception {
		Company company = getCompany(companyName);
		ChatUser chatUser = getChatUser(userName, company);
		List<Channel> channels = chatUser.getChannels();

		ChatUser toChatUser = getChatUser(toUserName, company);
		// List<Channel> toChannels = toChatUser.getChannels();

		Channel channel = null;
		if (channels != null)
			channel = channels.stream().filter((c -> c.getChatUsers().contains(toChatUser))).findAny().orElse(null);
		if (channel == null) {
			channel = new Channel();
			channel.setChatUsers(Arrays.asList(chatUser, toChatUser));
			channel.setName(getRandomChannelName(userName + toUserName));

			final Channel finalChannel = channelRepository.findTopByCompanyOrderByPartitionDesc(company);
			if (finalChannel != null) {
				channel.setPartition(finalChannel.getPartition() + 1);
			}
			channel.setCompany(company);
			channelRepository.save(channel);
		}

		Client client = new Client(companyName, userName, channel.getPartition());
		client.sendMessage(message);
		client.shutdown();
	}

	private ChatUser getChatUser(String userName, Company company) throws Exception {
		ChatUser chatUser = chatUserRepository.findByNameAndCompany(userName, company);
		Check.notNull(chatUser, "No Chat User found");
		// if (chatUser == null) {
		// chatUser = new ChatUser();
		// chatUser.setName(userName);
		// chatUser.setCompany(company);
		// chatUserRepository.save(chatUser);
		// }
		return chatUser;
	}

	private String getRandomChannelName(String combinedName) {
		String randomString = combinedName;
		while (true) {
			if (channelRepository.findByName(randomString) != null)
				randomString = combinedName + UUID.randomUUID().toString().substring(0, 4);
			else
				break;
		}
		return randomString;
	}

	@Override
	public void createChatUser(String companyName, String chatUserName) throws Exception {
		Company company = getCompany(companyName);
		ChatUser chatUser = chatUserRepository.findByNameAndCompany(chatUserName, company);
		Check.isNull(chatUser, "Chat User already exists");
		chatUser = new ChatUser();
		chatUser.setName(chatUserName);
		chatUser.setCompany(company);
		chatUserRepository.save(chatUser);

	}

	private Company getCompany(String companyName) throws Exception {
		Company company = companyRepository.findByName(companyName);
		Check.notNull(company, "No company found");
		return company;
	}

	@Override
	public List<ResponseBean> getAllMessages(String companyName, String chatUserName) throws Exception {
		ChatUser chatUser = getChatUser(chatUserName, getCompany(companyName));
		Function<Channel, List<ResponseBean>> function = channel -> {
			Client client = new Client(companyName, chatUserName, channel.getPartition());
			client.shutdown();
			return client.getAllMessages();
		};

		return chatUser.getChannels().stream().map(function).flatMap(List::stream).collect(Collectors.toList());

	}
}
