package com.dnd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class MiscsManager {
    private static MiscsManager instance;
    private static final Properties miscs = new Properties();
    private static final Properties spells = new Properties();
    private static final Properties ingredients = new Properties();
    public static String language = "it";

    private MiscsManager(String language){
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("miscs_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: miscs_" + language + ".properties");
            }
            miscs.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load miscs file: miscs_" + language + ".properties");
        }

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("spellDescriptions_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: spellDescriptions_" + language + ".properties");
            }
            spells.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load spells file: spellDescriptions_" + language + ".properties");
        }

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("spellMaterials_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: spellMaterials_" + language + ".properties");
            }
            ingredients.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load spells file: spellMaterials_" + language + ".properties");
        }
    }

    // Get the tooltip text for a given key
    public String fetchMisc(String key) {
        String formattedKey = key.replace(" ", "_");
        return miscs.getProperty(formattedKey, ""); // Return an empty string if the key is not found
    }

    public String fetchSpell(String key) {
        String formattedKey = key.replace(" ", "_");
        return spells.getProperty(formattedKey, ""); // Return an empty string if the key is not found
    }

    public String fetchIngredient(String key) {
        String formattedKey = key.replace(" ", "_");
        return ingredients.getProperty(formattedKey, ""); // Return an empty string if the key is not found
    }

    public static MiscsManager getInstance() {
        if (instance == null) {
            instance = new MiscsManager(language);
        }
        return instance;
    }
}
