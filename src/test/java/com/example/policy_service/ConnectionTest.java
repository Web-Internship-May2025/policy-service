package com.example.policy_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class ConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void ConnectionTest() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        if (result != null && result == 1) {
            System.out.println("Veza je uspesna");
        } else {
            System.out.println("Nije uspostavljena veza");
        }
    }
}
