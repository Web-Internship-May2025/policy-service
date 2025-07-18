package com.example.policy_service.unit.controller;

import com.example.policy_service.dto.ProposalDTO;
import com.example.policy_service.service.ProposalService;
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

import java.util.Collections;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyIntInt;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class ProposalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProposalService proposalService;

    private ProposalDTO sampleProposal;

    @BeforeEach
    void setup() {
        sampleProposal = ProposalDTO.builder()
                .id(1L)
                .build();
    }

    @Test
    @WithMockUser(roles = "SALES_AGENT")
    void testCreateProposal_success() throws Exception {
        doNothing().when(proposalService).createProposal();

        mockMvc.perform(post("/proposals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Proposal saved!"));
    }

    @Test
    @WithMockUser(roles = "SALES_AGENT")
    void testGetProposals_success() throws Exception {
        Page<ProposalDTO> page = new PageImpl<>(Collections.singletonList(sampleProposal), PageRequest.of(0, 10), 1);
        Mockito.when(proposalService.findAll(0, 10)).thenReturn(page);

        mockMvc.perform(get("/proposals")
                        .param("page_num", "0")
                        .param("page_size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }
}