package com.springboot.springboot.dao;

import com.springboot.springboot.entities.FormData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// Akash Gupta
// Akash Gupta
@EnableJpaRepositories
public interface FormDao extends JpaRepository<FormData,Long> {
}
