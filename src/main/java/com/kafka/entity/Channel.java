package com.kafka.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Channel extends BaseEntity {

	@ManyToMany(targetEntity = ChatUser.class)
	private List<ChatUser> chatUsers;

	private String name;

	private Integer partition = 0;

	@ManyToOne
	private Company company;
}
