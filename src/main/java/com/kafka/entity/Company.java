package com.kafka.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Company extends BaseEntity {

	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "company")
	private List<ChatUser> chatUsers;
	
	@OneToMany(mappedBy = "company")
	private List<Channel> channels;

}
