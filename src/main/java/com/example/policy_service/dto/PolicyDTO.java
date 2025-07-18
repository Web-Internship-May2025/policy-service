package com.example.policy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PolicyDTO {
    private Long id;
    private LocalDateTime dateSigned;
    private LocalDateTime expiringDate;
    private LocalDateTime moneyReceivedDate;
    private Double amount;
    private Boolean isDeleted;

    private Long proposalId;
    private Long carId;
    private Long subscriberId;

    private String firstName;
    private String lastName;
}
