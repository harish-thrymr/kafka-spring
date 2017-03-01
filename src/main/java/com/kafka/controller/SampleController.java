package com.kafka.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.bean.ResponseBean;
import com.kafka.chat2.Client;
import com.kafka.rpc.Request;
import com.kafka.rpc.Response;

@RestController
@RequestMapping("/chat")
public class SampleController {

	@GetMapping("{userName}")
	List<ResponseBean> sample(@PathVariable String userName) throws Exception {
		Client client = new Client(userName, userName);
		client.login();
		client.shutdown();
		return client.getAllMessages();
	}

	@PostMapping("{userName}/send/@{toUserName}")
	public Response get(@PathVariable String userName, @PathVariable String toUserName, @RequestBody Request request)
			throws Exception {
		Client client = new Client(toUserName, userName);
		client.login();
		client.sendMessage(userName + toUserName, request.getMessage());
		client.shutdown();
		return new Response();
	}
}
