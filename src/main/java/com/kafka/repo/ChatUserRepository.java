package com.kafka.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kafka.entity.ChatUser;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

}
