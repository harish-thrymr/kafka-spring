package com.kafka.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class ChatUser extends BaseEntity {

	private String name;

	@ManyToOne
	private Company company;

	@ManyToMany(mappedBy = "chatUsers")
	private List<Channel> channels;

}
