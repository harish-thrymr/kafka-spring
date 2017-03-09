package com.kafka;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication implements CommandLineRunner {

	private Logger LOGGER = Logger.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		LOGGER.info(this.getClass().getClassLoader().getResource("banner.txt"));

	}
}
