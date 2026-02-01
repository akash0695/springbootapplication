package com.springboot.springboot.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import com.springboot.springboot.model.DAOUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "queries")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Query {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueryStatus status = QueryStatus.NEW;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    private DAOUser assignedTo;
    
    @Column
    private LocalDateTime assignedAt;
    
    @Column
    private String assignedBy;
    
    @Column(length = 2000)
    private String notes;
    
    @Column
    private LocalDateTime lastUpdated;
    
    public enum QueryStatus {
        NEW, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED
    }
    
    // Constructors
    public Query() {}
    
    public Query(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public QueryStatus getStatus() {
        return status;
    }
    
    public void setStatus(QueryStatus status) {
        this.status = status;
    }
    
    public DAOUser getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(DAOUser assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
    
    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
    
    public String getAssignedBy() {
        return assignedBy;
    }
    
    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
} 
 