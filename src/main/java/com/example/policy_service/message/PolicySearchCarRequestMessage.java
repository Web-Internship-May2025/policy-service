package com.example.policy_service.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySearchCarRequestMessage {
    private String correlationId;
    private String brandName;
    private String modelName;
    private Integer year;
}
