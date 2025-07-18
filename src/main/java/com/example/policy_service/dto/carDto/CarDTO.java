package com.example.policy_service.dto.carDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarDTO {
    private Long id;
    private Integer year;
    private String image;
    private Boolean isDeleted;
    private List<CarPartDTO> carParts;
    private Long modelId;
}
