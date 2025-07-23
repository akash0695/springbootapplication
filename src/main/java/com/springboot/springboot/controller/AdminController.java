package com.springboot.springboot.controller;

import com.springboot.springboot.entities.FormData;
import com.springboot.springboot.model.DAOUser;
import com.springboot.springboot.services.FormService;
import com.springboot.springboot.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private FormService formService;

    // Get all pending users (not approved)
    @GetMapping("/pending-users")
    public ResponseEntity<List<DAOUser>> getPendingUsers() {
        List<DAOUser> pendingUsers = userDetailsService.getPendingUsers();
        return ResponseEntity.ok(pendingUsers);
    }

    // Get all approved users
    @GetMapping("/approved-users")
    public ResponseEntity<List<DAOUser>> getApprovedUsers() {
        List<DAOUser> approvedUsers = userDetailsService.getApprovedUsers();
        return ResponseEntity.ok(approvedUsers);
    }

    // Approve a user
    @PostMapping("/approve-user/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable Long userId, @RequestParam String approvedBy) {
        try {
            userDetailsService.approveUser(userId, approvedBy);
            return ResponseEntity.ok("User approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error approving user: " + e.getMessage());
        }
    }

    // Reject a user
    @PostMapping("/reject-user/{userId}")
    public ResponseEntity<String> rejectUser(@PathVariable Long userId) {
        try {
            userDetailsService.rejectUser(userId);
            return ResponseEntity.ok("User rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error rejecting user: " + e.getMessage());
        }
    }

    // Get all form submissions
    @GetMapping("/form-submissions")
    public ResponseEntity<List<FormData>> getAllFormSubmissions() {
        List<FormData> submissions = formService.getAllFormSubmissions();
        return ResponseEntity.ok(submissions);
    }

    // Get admin dashboard stats
    @GetMapping("/dashboard-stats")
    public ResponseEntity<Object> getDashboardStats() {
        try {
            Object stats = userDetailsService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting dashboard stats: " + e.getMessage());
        }
    }
} 