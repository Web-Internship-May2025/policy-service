package com.example.policy_service.dto;

import com.example.policy_service.model.ProposalStatus;
import com.example.policy_service.util.DateTimeUtil;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalDTO {

    private Long id;
    private Boolean isValid;

    @Pattern(regexp = DateTimeUtil.DATE_FORMAT_REGEX, message = "createdAt must match pattern: " + DateTimeUtil.DATE_FORMAT_PATTERN)
    private String creationDate;
    @Pattern(regexp = DateTimeUtil.DATE_FORMAT_REGEX, message = "createdAt must match pattern: " + DateTimeUtil.DATE_FORMAT_PATTERN)
    private String updateDate;

    @NotNull(message = "Proposal amount is mandatory.")
    @DecimalMin("0.0")
    private Double amount;

    private CarPlatesDTO carPlates;
    @NotNull
    private Boolean isDeleted = false;
    private ProposalStatus proposalStatus;

    private Long insurancePlanId;
    @NotNull
    private Long salesAgentId;
    private Long subscriberId;
    private String subscriberName;
    private List<Long> drivers;
    private Long carId;
}
