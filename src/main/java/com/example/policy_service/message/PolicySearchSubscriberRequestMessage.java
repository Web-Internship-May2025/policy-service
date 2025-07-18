package com.example.policy_service.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySearchSubscriberRequestMessage {
    private String correlationId;
    private String firstName;
    private String lastName;
    private String email;
}
