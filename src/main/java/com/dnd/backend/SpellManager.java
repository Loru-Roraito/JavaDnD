package com.dnd.backend;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SpellManager extends Manager {
    private static final SpellManager instance = new SpellManager();
    
    private SpellManager() {
        initialize();
    }

    public static SpellManager getInstance() {
        return instance;
    }

    @Override
    protected String getJsonFileName() {
        return "spells.json";
    }

    public String[] getAllSpells() {
        List<String> keys = new ArrayList<>();
        for (String spellName : rootNode.keySet()) {
            keys.add(spellName);
        }
        return keys.toArray(String[]::new);
    }

    public String[] getSpells() {
        List<String> keys = new ArrayList<>();
        for (String spellName : rootNode.keySet()) {
            if (rootNode.has(spellName)) {
                JsonObject spellNode = rootNode.getAsJsonObject(spellName);
                if (spellNode != null && spellNode.has("level")) {
                    int level = spellNode.get("level").getAsInt();
                    if (level > 0) { // Only include spells with level > 0
                        keys.add(spellName);
                    }
                }
            }
        }
        return keys.toArray(String[]::new);
    }

    public String[] getCantrips() {
        List<String> keys = new ArrayList<>();
        for (String spellName : rootNode.keySet()) {
            if (rootNode.has(spellName)) {
                JsonObject spellNode = rootNode.getAsJsonObject(spellName);
                if (spellNode != null && spellNode.has("level")) {
                    int level = spellNode.get("level").getAsInt();
                    if (level == 0) { // Only include cantrips (level 0)
                        keys.add(spellName);
                    }
                }
            }
        }
        return keys.toArray(String[]::new);
    }

    public String[] getSpellGroup(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) { // Check if node is null before accessing
                return new String[0];
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null || subGroup.equals("RANDOM")) {
                return new String[0];
            }
        }
        List<String> keys = new ArrayList<>();
        if (node == null) { // Also check after the loop
            return new String[0];
        }
        if (node.isJsonObject()) {
            JsonObject jsonObject = node.getAsJsonObject();
            for (String key : jsonObject.keySet()) {
                keys.add(key);
            }
        }
        else if (node.isJsonArray()) {
            JsonArray jsonArray = node.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                keys.add(element.getAsString());
            }
        }
        return keys.toArray(String[]::new); // Use method reference to create the array
    }

    public String getSpellString(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) {
                return ""; // Return empty string if the node is not found
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null) {
                return "";
            }
        }

        if (node.isJsonPrimitive() && node.getAsJsonPrimitive().isString()) {
            return node.getAsString(); // Convert the node to a string
        } else {
            System.err.println("Warning: Expected a string but found something else.");
            return ""; // Return empty string as a fallback
        }
    }

    public Boolean getSpellBoolean(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) {
                return false; // Return false if the node is not found
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null) {
                return false;
            }
        }

        if (node.isJsonPrimitive() && node.getAsJsonPrimitive().isBoolean()) {
            return node.getAsBoolean(); // Convert the node to a Boolean
        } else {
            System.err.println("Warning: Expected a Boolean but found something else.");
            return false; // Return false as a fallback
        }
    }

    public Boolean[] getSpellBooleans(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) { // Check if node is null before accessing
                return new Boolean[0];
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null || subGroup.equals("RANDOM")) {
                return new Boolean[0];
            }
        }
        List<Boolean> keys = new ArrayList<>();
        if (node == null) { // Also check after the loop
            return new Boolean[0];
        }
        if (node.isJsonObject()) {
            JsonObject jsonObject = node.getAsJsonObject();
            for (String fieldName : jsonObject.keySet()) {
                keys.add(Boolean.valueOf(fieldName));
            }
        }
        else if (node.isJsonArray()) {
            JsonArray jsonArray = node.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                keys.add(element.getAsBoolean());
            }
        }
        return keys.toArray(Boolean[]::new); // Convert List<Boolean> to Boolean[]
    }

    public int getSpellInt(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) {
                return 0;
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null) {
                return 0;
            }
        }

        if (node.isJsonPrimitive() && node.getAsJsonPrimitive().isNumber()) {
            return node.getAsInt(); // Convert the node to an integer
        } else {
            System.err.println("Warning: Expected an integer but found something else.");
            return 0; // Return 0 as a fallback
        }
    }
}