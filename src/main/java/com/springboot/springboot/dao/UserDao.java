package com.springboot.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.springboot.model.DAOUser;
import java.util.List;
import javax.persistence.Column;

@Repository
public interface UserDao extends JpaRepository<DAOUser, Long> {
	DAOUser findByUsername(String username);
	DAOUser findByEmail(String email);
	DAOUser findByPhone(String phone);
	List<DAOUser> findByIsApprovedFalse();
	List<DAOUser> findByIsApprovedTrue();
	long countByIsApprovedFalse();
	long countByIsApprovedTrue();
	long countByIsAdminTrue();
}