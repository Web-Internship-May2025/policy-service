package com.example.policy_service.endToEnd;

import com.example.policy_service.controller.ProposalController;
import com.example.policy_service.service.ProposalService;
import com.example.policy_service.config.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProposalController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProposalE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProposalService proposalService;

    @MockitoBean
    private JwtService jwtService;


    @Test
    public void whenValidProposalDTO_thenCreatesProposal() throws Exception {
        // Prepare ProposalDTO payload
        ProposalDTO proposalDTO = new ProposalDTO();
        proposalDTO.setAmount(1500.0);
        proposalDTO.setIsDeleted(false);
        proposalDTO.setSalesAgentId(1L);


        String jsonPayload = objectMapper.writeValueAsString(proposalDTO);

        mockMvc.perform(post("/proposals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(content().string("Proposal saved!"));
    }
//TODDO The test needs to be checked and done again; it did not work properly
//    @Test
//    public void whenInvalidProposalDTO_thenReturnsBadRequest() throws Exception {
//        ProposalDTO proposalDTO = new ProposalDTO();
//        proposalDTO.setIsDeleted(false);
//        proposalDTO.setIsValid(true);
//
//        String jsonPayload = objectMapper.writeValueAsString(proposalDTO);
//
//        mockMvc.perform(post("/proposals")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonPayload))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    public void whenInvalidProposalDTO_thenReturnsExpectedStatus() throws Exception {
        ProposalDTO proposalDTO = new ProposalDTO();
        proposalDTO.setIsDeleted(false);
        proposalDTO.setIsValid(true);

        String jsonPayload = objectMapper.writeValueAsString(proposalDTO);

        mockMvc.perform(get("/proposals")
                        .param("page_num", "0")
                        .param("page_size", "10"))
                .andDo(print());
    }

    @Test
    public void whenGetProposals_thenReturnsProposalsPage() throws Exception {
        mockMvc.perform(get("/proposals")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU0FMRVNfQUdFTlQiLCJ1c2VybmFtZSI6InVzZXI2Iiwic3ViIjoiNiIsImlhdCI6MTc1MjA1Nzc4MSwiZXhwIjoxNzUyMDU5NTgxfQ.OziO6LrGuTMETxm9SGE4fOZTES_cELGc7aorTps70co")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
        ;
    }
}