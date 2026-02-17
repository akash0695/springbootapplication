package com.springboot.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.springboot.config.JwtTokenUtil;
import com.springboot.springboot.model.JwtRequest;
import com.springboot.springboot.model.JwtResponse;
import com.springboot.springboot.model.UserDTO;
import com.springboot.springboot.model.DAOUser;
import com.springboot.springboot.services.JwtUserDetailsService;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpStatus;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		DAOUser user = userDetailsService.findByUsername(authenticationRequest.getUsername());
		if (user == null) {
			// User does not exist
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "AUTHENTICATION_FAILED");
			errorResponse.put("message", "Invalid username or password.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
		if (!user.getIsAdmin() && !user.getIsApproved()) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "ACCOUNT_NOT_APPROVED");
			errorResponse.put("message", "Your account is not approved yet. Please contact the administrator or submit feedback for assistance.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "AUTHENTICATION_FAILED");
			errorResponse.put("message", "Invalid username or password.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails, user.getIsAdmin());
		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("username", user.getUsername());
		response.put("isAdmin", user.getIsAdmin());
		response.put("isApproved", user.getIsApproved());
		response.put("email", user.getEmail());
		response.put("companyName", user.getCompanyName());
		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	@RequestMapping(value = "/register-admin", method = RequestMethod.POST)
	public ResponseEntity<?> saveAdmin(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(userDetailsService.saveAdmin(user));
	}

	@RequestMapping(value = "/check-approval-status", method = RequestMethod.POST)
	public ResponseEntity<?> checkApprovalStatus(@RequestBody Map<String, String> request) {
		try {
			String email = request.get("email");
			String mobile = request.get("mobile");
			
			if (email == null && mobile == null) {
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", "MISSING_PARAMETER");
				errorResponse.put("message", "Please provide either email or mobile number");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
			}
			
			DAOUser user = null;
			if (email != null && !email.trim().isEmpty()) {
				user = userDetailsService.findByEmail(email);
			} else if (mobile != null && !mobile.trim().isEmpty()) {
				user = userDetailsService.findByPhone(mobile);
			}
			
			if (user == null) {
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", "USER_NOT_FOUND");
				errorResponse.put("message", "No account found with the provided email or mobile number");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			}
			
			Map<String, Object> response = new HashMap<>();
			response.put("username", user.getUsername());
			response.put("email", user.getEmail());
			response.put("mobile", user.getPhone());
			response.put("isApproved", user.getIsApproved());
			response.put("isAdmin", user.getIsAdmin());
			
			if (!user.getIsApproved() && !user.getIsAdmin()) {
				response.put("message", "Your account is pending admin approval. Please wait for approval before logging in.");
			} else if (user.getIsAdmin()) {
				response.put("message", "Admin account - no approval required.");
			} else {
				response.put("message", "Account is approved and ready for login.");
			}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "SERVER_ERROR");
			errorResponse.put("message", "Error checking approval status");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}