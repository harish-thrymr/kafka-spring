package com.kafka.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kafka.entity.ChatUser;
import com.kafka.entity.Company;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

	ChatUser findByName(String userName);

	ChatUser findByNameAndCompany(String userName, Company company);

}
