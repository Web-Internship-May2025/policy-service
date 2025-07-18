package com.example.policy_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Franchise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Percentage is mandatory.")
    @Column(nullable = false)
    @Min(0)
    private Integer percentage;
    @NotNull
    @Column(nullable = false)
    private Boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name="proposal_id", nullable = false)
    private Proposal proposal;
    @ManyToOne
    @JoinColumn(name="insurance_item_id", nullable = false)
    private InsuranceItem insuranceItem;
}
