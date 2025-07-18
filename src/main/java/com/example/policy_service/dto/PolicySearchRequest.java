package com.example.policy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicySearchRequest {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate date;
    private String brandName;
    private String modelName;
    private Integer carYear;

    private Integer page = 0;
    private Integer size = 20;
}
