package com.dnd.frontend.language;

public class Constants {
    public static double WEIGHT_MULTIPLIER; // lb->kg conversion. 1 lb = 0.5 kg. lb is default
    public static double LENGTH_MULTIPLIER; // ft->m conversion. 1 ft = 0.3 m. ft is default

    public static void initialize(String language) {
        if (language.equals("it")) {
            WEIGHT_MULTIPLIER = 0.5;
            LENGTH_MULTIPLIER = 0.3;
        } else if (language.equals("en")) {
            WEIGHT_MULTIPLIER = 1;
            LENGTH_MULTIPLIER = 1;
        }
    }
}