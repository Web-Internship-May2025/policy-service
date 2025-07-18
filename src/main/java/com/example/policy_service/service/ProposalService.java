package com.example.policy_service.service;

import com.example.policy_service.client.UserClient;
import com.example.policy_service.config.CustomUserPrincipal;
import com.example.policy_service.config.JwtService;
import com.example.policy_service.dto.ProposalDTO;
import com.example.policy_service.dto.userDto.UserDTO;
import com.example.policy_service.mapper.ProposalMapper;
import com.example.policy_service.model.Proposal;
import com.example.policy_service.model.ProposalStatus;
import com.example.policy_service.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProposalMapper proposalMapper;

    private final UserClient userClient;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository, UserClient userClient) {
        this.proposalRepository = proposalRepository;
        this.userClient = userClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createProposal() {
        CustomUserPrincipal principal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getId();
//        ovo se kasnije dodaje, ne prilikom inicijalizacije
//        Long subscriberId = principal.getId();
//        Long carId = principal.getId();
        LocalDateTime now = LocalDateTime.now();
        Proposal proposal = new Proposal();
        proposal.setIsValid(true);
        proposal.setUpdateDate(now);
        proposal.setCreationDate(now);
        proposal.setProposalStatus(ProposalStatus.INITIALIZED);
        proposal.setSalesAgentId(userId);
        proposalRepository.save(proposal);
    }

    public Page<ProposalDTO> findAll(int pageNum, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        List<Proposal> proposals = proposalRepository.findAllByIsDeleted(false);

        List<ProposalDTO> proposalDTOS = new ArrayList<>();

        for(Proposal proposal : proposals){
            ProposalDTO proposalDTO = proposalMapper.toDto(proposal);
            if(proposal.getSubscriberId() != null){
                UserDTO userDTO = userClient.getUserById(proposal.getSubscriberId());
                proposalDTO.setSubscriberName(userDTO.getFirstName());
            }
            proposalDTOS.add(proposalDTO);
        }
        return new PageImpl<>(proposalDTOS, pageRequest, proposalDTOS.size());
    }
}
