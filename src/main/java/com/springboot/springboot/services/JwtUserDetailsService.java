package com.springboot.springboot.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.springboot.dao.UserDao;
import com.springboot.springboot.model.DAOUser;
import com.springboot.springboot.model.UserDTO;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		if ("javainuse".equals(username)) {
//			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
//					new ArrayList<>());
//		} else {
//			throw new UsernameNotFoundException("User not found with username: " + username);
//		}
//	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		DAOUser user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		// Check if user is approved (except for admin users)
		if (!user.getIsAdmin() && !user.getIsApproved()) {
			throw new UsernameNotFoundException("User account is not approved yet. Please wait for admin approval.");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	public DAOUser save(UserDTO user) {
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setCompanyName(user.getCompanyName());
		newUser.setPhone(user.getPhone());
		newUser.setIsAdmin(false); // Default to regular user
		newUser.setIsApproved(false); // Default to not approved
		return userDao.save(newUser);
	}

	public DAOUser saveAdmin(UserDTO user) {
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setCompanyName(user.getCompanyName());
		newUser.setPhone(user.getPhone());
		newUser.setIsAdmin(true); // Set as admin
		newUser.setIsApproved(true); // Auto-approve admin
		return userDao.save(newUser);
	}

	// Admin methods
	public List<DAOUser> getPendingUsers() {
		return userDao.findByIsApprovedFalse();
	}

	public List<DAOUser> getApprovedUsers() {
		return userDao.findByIsApprovedTrue();
	}

	public void approveUser(Long userId) {
		DAOUser user = userDao.findById(userId).orElse(null);
		if (user != null) {
			user.setIsApproved(true);
			userDao.save(user);
		}
	}

	public void approveUser(Long userId, String approvedBy) {
		DAOUser user = userDao.findById(userId).orElse(null);
		if (user != null) {
			user.setIsApproved(true);
			user.setApprovedBy(approvedBy);
			user.setApprovedAt(java.time.LocalDateTime.now());
			userDao.save(user);
		}
	}

	public void rejectUser(Long userId) {
		DAOUser user = userDao.findById(userId).orElse(null);
		if (user != null) {
			userDao.delete(user);
		}
	}

	public Map<String, Object> getDashboardStats() {
		Map<String, Object> stats = new HashMap<>();
		stats.put("totalUsers", userDao.count());
		stats.put("pendingUsers", userDao.countByIsApprovedFalse());
		stats.put("approvedUsers", userDao.countByIsApprovedTrue());
		stats.put("adminUsers", userDao.countByIsAdminTrue());
		return stats;
	}

	// Get user by username
	public DAOUser findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	// Get user by email
	public DAOUser findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	// Get user by phone
	public DAOUser findByPhone(String phone) {
		return userDao.findByPhone(phone);
	}

}