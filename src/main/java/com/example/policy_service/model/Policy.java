package com.example.policy_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateSigned;
    private LocalDateTime expiringDate;
    private LocalDateTime moneyReceivedDate;
    @NotNull(message = "Policy amount is mandatory.")
    @Column(nullable = false)
    @DecimalMin("0.0")
    private Double amount;
    @NotNull
    @Column(nullable = false)
    private Boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name="proposal_id", nullable = false)
    @JsonIgnore
    private Proposal proposal;
}
