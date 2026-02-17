package com.springboot.springboot.services;

import com.springboot.springboot.entities.Query;
import com.springboot.springboot.model.DAOUser;

import java.util.List;

public interface QueryService {
    
    // Create a new query
    Query createQuery(String name, String email, String message);
    
    // Get all queries
    List<Query> getAllQueries();
    
    // Get unassigned queries
    List<Query> getUnassignedQueries();
    
    // Get assigned queries
    List<Query> getAssignedQueries();
    
    // Get queries assigned to a specific user
    List<Query> getQueriesAssignedToUser(DAOUser user);
    
    // Get queries by status
    List<Query> getQueriesByStatus(Query.QueryStatus status);
    
    // Get unassigned queries by status
    List<Query> getUnassignedQueriesByStatus(Query.QueryStatus status);
    
    // Get assigned queries by status
    List<Query> getAssignedQueriesByStatus(Query.QueryStatus status);
    
    // Get queries assigned to a specific user by status
    List<Query> getQueriesAssignedToUserByStatus(DAOUser user, Query.QueryStatus status);
    
    // Assign query to a user
    Query assignQueryToUser(Long queryId, DAOUser user, String assignedBy);
    
    // Update query status
    Query updateQueryStatus(Long queryId, Query.QueryStatus status);
    
    // Add notes to query
    Query addNotesToQuery(Long queryId, String notes);
    
    // Get query by ID
    Query getQueryById(Long queryId);
    
    // Delete query
    void deleteQuery(Long queryId);
    
    // Get statistics
    long getUnassignedQueryCount();
    long getAssignedQueryCount();
    long getQueryCountByStatus(Query.QueryStatus status);
    long getQueryCountAssignedToUser(DAOUser user);
} 