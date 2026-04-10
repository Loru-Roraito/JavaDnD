package com.dnd.frontend.language;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DescriptionManager {
    private static final Properties itemDescriptions = new Properties();
    private static final Properties traitDescriptions = new Properties();
    private static final Properties spellDescriptions = new Properties();
    private static final Properties ingredients = new Properties();

    public static void initialize(String language) {
        try (var inputStream = DescriptionManager.class.getResourceAsStream("/itemDescriptions_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: itemDescriptions_" + language + ".properties");
            }
            itemDescriptions.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String customFileName = "/custom/itemDescriptions_" + language + ".properties";
            var customStream = DescriptionManager.class.getResourceAsStream(customFileName);
            if (customStream != null) {
                try (customStream;
                    var customReader = new java.io.InputStreamReader(customStream, StandardCharsets.UTF_8)) {
                    Properties customProperties = new Properties();
                    customProperties.load(customReader);
                    // Merge: custom properties overwrite base properties
                    itemDescriptions.putAll(customProperties);
                } catch (IOException e) {
                    System.err.println("Error: Failed to load custom translations from " + customFileName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load items file: itemDescriptions_" + language + ".properties");
        }

        try (var inputStream = DescriptionManager.class.getResourceAsStream("/traitDescriptions_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: traitDescriptions_" + language + ".properties");
            }
            traitDescriptions.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));

            // Try to load and merge custom version if it exists
            String customFileName = "/custom/traitDescriptions_" + language + ".properties";
            var customStream = DescriptionManager.class.getResourceAsStream(customFileName);
            if (customStream != null) {
                try (customStream;
                    var customReader = new java.io.InputStreamReader(customStream, StandardCharsets.UTF_8)) {
                    Properties customProperties = new Properties();
                    customProperties.load(customReader);
                    // Merge: custom properties overwrite base properties
                    traitDescriptions.putAll(customProperties);
                } catch (IOException e) {
                    System.err.println("Error: Failed to load custom translations from " + customFileName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load traits file: traitDescriptions_" + language + ".properties");
        }

        try (var inputStream = DescriptionManager.class.getResourceAsStream("/spellDescriptions_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: spellDescriptions_" + language + ".properties");
            }
            spellDescriptions.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load spells file: spellDescriptions_" + language + ".properties");
        }

        try (var inputStream = DescriptionManager.class.getResourceAsStream("/spellMaterials_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: spellMaterials_" + language + ".properties");
            }
            ingredients.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load ingredients file: spellMaterials_" + language + ".properties");
        }
    }

    public static String getItemDescription(String key) {
        return itemDescriptions.getProperty(key, "");
    }

    public static String getTraitDescription(String key) {
        return traitDescriptions.getProperty(key, "");
    }

    public static String getSpellDescription(String key) {
        return spellDescriptions.getProperty(key, "");
    }

    public static String getSpellIngredient(String key) {
        return ingredients.getProperty(key, "");
    }
}
