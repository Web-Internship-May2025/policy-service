package com.example.policy_service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignClientAuthorizationInterceptor();
    }
}
