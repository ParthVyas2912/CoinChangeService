package com.example.CoinChangeService.controller;

import com.example.CoinChangeService.service.ChangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChangeControllerIntegrationTest {

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
    })
    public void testMakeChangeIntegration() throws Exception {
        Map<Double, Integer> expectedChangeMap = new HashMap<>();
        expectedChangeMap.put(0.25, 1);
        expectedChangeMap.put(0.10, 1);
        expectedChangeMap.put(0.05, 1);
        expectedChangeMap.put(0.01, 1);

        String url = "http://localhost:" + port + "/api/change/0.41";
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, null, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedChangeMap, response.getBody());
    }
}
