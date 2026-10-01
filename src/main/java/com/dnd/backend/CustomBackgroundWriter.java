package com.dnd.backend;

import com.dnd.frontend.language.TranslationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CustomBackgroundWriter {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String upsertBackground(
        String itemName,
        String description,
        String tool,
        String feat,
        String[] abilities,
        String[] skills,
        String[] equipmentA,
        int equipmentB
    ) throws IOException {
        BackgroundBuilder builder = new BackgroundBuilder(itemName)
            .setDescription(description)
            .setTool(tool)
            .setFeat(feat)
            .setAbilities(abilities)
            .setSkills(skills)
            .setEquipmentA(equipmentA)
            .setEquipmentB(equipmentB);
        return upsertBackgroundFromBuilder(builder);
    }

    private static String upsertBackgroundFromBuilder(BackgroundBuilder builder) throws IOException {
        String key = toBackgroundKey(builder.getName());
        String cleanName = builder.getName().trim();
        String cleanDescription = builder.getDescription() == null ? "" : builder.getDescription().trim();

        for (Path customDir : getCustomDirectories()) {
            Files.createDirectories(customDir);

            upsertBackgroundJsonFromBuilder(customDir.resolve("groups.json"), key, builder);
            for (String language : getStrings(new String[] {"languages"})) {
                String lan = getString(new String[] {"languages", language});
                upsertProperty(customDir.resolve("translations_" + lan + ".properties"), key, cleanName);
                upsertProperty(customDir.resolve("definitions_" + lan + ".properties"), key, cleanDescription);
            }
        }

        return key;
    }

    private static String toBackgroundKey(String itemName) {
        String normalized = itemName.trim().toUpperCase()
            .replace("'", "")
            .replace("\u2019", "")
            .replaceAll("[^A-Z0-9]+", "_")
            .replaceAll("_+", "_")
            .replaceAll("^_", "")
            .replaceAll("_$", "");
        return "BACKGROUND_" + normalized;
    }

    private static void upsertBackgroundJsonFromBuilder(
        Path jsonPath,
        String key,
        BackgroundBuilder builder
    ) throws IOException {
        JsonObject root = new JsonObject();
        if (Files.exists(jsonPath)) {
            try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {
                root = JsonParser.parseReader(reader).getAsJsonObject();
            }
        }

        JsonObject backgrounds = root.has("backgrounds") && root.get("backgrounds").isJsonObject()
            ? root.getAsJsonObject("backgrounds")
            : new JsonObject();

        JsonObject background = new JsonObject();
        background.addProperty("tool", builder.getTool());
        background.addProperty("feat", builder.getFeat());
        background.add("abilities", GSON.toJsonTree(builder.getAbilities()));
        background.add("skills", GSON.toJsonTree(builder.getSkills()));
        background.add("equipment", GSON.toJsonTree(builder.getEquipmentA()));
        background.addProperty("gold", builder.getEquipmentB());

        backgrounds.add(key, background);
        root.add("backgrounds", backgrounds);

        try (Writer writer = Files.newBufferedWriter(jsonPath, StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        }
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

    public static List<String> getCustomBackgroundKeys() throws IOException {
        Path customDir = getPrimaryCustomDirectory();
        Path jsonPath = customDir.resolve("groups.json");
        if (!Files.exists(jsonPath)) {
            return new ArrayList<>();
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8)) {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        }

        JsonObject backgrounds = root.has("backgrounds") && root.get("backgrounds").isJsonObject()
            ? root.getAsJsonObject("backgrounds")
            : null;
        if (backgrounds == null) {
            return new ArrayList<>();
        }

        List<String> keys = new ArrayList<>();
        for (String key : backgrounds.keySet()) {
            keys.add(key);
        }
        Collections.sort(keys);
        return keys;
    }

    public static CustomBackgroundData getCustomBackground(String key) throws IOException {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Background key is required.");
        }

        String cleanKey = key.trim();
        Path customDir = getPrimaryCustomDirectory();
        Path jsonPath = customDir.resolve("groups.json");
        if (!Files.exists(jsonPath)) {
            return null;
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8))
        {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        }

        JsonObject backgrounds = root.has("backgrounds") && root.get("backgrounds").isJsonObject()
            ? root.getAsJsonObject("backgrounds")
            : null;
        if (backgrounds == null || !backgrounds.has(cleanKey)) {
            return null;
        }

        JsonObject background = root.getAsJsonObject("backgrounds").getAsJsonObject(cleanKey);
        String tool = background.has("tool") ? background.get("tool").getAsString() : "";
        String feat = background.has("feat") ? background.get("feat").getAsString() : "";
        String[] abilities = background.has("abilities") ? jsonArrayToStrings(background.getAsJsonArray("abilities")) : new String[0];
        String[] skills = background.has("skills") ? jsonArrayToStrings(background.getAsJsonArray("skills")) : new String[0];
        String[] equipmentA = background.has("equipment") ? jsonArrayToStrings(background.getAsJsonArray("equipment")) : new String[0];
        int equipmentB = background.has("gold") ? background.get("gold").getAsInt() : 0;

        String lan = TranslationManager.getLang();
        String name = getPropertyValue(customDir.resolve("translations_" + lan + ".properties"), cleanKey, cleanKey);
        String description = getPropertyValue(customDir.resolve("Definitions_" + lan + ".properties"), cleanKey, "");

        return new CustomBackgroundData(
            cleanKey,
            name,
            description,
            tool,
            feat,
            abilities,
            skills,
            equipmentA,
            equipmentB
        );
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

    public static final class CustomBackgroundData {
        private final String key;
        private final String name;
        private final String description;
        private final String tool;
        private final String feat;
        private final String[] abilities;
        private final String[] skills;
        private final String[] equipmentA;
        private final int equipmentB;
        
        public CustomBackgroundData(
            String key,
            String name,
            String description,
            String tool,
            String feat,
            String[] abilities,
            String[] skills,
            String[] equipmentA,
            int equipmentB
        ) {
            this.key = key;
            this.name = name;
            this.description = description;
            this.tool = tool;
            this.feat = feat;
            this.abilities = abilities;
            this.skills = skills;
            this.equipmentA = equipmentA;
            this.equipmentB = equipmentB;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getTool() {
            return tool;
        }

        public String getFeat() {
            return feat;
        }

        public String[] getAbilities() {
            return abilities;
        }

        public String[] getSkills() {
            return skills;
        }

        public String[] getEquipmentA() {
            return equipmentA;
        }

        public int getEquipmentB() {
            return equipmentB;
        }
    }

    public static final class BackgroundBuilder {
        private String name;
        private String description;
        private String tool;
        private String feat;
        private String[] abilities = new String[0];
        private String[] skills = new String[0];
        private String[] equipmentA = new String[0];
        private int equipmentB;

        public BackgroundBuilder(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Background name is required.");
            }
            this.name = name;
        }

        public BackgroundBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public BackgroundBuilder setTool(String tool) {
            this.tool = tool;
            return this;
        }

        public BackgroundBuilder setFeat(String feat) {
            this.feat = feat;
            return this;
        }

        public BackgroundBuilder setAbilities(String[] abilities) {
            this.abilities = abilities != null ? abilities : new String[0];
            return this;
        }

        public BackgroundBuilder setSkills(String[] skills) {
            this.skills = skills != null ? skills : new String[0];
            return this;
        }

        public BackgroundBuilder setEquipmentA(String[] equipmentA) {
            this.equipmentA = equipmentA != null ? equipmentA : new String[0];
            return this;
        }

        public BackgroundBuilder setEquipmentB(int equipmentB) {
            this.equipmentB = equipmentB;
            return this;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getTool() {
            return tool;
        }

        public String getFeat() {
            return feat;
        }

        public String[] getAbilities() {
            return abilities;
        }

        public String[] getSkills() {
            return skills;
        }

        public String[] getEquipmentA() {
            return equipmentA;
        }

        public int getEquipmentB() {
            return equipmentB;
        }
    }

    private static String[] jsonArrayToStrings(com.google.gson.JsonArray array) {
        if (array == null) {
            return new String[0];
        }

        List<String> values = new ArrayList<>();
        array.forEach(element -> values.add(element.getAsString()));
        return values.toArray(String[]::new);
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

    public static void deleteBackground(String key) throws IOException {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Background key is required.");
        }

        String cleanKey = key.trim();

        for (Path customDir : getCustomDirectories()) {
            Files.createDirectories(customDir);
            removeBackgroundFromJson(customDir.resolve("groups.json"), cleanKey);
            for (String language : getStrings(new String[] {"languages"})) {
                String lan = getString(new String[] {"languages", language});
                removeProperty(customDir.resolve("translations_" + lan + ".properties"), cleanKey);
                removeProperty(customDir.resolve("definitions_" + lan + ".properties"), cleanKey);
            }
        }
    }

    private static void removeBackgroundFromJson(Path jsonPath, String key) throws IOException {
        if (!Files.exists(jsonPath)) {
            return;
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(jsonPath, StandardCharsets.UTF_8))
        {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        }

        JsonObject backgrounds = root.has("backgrounds") && root.get("backgrounds").isJsonObject()
            ? root.getAsJsonObject("backgrounds")
            : null;

        if (backgrounds == null || !backgrounds.has(key)) {
            return;
        }

        backgrounds.remove(key);
        root.add("backgrounds", backgrounds);
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

    private static Path getPrimaryCustomDirectory() throws IOException {
        Path directory = getCustomDirectories().get(0);
        Files.createDirectories(directory);
        return directory;
    }

    private static String[] getStrings (String[] key) {
        return GroupManager.getInstance().getStrings(key);
    }

    private static String getString (String[] key) {
        return GroupManager.getInstance().getString(key);
    }
}
