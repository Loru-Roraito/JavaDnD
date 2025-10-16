package com.dnd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import com.dnd.utils.Constants;

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
    private static DefinitionManager instance;
    private static final Properties definitions = new Properties();
    private static final Properties tooltips = new Properties();
    public static String language = "it";

    private DefinitionManager(String language){
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("definitions_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: definitions_" + language + ".properties");
            }
            definitions.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load definitions file: definitions_" + language + ".properties");
        }

        try (var inputStream = getClass().getClassLoader().getResourceAsStream("tooltips_" + language + ".properties")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: tooltips_" + language + ".properties");
            }
            tooltips.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: Failed to load tooltips file: tooltips_" + language + ".properties");
        }
    }

    public static DefinitionManager getInstance() {
        if (instance == null) {
            instance = new DefinitionManager(language);
        }
        return instance;
    }

    public void fillTextFlow(TextFlow textFlow, String definition, TabPane mainTabPane, String text) {
        // Split the definition into lines by \n
        String[] lines = definition.split("\n");
        Boolean isFirstLine = true; // Flag to check if it's the first line
        for (String line : lines) {
            if (isFirstLine) {
                isFirstLine = false; // Set the flag to false after the first line
            } else {
                // Add a newline before each subsequent line
                textFlow.getChildren().add(new Text("\n"));
            }

            // Split each line into words and process each word
            String[] tokens = line.split("(?=[.,;!?])|(?<=[.,;!?])");
            for (String token : tokens) {
                String[] subTokens = token.split("(?=[\\s+])|(?<=[\\s+])");

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
                            wordText.setStyle("-fx-fill: #0095ff; -fx-cursor: hand;");
                            wordText.setOnMouseClicked(_ -> openDefinitionTab(clickableText, mainTabPane));
                        }
                    }

                    // Add the word and the space to the TextFlow
                    textFlow.getChildren().add(wordText);
                }
            }
        }
    }

    // Open a new tab with the definition of the word
    public void openDefinitionTab(String text, TabPane mainTabPane) {
        String realText = text.replace(" ", "_");
        String[] spells = getGroup(new String[] {"spells"});
        String[] cantrips = getGroup(new String[] {"cantrips"});
        String definition;
        String newDefinition;
        if (java.util.Arrays.asList(spells).contains(realText) || java.util.Arrays.asList(cantrips).contains(realText)) {
            definition = createSpellDefinition(realText);
        } else {
            definition = definitions.getProperty(realText, "");
            newDefinition = definitions.getProperty(definition, "");
            if (newDefinition != null && !newDefinition.isEmpty()) {
                text = definition;
                definition =  newDefinition; // Update the definition if a new one is found
            }
        }

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
    public String fetchTooltip(String key) {
        return tooltips.getProperty(key, ""); // Return an empty string if the key is not found
    }

    // Assign a tooltip to a UI element
    public void assignTooltip(javafx.scene.Node node, String key) {
        String tooltipText = fetchTooltip(key);
        String[] spells = getGroup(new String[] {"spells"});
        String[] cantrips = getGroup(new String[] {"cantrips"});
        if (java.util.Arrays.asList(spells).contains(key) || java.util.Arrays.asList(cantrips).contains(key)) {
            tooltipText = createSpellTooltip(key);
        }
        if (!tooltipText.isEmpty()) {

            Tooltip tooltip = new Tooltip(tooltipText);
            tooltip.setWrapText(true);
            tooltip.setMaxWidth(300);
            tooltip.setShowDuration(Duration.INDEFINITE); // Stay visible while hovering
            Tooltip.install(node, tooltip);
        }
    }

    public void forceTooltip(javafx.scene.Node node, String tooltipText) {
        if (!tooltipText.isEmpty()) {
            Tooltip tooltip = new Tooltip(tooltipText);
            tooltip.setWrapText(true);
            tooltip.setMaxWidth(300);
            tooltip.setShowDuration(Duration.INDEFINITE); // Stay visible while hovering
            Tooltip.install(node, tooltip);
        }
    }

    private String createSpellDefinition(String key) {
        StringBuilder definition = new StringBuilder();
        String spell = getOriginal(key);
        int level = getInt(new String[] {"spells", spell, "level"});
        if (level > 0) {
            definition.append(spell)
                .append(" (")
                .append(getTranslation("LEVEL"))
                .append(" ")
                .append(level)
                .append(")")
                .append("\n\n");
            definition.append(createSpellTooltip(key));
        } else {
            definition.append(spell)
                .append(" (")
                .append(getTranslation("CANTRIP"))
                .append(")")
                .append("\n\n");
            definition.append(createSpellTooltip(key));
        }

        return definition.toString();
    }

    private String createSpellTooltip(String key) {
        String spell = getOriginal(key);
        String tooltip = getTranslation(getString(new String[] {"spells", spell, "school"}));

        if (getBoolean(new String[] {"spells", spell, "ritual"})) {
            tooltip += ", " + getTranslation("RITUAL");
        }

        if (getBoolean(new String[] {"spells", spell, "concentration"})) {
            tooltip += ", " + getTranslation("CONCENTRAZIONE");
        }
        
        int time = getInt(new String[] {"spells", spell, "time"});                
        String timeSpan = getString(new String[] {"spells", spell, "timeSpan"});
        if (timeSpan.equals("DISPELLED")) {
            tooltip += "\n" + getTranslation("CASTING_TIME") + ": " + getTranslation(timeSpan);
        } else {
            tooltip += "\n" + getTranslation("CASTING_TIME") + ": " + time + " " + getTranslation(timeSpan);
        }

        int range = getInt(new String[] {"spells", spell, "range"});
        if (range > 0) {
            tooltip += "\n" + getTranslation("RANGE") + ": " + range * Constants.LENGTH_MULTIPLIER + " " + getTranslation("LENGTH_UNIT");
        } else if (range == 0) {
            tooltip += "\n" + getTranslation("RANGE") + ": " + getTranslation("TOUCH");
        } else if (range == -1) {
            tooltip += "\n" + getTranslation("RANGE") + ": " + getTranslation("SELF");
        }

        tooltip += "\n" + getTranslation("COMPONENTS") + ": ";
        Boolean[] components = getBooleans(new String[] {"spells", spell, "components"});
        String[] componentNames = {"V", "S", "M"};
        for (int i = 0; i < components.length; i++) {
            if (components[i]) {
                tooltip += getTranslation(componentNames[i]);
                if (i == 2) {
                    tooltip += " (" + fetchIngredient(spell) + "), ";
                } else {
                    tooltip += ", ";  
                }
            }
        }
        tooltip = tooltip.substring(0, tooltip.length() - 2); // Remove trailing comma and space

        int duration = getInt(new String[] {"spells", spell, "duration"});
        String durationSpan = getString(new String[] {"spells", spell, "durationSpan"});
        if (durationSpan.equals("INSTANTANEOUS")) {
            tooltip += "\n" + getTranslation("DURATION") + ": " + getTranslation(durationSpan);
        } else {
            tooltip += "\n" + getTranslation("DURATION") + ": " + duration + " " + getTranslation(durationSpan);
        }

        tooltip += "\n\n" + fetchSpell(spell);

        return tooltip;
    }

    private int getInt(String[] key) {
        return TranslationManager.getInstance().getInt(key);
    }

    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    public String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    }

    private String getString(String[] key) {
        return TranslationManager.getInstance().getString(key);
    }

    private boolean getBoolean(String[] key) {
        return TranslationManager.getInstance().getBoolean(key);
    }

    private Boolean[] getBooleans(String[] key) {
        return TranslationManager.getInstance().getBooleans(key);
    }

    private String getOriginal(String key) {
        return TranslationManager.getInstance().getOriginal(key);
    }

    private String fetchSpell(String key) {
        return MiscsManager.getInstance().fetchSpell(key);
    }

    private String fetchIngredient(String key) {
        return MiscsManager.getInstance().fetchIngredient(key);
    }
}