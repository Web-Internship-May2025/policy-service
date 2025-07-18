package com.example.policy_service.mapper;

import com.example.policy_service.dto.InsuranceItemDTO;
import com.example.policy_service.model.InsuranceItem;
import org.springframework.stereotype.Component;

@Component
public class InsuranceItemMapper implements IMapper<InsuranceItem, InsuranceItemDTO> {

    @Override
    public InsuranceItemDTO toDto(InsuranceItem entity) {
        if (entity == null) {
            return null;
        }
        return new InsuranceItemDTO(
                entity.getId(),
                entity.getName(),
                entity.isOptional(),
                entity.getFranchisePercentage(),
                entity.getAmount(),
                entity.getIsDeleted()
        );
    }

    @Override
    public InsuranceItem toEntity(InsuranceItemDTO dto) {
        if (dto == null) {
            return null;
        }
        InsuranceItem item = new InsuranceItem();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setOptional(dto.getIsOptional());
        item.setFranchisePercentage(dto.getFranchisePercentage());
        item.setAmount(dto.getAmount());
        item.setIsDeleted(dto.getIsDeleted());
        return item;
    }
}