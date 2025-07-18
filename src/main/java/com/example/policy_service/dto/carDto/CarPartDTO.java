package com.example.policy_service.dto.carDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarPartDTO {
    private Long id;
    private String description;
    private Double costOfRepair;
    private Boolean isDeleted;
}
