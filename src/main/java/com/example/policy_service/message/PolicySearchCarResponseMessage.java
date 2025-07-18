package com.example.policy_service.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySearchCarResponseMessage {
    private String correlationId;
    private List<Long> carIds;
}
