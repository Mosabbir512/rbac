package com.example.mosabbir.dto;


import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String name;

    public JwtResponse(String token, String username, String name) {
        this.token = token;
        this.username = username;
        this.name = name;
    }
}