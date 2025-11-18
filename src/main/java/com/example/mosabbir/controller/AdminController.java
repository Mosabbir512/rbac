package com.example.mosabbir.controller;

import com.example.mosabbir.entity.User;
import com.example.mosabbir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by username
    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Promote user to admin
    @PostMapping("/promote/{username}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable String username) {
        try {
            User user = userService.assignAdminRole(username);
            return ResponseEntity.ok(" User promoted to ADMIN: " + username);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(" Error: " + e.getMessage());
        }
    }

    // Demote admin to user
    @PostMapping("/demote/{username}")
    public ResponseEntity<?> demoteToUser(@PathVariable String username) {
        try {
            User user = userService.removeAdminRole(username);
            return ResponseEntity.ok(" User demoted to USER: " + username);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(" Error: " + e.getMessage());
        }
    }

    // Admin dashboard info
    @GetMapping("/dashboard")
    public ResponseEntity<?> getAdminDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("message", "Welcome to Admin Dashboard");
        dashboard.put("totalUsers", userService.getAllUsers().size());
        dashboard.put("adminActions", List.of("Manage Users", "View Reports", "System Settings"));
        return ResponseEntity.ok(dashboard);
    }
}