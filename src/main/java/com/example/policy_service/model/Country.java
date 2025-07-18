package com.example.policy_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotBlank
    @Column(nullable = false)
    private String countryCode;
    private String name;
    @NotBlank
    @Column(nullable = false, name = "plates_regex")
    private String carPlateNumberRegex;
}
