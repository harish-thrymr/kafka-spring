package com.kafka.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.bean.ResponseObject;

@RestController
@ControllerAdvice
public class KafkaExceptionHandler {

	private final static Logger LOGGER = Logger.getLogger(KafkaExceptionHandler.class);

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public ResponseObject handleException(Exception e) {
		e.printStackTrace();
		LOGGER.error(e);
		return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}
}
