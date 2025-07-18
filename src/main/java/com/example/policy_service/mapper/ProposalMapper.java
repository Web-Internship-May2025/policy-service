package com.example.policy_service.mapper;

import com.example.policy_service.dto.ProposalDTO;
import com.example.policy_service.model.Proposal;
import com.example.policy_service.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ProposalMapper implements IMapper<Proposal, ProposalDTO>{

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private final CarPlatesMapper carPlatesMapper =  new CarPlatesMapper();
    private final InsurancePlanMapper insurancePlanMapper = new InsurancePlanMapper();

    @Override
    public ProposalDTO toDto (Proposal entity){
        if (entity == null) {
            return null;
        }

        return new ProposalDTO(
                entity.getId(),
                entity.getIsValid(),
                DateTimeUtil.convertToString(entity.getUpdateDate()),
                DateTimeUtil.convertToString(entity.getCreationDate()),
                entity.getAmount(),
                carPlatesMapper.toDto(entity.getCarPlates()),
                entity.getIsDeleted(),
                entity.getProposalStatus(),
                entity.getInsurancePlan() != null ? insurancePlanMapper.toDto(entity.getInsurancePlan()).getId() : null,
                entity.getSalesAgentId(),
                entity.getSubscriberId(),
                null,
                entity.getDrivers(),
                entity.getCarId()
        );
    }

    @Override
    public Proposal toEntity (ProposalDTO dto){
        if (dto == null) {
            return null;
        }
        Proposal proposal = new Proposal();
        proposal.setId(dto.getId());
        proposal.setIsValid(dto.getIsValid());
        if (dto.getUpdateDate() != null && !dto.getUpdateDate().isEmpty()) {
            proposal.setUpdateDate(LocalDateTime.parse(dto.getUpdateDate(), formatter));
        }
        if (dto.getCreationDate() != null && !dto.getCreationDate().isEmpty()) {
            proposal.setCreationDate(LocalDateTime.parse(dto.getCreationDate(), formatter));
        }
        proposal.setAmount(dto.getAmount());
        proposal.setIsDeleted(dto.getIsDeleted());
        proposal.setProposalStatus(dto.getProposalStatus());
        proposal.setSalesAgentId(dto.getSalesAgentId());
        proposal.setSubscriberId(dto.getSubscriberId());
        proposal.setDrivers(dto.getDrivers());
        proposal.setCarId(dto.getCarId());

        return proposal;
    }
}
