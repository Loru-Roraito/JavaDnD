package com.dnd.utils;

import java.util.Random;

import com.dnd.backend.GameCharacter;
import com.dnd.frontend.panes.SystemPane;

public class ThrowManager {
    private static ThrowManager instance;
    private final Random random = new Random(); // Reusable Random instance

    public int throwDice(int times, int size, int bonus, Boolean advantage, Boolean disadvantage, int ability, GameCharacter character, SystemPane systemPane) {
        if (size == 20 && times == 1) {
            // Exhaustion affects D20 tests
            int exhaustion = systemPane.getCharacter().getExhaustion().get();
            bonus -= exhaustion * 2;

            if ((ability == 0 || ability == 1) && !character.hasArmorProficiency().get()) {
                disadvantage = true;
            }
        }
        Boolean adv;
        Boolean disadv;
        
        switch (systemPane.getAdvantage()) {
            case "NONE_M" -> {
                adv = false;
                disadv = false;
            }
            case "ADVANTAGE" -> {
                adv = true;
                disadv = false;
            }
            case "DISADVANTAGE" -> {
                adv = false;
                disadv = true;
            }
            default -> {
                adv = advantage;
                disadv = disadvantage;
            }
        }

        return throwDice(times, size, bonus, adv, disadv);
    }

    public int throwDice(int times, int size, int bonus, Boolean advantage, Boolean disadvantage) {
        if (size <= 0) {
            throw new IllegalArgumentException("Die size must be greater than 0");
        }
        if (times <= 0) {
            throw new IllegalArgumentException("Number of rolls (times) must be greater than 0");
        }
    
        int total = 0;
    
        for (int i = 0; i < times; i++) {
            int roll;
            if (advantage && !disadvantage) {
                roll = Math.max(singleRoll(size), singleRoll(size));
            } else if (disadvantage && !advantage) {
                roll = Math.min(singleRoll(size), singleRoll(size));
            } else {
                roll = singleRoll(size);
            }
            total += roll;
        }

        return total + bonus;
    }
    
    public int rollFourDropLowest() {
        // Roll 4 dice
        int[] rolls = new int[4];
        for (int i = 0; i < 4; i++) {
            rolls[i] = throwDice(1, 6, 0, false, false); // Roll a d6
        }
        
        // Find the lowest value
        int lowest = 6;
        for (int roll : rolls) {
            if (roll < lowest) {
                lowest = roll;
            }
        }
    
        // Calculate the sum of the remaining three rolls
        int sum = 0;
        for (int roll : rolls) {
            sum += roll;
        }
        sum -= lowest; // Subtract the lowest roll
    
        return sum;
    }

    public static ThrowManager getInstance() {
        if (instance == null) {
            instance = new ThrowManager();
        }
        return instance;
    }

    // Helper method for a single die roll
    private int singleRoll(int size) {
        return random.nextInt(size) + 1; // Generate a number between 1 and size
    }
}