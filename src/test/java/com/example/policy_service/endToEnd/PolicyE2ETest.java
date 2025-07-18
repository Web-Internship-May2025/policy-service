package com.example.policy_service.endToEnd;
import com.example.policy_service.config.JwtService;
import com.example.policy_service.controller.PolicyController;
import com.example.policy_service.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(controllers = PolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PolicyE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PolicyService policyService;
    @MockitoBean
    private JwtService jwtService;

    @Test
    public void testGetPoliciesE2E() throws Exception {
        mockMvc.perform(get("/policies")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print());

    }

    @Test
    public void testGetSalesAgentPoliciesE2E() throws Exception {
        mockMvc.perform(get("/policies/sales-agent/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print());

    }
}