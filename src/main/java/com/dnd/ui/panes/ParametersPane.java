package com.dnd.ui.panes;

import java.util.HashMap;
import java.util.Map;

import com.dnd.TranslationManager;
import com.dnd.ui.tooltip.TooltipComboBox;
import com.dnd.ui.tooltip.TooltipLabel;
import com.dnd.characters.GameCharacter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class ParametersPane extends GridPane {
    private final Map<String, String> speciesMap = new HashMap<>();
    public ParametersPane(GameCharacter character, TabPane mainTabPane) {
        getStyleClass().add("grid-pane");
        
        // Create a label as the title for the ComboBox
        TooltipLabel speciesLabel = new TooltipLabel(getTranslation("SPECIES"), mainTabPane);
        add(speciesLabel, 0, 0); // Add the label to the GridPane (Column 0, Row 0)

        // Populate the class list and translation map
        ObservableList<String> species = FXCollections.observableArrayList();
        for (String classKey : getGroup(new String[] {"SPECIES"})) {
            String translatedClass = getTranslation(classKey);
            species.add(translatedClass);
            speciesMap.put(translatedClass, classKey); // Map translated name to original key
        }

        species.add(0, getTranslation("RANDOM"));

        TooltipComboBox<String> speciesComboBox = new TooltipComboBox<>(species, mainTabPane);
        speciesComboBox.setPromptText(getTranslation("RANDOM"));
        add(speciesComboBox, 0, 1);

        // Bind the ComboBox's selected value to the Character's classe property
        speciesComboBox.valueProperty().bindBidirectional(character.getSpecies());
        
        // Create a label as the title for the second ComboBox
        TooltipLabel lineageLabel = new TooltipLabel(getTranslation("LINEAGE"), mainTabPane);
        add(lineageLabel, 0, 2); // Add the label to the GridPane (Column 0, Row 2)

        // Create the second ComboBox (subclass selection)
        ObservableList<String> lineages = FXCollections.observableArrayList();
        TooltipComboBox<String> lineageComboBox = new TooltipComboBox<>(lineages, mainTabPane);
        lineageComboBox.setPromptText(getTranslation("RANDOM"));
        add(lineageComboBox, 0, 3);
        
        // Bind the ComboBox's selected value to the Character's lineage property
        lineageComboBox.valueProperty().bindBidirectional(character.getLineage());
    
        // Update the lineages based on the selected species
        speciesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Get the original key for the selected species
            String classKey = speciesMap.get(newValue);
            if (classKey != null) {
                lineages.setAll(getTranslation("RANDOM"));
                lineageComboBox.valueProperty().set(getTranslation("RANDOM"));
                // Fetch the lineages for the selected class
                String[] lineagesArray = getGroupTranslations(new String[]{"SPECIES", classKey, "LINEAGES"});
                if (lineagesArray.length > 0) {
                    lineages.addAll(lineagesArray);
                } else {
                    lineages.clear(); // Clear if no lineages are found
                }
            } else {
                lineages.clear();
            }

            // Dynamically add or remove the lineage elements
            if (lineages.isEmpty()) {
                getChildren().removeAll(lineageLabel, lineageComboBox); // Remove from GridPane
            } else {
                if (!getChildren().contains(lineageLabel)) {
                    add(lineageLabel, 0, 2); // Add back to GridPane
                }
                if (!getChildren().contains(lineageComboBox)) {
                    add(lineageComboBox, 0, 3); // Add back to GridPane
                }
            }
        });

        getChildren().removeAll(lineageLabel, lineageComboBox); // Initially remove specific elements
    }   

    // Helper method to get translations
    private String getTranslation(String key) {
        return TranslationManager.getInstance().getTranslation(key);
    }

    private String[] getGroup(String[] key) {
        return TranslationManager.getInstance().getGroup(key);
    } 

    private String[] getGroupTranslations(String[] key) {
        return TranslationManager.getInstance().getGroupTranslations(key);
    }
}
