package com.example.policy_service.mapper;

import com.example.policy_service.dto.PolicyDTO;
import com.example.policy_service.model.Policy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PolicyMapper implements IMapper<Policy, PolicyDTO>{
    @Override
    public PolicyDTO toDto(Policy entity){
        if (entity == null) return null;

        PolicyDTO dto = new PolicyDTO();
        dto.setId(entity.getId());
        dto.setDateSigned(entity.getDateSigned());
        dto.setExpiringDate(entity.getExpiringDate());
        dto.setMoneyReceivedDate(entity.getMoneyReceivedDate());
        dto.setAmount(entity.getAmount());
        dto.setIsDeleted(entity.getIsDeleted());

        if (entity.getProposal() != null) {
            dto.setProposalId(entity.getProposal().getId());
            dto.setSubscriberId(entity.getProposal().getSubscriberId());
            dto.setCarId(entity.getProposal().getCarId());
        }
        return dto;
    }

    @Override
    public Policy toEntity(PolicyDTO dto){
        // nigde se ne koristi
        return null;
    }

    public List<PolicyDTO> listToDTO(List<Policy> policies) {
        if (policies == null) return List.of();
        return policies.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
