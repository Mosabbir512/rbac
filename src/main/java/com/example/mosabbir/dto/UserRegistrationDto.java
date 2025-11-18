package com.example.mosabbir.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String phoneNumber;
    private String photo;
    private String role; // NEW: "USER" or "ADMIN"
}