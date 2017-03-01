package com.kafka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.bean.ResponseBean;
import com.kafka.bean.ResponseObject;
import com.kafka.chat2.Client;
import com.kafka.rpc.Request;
import com.kafka.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@GetMapping("{userName}")
	List<ResponseBean> sample(@PathVariable String userName) throws Exception {
		Client client = new Client(userName, userName);
		client.login();
		client.shutdown();
		return client.getAllMessages();
	}

	@PostMapping("{companyName}/{userName}/send/@{toUserName}")
	public ResponseEntity<ResponseObject> get(@PathVariable("companyName") String companyName,
			@PathVariable String userName, @PathVariable String toUserName, @RequestBody Request request)
			throws Exception {
		chatService.sendMessage(companyName, userName, toUserName);
		return new ResponseObject().getResponseEntity();
	}

	@GetMapping("create-company/{name}")
	ResponseEntity<ResponseObject> createCompany(@PathVariable("name") String name) throws Exception {
		return new ResponseObject(chatService.createCompany(name)).getResponseEntity();
	}
}
