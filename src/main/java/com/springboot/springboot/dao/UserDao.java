package com.springboot.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.springboot.model.DAOUser;
import java.util.List;

@Repository
public interface UserDao extends JpaRepository<DAOUser, Long> {
	DAOUser findByUsername(String username);
	List<DAOUser> findByIsApprovedFalse();
	List<DAOUser> findByIsApprovedTrue();
	long countByIsApprovedFalse();
	long countByIsApprovedTrue();
	long countByIsAdminTrue();
}