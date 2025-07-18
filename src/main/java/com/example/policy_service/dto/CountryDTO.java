package com.example.policy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

    private Long id;
    private String countryCode;
    private String name;
    private String carPlateNumberRegex;
}
