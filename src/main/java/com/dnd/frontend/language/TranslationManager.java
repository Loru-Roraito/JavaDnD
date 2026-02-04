package com.dnd.frontend.language;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TranslationManager {
    private static final Properties languageProperties = new Properties();
    private static final Map<String, String> keyToTranslation = new HashMap<>();
    private static final Map<String, String> translationToKey = new HashMap<>();

    public static void initialize(String language) {
        try (var inputStream = TranslationManager.class.getResourceAsStream("/translations_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: translations_" + language + ".properties");
            }
            languageProperties.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));

            // Try to load and merge custom version if it exists
            String customFileName = "/custom/translations_" + language + ".properties";
            var customStream = TranslationManager.class.getResourceAsStream(customFileName);
            if (customStream != null) {
                try (customStream;
                    var customReader = new java.io.InputStreamReader(customStream, StandardCharsets.UTF_8)) {
                    Properties customProperties = new Properties();
                    customProperties.load(customReader);
                    // Merge: custom properties overwrite base properties
                    languageProperties.putAll(customProperties);
                } catch (IOException e) {
                    System.err.println("Error: Failed to load custom translations from " + customFileName);
                }
            }

            // Populate the maps
            for (String key : languageProperties.stringPropertyNames()) {
                String value = languageProperties.getProperty(key);
                keyToTranslation.put(key, value);
                translationToKey.put(value, key);
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load translations file: translations_" + language + ".properties");
        }
    }

    public static String getTranslation(String key) {
        return keyToTranslation.getOrDefault(key, key);
    }

    public static String getOriginal(String translation) {
        return translationToKey.getOrDefault(translation, translation);
    }

    public static String[] getTranslations(String[] keys) {
        String[] translations = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            translations[i] = getTranslation(keys[i]);
        }
        return translations;
    }
}