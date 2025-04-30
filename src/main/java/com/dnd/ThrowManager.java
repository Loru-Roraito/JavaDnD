package com.dnd;

import java.util.Random;

public class ThrowManager {
    private static ThrowManager instance;
    private final Random random = new Random(); // Reusable Random instance

    public int ThrowDice(int times, int size, int base, int bonus, boolean advantage, boolean disadvantage) {
        if (size <= 0) {
            throw new IllegalArgumentException("Die size must be greater than 0");
        }
        if (times <= 0) {
            throw new IllegalArgumentException("Number of rolls (times) must be greater than 0");
        }
    
        int total = base;
    
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