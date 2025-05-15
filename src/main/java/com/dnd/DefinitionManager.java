package com.dnd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class DefinitionManager {
    private static DefinitionManager instance;
    private static final Properties definitions = new Properties();
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
    }

    public static DefinitionManager getInstance() {
        if (instance == null) {
            instance = new DefinitionManager(language);
        }
        return instance;
    }

    // Open a new tab with the definition of the word
    public void openDefinitionTab(String text, TabPane mainTabPane) {
        String definition = definitions.getProperty(text, "");
        String newDefinition = definitions.getProperty(definition, "");
        if (newDefinition != null && !newDefinition.isEmpty()) {
            text = definition;
            definition =  newDefinition; // Update the definition if a new one is found
        }

        // Check if a tab with the same title already exists
        for (Tab tab : mainTabPane.getTabs()) {
            if (tab.getText().equals(text)) {
                // If the tab exists, select it and return
                mainTabPane.getSelectionModel().select(tab);
                return;
            }
        }

        if (definition != null && !definition.isEmpty()) {
            Tab definitionTab = new Tab(text.replace("_", " ")); // Tab title is the word

            // Create a TextFlow to hold the definition text
            TextFlow textFlow = new TextFlow();

            // Split the definition into lines by \n
            String[] lines = definition.split("\n");
            for (String line : lines) {
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

                            if (j + 4 < subTokens.length) {
                                definition = definitions.getProperty(subToken.concat("_").concat(subTokens[j+2]).concat("_").concat(subTokens[j+4]), "");
                                if (definition != null && !definition.isEmpty()) {
                                    wordText.setText(subToken.concat(" ").concat(subTokens[j+2]).concat(" ").concat(subTokens[j+4]));
                                    j+=4;
                                }
                            }
                            if(definition == null || definition.isEmpty() && j + 2 < subTokens.length) {
                                definition = definitions.getProperty(subToken.concat("_").concat(subTokens[j+2]), "");
                                if (definition != null && !definition.isEmpty()) {
                                    wordText.setText(subToken.concat(" ").concat(subTokens[j+2]));
                                    j+=2;
                                }
                            }
                            if(definition == null || definition.isEmpty()) {
                                definition = definitions.getProperty(subToken, "");
                            }

                            newDefinition = definitions.getProperty(definition, "");
                            String newText = subToken;
                            if (newDefinition != null && !newDefinition.isEmpty()) {
                                newText = definition;
                                definition =  newDefinition; // Update the definition if a new one is found
                            }

                            if (definition != null && !definition.isEmpty() && !newText.equals(text) && !subToken.equals(text)) {
                                // Use a final variable to capture the value of newText for the lambda
                                final String clickableText = newText;

                                // Underline the word and make it clickable
                                wordText.setStyle("-fx-underline: true; -fx-fill: blue; -fx-cursor: hand;");
                                wordText.setOnMouseClicked(_ -> openDefinitionTab(clickableText, mainTabPane));
                            }
                        }

                        // Add the word and the space to the TextFlow
                        textFlow.getChildren().add(wordText);
                    }
                }

                // Add a newline after each line
                textFlow.getChildren().add(new Text("\n"));
            }

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
}