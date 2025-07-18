package com.example.policy_service.client;

import com.example.policy_service.dto.carDto.CarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "car-service")
public interface CarClient {

    @GetMapping("/api/cars/{carId}")
    CarDTO getCarById(@PathVariable("carId") Long carId);
}
