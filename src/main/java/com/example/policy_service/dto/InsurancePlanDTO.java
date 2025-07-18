package com.example.policy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePlanDTO {

    private Long id;
    private String name;
    private Boolean isPremium;
    private Boolean isDeleted;
    private Set<InsuranceItemDTO> insuranceItem;
}
