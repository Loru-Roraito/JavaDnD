package com.dnd.items;

import com.dnd.TranslationManager;

public class Item {
    private final int cost;
    private final int weight;
    private final String currency;
    private final String name;
    private final String alias; // Used if a slightly different weapon still needs to be cathegorised as another (like a fire sword being considered a sword)
    private final String type;
    private final String[] tags;
    
    // Weapons attributes
    private final int hits;
    private final int damage;
    private final int versatileHits;
    private final int versatileDamage;
    private final int shortRange;
    private final int longRange;
    private final String mastery;
    private final String ammo;
    private final String[] attributes;
    private final String[] properties;

    // Armors attributes
    private final int armorClass;
    private final int dexterity;
    private final int strength;
    private final Boolean stealth;

    // Kits attributes
    private final int uses;

    public Item(String nominative) {
        name = nominative;
        
        alias = getString(new String[] {"items", name, "alias"});
        type = getString(new String[] {"items", name, "type"});
        cost = getInt(new String[] {"items", name, "cost"});
        currency = getString(new String[] {"items", name, "currency"});
        weight = getInt(new String[] {"items", name, "weight"});

        tags = getGroup(new String[] {"items", name, "tags"});

        hits = getInt(new String[] {"items", name, "hits"});
        damage = getInt(new String[] {"items", name, "damage"});
        versatileHits = getInt(new String[] {"items", name, "versatileHits"});
        versatileDamage = getInt(new String[] {"items", name, "versatileDamage"});
        shortRange = getInt(new String[] {"items", name, "shortRange"});
        longRange = getInt(new String[] {"items", name, "longRange"});
        mastery = getString(new String[] {"items", name, "mastery"});
        ammo = getString(new String[] {"items", name, "ammo"});

        attributes = getGroup(new String[] {"items", name, "attributes"});
        properties = getGroup(new String[] {"items", name, "properties"});

        armorClass = getInt(new String[] {"items", name, "armorClass"});
        dexterity = getInt(new String[] {"items", name, "dexterity"});
        strength = getInt(new String[] {"items", name, "strength"});
        stealth = getBoolean(new String[] {"items", name, "stealth"});

        uses = getInt(new String[] {"items", name, "uses"});
    }

    private String getString(String[] group) {
        return TranslationManager.getInstance().getString(group);
    }

    private String[] getGroup(String[] group) {
        return TranslationManager.getInstance().getGroup(group);
    }

    private int getInt(String[] group) {
        return TranslationManager.getInstance().getInt(group);
    }

    private Boolean getBoolean(String[] group) {
        return TranslationManager.getInstance().getBoolean(group);
    }
}