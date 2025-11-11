package com.dnd.characters;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpellManager {
    private static SpellManager instance;
    private JsonNode rootNode;

    private SpellManager() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("spells.json")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: spells.json");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            System.err.println("Error: Failed to load spells.json");
        }
    }

    public static SpellManager getInstance() {
        if (instance == null) {
            instance = new SpellManager();
        }
        return instance;
    }

    public String[] getTotalSpells() {
        List<String> keys = new ArrayList<>();
        rootNode.fieldNames().forEachRemaining(spellName -> {
            keys.add(spellName);
        });
        return keys.toArray(String[]::new);
    }

    public String[] getSpells() {
        List<String> keys = new ArrayList<>();
        rootNode.fieldNames().forEachRemaining(spellName -> {
            JsonNode spellNode = rootNode.get(spellName);
            if (spellNode != null && spellNode.has("level")) {
                int level = spellNode.get("level").asInt();
                if (level > 0) { // Only include spells with level > 0
                    keys.add(spellName);
                }
            }
        });
        return keys.toArray(String[]::new);
    }

    public String[] getCantrips() {
        List<String> keys = new ArrayList<>();
        rootNode.fieldNames().forEachRemaining(spellName -> {
            JsonNode spellNode = rootNode.get(spellName);
            if (spellNode != null && spellNode.has("level")) {
                int level = spellNode.get("level").asInt();
                if (level == 0) { // Only include cantrips (level 0)
                    keys.add(spellName);
                }
            }
        });
        return keys.toArray(String[]::new);
    }

    public String[] getSpellGroup(String[] group) {
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

    public String getSpellString(String[] group) {
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

    public Boolean getSpellBoolean(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            node = node.get(subGroup);
            if (node == null) {
                return false; // Return false if the node is not found
            }
        }

        if (node.isBoolean()) {
            return node.asBoolean(); // Convert the node to a Boolean
        } else {
            System.err.println("Warning: Expected a Boolean but found something else.");
            return false; // Return false as a fallback
        }
    }

    public Boolean[] getSpellBooleans(String[] group) {
        JsonNode node = rootNode;
        for (String subGroup : group) {
            if (node == null) { // Check if node is null before accessing
                return new Boolean[0];
            }
            node = node.get(subGroup);
            if (subGroup.equals("RANDOM")) {
                return new Boolean[0];
            }
        }
        List<Boolean> keys = new ArrayList<>();
        if (node == null) { // Also check after the loop
            return new Boolean[0];
        }
        if (node.isObject()) {
            node.fieldNames().forEachRemaining(fieldName -> {
                keys.add(Boolean.valueOf(fieldName));
            }); // Add all field names (parsed as Booleans) to the list
        }
        else {
            for (JsonNode element : node) {
                keys.add(element.asBoolean());
            }
        }
        return keys.toArray(Boolean[]::new); // Convert List<Boolean> to Boolean[]
    }

    public int getSpellInt(String[] group) {
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
}