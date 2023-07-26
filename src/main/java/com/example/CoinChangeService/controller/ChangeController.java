// ChangeController
package com.example.CoinChangeService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.CoinChangeService.service.ChangeService;

import java.util.Map;

@RestController
@RequestMapping("/api/change")
public class ChangeController {

    private ChangeService changeService;

    @Autowired
    public ChangeController(ChangeService changeService) {
        this.changeService = changeService;
    }

    @PostMapping("/{bill}")
    public ResponseEntity<Map<Double, Integer>> makeChange(@PathVariable double bill, @RequestParam(defaultValue = "false") boolean maxCoins) {
        try {
            Map<Double, Integer> change = changeService.calculateChange(bill, maxCoins);
            return ResponseEntity.ok(change);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
