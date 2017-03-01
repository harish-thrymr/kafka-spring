package com.kafka.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Company extends BaseEntity {

	private String companyName;

	@OneToMany(mappedBy = "company")
	private List<ChatUser> chatUsers;

}
