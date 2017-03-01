package com.kafka.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kafka.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
