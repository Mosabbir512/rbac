package com.example.mosabbir.service;

import com.example.mosabbir.dto.UserRegistrationDto;
import com.example.mosabbir.entity.Role;
import com.example.mosabbir.entity.User;
import com.example.mosabbir.repository.RoleRepository;
import com.example.mosabbir.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDto registrationDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + registrationDto.getUsername());
        }

        // Create new user
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setName(registrationDto.getName());
        user.setAge(registrationDto.getAge());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setPhoto(registrationDto.getPhoto());
        user.setEnabled(true);

        // Assign default USER role to all new registrations
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found. Please run data initialization."));
        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);
        System.out.println(" User registered with ROLE_USER: " + savedUser.getUsername());

        return savedUser;
    }

    // Get all users (for admin)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Assign admin role to user
    public User assignAdminRole(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        user.getRoles().add(adminRole);
        User savedUser = userRepository.save(user);
        System.out.println(" Admin role assigned to: " + username);
        return savedUser;
    }

    // Remove admin role from user
    public User removeAdminRole(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        user.getRoles().remove(adminRole);

        // Ensure user has at least ROLE_USER
        if (user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            user.getRoles().add(userRole);
        }

        User savedUser = userRepository.save(user);
        System.out.println(" Admin role removed from: " + username);
        return savedUser;
    }

    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}