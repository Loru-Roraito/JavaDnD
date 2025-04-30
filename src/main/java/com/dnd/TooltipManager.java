package com.dnd;

import javafx.scene.control.Tooltip;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class TooltipManager {
    private static TooltipManager instance;
    private static final Properties tooltips = new Properties();
    public static String language = "it";

    private TooltipManager(String language){
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("tooltips_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: tooltips_" + language + ".properties");
            }
            tooltips.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load tooltips file: tooltips_" + language + ".properties");
        }
    }

    // Get the tooltip text for a given key
    public String fetchTooltip(String key) {
        return tooltips.getProperty(key, ""); // Return an empty string if the key is not found
    }

    public static TooltipManager getInstance() {
        if (instance == null) {
            instance = new TooltipManager(language);
        }
        return instance;
    }

    // Assign a tooltip to a UI element
    public void assignTooltip(javafx.scene.Node node, String key) {
        String tooltipText = fetchTooltip(key);
        if (!tooltipText.isEmpty()) {
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(node, tooltip);
        }
    }
}
