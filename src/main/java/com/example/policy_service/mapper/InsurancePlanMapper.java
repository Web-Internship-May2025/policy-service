package com.example.policy_service.mapper;

import com.example.policy_service.dto.InsurancePlanDTO;
import com.example.policy_service.model.InsurancePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class InsurancePlanMapper implements IMapper<InsurancePlan, InsurancePlanDTO> {

    @Autowired
    private final InsuranceItemMapper itemMapper = new InsuranceItemMapper();

    @Override
    public InsurancePlanDTO toDto(InsurancePlan entity) {
        if (entity == null) {
            return null;
        }
        return new InsurancePlanDTO(
                entity.getId(),
                entity.getName(),
                entity.getIsPremium(),
                entity.getIsDeleted(),
                entity.getInsuranceItems().stream()
                        .map(itemMapper::toDto)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public InsurancePlan toEntity(InsurancePlanDTO dto) {
        if (dto == null) {
            return null;
        }
        InsurancePlan entity = new InsurancePlan();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setIsPremium(dto.getIsPremium());
        entity.setIsDeleted(dto.getIsDeleted());
        if (dto.getInsuranceItem() != null) {
            entity.setInsuranceItems(
                    dto.getInsuranceItem().stream()
                            .map(itemMapper::toEntity)
                            .collect(Collectors.toSet())
            );
        }
        return entity;
    }
}