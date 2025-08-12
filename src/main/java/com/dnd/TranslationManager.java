package com.dnd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TranslationManager {
    private static TranslationManager instance;
    private JsonNode rootNode;
    private final Properties languageProperties = new Properties();
    public static String language = "it";
    private final Map<String, String> keyToTranslation = new HashMap<>();
    private final Map<String, String> translationToKey = new HashMap<>();

    private TranslationManager(String language) {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("translations_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: translations_" + language + ".properties");
            }
            languageProperties.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
            // Populate the maps
            for (String key : languageProperties.stringPropertyNames()) {
                String value = languageProperties.getProperty(key);
                keyToTranslation.put(key, value);
                translationToKey.put(value, key);
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load translations file: translations_" + language + ".properties");
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("groups.json")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: groups.json");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            System.err.println("Error: Failed to load groups.json");
        }
    }

    public static TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager(language);
        }
        return instance;
    }

    public String[] getGroup(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            if (node == null) { // Check if node is null before accessing
                return new String[0];
            }
            node = node.get(subGroup);
            if (subGroup.equals("RANDOM")) {
                return new String[0];
            }
        }
        List<String> keys = new ArrayList<>();
        if (node == null) { // Also check after the loop
            return new String[0];
        }
        if (node.isObject()) {
            node.fieldNames().forEachRemaining(keys::add); // Add all field names (keys) to the list
        }
        else {
            for (JsonNode element : node) {
                keys.add(element.asText());
            }
        }
        return keys.toArray(String[]::new); // Use method reference to create the array
    }

    public String getString(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            node = node.get(subGroup);
            if (node == null) {
                return ""; // Return empty string if the node is not found
            }
        }

        if (node.isTextual()) {
            return node.asText(); // Convert the node to a string
        } else {
            System.err.println("Warning: Expected a string but found something else.");
            return ""; // Return empty string as a fallback
        }
    }

    public Boolean getBoolean(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            node = node.get(subGroup);
            if (node == null) {
                return false; // Return false if the node is not found
            }
        }

        if (node.isBoolean()) {
            return node.asBoolean(); // Convert the node to a boolean
        } else {
            System.err.println("Warning: Expected a boolean but found something else.");
            return false; // Return false as a fallback
        }
    }

    public int getInt(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            node = node.get(subGroup);
            if (node == null) {
                return 0;
            }
        }

        if (node.isInt()) {
            return node.asInt(); // Convert the node to an integer
        } else {
            System.err.println("Warning: Expected an integer but found something else.");
            return 0; // Return 0 as a fallback
        }
    }

    public int[] getInts(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            if (node == null) { // Check if node is null before accessing
                return new int[0];
            }
            node = node.get(subGroup);
            if (subGroup.equals("RANDOM")) {
                return new int[0];
            }
        }
        List<Integer> keys = new ArrayList<>();
        if (node == null) { // Also check after the loop
            return new int[0];
        }
        if (node.isObject()) {
            node.fieldNames().forEachRemaining(fieldName -> {
                keys.add(Integer.valueOf(fieldName));
            }); // Add all field names (parsed as integers) to the list
        }
        else {
            for (JsonNode element : node) {
                keys.add(element.asInt());
            }
        }
        return keys.stream().mapToInt(Integer::intValue).toArray(); // Convert List<Integer> to int[]
    }

    public String getTranslation(String key) {
        return keyToTranslation.getOrDefault(key, key);
    }

    public String getOriginal(String translation) {
        return translationToKey.getOrDefault(translation, translation);
    }

    public String[] getTranslations(String key) {
        String[] keys = getGroup(new String[] {key});
        String[] translations = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            translations[i] = getTranslation(keys[i]);
        }
        return translations;
    }

    public String[] getGroupTranslations(String[] key) {
        String[] keys = getGroup(key);
        String[] translations = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            translations[i] = getTranslation(keys[i]);
        }
        return translations;
    }
}