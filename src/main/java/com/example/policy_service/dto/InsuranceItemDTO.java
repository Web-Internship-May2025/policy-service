package com.example.policy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceItemDTO {

    private Long id;
    private String name;
    private Boolean isOptional;
    private Integer franchisePercentage;
    private Double amount;
    private Boolean isDeleted;
}
