package com.example.mosabbir.controller;

import com.example.mosabbir.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/my-permissions")
    public ResponseEntity<?> getMyPermissions(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.badRequest().body("Not authenticated");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        List<String> authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", userPrincipal.getUsername());
        userInfo.put("authorities", authorities);
        userInfo.put("hasProductRead", authorities.contains("PRODUCT_READ"));
        userInfo.put("hasProductWrite", authorities.contains("PRODUCT_WRITE"));
        userInfo.put("hasProductUpdate", authorities.contains("PRODUCT_UPDATE"));
        userInfo.put("hasProductDelete", authorities.contains("PRODUCT_DELETE"));
        userInfo.put("isAdmin", authorities.contains("ROLE_ADMIN"));
        userInfo.put("isUser", authorities.contains("ROLE_USER"));

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/check-db")
    public ResponseEntity<?> checkDatabase() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Database connection is working");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }
}