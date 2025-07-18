package com.example.policy_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//// preskoči Hibernate interne proxy‐e
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isValid;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    private LocalDateTime updateDate;

    @NotNull(message = "Proposal amount is mandatory.")
    private Double amount = 0.0;

    @ManyToOne
    @JoinColumn(name="car_plates_id", nullable = true)
    private CarPlates carPlates;

    @NotNull
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private ProposalStatus proposalStatus;

    @ManyToOne
    @JoinColumn(name="insurance_id")
    private InsurancePlan insurancePlan;

    private Long salesAgentId;
    // lista drivers ids
    private List<Long> drivers;

    @Column(name = "subscriber_id")
    private Long subscriberId;

    @Column(name="car_id")
    private Long carId;

    @OneToOne(mappedBy = "proposal", fetch = FetchType.LAZY)
    @JsonIgnore
    private Policy policy;
}
