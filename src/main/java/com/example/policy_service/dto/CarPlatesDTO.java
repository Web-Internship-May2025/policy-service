package com.example.policy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarPlatesDTO {
    private Long id;
    private CountryDTO country;
    private String plateNumber;
}
