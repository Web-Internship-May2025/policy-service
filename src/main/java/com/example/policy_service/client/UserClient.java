package com.example.policy_service.client;

import com.example.policy_service.config.FeignConfig;
import com.example.policy_service.dto.userDto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-management-service", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/users/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
