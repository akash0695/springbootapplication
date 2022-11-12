package com.springboot.springboot.dao;

import com.springboot.springboot.entities.FormData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface FormDao extends JpaRepository<FormData,Long> {
}
