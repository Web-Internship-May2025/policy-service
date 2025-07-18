package com.example.policy_service.controller;

import com.example.policy_service.dto.ProposalDTO;
import com.example.policy_service.service.ProposalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @PostMapping
    @Valid
    @PreAuthorize("hasRole('SALES_AGENT')")
    public ResponseEntity<?> createProposal() {
        proposalService.createProposal();
        return ResponseEntity.ok("Proposal saved!");
    }

    @GetMapping
    @PreAuthorize("hasRole('SALES_AGENT')")
    public ResponseEntity<Page<ProposalDTO>> getProposals(@RequestParam(value = "page_num", defaultValue = "0") int pageNum, @RequestParam(value = "page_size", defaultValue = "10") int pageSize){
        return ResponseEntity.ok(proposalService.findAll(pageNum, pageSize));
    }
}
