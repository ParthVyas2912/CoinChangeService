package com.example.CoinChangeService.service;

import com.example.CoinChangeService.exception.CoinChangeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChangeService {

    @Value("${coin.change.service.initialCoins.penny}")
    private int initialPennies;

    @Value("${coin.change.service.initialCoins.nickel}")
    private int initialNickels;

    @Value("${coin.change.service.initialCoins.dime}")
    private int initialDimes;

    @Value("${coin.change.service.initialCoins.quarter}")
    private int initialQuarters;

    private Map<Double, Integer> coinInventory;
    private Set<Double> allowedBills = new HashSet<>(Arrays.asList(1.0, 2.0, 5.0, 10.0, 20.0, 50.0, 100.0));

    public ChangeService() {
        coinInventory = new HashMap<>();
        coinInventory.put(0.01, 100);
        coinInventory.put(0.05, 100);
        coinInventory.put(0.10, 100);
        coinInventory.put(0.25, 100);
    }

    public Map<Double, Integer> calculateChange(double bill, boolean maxCoins) {
        // Check if the given bill is allowed
        if(!allowedBills.contains(bill)) {
            throw new CoinChangeException("Invalid bill passed: " + bill + ". Available bills are (1, 2, 5, 10, 20, 50, 100)");
        }

        Map<Double, Integer> change = new HashMap<>();
        double remainingAmount = bill;

        // Loop over the coin inventory in decreasing order of coin value if maxCoins is false, 
        // and in increasing order if maxCoins is true
        List<Double> denominations = new ArrayList<>(coinInventory.keySet());
        if(maxCoins) {
            denominations.sort(Comparator.naturalOrder());
        } else {
            denominations.sort(Comparator.reverseOrder());
        }

        for (double denomination : denominations) {
            int coinCount = coinInventory.get(denomination);

            // Check how many coins of this denomination we need for the remaining amount
            int neededCoins = (int) (remainingAmount / denomination);

            // Use the smaller of the two counts
            int usedCoins = Math.min(neededCoins, coinCount);

            // Update the remaining amount and the coin inventory
            remainingAmount -= usedCoins * denomination;
            coinInventory.put(denomination, coinCount - usedCoins);

            // If we used any coins of this denomination, add them to the change
            if (usedCoins > 0) {
                change.put(denomination, usedCoins);
            }
        }

        // If we have a remaining amount, we don't have enough change
        if (remainingAmount > 0) {
            throw new CoinChangeException("Not enough change available.");
        }
        return change;
    }
}
