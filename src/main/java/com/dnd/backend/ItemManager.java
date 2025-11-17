package com.dnd.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnd.utils.items.Item;

public class ItemManager extends Manager{
    private static final ItemManager instance = new ItemManager();
    private final List<String> sets = Arrays.asList(getStrings(new String[] {"sets"})); // choose one element
    private final List<String> packages = Arrays.asList(getStrings(new String[] {"packages"})); // take all elements
    
    private ItemManager() {
        initialize();
    }

    public static ItemManager getInstance() {
        return instance;
    }

    @Override
    protected String getJsonFileName() {
        return "items.json";
    }

    public String[] getAllItems() {
        List<String> keys = new ArrayList<>();
        for (String itemName : rootNode.keySet()) {
            keys.add(itemName);
        }
        return keys.toArray(String[]::new);
    }

    public void addItem(GameCharacter character, String itemName) {
        if (!itemName.equals("") && !sets.contains(itemName)) {
            if (packages.contains(itemName)) {
                for (String item : getStrings(new String[] {"packages", itemName})) {
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
}
