package com.dnd.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// TODO: Done with AI, need to redo by hand

public final class CustomItemWriter {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String upsertItem(
        String itemName,
        int weight,
        int cost,
        String costUnit,
        String description
    ) throws IOException {
        ItemBuilder builder = new ItemBuilder(itemName)
            .setWeight(weight)
            .setCost(cost)
            .setCurrency(costUnit)
            .setDescription(description)
            .setType("ITEM");
        return upsertItemFromBuilder(builder);
    }

    public static String upsertWeapon(
        String itemName,
        int weight,
        int cost,
        String costUnit,
        String description,
        int hits,
        int damage,
        Integer versatileHits,
        Integer versatileDamage,
        String[] attributes,
        String[] properties,
        String[] tags,
        String mastery,
        Integer shortRange,
        Integer longRange,
        String ammo
    ) throws IOException {
        ItemBuilder builder = new ItemBuilder(itemName)
            .setWeight(weight)
            .setCost(cost)
            .setCurrency(costUnit)
            .setDescription(description)
            .setType("WEAPON")
            .setHits(hits)
            .setDamage(damage)
            .setVersatileHits(versatileHits == null ? 0 : versatileHits)
            .setVersatileDamage(versatileDamage == null ? 0 : versatileDamage)
            .setAttributes(attributes)
            .setProperties(properties)
            .setTags(tags)
            .setMastery(mastery);
        if (shortRange != null) builder.setShortRange(shortRange);
        if (longRange != null) builder.setLongRange(longRange);
        if (ammo != null) builder.setAmmo(ammo);
        return upsertItemFromBuilder(builder);
    }

    public static String upsertArmor(
        String itemName,
        int weight,
        int cost,
        String costUnit,
        String description,
        int armorClass,
        int dexterity,
        int strength,
        boolean stealth,
        String[] tags
    ) throws IOException {
        ItemBuilder builder = new ItemBuilder(itemName)
            .setWeight(weight)
            .setCost(cost)
            .setCurrency(costUnit)
            .setDescription(description)
            .setType("ARMOR")
            .setArmorClass(armorClass)
            .setDexterity(dexterity)
            .setStrength(strength)
            .setStealth(stealth)
            .setTags(tags);
        return upsertItemFromBuilder(builder);
    }

    private static String upsertItemFromBuilder(ItemBuilder builder) throws IOException {
        String key = toItemKey(builder.getName());
        String cleanName = builder.getName().trim();
        String cleanDescription = builder.getDescription() == null ? "" : builder.getDescription().trim();
        String normalizedCostUnit = normalizeUnit(builder.getCurrency());

        for (Path customDir : getCustomDirectories()) {
            Files.createDirectories(customDir);

            upsertItemJsonFromBuilder(customDir.resolve("items.json"), key, builder, normalizedCostUnit);
            for (String language : getStrings(new String[] {"languages"})) {
                String lan = getString(new String[] {"languages", language});
                upsertProperty(customDir.resolve("translations_" + lan + ".properties"), key, cleanName);
                upsertProperty(customDir.resolve("itemDescriptions_" + lan + ".properties"), key, cleanDescription);
            }
        }

        return key;
    }

    public static List<String> getCustomItemKeys() throws IOException {
        Path customDir = getPrimaryCustomDirectory();
        Path jsonPath = customDir.resolve("items.json");
        if (!Files.exists(jsonPath)) {
            return new ArrayList<>();
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        }

        List<String> keys = new ArrayList<>();
        for (String key : root.keySet()) {
            keys.add(key);
        }
        Collections.sort(keys);
        return keys;
    }

    public static CustomItemData getCustomItem(String key) throws IOException {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Item key is required.");
        }

        String cleanKey = key.trim();
        Path customDir = getPrimaryCustomDirectory();
        Path jsonPath = customDir.resolve("items.json");
        if (!Files.exists(jsonPath)) {
            return null;
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        }

        if (!root.has(cleanKey)) {
            return null;
        }

        JsonObject item = root.getAsJsonObject(cleanKey);
        int weight = item.has("weight") ? item.get("weight").getAsInt() : 0;
        int cost = item.has("cost") ? item.get("cost").getAsInt() : 0;
        String costUnit = item.has("currency") ? item.get("currency").getAsString() : "";
        String type = item.has("type") ? item.get("type").getAsString() : "ITEM";

        String name = getPropertyValue(customDir.resolve("translations_en.properties"), cleanKey, cleanKey);
        String description = getPropertyValue(customDir.resolve("itemDescriptions_en.properties"), cleanKey, "");

        int hits = item.has("hits") ? item.get("hits").getAsInt() : 0;
        int damage = item.has("damage") ? item.get("damage").getAsInt() : 0;
        int versatileHits = item.has("versatileHits") ? item.get("versatileHits").getAsInt() : 0;
        int versatileDamage = item.has("versatileDamage") ? item.get("versatileDamage").getAsInt() : 0;
        int shortRange = item.has("shortRange") ? item.get("shortRange").getAsInt() : 0;
        int longRange = item.has("longRange") ? item.get("longRange").getAsInt() : 0;
        String mastery = item.has("mastery") ? item.get("mastery").getAsString() : "";
        String ammo = item.has("ammo") ? item.get("ammo").getAsString() : "";

        String[] attributes = item.has("attributes") ? jsonArrayToStrings(item.getAsJsonArray("attributes")) : new String[0];
        String[] properties = item.has("properties") ? jsonArrayToStrings(item.getAsJsonArray("properties")) : new String[0];
        String[] tags = item.has("tags") ? jsonArrayToStrings(item.getAsJsonArray("tags")) : new String[0];

        int armorClass = item.has("armorClass") ? item.get("armorClass").getAsInt() : 0;
        int dexterity = item.has("dexterity") ? item.get("dexterity").getAsInt() : 0;
        int strength = item.has("strength") ? item.get("strength").getAsInt() : 0;
        boolean stealth = item.has("stealth") && item.get("stealth").getAsBoolean();

        return new CustomItemData(
            cleanKey,
            name,
            weight,
            cost,
            costUnit,
            description,
            type,
            hits,
            damage,
            versatileHits,
            versatileDamage,
            attributes,
            properties,
            tags,
            mastery,
            shortRange,
            longRange,
            ammo,
            armorClass,
            dexterity,
            strength,
            stealth
        );
    }

    public static void deleteItem(String key) throws IOException {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Item key is required.");
        }

        String cleanKey = key.trim();

        for (Path customDir : getCustomDirectories()) {
            Files.createDirectories(customDir);
            removeItemFromJson(customDir.resolve("items.json"), cleanKey);
            for (String language : getStrings(new String[] {"languages"})) {
                String lan = getString(new String[] {"languages", language});
                removeProperty(customDir.resolve("translations_" + lan + ".properties"), cleanKey);
                removeProperty(customDir.resolve("itemDescriptions_" + lan + ".properties"), cleanKey);
            }
        }
    }

    private static String[] getStrings (String[] key) {
        return GroupManager.getInstance().getStrings(key);
    }

    private static String getString (String[] key) {
        return GroupManager.getInstance().getString(key);
    }

    private static List<Path> getCustomDirectories() {
        Path baseDir = Path.of(System.getProperty("user.dir"));
        List<Path> directories = new ArrayList<>();
        directories.add(baseDir.resolve("src").resolve("main").resolve("resources").resolve("custom"));

        Path targetClassesCustom = baseDir.resolve("target").resolve("classes").resolve("custom");
        if (Files.exists(targetClassesCustom.getParent())) {
            directories.add(targetClassesCustom);
        }
        return directories;
    }

    private static Path getPrimaryCustomDirectory() throws IOException {
        Path directory = getCustomDirectories().get(0);
        Files.createDirectories(directory);
        return directory;
    }

    private static void upsertItemJsonFromBuilder(
        Path jsonPath,
        String key,
        ItemBuilder builder,
        String normalizedCostUnit
    ) throws IOException {
        JsonObject root;
        if (Files.exists(jsonPath)) {
            try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {
                root = JsonParser.parseReader(reader).getAsJsonObject();
            }
        } else {
            root = new JsonObject();
        }

        JsonObject item = new JsonObject();
        item.addProperty("type", builder.getType());
        item.addProperty("cost", builder.getCost());
        item.addProperty("currency", normalizedCostUnit);
        item.addProperty("weight", builder.getWeight());

        switch (builder.getType()) {
            case "WEAPON" -> {
                item.addProperty("hits", builder.getHits());
                item.addProperty("damage", builder.getDamage());
                if (builder.getVersatileDamage() > 0) {
                    item.addProperty("versatileHits", builder.getVersatileHits());
                    item.addProperty("versatileDamage", builder.getVersatileDamage());
                }
                addStringArray(item, "attributes", builder.getAttributes());
                addStringArray(item, "properties", builder.getProperties());
                addStringArray(item, "tags", builder.getTags());
                item.addProperty("mastery", builder.getMastery() == null ? "" : builder.getMastery());
                if (builder.getShortRange() != null) {
                    item.addProperty("shortRange", builder.getShortRange());
                }
                if (builder.getLongRange() != null) {
                    item.addProperty("longRange", builder.getLongRange());
                }
                if (builder.getAmmo() != null) {
                    item.addProperty("ammo", builder.getAmmo());
                }
            }
            case "ARMOR" -> {
                item.addProperty("armorClass", builder.getArmorClass());
                item.addProperty("dexterity", builder.getDexterity());
                item.addProperty("strength", builder.getStrength());
                item.addProperty("stealth", builder.isStealth());
                addStringArray(item, "tags", builder.getTags());
            }
            default -> {
                item.add("tags", new JsonArray());
                item.add("properties", new JsonArray());
            }
        }

        root.add(key, item);

        try (Writer writer = Files.newBufferedWriter(jsonPath, StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        }
    }

    private static void addStringArray(JsonObject obj, String key, String[] values) {
        JsonArray array = new JsonArray();
        if (values != null) {
            for (String val : values) {
                if (val != null && !val.isEmpty()) {
                    array.add(val);
                }
            }
        }
        obj.add(key, array);
    }

    private static String[] jsonArrayToStrings(JsonArray array) {
        if (array == null) {
            return new String[0];
        }

        List<String> values = new ArrayList<>();
        array.forEach(element -> values.add(element.getAsString()));
        return values.toArray(String[]::new);
    }

    private static void upsertProperty(Path propertiesPath, String key, String value) throws IOException {
        Properties properties = new Properties();

        if (Files.exists(propertiesPath)) {
            try (InputStream input = Files.newInputStream(propertiesPath);
                Reader reader = new java.io.InputStreamReader(input, StandardCharsets.UTF_8)) {
                properties.load(reader);
            }
        }

        properties.setProperty(key, value);

        try (OutputStream output = Files.newOutputStream(propertiesPath);
            Writer writer = new java.io.OutputStreamWriter(output, StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        }
    }

    private static void removeItemFromJson(Path jsonPath, String key) throws IOException {
        if (!Files.exists(jsonPath)) {
            return;
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        }

        if (!root.has(key)) {
            return;
        }

        root.remove(key);
        try (Writer writer = Files.newBufferedWriter(jsonPath, StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        }
    }

    private static void removeProperty(Path propertiesPath, String key) throws IOException {
        if (!Files.exists(propertiesPath)) {
            return;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(propertiesPath);
            Reader reader = new java.io.InputStreamReader(input, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }

        Object previous = properties.remove(key);
        if (previous == null) {
            return;
        }

        try (OutputStream output = Files.newOutputStream(propertiesPath);
            Writer writer = new java.io.OutputStreamWriter(output, StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        }
    }

    private static String getPropertyValue(Path propertiesPath, String key, String defaultValue) throws IOException {
        if (!Files.exists(propertiesPath)) {
            return defaultValue;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(propertiesPath);
            Reader reader = new java.io.InputStreamReader(input, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        return properties.getProperty(key, defaultValue);
    }

    private static String toItemKey(String itemName) {
        String normalized = itemName.trim().toUpperCase()
            .replace("'", "")
            .replace("\u2019", "")
            .replaceAll("[^A-Z0-9]+", "_")
            .replaceAll("_+", "_")
            .replaceAll("^_", "")
            .replaceAll("_$", "");
        return "ITEM_" + normalized;
    }

    private static String normalizeUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            return "";
        }
        return unit.trim().toUpperCase()
            .replaceAll("[^A-Z0-9]+", "_")
            .replaceAll("_+", "_")
            .replaceAll("^_", "")
            .replaceAll("_$", "");
    }

    public static final class CustomItemData {
        private final String key;
        private final String name;
        private final int weight;
        private final int cost;
        private final String costUnit;
        private final String description;
        private final String type;
        private final int hits;
        private final int damage;
        private final int versatileHits;
        private final int versatileDamage;
        private final String[] attributes;
        private final String[] properties;
        private final String[] tags;
        private final String mastery;
        private final int shortRange;
        private final int longRange;
        private final String ammo;
        private final int armorClass;
        private final int dexterity;
        private final int strength;
        private final boolean stealth;

        public CustomItemData(
            String key,
            String name,
            int weight,
            int cost,
            String costUnit,
            String description,
            String type,
            int hits,
            int damage,
            int versatileHits,
            int versatileDamage,
            String[] attributes,
            String[] properties,
            String[] tags,
            String mastery,
            int shortRange,
            int longRange,
            String ammo,
            int armorClass,
            int dexterity,
            int strength,
            boolean stealth
        ) {
            this.key = key;
            this.name = name;
            this.weight = weight;
            this.cost = cost;
            this.costUnit = costUnit;
            this.description = description;
            this.type = type;
            this.hits = hits;
            this.damage = damage;
            this.versatileHits = versatileHits;
            this.versatileDamage = versatileDamage;
            this.attributes = attributes;
            this.properties = properties;
            this.tags = tags;
            this.mastery = mastery;
            this.shortRange = shortRange;
            this.longRange = longRange;
            this.ammo = ammo;
            this.armorClass = armorClass;
            this.dexterity = dexterity;
            this.strength = strength;
            this.stealth = stealth;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public int getCost() {
            return cost;
        }

        public String getCostUnit() {
            return costUnit;
        }

        public String getDescription() {
            return description;
        }

        public String getType() {
            return type;
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

        public String[] getAttributes() {
            return attributes;
        }

        public String[] getProperties() {
            return properties;
        }

        public String[] getTags() {
            return tags;
        }

        public String getMastery() {
            return mastery;
        }

        public int getShortRange() {
            return shortRange;
        }

        public int getLongRange() {
            return longRange;
        }

        public String getAmmo() {
            return ammo;
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

        public boolean isStealth() {
            return stealth;
        }
    }

    public static final class ItemBuilder {
        private final String name;
        private int weight;
        private int cost;
        private String currency;
        private String description;
        private String type = "ITEM";
        private int hits;
        private int damage;
        private int versatileHits;
        private int versatileDamage;
        private String[] attributes;
        private String[] properties;
        private String[] tags;
        private String mastery;
        private Integer shortRange;
        private Integer longRange;
        private String ammo;
        private int armorClass;
        private int dexterity;
        private int strength;
        private boolean stealth;

        public ItemBuilder(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name is required.");
            }
            this.name = name;
        }

        public ItemBuilder setWeight(int weight) {
            this.weight = weight;
            return this;
        }

        public ItemBuilder setCost(int cost) {
            this.cost = cost;
            return this;
        }

        public ItemBuilder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public ItemBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public ItemBuilder setHits(int hits) {
            this.hits = hits;
            return this;
        }

        public ItemBuilder setDamage(int damage) {
            this.damage = damage;
            return this;
        }

        public ItemBuilder setVersatileHits(int versatileHits) {
            this.versatileHits = versatileHits;
            return this;
        }

        public ItemBuilder setVersatileDamage(int versatileDamage) {
            this.versatileDamage = versatileDamage;
            return this;
        }

        public ItemBuilder setAttributes(String[] attributes) {
            this.attributes = attributes;
            return this;
        }

        public ItemBuilder setProperties(String[] properties) {
            this.properties = properties;
            return this;
        }

        public ItemBuilder setTags(String[] tags) {
            this.tags = tags;
            return this;
        }

        public ItemBuilder setMastery(String mastery) {
            this.mastery = mastery;
            return this;
        }

        public ItemBuilder setShortRange(Integer shortRange) {
            this.shortRange = shortRange;
            return this;
        }

        public ItemBuilder setLongRange(Integer longRange) {
            this.longRange = longRange;
            return this;
        }

        public ItemBuilder setAmmo(String ammo) {
            this.ammo = ammo;
            return this;
        }

        public ItemBuilder setArmorClass(int armorClass) {
            this.armorClass = armorClass;
            return this;
        }

        public ItemBuilder setDexterity(int dexterity) {
            this.dexterity = dexterity;
            return this;
        }

        public ItemBuilder setStrength(int strength) {
            this.strength = strength;
            return this;
        }

        public ItemBuilder setStealth(boolean stealth) {
            this.stealth = stealth;
            return this;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public int getCost() {
            return cost;
        }

        public String getCurrency() {
            return currency;
        }

        public String getDescription() {
            return description;
        }

        public String getType() {
            return type;
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

        public String[] getAttributes() {
            return attributes;
        }

        public String[] getProperties() {
            return properties;
        }

        public String[] getTags() {
            return tags;
        }

        public String getMastery() {
            return mastery;
        }

        public Integer getShortRange() {
            return shortRange;
        }

        public Integer getLongRange() {
            return longRange;
        }

        public String getAmmo() {
            return ammo;
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

        public boolean isStealth() {
            return stealth;
        }
    }
}