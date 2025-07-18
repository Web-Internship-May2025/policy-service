package com.example.policy_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean isPremium;
    @NotNull
    @Column(nullable = false)
    private Boolean isDeleted = false;
    @ManyToMany
    @JoinTable(
            name = "insurance_plan_item",
            joinColumns = @JoinColumn(name = "insurance_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "insurance_item_id", nullable = false))
    private Set<InsuranceItem> insuranceItems = new HashSet<>();
}
