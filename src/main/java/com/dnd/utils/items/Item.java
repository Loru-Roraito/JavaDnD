package com.dnd.utils.items;

import com.dnd.backend.ItemManager;
import com.dnd.utils.observables.ObservableString;

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

        type = getString(new String[] {nominative, "type"});
        // alias = getString(new String[] {nominative, "alias"});
        // cost = getInt(new String[] {nominative, "cost"});
        // currency = getString(new String[] {nominative, "currency"});
        // weight = getInt(new String[] {nominative, "weight"});

        tags = getStrings(new String[] {nominative, "tags"});

        hits = getInt(new String[] {nominative, "hits"});
        damage = getInt(new String[] {nominative, "damage"});
        versatileHits = getInt(new String[] {nominative, "versatileHits"});
        versatileDamage = getInt(new String[] {nominative, "versatileDamage"});
        shortRange = getInt(new String[] {nominative, "shortRange"});
        longRange = getInt(new String[] {nominative, "longRange"});
        // mastery = getString(new String[] {nominative, "mastery"});
        ammo = getString(new String[] {nominative, "ammo"});

        attributes = getStrings(new String[] {nominative, "attributes"});
        properties = getStrings(new String[] {nominative, "properties"});

        armorClass = getInt(new String[] {nominative, "armorClass"});
        dexterity = getInt(new String[] {nominative, "dexterity"});
        strength = getInt(new String[] {nominative, "strength"});
        stealth = getBoolean(new String[] {nominative, "stealth"});

        // uses = getInt(new String[] {nominative, "uses"});
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

    public String[] getAttributes() {
        return attributes;
    }

    public int getShortRange() {
        return shortRange;
    }

    public int getLongRange() {
        return longRange;
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

    private String getString(String[] group) {
        return ItemManager.getInstance().getString(group);
    }

    private String[] getStrings(String[] group) {
        return ItemManager.getInstance().getStrings(group);
    }

    private int getInt(String[] group) {
        return ItemManager.getInstance().getInt(group);
    }

    private Boolean getBoolean(String[] group) {
        return ItemManager.getInstance().getBoolean(group);
    }
}