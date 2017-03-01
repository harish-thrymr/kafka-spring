package com.kafka.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kafka.entity.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

	Channel findByName(String name);

}
