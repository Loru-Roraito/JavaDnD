package com.dnd.backend;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class Manager {
    protected JsonObject rootNode;

    protected abstract String getJsonFileName();

    public void initialize() {
        String fileName = getJsonFileName();
        try (var inputStream = Manager.class.getClassLoader().getResourceAsStream(fileName);
             var reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            rootNode = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            System.err.println("Error: Failed to load " + fileName);
        }
    }

    public String[] getStrings(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || subGroup == null) { // Check if node is null before accessing
                return new String[0];
            }
            node = node.getAsJsonObject().get(subGroup);
            if (subGroup.equals("RANDOM")) {
                return new String[0];
            }
        }
        List<String> keys = new ArrayList<>();
        if (node == null) { // Also check after the loop
            return new String[0];
        }
        
        if (node.isJsonObject()) {
            // Add all field names (keys) to the list
            JsonObject jsonObject = node.getAsJsonObject();
            for (String key : jsonObject.keySet()) {
                keys.add(key);
            }
        } else if (node.isJsonArray()) {
            JsonArray jsonArray = node.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                keys.add(element.getAsString());
            }
        }
        return keys.toArray(String[]::new);
    }

    public String getString(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) {
                return ""; // Return empty string if the node is not found
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null) {
                return ""; // Return empty string if the node is not found
            }
        }

        if (node.isJsonPrimitive() && node.getAsJsonPrimitive().isString()) {
            return node.getAsString();
        } else {
            System.err.println("Warning: Expected a string but found something else.");
            return "";
        }
    }

    public Boolean getBoolean(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) {
                return false; // Return false if the node is not found
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null) {
                return false; // Return false if the node is not found
            }
        }

        if (node.isJsonPrimitive() && node.getAsJsonPrimitive().isBoolean()) {
            return node.getAsBoolean();
        } else {
            System.err.println("Warning: Expected a Boolean but found something else.");
            return false;
        }
    }

    public Boolean[] getBooleans(String[] group) {
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
        } else if (node.isJsonArray()) {
            JsonArray jsonArray = node.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                keys.add(element.getAsBoolean());
            }
        }
        return keys.toArray(Boolean[]::new); // Convert List<Boolean> to Boolean[]
    }

    public int getInt(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            node = node.getAsJsonObject().get(subGroup);

            if (node == null) {
                return 0;
            }
        }

        if (node.isJsonPrimitive() && node.getAsJsonPrimitive().isNumber()) {
            return node.getAsInt();
        } else {
            System.err.println("Warning: Expected an integer but found something else.");
            return 0;
        }
    }

    public int[] getInts(String[] group) {
        JsonElement node = rootNode;
        for (String subGroup : group) {
            if (node == null || !node.isJsonObject()) { // Check if node is null before accessing
                return new int[0];
            }
            node = node.getAsJsonObject().get(subGroup);
            if (node == null || subGroup.equals("RANDOM")) {
                return new int[0];
            }
        }
        List<Integer> keys = new ArrayList<>();
        if (node == null) { // Also check after the loop
            return new int[0];
        }
        
        if (node.isJsonObject()) {
            JsonObject jsonObject = node.getAsJsonObject();
            for (String fieldName : jsonObject.keySet()) {
                keys.add(Integer.valueOf(fieldName));
            }
        } else if (node.isJsonArray()) {
            JsonArray jsonArray = node.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                keys.add(element.getAsInt());
            }
        }
        return keys.stream().mapToInt(Integer::intValue).toArray(); // Convert List<Integer> to int[]
    }
}