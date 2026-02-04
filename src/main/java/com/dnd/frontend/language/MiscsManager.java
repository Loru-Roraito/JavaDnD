package com.dnd.frontend.language;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class MiscsManager {
    private static final Properties miscs = new Properties();
    private static final Properties spells = new Properties();
    private static final Properties ingredients = new Properties();

    public static void initialize(String language) {
        try (var inputStream = MiscsManager.class.getResourceAsStream("/miscs_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: miscs_" + language + ".properties");
            }
            miscs.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));

            // Try to load and merge custom version if it exists
            String customFileName = "/custom/miscs_" + language + ".properties";
            var customStream = MiscsManager.class.getResourceAsStream(customFileName);
            if (customStream != null) {
                try (customStream;
                    var customReader = new java.io.InputStreamReader(customStream, StandardCharsets.UTF_8)) {
                    Properties customProperties = new Properties();
                    customProperties.load(customReader);
                    // Merge: custom properties overwrite base properties
                    miscs.putAll(customProperties);
                } catch (IOException e) {
                    System.err.println("Error: Failed to load custom translations from " + customFileName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load miscs file: miscs_" + language + ".properties");
        }

        try (var inputStream = MiscsManager.class.getResourceAsStream("/spellDescriptions_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: spellDescriptions_" + language + ".properties");
            }
            spells.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load spells file: spellDescriptions_" + language + ".properties");
        }

        try (var inputStream = MiscsManager.class.getResourceAsStream("/spellMaterials_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: spellMaterials_" + language + ".properties");
            }
            ingredients.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load spells file: spellMaterials_" + language + ".properties");
        }
    }

    // Get the tooltip text for a given key
    public static String getMisc(String key) {
        String formattedKey = key.replace(" ", "_");
        return miscs.getProperty(formattedKey, ""); // Return an empty string if the key is not found
    }

    public static String getDescription(String key) {
        String formattedKey = key.replace(" ", "_");
        return spells.getProperty(formattedKey, ""); // Return an empty string if the key is not found
    }

    public static String getIngredient(String key) {
        String formattedKey = key.replace(" ", "_");
        return ingredients.getProperty(formattedKey, ""); // Return an empty string if the key is not found
    }
}
