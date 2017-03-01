package com.kafka.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ChatUserRepository chatUserRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Override
	public boolean createCompany(String companyName) throws Exception {
		Company company = companyRepository.findByName(companyName);
		if (company == null) {
			company = new Company();
			company.setName(companyName);
			companyRepository.save(company);
			return true;
		}
		return false;
	}

	@Override
	public void sendMessage(String companyName, String userName, String toUserName) throws Exception {
		Company company = companyRepository.findByName(companyName);
		Check.notNull(company, "No company found");
		ChatUser chatUser = getUser(userName, company);
		List<Channel> channels = chatUser.getChannels();

		ChatUser toChatUser = getUser(toUserName, company);
		// List<Channel> toChannels = toChatUser.getChannels();

		Channel channel = null;
		if (channels != null)
			channel = channels.stream().filter((c -> c.getChatUsers().contains(toChatUser))).findAny().orElse(null);
		if (channel == null) {
			channel = new Channel();
			channel.setChatUsers(Arrays.asList(chatUser, toChatUser));
			channel.setName(getRandomChannelName(userName + toUserName));
			channelRepository.save(channel);
		}
	}

	private ChatUser getUser(String userName, Company company) {
		ChatUser chatUser = chatUserRepository.findByName(userName);
		if (chatUser == null) {
			chatUser = new ChatUser();
			chatUser.setName(userName);
			chatUser.setCompany(company);
			chatUserRepository.save(chatUser);
		}
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

}
