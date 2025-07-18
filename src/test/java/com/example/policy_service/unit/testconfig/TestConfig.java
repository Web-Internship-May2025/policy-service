package com.example.policy_service.unit.testconfig;

import com.example.policy_service.service.PolicyService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public PolicyService policyService(){
        return Mockito.mock(PolicyService.class);
    }
}
