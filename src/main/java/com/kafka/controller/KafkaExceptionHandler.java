package com.kafka.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.bean.ResponseObject;

@RestController
@ControllerAdvice
public class KafkaExceptionHandler {

	private final static Logger LOGGER = Logger.getLogger(KafkaExceptionHandler.class);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseObject handleIllegalArgumentException(IllegalArgumentException e) {
		e.printStackTrace();
		LOGGER.error(e);
		return new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ResponseObject handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		e.printStackTrace();
		LOGGER.error(e);
		return new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseObject handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		e.printStackTrace();
		LOGGER.error(e);
		return new ResponseObject(HttpStatus.BAD_REQUEST, "Unable to parse the request");
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public ResponseObject handleException(Exception e) {
		e.printStackTrace();
		LOGGER.error(e);
		return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}
}
