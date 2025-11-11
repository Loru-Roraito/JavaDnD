package com.dnd.characters;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.TranslationManager;
import com.dnd.items.Item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemManager {
    private static ItemManager instance;
    private JsonNode rootNode;
    private List<String> sets = Arrays.asList(getGroup(new String[] {"sets"})); // choose one element
    private List<String> packages = Arrays.asList(getGroup(new String[] {"packages"})); // take all elements

    private ItemManager() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("items.json")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: items.json");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            System.err.println("Error: Failed to load items.json");
        }
    }

    public void addItem(GameCharacter character, String itemName) {
        if (!itemName.equals("") && !sets.contains(itemName)) {
            if (packages.contains(itemName)) {
                for (String item : getGroup(new String[] {"packages", itemName})) {
                    addItem(character, item);
                }
                return;
            } else if (itemName.endsWith("COPPER")) {
                if (itemName.split(" ")[0].matches("\\d+")) {
                    int amount = Integer.parseInt(itemName.replace("COPPER", "").replace(" ", ""));
                    character.getMoney(0).set(character.getMoney(0).get() + amount);
                    return;
                } else {
                    character.getMoney(0).set(character.getMoney(0).get() + 1);
                    return;
                }
            } else if (itemName.endsWith("SILVER")) {
                if (itemName.split(" ")[0].matches("\\d+")) {
                    int amount = Integer.parseInt(itemName.replace("SILVER", "").replace(" ", ""));
                    character.getMoney(1).set(character.getMoney(1).get() + amount);
                    return;
                } else {
                    character.getMoney(1).set(character.getMoney(1).get() + 1);
                    return;
                }
            } else if (itemName.endsWith("ELECTRUM")) {
                if (itemName.split(" ")[0].matches("\\d+")) {
                    int amount = Integer.parseInt(itemName.replace("ELECTRUM", "").replace(" ", ""));
                    character.getMoney(2).set(character.getMoney(2).get() + amount);
                    return;
                } else {
                    character.getMoney(2).set(character.getMoney(2).get() + 1);
                    return;
                }
            } else if (itemName.endsWith("GOLD")) {
                if (itemName.split(" ")[0].matches("\\d+")) {
                    int amount = Integer.parseInt(itemName.replace("GOLD", "").replace(" ", ""));
                    character.getMoney(3).set(character.getMoney(3).get() + amount);
                    return;
                } else {
                    character.getMoney(3).set(character.getMoney(3).get() + 1);
                    return;
                }
            } else if (itemName.endsWith("PLATINUM")) {
                if (itemName.split(" ")[0].matches("\\d+")) {
                    int amount = Integer.parseInt(itemName.replace("PLATINUM", "").replace(" ", ""));
                    character.getMoney(4).set(character.getMoney(4).get() + amount);
                    return;
                } else {
                    character.getMoney(4).set(character.getMoney(4).get() + 1);
                    return;
                }
            } else if (itemName.split(" ")[0].matches("\\d+")) {
                int quantity = Integer.parseInt(itemName.split(" ")[0]);
                String singleItem = itemName.substring(itemName.indexOf(" ") + 1);
                for (int i = 0; i < quantity; i++) {
                    addItem(character, singleItem);
                }
                return;
            }
            
            Item item = new Item(itemName);
            character.getItems().add(item);
        }
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    public String[] getTranslatedItems() {
        List<String> keys = new ArrayList<>();
        rootNode.fieldNames().forEachRemaining(itemName -> {
            if (!itemName.equals("UNARMED_STRIKE") && !itemName.equals("IMPROVISED_WEAPON")) {
                keys.add(getTranslation(itemName));
            }
        });
        return keys.toArray(String[]::new);
    }

    public String[] getItemGroup(String[] group) {
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

    public String getItemString(String[] group) {
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

    public Boolean getItemBoolean(String[] group) {
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

    public int getItemInt(String[] group) {
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

    private String[] getGroup(String[] group) {
        return TranslationManager.getInstance().getGroup(group);
    }

    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }
}
