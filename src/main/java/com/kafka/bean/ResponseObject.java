package com.kafka.bean;

/**
 * Response object for RestFull APIs
 * 
 * @author Harish Thatikonda
 * @since 2017-march-01
 *
 */
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
		this.status = status.value();
		this.message = this.error = status.getReasonPhrase();
	}

	public ResponseObject(String message) {
		super();
		this.message = message;
	}

	public ResponseObject(HttpStatus status, String error) {
		super();
		this.error = error != null ? error : "Something unexpected occured";
		this.status = status.value();
	}

	public ResponseObject(int status, String error) {
		super();
		this.error = error;
		this.status = status;
	}

	public ResponseObject(int status, String error, String message) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
	}

	@JsonIgnore
	public ResponseEntity<ResponseObject> getResponseEntity() {
		return ResponseEntity.status(status).body(this);
	}

}
