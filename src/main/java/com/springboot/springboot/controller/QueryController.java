package com.springboot.springboot.controller;

import com.springboot.springboot.entities.Query;
import com.springboot.springboot.model.DAOUser;
import com.springboot.springboot.services.QueryService;
import com.springboot.springboot.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queries")
@CrossOrigin(origins = "*")
public class QueryController {
    
    @Autowired
    private QueryService queryService;
    
    @Autowired
    private JwtUserDetailsService userDetailsService;
    
    // Create a new query (from landing page)
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuery(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            String message = request.get("message");
            
            if (name == null || email == null || message == null) {
                return ResponseEntity.badRequest().body("Name, email, and message are required");
            }
            
            Query query = queryService.createQuery(name, email, message);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Query submitted successfully");
            response.put("id", query.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting query: " + e.getMessage());
        }
    }
    
    // Get all unassigned queries (for non-admin users)
    @GetMapping("/unassigned")
    public ResponseEntity<?> getUnassignedQueries() {
        try {
            List<Query> queries = queryService.getUnassignedQueries();
            return ResponseEntity.ok(queries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching unassigned queries: " + e.getMessage());
        }
    }
    
    // Get queries assigned to current user
    @GetMapping("/my-assigned")
    public ResponseEntity<?> getMyAssignedQueries() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(401).body("Authentication required");
            }
            
            String username = authentication.getName();
            
            if (username == null || username.isEmpty() || "anonymousUser".equals(username)) {
                return ResponseEntity.status(401).body("Invalid authentication");
            }
            
            DAOUser currentUser = userDetailsService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            
            List<Query> queries = queryService.getQueriesAssignedToUser(currentUser);
            return ResponseEntity.ok(queries);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            return ResponseEntity.status(500).body("Error fetching assigned queries: " + e.getMessage());
        }
    }
    
    // Assign query to self
    @PostMapping("/{queryId}/assign-to-self")
    public ResponseEntity<?> assignQueryToSelf(@PathVariable Long queryId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            DAOUser currentUser = userDetailsService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            
            Query query = queryService.assignQueryToUser(queryId, currentUser, username);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Query assigned successfully");
            response.put("query", query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error assigning query: " + e.getMessage());
        }
    }
    
    // Update query status
    @PutMapping("/{queryId}/status")
    public ResponseEntity<?> updateQueryStatus(@PathVariable Long queryId, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            if (status == null) {
                return ResponseEntity.badRequest().body("Status is required");
            }
            
            Query.QueryStatus queryStatus = Query.QueryStatus.valueOf(status.toUpperCase());
            Query query = queryService.updateQueryStatus(queryId, queryStatus);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Status updated successfully");
            response.put("query", query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }
    
    // Add notes to query
    @PostMapping("/{queryId}/notes")
    public ResponseEntity<?> addNotesToQuery(@PathVariable Long queryId, @RequestBody Map<String, String> request) {
        try {
            String notes = request.get("notes");
            if (notes == null) {
                return ResponseEntity.badRequest().body("Notes are required");
            }
            
            Query query = queryService.addNotesToQuery(queryId, notes);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Notes added successfully");
            response.put("query", query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding notes: " + e.getMessage());
        }
    }
    
    // Get query by ID
    @GetMapping("/{queryId}")
    public ResponseEntity<?> getQueryById(@PathVariable Long queryId) {
        try {
            Query query = queryService.getQueryById(queryId);
            return ResponseEntity.ok(query);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching query: " + e.getMessage());
        }
    }
    
    // Get query statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getQueryStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("unassigned", queryService.getUnassignedQueryCount());
            stats.put("assigned", queryService.getAssignedQueryCount());
            stats.put("new", queryService.getQueryCountByStatus(Query.QueryStatus.NEW));
            stats.put("inProgress", queryService.getQueryCountByStatus(Query.QueryStatus.IN_PROGRESS));
            stats.put("resolved", queryService.getQueryCountByStatus(Query.QueryStatus.RESOLVED));
            stats.put("closed", queryService.getQueryCountByStatus(Query.QueryStatus.CLOSED));
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching statistics: " + e.getMessage());
        }
    }
} 