package com.springboot.springboot.services;

import com.springboot.springboot.dao.QueryDao;
import com.springboot.springboot.entities.Query;
import com.springboot.springboot.model.DAOUser;
import com.springboot.springboot.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QueryServiceImpl implements QueryService {
    
    @Autowired
    private QueryDao queryDao;
    
    @Override
    public Query createQuery(String name, String email, String message) {
        Query query = new Query(name, email, message);
        return queryDao.save(query);
    }
    
    @Override
    public List<Query> getAllQueries() {
        return queryDao.findAll();
    }
    
    @Override
    public List<Query> getUnassignedQueries() {
        return queryDao.findByAssignedToIsNullOrderByCreatedAtDesc();
    }
    
    @Override
    public List<Query> getAssignedQueries() {
        return queryDao.findByAssignedToIsNotNullOrderByCreatedAtDesc();
    }
    
    @Override
    public List<Query> getQueriesAssignedToUser(DAOUser user) {
        return queryDao.findByAssignedToOrderByCreatedAtDesc(user);
    }
    
    @Override
    public List<Query> getQueriesByStatus(Query.QueryStatus status) {
        return queryDao.findByStatusOrderByCreatedAtDesc(status);
    }
    
    @Override
    public List<Query> getUnassignedQueriesByStatus(Query.QueryStatus status) {
        return queryDao.findByAssignedToIsNullAndStatusOrderByCreatedAtDesc(status);
    }
    
    @Override
    public List<Query> getAssignedQueriesByStatus(Query.QueryStatus status) {
        return queryDao.findByAssignedToIsNotNullAndStatusOrderByCreatedAtDesc(status);
    }
    
    @Override
    public List<Query> getQueriesAssignedToUserByStatus(DAOUser user, Query.QueryStatus status) {
        return queryDao.findByAssignedToAndStatusOrderByCreatedAtDesc(user, status);
    }
    
    @Override
    public Query assignQueryToUser(Long queryId, DAOUser user, String assignedBy) {
        Query query = queryDao.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with id: " + queryId));
        
        if (query.getAssignedTo() != null) {
            throw new RuntimeException("Query is already assigned to: " + query.getAssignedTo().getUsername());
        }
        
        query.setAssignedTo(user);
        query.setAssignedBy(assignedBy);
        query.setAssignedAt(LocalDateTime.now());
        query.setStatus(Query.QueryStatus.ASSIGNED);
        query.setLastUpdated(LocalDateTime.now());
        
        return queryDao.save(query);
    }
    
    @Override
    public Query updateQueryStatus(Long queryId, Query.QueryStatus status) {
        Query query = queryDao.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with id: " + queryId));
        
        query.setStatus(status);
        query.setLastUpdated(LocalDateTime.now());
        
        return queryDao.save(query);
    }
    
    @Override
    public Query addNotesToQuery(Long queryId, String notes) {
        Query query = queryDao.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with id: " + queryId));
        
        String currentNotes = query.getNotes();
        String newNotes = currentNotes == null ? notes : currentNotes + "\n\n" + LocalDateTime.now() + ":\n" + notes;
        
        query.setNotes(newNotes);
        query.setLastUpdated(LocalDateTime.now());
        
        return queryDao.save(query);
    }
    
    @Override
    public Query getQueryById(Long queryId) {
        return queryDao.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with id: " + queryId));
    }
    
    @Override
    public void deleteQuery(Long queryId) {
        Query query = queryDao.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found with id: " + queryId));
        queryDao.delete(query);
    }
    
    @Override
    public long getUnassignedQueryCount() {
        return queryDao.countByAssignedToIsNull();
    }
    
    @Override
    public long getAssignedQueryCount() {
        return queryDao.countByAssignedToIsNotNull();
    }
    
    @Override
    public long getQueryCountByStatus(Query.QueryStatus status) {
        return queryDao.countByStatus(status);
    }
    
    @Override
    public long getQueryCountAssignedToUser(DAOUser user) {
        return queryDao.countByAssignedTo(user);
    }
} 