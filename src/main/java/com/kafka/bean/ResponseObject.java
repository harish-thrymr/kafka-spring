package com.kafka.bean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class ResponseObject {

	private String error;
	private String message;
	private Object payLoad;
	private int status = 200;

	public ResponseObject() {
		super();
	}

	public ResponseObject(Object payLoad) {
		super();
		this.payLoad = payLoad;
	}

	public ResponseObject(HttpStatus status) {
		super();
		this.error = status.getReasonPhrase();
		this.status = status.value();
	}

	public ResponseObject(HttpStatus status, String error) {
		super();
		this.error = error;
		this.status = status.value();
	}

	@JsonIgnore
	private ResponseEntity<ResponseObject> getResponseEntity() {
		return ResponseEntity.status(status).body(this);
	}

}
