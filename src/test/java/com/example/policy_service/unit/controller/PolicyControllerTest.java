package com.example.policy_service.unit.controller;

import com.example.policy_service.dto.PolicyDTO;
import com.example.policy_service.dto.ProposalDTO;
import com.example.policy_service.service.PolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;

    private PolicyDTO samplePolicy;

    @BeforeEach
    void setup() {
        ProposalDTO proposal = ProposalDTO.builder()
                .id(1L)
                .build();

        samplePolicy = PolicyDTO.builder()
                .id(1L)
                .dateSigned(LocalDateTime.of(2023, 1, 1, 12, 0))
                .expiringDate(LocalDateTime.of(2024, 1, 1, 12, 0))
                .moneyReceivedDate(LocalDateTime.of(2023, 1, 2, 12, 0))
                .amount(1000.0)
                .isDeleted(false)
                .proposalId(1L)
                .carName("Toyota Corolla")
                .proposal(proposal)
                .build();
    }

    @Test
    @WithMockUser(roles = "SALES_AGENT")
    void testGetPolicy_found() throws Exception {
        Mockito.when(policyService.getPolicyById(1L)).thenReturn(samplePolicy);

        mockMvc.perform(get("/policies/policy/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.carName").value("Toyota Corolla"))
                .andExpect(jsonPath("$.proposal.id").value(1));
    }

    @Test
    @WithMockUser(roles = "SALES_AGENT")
    void testGetPolicy_notFound() throws Exception {
        Mockito.when(policyService.getPolicyById(999L)).thenReturn(null);

        mockMvc.perform(get("/policies/policy/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "SALES_AGENT")
    void testGetPolicies() throws Exception {
        Page<PolicyDTO> page = new PageImpl<>(Collections.singletonList(samplePolicy), PageRequest.of(0, 10), 1);
        Mockito.when(policyService.findAll(0, 10)).thenReturn(page);

        mockMvc.perform(get("/policies")
                        .param("page_num", "0")
                        .param("page_size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "SALES_AGENT")
    void testGetPoliciesBySalesAgent() throws Exception {
        Page<PolicyDTO> page = new PageImpl<>(Collections.singletonList(samplePolicy), PageRequest.of(0, 10), 1);
        Mockito.when(policyService.getPoliciesBySalesAgentId(eq(1L), eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/policies/sales-agent/1")
                        .param("page_num", "0")
                        .param("page_size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "SUBSCRIBER")
    void testGetPoliciesBySubscriber() throws Exception {
        Page<PolicyDTO> page = new PageImpl<>(Collections.singletonList(samplePolicy), PageRequest.of(0, 10), 1);
        Mockito.when(policyService.getPolicies(eq(3L), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        mockMvc.perform(get("/policies/subscriber/3")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortField", "dateSigned")
                        .param("sortDir", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }
}