package com.springboot.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.springboot.springboot.entities.Course;
@EnableJpaRepositories
public interface CourseDao extends JpaRepository<Course, Long> {

}
