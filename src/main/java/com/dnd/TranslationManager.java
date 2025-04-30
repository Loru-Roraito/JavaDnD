package com.dnd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TranslationManager {
    private static TranslationManager instance;
    private JsonNode rootNode;
    private final Properties languageProperties = new Properties();
    public static String language = "it";

    private TranslationManager(String language) {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("translations_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: translations_" + language + ".properties");
            }
            languageProperties.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
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
            node = node.get(subGroup);
            if (node == null) {
                System.err.println("Warning: subGroup not found: " + subGroup);
                return new String[0];
            }
        }
        List<String> keys = new ArrayList<>();
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

    public int getInt(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            node = node.get(subGroup);
            if (node == null) {
                System.err.println("Warning: subGroup not found: " + subGroup);
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

    public String getTranslation(String key) {
        String translation = languageProperties.getProperty(key);
        if (translation == null) {
            System.err.println("Warning: Missing translation for key: " + key);
            return key; // Return the key as a fallback
        }
        return translation;
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