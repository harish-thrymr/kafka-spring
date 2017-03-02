package com.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.bean.ResponseObject;
import com.kafka.rpc.Request;
import com.kafka.service.ChatService;

@RestController
@RequestMapping("/chat/{companyName}")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@GetMapping("{userName}")
	ResponseEntity<ResponseObject> getAllMessages(@PathVariable("companyName") String companyName,
			@PathVariable String userName) throws Exception {
		return new ResponseObject(chatService.getAllMessages(companyName, userName)).getResponseEntity();
	}

	@PostMapping("{userName}/send/@{toUserName}")
	public ResponseEntity<ResponseObject> get(@PathVariable("companyName") String companyName,
			@PathVariable String userName, @PathVariable String toUserName, @RequestBody Request request)
			throws Exception {
		chatService.sendMessage(companyName, userName, toUserName, request.getMessage());
		return new ResponseObject("Message sent").getResponseEntity();
	}

	@GetMapping("create-company")
	ResponseEntity<ResponseObject> createCompany(@PathVariable("companyName") String companyName) throws Exception {
		chatService.createCompany(companyName);
		return new ResponseObject("Company is created successfully").getResponseEntity();
	}

	@GetMapping("create-user/{userName}")
	ResponseEntity<ResponseObject> createUser(@PathVariable("companyName") String companyName,
			@PathVariable String userName) throws Exception {
		chatService.createChatUser(companyName, userName);
		return new ResponseObject("Chat user is created successfully").getResponseEntity();
	}

}
