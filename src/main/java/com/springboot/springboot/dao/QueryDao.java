package com.springboot.springboot.dao;

import com.springboot.springboot.entities.Query;
import com.springboot.springboot.model.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryDao extends JpaRepository<Query, Long> {
    
    // Find all unassigned queries
    List<Query> findByAssignedToIsNullOrderByCreatedAtDesc();
    
    // Find all assigned queries
    List<Query> findByAssignedToIsNotNullOrderByCreatedAtDesc();
    
    // Find queries assigned to a specific user
    @org.springframework.data.jpa.repository.Query("SELECT q FROM Query q LEFT JOIN FETCH q.assignedTo WHERE q.assignedTo = :assignedTo ORDER BY q.createdAt DESC")
    List<Query> findByAssignedToOrderByCreatedAtDesc(@Param("assignedTo") DAOUser assignedTo);
    
    // Find queries by status
    List<Query> findByStatusOrderByCreatedAtDesc(Query.QueryStatus status);
    
    // Find unassigned queries by status
    List<Query> findByAssignedToIsNullAndStatusOrderByCreatedAtDesc(Query.QueryStatus status);
    
    // Find assigned queries by status
    List<Query> findByAssignedToIsNotNullAndStatusOrderByCreatedAtDesc(Query.QueryStatus status);
    
    // Find queries assigned to a specific user by status
    List<Query> findByAssignedToAndStatusOrderByCreatedAtDesc(DAOUser assignedTo, Query.QueryStatus status);
    
    // Count unassigned queries
    long countByAssignedToIsNull();
    
    // Count assigned queries for a user
    long countByAssignedTo(DAOUser assignedTo);
    
    // Count queries by status
    long countByStatus(Query.QueryStatus status);
    
    // Count unassigned queries by status
    long countByAssignedToIsNullAndStatus(Query.QueryStatus status);
    
    // Count assigned queries by status
    long countByAssignedToIsNotNullAndStatus(Query.QueryStatus status);
    
    // Count all assigned queries
    long countByAssignedToIsNotNull();
    
    // Find queries created in the last N days
    @org.springframework.data.jpa.repository.Query("SELECT q FROM Query q WHERE q.createdAt >= :daysAgo ORDER BY q.createdAt DESC")
    List<Query> findRecentQueries(@Param("daysAgo") java.time.LocalDateTime daysAgo);
    
    // Find queries with notes
    List<Query> findByNotesIsNotNullOrderByLastUpdatedDesc();
    
    // Find queries without notes
    List<Query> findByNotesIsNullOrderByCreatedAtDesc();
} 