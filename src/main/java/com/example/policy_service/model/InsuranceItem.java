package com.example.policy_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsuranceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isOptional;
    @NotNull(message = "Franchise percentage is mandatory.")
    @Column(nullable = false)
    private Integer franchisePercentage;
    @NotNull(message = "Item amount is mandatory.")
    @Column(nullable = false)
    @DecimalMin("0.0")
    private Double amount;
    @NotNull
    @Column(nullable = false)
    private Boolean isDeleted = false;
}
