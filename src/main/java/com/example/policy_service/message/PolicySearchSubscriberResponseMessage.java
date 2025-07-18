package com.example.policy_service.message;

import com.example.policy_service.dto.userDto.SubscriberInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySearchSubscriberResponseMessage {
    private String correlationId;
    private List<SubscriberInfoDTO> subscriberInfos;
}
