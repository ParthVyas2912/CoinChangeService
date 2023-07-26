package com.example.CoinChangeService.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.CoinChangeService.service.ChangeService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

@SpringBootTest
public class ChangeServiceTest {

    @Autowired
    private ChangeService changeService;

    @Test
    public void testCalculateChange() {
        Map<Double, Integer> change = changeService.calculateChange(0.41, false);
        assertEquals(1, change.get(0.25));
        assertEquals(1, change.get(0.10));
        assertEquals(1, change.get(0.05));
        assertEquals(1, change.get(0.01));
    }

    @Test
    public void testCalculateChangeNotEnoughCoins() {
        assertThrows(IllegalArgumentException.class, () -> {
            changeService.calculateChange(1000.00, false); // this should fail because we don't have enough coins
        });
    }
}
