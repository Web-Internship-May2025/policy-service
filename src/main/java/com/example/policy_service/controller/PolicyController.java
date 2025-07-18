package com.example.policy_service.controller;

import com.example.policy_service.dto.PolicyDTO;
import com.example.policy_service.dto.PolicySearchRequest;
import com.example.policy_service.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @PreAuthorize("hasRole('SALES_AGENT')")
    @PostMapping("/search")
    public CompletableFuture<ResponseEntity<?>> search(
            @RequestBody PolicySearchRequest request
    ) {
        return policyService.searchPolicies(request)
                .<ResponseEntity<?>>thenApply(policies -> {
                    Map<String,Object> okBody = Map.of(
                            "message","Search completed successfully",
                            "data",   policies
                    );
                    return ResponseEntity.ok(okBody);
                })
                .exceptionally(ex -> {
                    String errorMsg = ex.getCause()!=null
                            ? ex.getCause().getMessage()
                            : ex.getMessage();
                    Map<String,Object> errBody = Map.of(
                            "message","Search failed",
                            "error",   errorMsg
                    );
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(errBody);
                });
    }

    @PreAuthorize("hasRole('SALES_AGENT')")
    @GetMapping("/policy/{id}")
    public ResponseEntity<PolicyDTO> getPolicy(@PathVariable Long id) {
        PolicyDTO policy = policyService.getPolicyById(id);
        if (policy != null) {
            return ResponseEntity.ok(policy);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('SALES_AGENT')")
    @GetMapping
    public ResponseEntity<Page<PolicyDTO>> getPolicies(
            @RequestParam(value = "page_num", defaultValue = "0") int pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") int pageSize){
        Page<PolicyDTO> policies = policyService.findAll(pageNum, pageSize);
        return ResponseEntity.ok(policies);
    }

    @PreAuthorize("hasRole('SALES_AGENT')")
    @GetMapping("/sales-agent/{salesAgentId}")
    public ResponseEntity<Page<PolicyDTO>> getPoliciesBySalesAgent(
            @PathVariable Long salesAgentId,
            @RequestParam(value = "page_num", defaultValue = "0") int pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(policyService.getPoliciesBySalesAgentId(salesAgentId, pageNum, pageSize));
    }

    @PreAuthorize("hasRole('SUBSCRIBER')")
    @GetMapping("/subscriber/{subscriberId}")
    public ResponseEntity<Page<PolicyDTO>> getPolicies(
            @PathVariable Long subscriberId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="dateSigned") String sortField,
            @RequestParam(defaultValue="asc") String sortDir) {
        Page<PolicyDTO> result = policyService.getPolicies(subscriberId, page, size, sortField, sortDir);
        return ResponseEntity.ok(result);
    }
}
