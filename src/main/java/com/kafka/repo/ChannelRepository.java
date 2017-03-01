package com.kafka.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kafka.chat.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

}
