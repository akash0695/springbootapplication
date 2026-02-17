package com.springboot.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "users") // Changed from 'user' to 'users' to avoid reserved keyword issues and match DB
public class DAOUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique=true, nullable=false)
	private String username;
	@Column
	@JsonIgnore
	private String password;
	@Column(unique=true)
	private String email;
	@Column
	private String companyName;
	@Column(unique=true)
	private String phone;
	@Column
	private Boolean isAdmin = false;
	@Column
	private Boolean isApproved = false;
	@Column
	private String approvedBy;
	@Column
	private java.time.LocalDateTime approvedAt;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public java.time.LocalDateTime getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(java.time.LocalDateTime approvedAt) {
		this.approvedAt = approvedAt;
	}

	public long getId() {
		return id;
	}
}