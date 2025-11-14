package com.dnd.items;

import com.dnd.characters.ItemManager;
import com.dnd.utils.ObservableString;

public class Item implements MyItems<Item> {
    private final ObservableString name;
    private final String nominative;
    // private final int cost;
    // private final int weight;
    // private final String currency;
    // private final String alias; // Used if a slightly different weapon still needs to be cathegorised as another (like a fire sword being considered a sword)
    private final String type;
    private final String[] tags;
    
    // Weapons attributes
    private final int hits;
    private final int damage;
    private final int versatileHits;
    private final int versatileDamage;
    private final int shortRange;
    private final int longRange;
    // private final String mastery;
    private final String ammo;
    private final String[] attributes;
    private final String[] properties;

    // Armors attributes
    private final int armorClass;
    private final int dexterity;
    private final int strength;
    private final Boolean stealth;

    // Kits attributes
    // private final int uses;

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String value) {
        name.set(value);
    }

    @Override
    public Item copy() {
        return new Item(nominative);
    }

    @Override
    public ObservableString getNameProperty() {
        return name;
    }

    public Item(String nominative) {
        this.nominative = nominative;
        name = new ObservableString(nominative);

        type = getItemString(new String[] {nominative, "type"});
        // alias = getItemString(new String[] {nominative, "alias"});
        // cost = getItemInt(new String[] {nominative, "cost"});
        // currency = getItemString(new String[] {nominative, "currency"});
        // weight = getItemInt(new String[] {nominative, "weight"});

        tags = getItemGroup(new String[] {nominative, "tags"});

        hits = getItemInt(new String[] {nominative, "hits"});
        damage = getItemInt(new String[] {nominative, "damage"});
        versatileHits = getItemInt(new String[] {nominative, "versatileHits"});
        versatileDamage = getItemInt(new String[] {nominative, "versatileDamage"});
        shortRange = getItemInt(new String[] {nominative, "shortRange"});
        longRange = getItemInt(new String[] {nominative, "longRange"});
        // mastery = getItemString(new String[] {nominative, "mastery"});
        ammo = getItemString(new String[] {nominative, "ammo"});

        attributes = getItemGroup(new String[] {nominative, "attributes"});
        properties = getItemGroup(new String[] {nominative, "properties"});

        armorClass = getItemInt(new String[] {nominative, "armorClass"});
        dexterity = getItemInt(new String[] {nominative, "dexterity"});
        strength = getItemInt(new String[] {nominative, "strength"});
        stealth = getItemBoolean(new String[] {nominative, "stealth"});

        // uses = getItemInt(new String[] {nominative, "uses"});
    }

    public String getNominative() {
        return nominative;
    }

    public String getType() {
        return type;
    }

    public String getAmmo() {
        return ammo;
    }

    public String[] getProperties() {
        return properties;
    }

    public String[] getTags() {
        return tags;
    }

    public int getArmorClass() {
        return armorClass;
    }
    
    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public int getHits() {
        return hits;
    }

    public int getDamage() {
        return damage;
    }

    public int getVersatileHits() {
        return versatileHits;
    }

    public int getVersatileDamage() {
        return versatileDamage;
    }

    public Boolean getStealth() {
        return stealth;
    }

    public Boolean equals(Item other) {
        return nominative.toLowerCase().equals(other.nominative.toLowerCase());
    }

    private String getItemString(String[] group) {
        return ItemManager.getInstance().getItemString(group);
    }

    private String[] getItemGroup(String[] group) {
        return ItemManager.getInstance().getItemGroup(group);
    }

    private int getItemInt(String[] group) {
        return ItemManager.getInstance().getItemInt(group);
    }

    private Boolean getItemBoolean(String[] group) {
        return ItemManager.getInstance().getItemBoolean(group);
    }
}