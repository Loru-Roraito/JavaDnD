package com.dnd.frontend.language;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import com.dnd.backend.GameCharacter;
import com.dnd.backend.GroupManager;
import com.dnd.utils.items.Item;
import com.dnd.utils.items.Spell;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class DefinitionManager {
    private static final Properties definitions = new Properties();
    private static final Properties tooltips = new Properties();

    public static void initialize(String language) {
        try (var inputStream = DefinitionManager.class.getResourceAsStream("/definitions_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: definitions_" + language + ".properties");
            }
            definitions.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));

            // Try to load and merge custom version if it exists
            String customFileName = "/custom/definitions_" + language + ".properties";
            var customStream = DefinitionManager.class.getResourceAsStream(customFileName);
            if (customStream != null) {
                try (customStream;
                    var customReader = new java.io.InputStreamReader(customStream, StandardCharsets.UTF_8)) {
                    Properties customProperties = new Properties();
                    customProperties.load(customReader);
                    // Merge: custom properties overwrite base properties
                    definitions.putAll(customProperties);
                } catch (IOException e) {
                    System.err.println("Error: Failed to load custom translations from " + customFileName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load definitions file: definitions_" + language + ".properties");
        }

        try (var inputStream = DefinitionManager.class.getResourceAsStream("/tooltips_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: tooltips_" + language + ".properties");
            }
            tooltips.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load tooltips file: tooltips_" + language + ".properties");
        }
    }

    public static void fillTextFlow(TextFlow textFlow, String definition, TabPane mainTabPane, String text) {
        // Split the definition into lines by \n
        String[] lines = definition.split("\n");
        Boolean isFirstLine = true; // Flag to check if it's the first line
        for (String line : lines) {
            if (isFirstLine) {
                isFirstLine = false; // Set the flag to false after the first line
            } else {
                // Add a newline before each subsequent line
                textFlow.getChildren().add(new Text("\n "));
            }

            // Split each line into words and process each word
            String[] tokens = line.split("(?=[.,:;!?])|(?<=[.,:;!?])");
            for (String token : tokens) {
                String[] subTokens = token.split("(?=\\s)|(?<=\\s)");

                for (int j = 0; j < subTokens.length; j++) {
                    String subToken = subTokens[j]; // Trim whitespace from the token
                    Text wordText = new Text(subToken); // Create a Text node for the word
                    // Check if the token is a word (not just punctuation or whitespace)
                    if (subToken.matches("\\w+")) { // Matches words (alphanumeric characters)
                        definition = "";
                        String newText = subToken;

                        if (j + 6 < subTokens.length) {
                            definition = definitions.getProperty(subToken.concat("_").concat(subTokens[j+2]).concat("_").concat(subTokens[j+4]).concat("_").concat(subTokens[j+6]), "");
                            if (definition != null && !definition.isEmpty()) {
                                newText = subToken.concat("_").concat(subTokens[j+2]).concat("_").concat(subTokens[j+4]).concat("_").concat(subTokens[j+6]);
                                wordText.setText(newText.replace("_", " "));
                                j+=6;
                            }
                        }
                        if (definition == null || definition.isEmpty() && j + 4 < subTokens.length) {
                            definition = definitions.getProperty(subToken.concat("_").concat(subTokens[j+2]).concat("_").concat(subTokens[j+4]), "");
                            if (definition != null && !definition.isEmpty()) {
                                newText = subToken.concat("_").concat(subTokens[j+2]).concat("_").concat(subTokens[j+4]);
                                wordText.setText(newText.replace("_", " "));
                                j+=4;
                            }
                        }
                        if(definition == null || definition.isEmpty() && j + 2 < subTokens.length) {
                            definition = definitions.getProperty(subToken.concat("_").concat(subTokens[j+2]), "");
                            if (definition != null && !definition.isEmpty()) {
                                newText = subToken.concat("_").concat(subTokens[j+2]);
                                wordText.setText(newText.replace("_", " "));
                                j+=2;
                            }
                        }
                        if(definition == null || definition.isEmpty()) {
                            definition = definitions.getProperty(subToken, "");
                        }

                        String newerDefinition = definitions.getProperty(definition, "");
                        if (newerDefinition != null && !newerDefinition.isEmpty()) {
                            newText = definition;
                            definition = newerDefinition; // Update the definition if a new one is found
                        }

                        if (definition != null && !definition.isEmpty() && !newText.equals(text) && !subToken.equals(text)) {
                            // Use a final variable to capture the value of newText for the lambda
                            final String clickableText = newText;

                            // Underline the word and make it clickable
                            wordText.getStyleClass().clear();
                            wordText.setStyle("-fx-fill: #694704ff; -fx-cursor: hand;");
                            wordText.setOnMouseClicked(_ -> openDefinitionTab(clickableText, mainTabPane));

                            assignTooltip(wordText, newText);
                        }
                    }

                    // Add the word and the space to the TextFlow
                    textFlow.getChildren().add(wordText);
                }
            }
        }
    }

    private static String fetchItemTooltip(Item item) {
        String tooltip = "";
        if (item.getType().equals("WEAPON")) {
            tooltip = String.valueOf(item.getHits()) + "d" + String.valueOf(item.getDamage());
            
            String properties[] = item.getProperties();
            if (Arrays.asList(properties).contains("VERSATILE")) {
                tooltip += " (" + String.valueOf(item.getVersatileHits()) + "d" + String.valueOf(item.getVersatileDamage()) + ")";
            }

            String attributes[] = item.getAttributes();
            for (String attribute : attributes) {
                tooltip += " " + getTranslation(attribute);
            }
            
            String[] tags = item.getTags();
            if (Arrays.asList(tags).contains("RANGED")) {
                tooltip += ", " + getTranslation("RANGE") + ": " + (int) (item.getShortRange() * Constants.LENGTH_MULTIPLIER) + "/" + (int) (item.getLongRange() * Constants.LENGTH_MULTIPLIER) + " " + getTranslation("LENGTH_UNIT");
            }

            if (Arrays.asList(properties).contains("AMMUNITION")) {
                tooltip += "\n" + getTranslation("AMMUNITION") + ": " + getTranslation(item.getAmmo());
            }

            tooltip += "\n\n";
            for (String property : properties) {
                tooltip += getTranslation(property) + ", ";
            }
            tooltip = tooltip.substring(0, tooltip.length() - 2); // Remove trailing comma and space

            tooltip += "\n";
            for (String tag : tags) {
                tooltip += getTranslation(tag) + ", ";
            }
            tooltip = tooltip.substring(0, tooltip.length() - 2); // Remove trailing comma and space

        } else if (item.getType().equals("ARMOR")) {
            
        }

        return tooltip;
    }

    public static void openDefinitionTab(Item item, TabPane mainTabPane) {
        StringBuilder definition = new StringBuilder();
        String itemName = item.getName();

        definition.append(itemName);
        definition.append("\n\n");
        definition.append(fetchItemTooltip(item));

        displayDefinitionTab(itemName, definition.toString(), mainTabPane);
    }
    
    public static void openDefinitionTab(Spell spell, TabPane mainTabPane) {
        StringBuilder definition = new StringBuilder();
        String spellName = spell.getName();
        int level = spell.getLevel();
        if (level > 0) {
            definition.append(spellName)
                .append(" (")
                .append(getTranslation("LEVEL"))
                .append(" ")
                .append(level)
                .append(")")
                .append("\n\n");
            definition.append(fetchSpellTooltip(spell));
        } else {
            definition.append(spellName)
                .append(" (")
                .append(getTranslation("CANTRIP"))
                .append(")")
                .append("\n\n");
            definition.append(fetchSpellTooltip(spell));
        }

        displayDefinitionTab(spellName, definition.toString(), mainTabPane);
    }

    // Open a new tab with the definition of the word
    public static void openDefinitionTab(String text, TabPane mainTabPane) {
        String realText = text.replace(" ", "_");
        String definition;
        String newDefinition;
        definition = definitions.getProperty(realText, "");
        newDefinition = definitions.getProperty(definition, "");
        if (newDefinition != null && !newDefinition.isEmpty()) {
            text = definition;
            definition =  newDefinition; // Update the definition if a new one is found
        }
        displayDefinitionTab(text, definition, mainTabPane);
    }

    public static void displayDefinitionTab(String text, String definition, TabPane mainTabPane) {
        // Check if a tab with the same title already exists
        for (Tab tab : mainTabPane.getTabs()) {
            if (tab.getText().replace(" ", "_").equals(text)) {
                // If the tab exists, select it and return
                mainTabPane.getSelectionModel().select(tab);
                return;
            }
        }

        if (definition != null && !definition.isEmpty()) {
            Tab definitionTab = new Tab(text.replace("_", " ")); // Tab title is the word

            // Create a TextFlow to hold the definition text
            TextFlow textFlow = new TextFlow();
            textFlow.maxWidthProperty().bind(mainTabPane.widthProperty().multiply(0.5));

            fillTextFlow(textFlow, definition, mainTabPane, text);

            VBox centeredBox = new VBox(textFlow);
            centeredBox.setAlignment(Pos.CENTER); // Center the TextFlow horizontally and vertically
            centeredBox.setFillWidth(true); // Ensure the TextFlow fills the width
            centeredBox.setPadding(new Insets(10, 20, 10, 20)); // Add 20px padding on all sides (top, right, bottom, left)


            // Wrap the VBox in a ScrollPane for better usability
            ScrollPane scrollPane = new ScrollPane(centeredBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setPannable(true); // Allow panning if the content overflows

            // Set the ScrollPane as the content of the tab
            definitionTab.setContent(scrollPane);

            // Add the tab to the TabPane and select it
            mainTabPane.getTabs().add(definitionTab);
            mainTabPane.getSelectionModel().select(definitionTab);
        }
    }

    // Get the tooltip text for a given key
    public static String fetchTooltip(String key) {
        return tooltips.getProperty(key, ""); // Return an empty string if the key is not found
    }

    // Assign a tooltip to a UI element
    public static void assignTooltip(javafx.scene.Node node, String key) {
        String tooltipText = fetchTooltip(key);
        placeTooltip(node, tooltipText);
    }

    // Assign a tooltip to a UI element
    public static void assignTooltip(javafx.scene.Node node, Spell spell) {
        Boolean limited = spell.getLimited();
        String number = " ";
        if (limited) {
            number = " 1 ";
        }

        String tooltipText = getTranslation("PREPARE") + number + getTranslation("EVERY") + " " + getTranslation(spell.getPrepare());
        tooltipText += " (" + getTranslation(GameCharacter.getAbilityName(spell.getAbility())) + ")";
        tooltipText += "\n\n" + fetchSpellTooltip(spell);
        placeTooltip(node, tooltipText);
    }

    public static void assignTooltip(javafx.scene.Node node, Item item) {
        String tooltipText = fetchItemTooltip(item); // Placeholder: replace with actual tooltip fetching logic for Item
        placeTooltip(node, tooltipText);
    }
    
    private static String fetchSpellTooltip(Spell spell) {
        String tooltip = getTranslation(spell.getSchool());

        if (spell.getRitual()) {
            tooltip += ", " + getTranslation("RITUAL");
        }

        if (spell.getConcentration()) {
            tooltip += ", " + getTranslation("CONCENTRATION");
        }

        int time = spell.getTime();
        String timeSpan = spell.getTimeSpan();
        if (timeSpan.equals("DISPELLED")) {
            tooltip += "\n" + getTranslation("CASTING_TIME") + ": " + getTranslation(timeSpan);
        } else {
            tooltip += "\n" + getTranslation("CASTING_TIME") + ": " + time + " " + getTranslation(timeSpan);
        }

        int range = spell.getRange();
        if (range > 0) {
            tooltip += "\n" + getTranslation("RANGE") + ": " + range * Constants.LENGTH_MULTIPLIER + " " + getTranslation("LENGTH_UNIT");
        } else if (range == 0) {
            tooltip += "\n" + getTranslation("RANGE") + ": " + getTranslation("TOUCH");
        } else if (range == -1) {
            tooltip += "\n" + getTranslation("RANGE") + ": " + getTranslation("SELF");
        }

        tooltip += "\n" + getTranslation("COMPONENTS") + ": ";
        Boolean[] components = spell.getComponents();
        String[] componentNames = {"V", "S", "M"};
        for (int i = 0; i < components.length; i++) {
            if (components[i]) {
                tooltip += getTranslation(componentNames[i]);
                if (i == 2) {
                    tooltip += " (" + getIngredient(spell.getNominative()) + "), ";
                } else {
                    tooltip += ", ";  
                }
            }
        }
        tooltip = tooltip.substring(0, tooltip.length() - 2); // Remove trailing comma and space

        int duration = spell.getDuration();
        String durationSpan = spell.getDurationSpan();
        if (durationSpan.equals("INSTANTANEOUS")) {
            tooltip += "\n" + getTranslation("DURATION") + ": " + getTranslation(durationSpan);
        } else {
            tooltip += "\n" + getTranslation("DURATION") + ": " + duration + " " + getTranslation(durationSpan);
        }

        tooltip += "\n\n" + getDescription(spell.getNominative());

        return tooltip;
    }

    public static void placeTooltip(javafx.scene.Node node, String tooltipText) {
        if (tooltipText == null || tooltipText.isEmpty()) {
            return; // No tooltip to place
        }
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(300);
        tooltip.setShowDuration(Duration.INDEFINITE); // Stay visible while hovering

        tooltip.setAutoFix(true); // Automatically adjust position to stay on screen
    
        Tooltip.install(node, tooltip);
    }

    public static String[] getStrings(String[] key) {
        return GroupManager.getInstance().getStrings(key);
    }

    private static String getTranslation(String key) {
        return TranslationManager.getTranslation(key);
    }

    private static String getDescription(String key) {
        return MiscsManager.getDescription(key);
    }

    private static String getIngredient(String key) {
        return MiscsManager.getIngredient(key);
    }
}