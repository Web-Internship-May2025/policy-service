package com.example.policy_service.config;

import lombok.Getter;

@Getter
public class CustomUserPrincipal {
    private Long id;
    private String username;
    private String token;

    public CustomUserPrincipal(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }
}
