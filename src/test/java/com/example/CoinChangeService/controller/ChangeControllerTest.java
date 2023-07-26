package com.example.CoinChangeService.controller;

import com.example.CoinChangeService.service.ChangeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChangeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ChangeService changeService;

	private ObjectMapper objectMapper = new ObjectMapper();
    
	@Test
    public void testMakeChange() throws Exception {
        // Define your expected change map
        Map<Double, Integer> expectedChangeMap = new HashMap<>();
        expectedChangeMap.put(0.25, 1);
        expectedChangeMap.put(0.10, 1);
        expectedChangeMap.put(0.05, 1);
        expectedChangeMap.put(0.01, 1);

        // Convert map to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedChangeMapAsJsonString = objectMapper.writeValueAsString(expectedChangeMap);

        // When calculateChange() is called with 0.41, return the expectedChangeMap
        when(changeService.calculateChange(0.41, false)).thenReturn(expectedChangeMap);

        // Perform the POST request and check that the response is the expected JSON
        mockMvc.perform(post("/api/change/0.41")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedChangeMapAsJsonString));
    }

    @Test
    public void testMakeChangeNotEnoughCoins() throws Exception {
        // When calculateChange() is called with 1000.00, throw an IllegalArgumentException
        when(changeService.calculateChange(1000.00, false)).thenThrow(new IllegalArgumentException("Not enough change available."));

        // Perform the POST request and check that the response status is 400 Bad Request
        mockMvc.perform(post("/api/change/1000.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    
    @Test
    public void testMakeChangeZeroAmount() throws Exception {
        Map<Double, Integer> expectedChangeMap = new HashMap<>();
        expectedChangeMap.put(0.25, 0);
        expectedChangeMap.put(0.10, 0);
        expectedChangeMap.put(0.05, 0);
        expectedChangeMap.put(0.01, 0);

        String expectedChangeMapAsJsonString = objectMapper.writeValueAsString(expectedChangeMap);

        when(changeService.calculateChange(0.0, false)).thenReturn(expectedChangeMap);

        mockMvc.perform(post("/api/change/0.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedChangeMapAsJsonString));
    }

    @Test
    public void testMakeChangeMaxCoins() throws Exception {
        Map<Double, Integer> expectedChangeMap = new HashMap<>();
        expectedChangeMap.put(0.25, 1);
        expectedChangeMap.put(0.10, 0);
        expectedChangeMap.put(0.05, 1);
        expectedChangeMap.put(0.01, 4);

        String expectedChangeMapAsJsonString = objectMapper.writeValueAsString(expectedChangeMap);

        when(changeService.calculateChange(0.29, true)).thenReturn(expectedChangeMap);

        mockMvc.perform(post("/api/change/max-coins/0.29")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedChangeMapAsJsonString));
    }

}
